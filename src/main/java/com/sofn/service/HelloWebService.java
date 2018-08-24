package com.sofn.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sofn.utils.ESUtil;
import com.sofn.utils.MysqlUtil;
import com.sofn.utils.RedisUtil;
import com.sofn.utils.StringUtils;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

@Service("helloWebService")
public class HelloWebService {
    private static final int QUERY_SIZE = 10000; //每次从索引库查出的记录数上限
    private static final int pageSize = 10; //每页显示10个索引库的数据
    private static String REDIS_KEY_TABLE_PREFIX = "TABLE_INFO:";
    private static String REDIS_KEY_FIELD_PREFIX = "FIELD_INFO:";
    private static final String REDIS_KEY_KEYWORD_PREFIX = "KEYWORD:";
    private static final int REDIS_KEYWORD_EXPIRE_SECONDS = 300; //5分钟过期

    private static List<String> indices = null; //保存es的所有索引库名
    private static TransportClient client;
    private static Jedis jedis;


    public Map getRecordsByIndex(String keyword, int index, int pageIndex) {
        index = pageIndex*pageSize+index;
        String currMapInfo = null;
        try{
            currMapInfo = jedis.lrange(REDIS_KEY_KEYWORD_PREFIX+keyword,index,index).get(0);
        }catch (Exception e){
            //异常原因可能是key已经过期，需要重新查询
            searchES(keyword, pageIndex);
            currMapInfo = jedis.lrange(REDIS_KEY_KEYWORD_PREFIX+keyword,index,index).get(0);
        }

        Map result = new HashMap();
        JSONObject currTable = JSONObject.parseObject(currMapInfo);
        String tableName = currTable.getString("tableName");
        String indexName = currTable.getString("indexName");
        JSONArray oriRecords = currTable.getJSONArray("records");
        JSONArray finalRecords = new JSONArray();

        result.put("tableName",tableName);
        result.put("indexName",indexName);

        //过滤出有用的字段，用于前端显示
        Set<String> invalidFields = new HashSet<>();
        for(Object obj:oriRecords){
            JSONObject jsonObject = JSONObject.parseObject(obj.toString());
            for(Map.Entry<String,Object> entry:jsonObject.entrySet()){
                Object value = entry.getValue();
                if(null == value || StringUtils.isEmpty(value.toString())){
                    invalidFields.add(entry.getKey());
                }
            }
        }
        for(Object obj:oriRecords){
            JSONObject jsonObject = JSONObject.parseObject(obj.toString());
            for(String key:invalidFields){
                jsonObject.remove(key);
            }
            JSONObject replaceObj = replaceKeyUseCHN(indexName, jsonObject);
            finalRecords.add(replaceObj);
        }
        result.put("records",finalRecords);
        return result;
    }

