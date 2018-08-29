package com.sofn.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sofn.utils.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

@Service("sofnService")
public class SofnService {
    private static final int QUERY_SIZE = 10000; //每次从索引库查出的记录数上限
    private static final int pageSize = 10; //每页显示10个索引库的数据
    private static final String REDIS_KEY_KEYWORD_PREFIX = "KEYWORD:";//redis中key的前缀，用于获取包含关键字内容的数据
    private static final int REDIS_KEYWORD_EXPIRE_SECONDS = 300; //包含关键字内容的数据，缓存在redis的时间(5分钟)
    private static final String QUERY_EXPORT_PATH = "/sofn-query-export/"; //用于导出关键字查询结果的目录

    private static List<String> date_fields; //时间相关的字段
    private static List<String> indices; //保存es的所有索引库名
    private static TransportClient client; //es客户端
    private static Map<String,String> tableName2Chn; //保存索引名和对应的中文注释
    private static Map<String,String> tableFieldName2Chn; //保存索引字段名和对应的中文注释

    //静态加载，暂时这样处理
    static {
        client = ESUtil.getClient();
        tableName2Chn = SofnServiceAssist.getTableInfoFromMysql();
        tableFieldName2Chn = SofnServiceAssist.getTableFieldInfoFromMysql();
        indices = SofnServiceAssist.getIndices(client);
        date_fields = SofnServiceAssist.getDateFields();
    }

    public Map searchES(Jedis jedis, String keyword, int pageIndex, String startDate, String endDate) {
        String redisKey = REDIS_KEY_KEYWORD_PREFIX + keyword + startDate + endDate;
        int startIndex = pageIndex * pageSize; //加载目标页面数据第一条数据所对应的index
        int endIndex = (pageIndex + 1) * pageSize - 1;
        List<String> redisRes = jedis.lrange(redisKey, startIndex, endIndex);
        if (redisRes.size() == 0) {
            generateKeywordToRedis(jedis, keyword, startDate, endDate, redisKey);
            redisRes = jedis.lrange(redisKey, startIndex, endIndex);
        }
        //当前分页的记录，共pageSize条，每个JSONObject对应一个索引库里的所有数据
        List<JSONObject> list = new ArrayList<>();
        parseRedisInfo(redisRes, keyword, list);

        Map result = new HashMap(); //返回结果
        result.put("list", list);
        result.put("indexCount", SofnServiceAssist.getKeywordIndexCount(jedis, redisKey)); //匹配到结果的索引库的条数
        result.put("recordCount", SofnServiceAssist.getKeywordRecordCount(jedis, redisKey));
        jedis.expire(redisKey, REDIS_KEYWORD_EXPIRE_SECONDS);//刷新持久化时间
        return result;
    }

    public Map getRecordsByIndex(Jedis jedis, String keyword, int index, int pageIndex, String startDate, String endDate) {
        index = pageIndex * pageSize + index;
        String redisKey = REDIS_KEY_KEYWORD_PREFIX + keyword + startDate + endDate;
        //拿去指定索引里对应的信息
        List<String> records = jedis.lrange(redisKey, index, index);
        if(records.size() == 0){
            generateKeywordToRedis(jedis, keyword, startDate, endDate, redisKey);
            records = jedis.lrange(redisKey,index,index);
        }

        JSONObject currTable = JSONObject.parseObject(records.get(0));
        String tableName = currTable.getString("tableName");
        String indexName = currTable.getString("indexName");
        JSONArray oriRecords = currTable.getJSONArray("records");
        JSONArray finalRecords = new JSONArray();

        //过滤出有用的字段，并获得对应的中文注释，用于前端显示
        JSONObject validField_eng2chn = SofnServiceAssist.getValidFields(indexName, oriRecords, tableFieldName2Chn);
        for (Object obj : oriRecords) {
            JSONObject jsonObject = JSONObject.parseObject(obj.toString());
            JSONObject validObj = new JSONObject();
            for (String key : validField_eng2chn.keySet()) {
                validObj.put(key, jsonObject.get(key));
            }
            finalRecords.add(validObj);
        }

        Map result = new HashMap();
        result.put("tableName", tableName);
        result.put("indexName", indexName);
        result.put("eng2chn", validField_eng2chn);
        result.put("records", finalRecords);
        return result;
    }