    /**
     * key即fieldName替换成中文
     */
    private JSONObject replaceKeyUseCHN(String indexName, JSONObject jsonObject) {
        JSONObject replaceObj = new JSONObject();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String CHN_Name = jedis.get(REDIS_KEY_FIELD_PREFIX + indexName + "_" + entry.getKey());
            replaceObj.put(CHN_Name, entry.getValue());
        }
        return replaceObj;
    }

    public Map searchES(String keyword, int pageIndex) {
        Map result = new HashMap();
        long indexCount = 0;
        long recordCount = 0;

        //先去redis缓存查找
        if (jedis.exists(REDIS_KEY_KEYWORD_PREFIX + keyword)) {
            jedis.expire(REDIS_KEY_KEYWORD_PREFIX + keyword, REDIS_KEYWORD_EXPIRE_SECONDS);
            List<String> redisRes = jedis.lrange(REDIS_KEY_KEYWORD_PREFIX + keyword, pageIndex * pageSize, pageIndex * pageSize + pageSize - 1);
            result.put("list", parseObj(redisRes, keyword));
            result.put("indexCount",jedis.llen(REDIS_KEY_KEYWORD_PREFIX + keyword));
            for(String info:jedis.lrange(REDIS_KEY_KEYWORD_PREFIX + keyword,0,-1)){
                recordCount += JSONObject.parseObject(info).getJSONArray("records").size();
            }
            result.put("recordCount",recordCount);
            return result;
        }

        QueryBuilder query = QueryBuilders.queryStringQuery(keyword).defaultOperator(Operator.AND);
        for (String indexName : indices) {
            SearchResponse response = client.prepareSearch(indexName)
                    .setQuery(query)
                    .setSize(QUERY_SIZE)
                    .get();
            if (response.getHits().totalHits == 0) continue;

            JSONObject curr = new JSONObject();
            List records = new ArrayList();
            for (SearchHit hit : response.getHits()) {
                if(judgeResourceContainKeyword(keyword,hit.getSourceAsMap())){
                    records.add(hit.getSourceAsString());
                }
            }
            if(records.size() == 0) continue;
            indexCount++;
            recordCount += records.size();
            curr.put("indexName", indexName);
            curr.put("records", records);
            curr.put("oneIndexCount",records.size());
            curr.put("tableName", jedis.get(REDIS_KEY_TABLE_PREFIX + indexName));
            jedis.rpush(REDIS_KEY_KEYWORD_PREFIX + keyword, curr.toJSONString());
        }
        jedis.expire(REDIS_KEY_KEYWORD_PREFIX + keyword, REDIS_KEYWORD_EXPIRE_SECONDS);
        List<String> redisRes = jedis.lrange(REDIS_KEY_KEYWORD_PREFIX + keyword, pageIndex * pageSize, pageIndex * pageSize + pageSize - 1);
        result.put("list", parseObj(redisRes, keyword));
        result.put("recordCount",recordCount);
        result.put("indexCount",indexCount);
        return result;
    }

    /**
     * 能够找到关键字的索引库个数
     * @param keyword
     * @return
     */
    public long keywordIndexCount(String keyword) {
        return jedis.llen(REDIS_KEY_KEYWORD_PREFIX + keyword);
    }

    /**
     * 判断resource信息是否直接包含关键字
     * @param keyword
     * @param resourceMap
     * @return
     */
    public boolean judgeResourceContainKeyword(String keyword, Map<String,Object> resourceMap){
        for(Object obj:resourceMap.values()){
            if(obj != null && obj.toString().contains(keyword)){
                return true;
            }
        }
        return false;
    }

    /**
     * 能够找到关键字的总记录数
     * @param keyword
     * @return
     */
    public long keywordRecordCount(String keyword) {
        return jedis.llen(REDIS_KEY_KEYWORD_PREFIX + keyword);
    }


    /**
     * 解析
     * @param jsonInfo
     * @return
     */
    private List<JSONObject> parseObj(List<String> jsonInfo, String keyword) {
        List<JSONObject> list = new ArrayList<>();
        for (String str : jsonInfo) {
            JSONObject eachObject = JSONObject.parseObject(str);
            String indexName = eachObject.getString("indexName");
            JSONArray oriRecords = eachObject.getJSONArray("records");

            //采集部分数据，用于前端结果页面效果展示. 每个索引库至多显示10条
            JSONArray exampleShowRecords = new JSONArray();
            eachObject.put("showRecords",exampleShowRecords);
            for(Object obj:oriRecords){
                JSONObject record = JSONObject.parseObject(obj.toString());
                Set<String> keys = record.keySet();
                String keyword2key = keywordHighLight(keyword, record);
                //除包含关键字段信息外，额外展示两个字段的内容
                JSONObject showObj = new JSONObject();
                showObj.put(keyword2key,record.get(keyword2key));
                for(String key:keys){
                    Object value = record.get(key);
                    if(value!=null && !StringUtils.isEmpty(value.toString()) && !keyword2key.equals(key)){
                        showObj.put(key,value);
                    }
                    if(showObj.size()>3) {
                        break;
                    }
                }
                JSONObject replaceObj = replaceKeyUseCHN(indexName, showObj);
                exampleShowRecords.add(replaceObj);
                if(exampleShowRecords.size()==10) break;
            }
            list.add(eachObject);
        }
        return list;
    }

    /**
     * 关键字高亮
     * @param keyword
     * @param jsonObject
     */
    private String keywordHighLight(String keyword, JSONObject jsonObject) {
        Set<String> keys = jsonObject.keySet();
        for(String key:keys){
            if(null == jsonObject.get(key)) continue;
            String value = jsonObject.get(key).toString();
            if(value.contains(keyword)){
                value = value.replace(keyword, "<span style='color:red;'>" + keyword + "</span>");
                jsonObject.put(key,value);
                return key;
            }
        }
        return null;
    }

    //静态加载，暂时这样处理
    static {
        client = ESUtil.getClient();
        jedis = RedisUtil.getJedis();
        loadTableInfo();
        loadTableFieldInfo();
        loadIndices();
    }


    /**
     * 从mysql里读取数据，加载索引库名对应的中文注释到redis
     */
    private static void loadTableInfo() {
        Connection connection = MysqlUtil.getConn();
        try {
            String sql = "select name,comment from sofn_table_info;";
            ResultSet rs = connection.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                String tableName = rs.getString(1);
                String comment = rs.getString(2);
                jedis.set(REDIS_KEY_TABLE_PREFIX + tableName, comment);
                jedis.persist(REDIS_KEY_TABLE_PREFIX + tableName);//设置永久有效
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从mysql里读取数据，加载索引库下的所有字段的的中文注释到redis
     */
    private static void loadTableFieldInfo() {
        Connection connection = MysqlUtil.getConn();
        try {
            String sql = "select a.name,a.comment,b.name from sofn_field_info a join sofn_table_info b on a.tableId=b.id;";
            ResultSet rs = connection.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                String fieldName = rs.getString(1);
                String comment = rs.getString(2);
                String tableName = rs.getString(3);
                jedis.set(REDIS_KEY_FIELD_PREFIX + tableName + "_" + fieldName, comment);
                jedis.persist(REDIS_KEY_FIELD_PREFIX + tableName + "_" + fieldName);//设置永久
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载索引库的所有索引名
     *
     * @return
     */
    private static List<String> loadIndices() {
        if (null == indices) {
            ActionFuture<IndicesStatsResponse> isr = client.admin().indices().stats(new IndicesStatsRequest().all());
            Set<String> set = isr.actionGet().getIndices().keySet();
            indices = new ArrayList<String>(set);
        }
        return indices;
    }
}