    /**
     * 从es搜索关键字，将搜索结果缓存到redis
     */
    private void generateKeywordToRedis(Jedis jedis, String keyword, String startDate, String endDate, String redisKey) {
        //构建关键字查询的条件
        QueryBuilder query = QueryBuilders.queryStringQuery(keyword).defaultOperator(Operator.AND);
        //遍历es中所有索引
        for (String indexName : indices) {
            SearchResponse response = client.prepareSearch(indexName).setQuery(query).setSize(QUERY_SIZE).get();

            List records = new ArrayList(); //保存当前索引库下满足条件的记录
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> sourceMap = hit.getSourceAsMap();
                if (SofnServiceAssist.containKeyword(keyword, sourceMap)
                        && SofnServiceAssist.isValidDate(startDate, endDate, sourceMap, date_fields)) {
                    records.add(hit.getSourceAsString());
                }
            }
            if (records.size() == 0) continue;

            JSONObject curr = new JSONObject();
            curr.put("indexName", indexName);   //索引名
            curr.put("records", records);   //记录
            curr.put("tableName", tableName2Chn.get(indexName));//索引名对应的中文名
            curr.put("oneIndexCount", records.size()); //当前索引的有效记录数
            curr.put("selectFields", SofnServiceAssist.getValidFields(indexName, records, tableFieldName2Chn)); //当前索引有有效字段
            jedis.rpush(redisKey, curr.toJSONString());
        }
    }

    /**
     * 解析，用于分页显示
     */
    private void parseRedisInfo(List<String> jsonInfo, String keyword, List<JSONObject> list) {
        for (String str : jsonInfo) {
            JSONObject eachObject = JSONObject.parseObject(str);
            String indexName = eachObject.getString("indexName");
            JSONArray oriRecords = eachObject.getJSONArray("records");

            //采集部分数据，用于前端结果页面效果展示. 每个索引库至多显示10条
            JSONArray exampleShowRecords = new JSONArray();
            eachObject.put("showRecords", exampleShowRecords);
            for (Object obj : oriRecords) {
                JSONObject record = JSONObject.parseObject(obj.toString());
                Set<String> keys = record.keySet();
                String keyword2key = SofnServiceAssist.keywordHighLight(keyword, record);
                //除包含关键字段信息外，额外展示两个字段的内容
                JSONObject showObj = new JSONObject();
                showObj.put(keyword2key, record.get(keyword2key));
                for (String key : keys) {
                    Object value = record.get(key);
                    if (value != null && !StringUtils.isEmpty(value.toString()) && !keyword2key.equals(key)) {
                        showObj.put(key, value);
                    }
                    if (showObj.size() > 3) {
                        break;
                    }
                }
                JSONObject replaceObj = SofnServiceAssist.replaceKeyUseCHN(indexName, showObj, tableFieldName2Chn);
                exampleShowRecords.add(replaceObj);
                if (exampleShowRecords.size() == 10) break;
            }
            list.add(eachObject);
        }
    }

    public String exportXls(Jedis jedis, String keyword, String startDate, String endDate, int pageIndex, String exportItems) {
        //step1: 先拿到当前关键字查询到的所有结果
        String redisKey = REDIS_KEY_KEYWORD_PREFIX + keyword + startDate + endDate;
        //拿去指定索引里对应的信息
        List<String> list = jedis.lrange(redisKey, 0, -1);;
        if (list.size() == 0) {
            generateKeywordToRedis(jedis, keyword, startDate, endDate, redisKey);
            list = jedis.lrange(redisKey, 0, -1);
        }

        //step2 解析出我们需要导出的数据
        Map<String, List<String>> index2FieldsMap = new HashMap<>();
        JSONObject index2Fields = JSONObject.parseObject(exportItems);
        for (Map.Entry entry : index2Fields.entrySet()) {
            String indexName = (String) entry.getKey();
            String[] fields = entry.getValue().toString().split(",");
            List<String> fieldsList = new ArrayList<>(Arrays.asList(fields));
            index2FieldsMap.put(indexName, fieldsList);
        }

        //step3.定义输出文件
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(keyword);

        //step4 执行导出
        for (String oneTableInfo : list) {
            JSONObject currTable = JSONObject.parseObject(oneTableInfo);
            String tableName = currTable.getString("tableName");
            String indexName = currTable.getString("indexName");
            JSONArray oriRecords = currTable.getJSONArray("records");

            if (index2FieldsMap.containsKey(indexName)) {
                List<String> exportFields = index2FieldsMap.get(indexName);

                //获得标题栏
                List<String> columnNames = new ArrayList<>();
                JSONObject oneRecord = JSONObject.parseObject(oriRecords.get(0).toString());
                for (Map.Entry<String, Object> entry : oneRecord.entrySet()) {
                    if (exportFields.contains(entry.getKey())) {
                        String colName = tableFieldName2Chn.get(indexName + ":" + entry.getKey());
                        columnNames.add(colName);
                    }
                }

                //获得数据
                List<List> dataList = new ArrayList<>();
                for (Object obj : oriRecords) {
                    JSONObject jsonObject = JSONObject.parseObject(obj.toString());
                    List fieldValues = new ArrayList<>();
                    dataList.add(fieldValues);
                    for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                        if (exportFields.contains(entry.getKey())) {
                            fieldValues.add(entry.getValue());
                        }
                    }
                }
                ExcelTool.ExportNoResponse(wb, sheet, tableName + indexName, columnNames, dataList);
            }
        }

        //写出到文件
        FileOutputStream fout = null;
        String filePath = null;
        try {
            File file = new File(QUERY_EXPORT_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            filePath = QUERY_EXPORT_PATH + keyword + "_" + startDate + "_" + endDate + ".xls";
            fout = new FileOutputStream(filePath);
            wb.write(fout);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
