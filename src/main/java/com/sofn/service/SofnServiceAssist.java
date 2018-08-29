package com.sofn.service;

import com.alibaba.fastjson.JSONObject;
import com.sofn.utils.MysqlUtil;
import com.sofn.utils.StringUtils;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.client.transport.TransportClient;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

public class SofnServiceAssist {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 初始化时间相关的字段，该字段用于过滤出搜索结果有效时间范围的数据
     */
    protected static List<String> getDateFields() {
        String[] dateFields = {"create_date", "create_time", "inspection_time", "approve_time", "register_time",
                "cred_time", "check_time", "confirm_time", "task_date", "produce_date", "sample_date",
                "check_report_time", "report_time", "sample_report_time", "upload_time", "receipt_time",
                "enforce_law_time", "sale_time", "print_time", "createtime", "createdate"};
        return new ArrayList<>(Arrays.asList(dateFields));
    }

    /**
     * 获得索引库的所有索引名
     */
    protected static List<String> getIndices(TransportClient client) {
            ActionFuture<IndicesStatsResponse> isr = client.admin().indices().stats(new IndicesStatsRequest().all());
            Set<String> set = isr.actionGet().getIndices().keySet();
            return new ArrayList<>(set);
    }

    /**
     * 从mysql里读取数据，加载索引库名对应的中文注释内存
     */
    protected static Map<String,String> getTableInfoFromMysql() {
        Map<String,String> tableName2Chn = new HashMap<>();
        Connection connection = MysqlUtil.getConn();
        try {
            String sql = "select name,comment from sofn_table_info;";
            ResultSet rs = connection.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                String tableName = rs.getString(1);
                String comment = rs.getString(2);
                tableName2Chn.put(tableName,comment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableName2Chn;
    }

    /**
     * 从mysql里读取数据，加载索引库下的所有字段到内存
     */
    protected static Map<String,String> getTableFieldInfoFromMysql() {
        Map<String,String> tableFieldName2Chn = new HashMap<>();
        Connection connection = MysqlUtil.getConn();
        try {
            String sql = "select a.name,a.comment,b.name from sofn_field_info a join sofn_table_info b on a.tableId=b.id;";
            ResultSet rs = connection.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                String fieldName = rs.getString(1);
                String comment = rs.getString(2);
                String tableName = rs.getString(3);
                tableFieldName2Chn.put(tableName + ":" + fieldName, comment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableFieldName2Chn;
    }

    /**
     * 判断source信息是否直接包含关键字
     */
    protected static boolean containKeyword(String keyword, Map<String,Object> resourceMap){
        for(Object obj:resourceMap.values()){
            if(obj != null && obj.toString().contains(keyword)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否在有效时间范围内
     */
    protected static boolean isValidDate(String startDateInfo,String endDateInfo, Map<String,Object> sourceMap, List<String> date_fields){
        //不考虑时间范围
        if(StringUtils.isEmpty(startDateInfo) && StringUtils.isEmpty(endDateInfo)){
            return true;
        }

        boolean result = true;
        try {
            Date startDate = StringUtils.isEmpty(startDateInfo) ? null: sdf.parse(startDateInfo);
            Date endDate = StringUtils.isEmpty(endDateInfo)? null : sdf.parse(endDateInfo);
            if(null != endDate) endDate.setDate(endDate.getDate()+1);

            for(Map.Entry<String,Object> entry:sourceMap.entrySet()){
                String key = entry.getKey();
                if(key.endsWith("time") || key.endsWith("date")){
                    for(String dateField: date_fields){
                        if(dateField.equals(entry.getKey()) && entry.getValue() != null){
                            Date currDate = sdf.parse(entry.getValue().toString());
                            if(startDate != null)   result=result&&currDate.after(startDate);
                            if(endDate != null)     result=result&&currDate.before(endDate);
                            return result;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关键字高亮
     */
    protected static String keywordHighLight(String keyword, JSONObject jsonObject) {
        Set<String> keys = jsonObject.keySet();
        for (String key : keys) {
            if (null == jsonObject.get(key)) continue;
            String value = jsonObject.get(key).toString();
            if (value.contains(keyword)) {
                value = value.replace(keyword, "<span style='color:red;'>" + keyword + "</span>");
                jsonObject.put(key, value);
                return key;
            }
        }
        return null;
    }

    /**
     * key即fieldName替换成中文
     */
    protected static JSONObject replaceKeyUseCHN(String indexName, JSONObject jsonObject, Map<String,String> map) {
        JSONObject replaceObj = new JSONObject();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String CHN_Name = map.get(indexName + ":" + entry.getKey());
            replaceObj.put(CHN_Name, entry.getValue());
        }
        return replaceObj;
    }

    /**
     * 获得有效的字段及对应的中文注释，  当每条记录的某个字段的值都为无效值时，直接过滤掉
     */
    protected static JSONObject getValidFields(String indexName, List records, Map<String,String> map) {
        JSONObject result = new JSONObject();
        for (Object obj : records) {
            JSONObject jsonObject = JSONObject.parseObject(obj.toString());
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                Object value = entry.getValue();
                if (null != value && !StringUtils.isEmpty(value.toString())) {
                    String chn = map.get(indexName + ":" + entry.getKey());
                    result.put(entry.getKey(),chn);
                }
            }
        }
        return result;
    }

    /**
     * 匹配到结果的索引库的条数
     */
    protected static int getKeywordIndexCount(Jedis jedis, String redisKey) {
        return jedis.llen(redisKey).intValue();
    }

    /**
     * 匹配到结果的总记录条数
     */
    protected static int getKeywordRecordCount(Jedis jedis, String redisKey) {
        int recordCount = 0;
        for (String info : jedis.lrange(redisKey, 0, -1)) {
            recordCount += JSONObject.parseObject(info).getJSONArray("records").size();
        }
        return recordCount;
    }
}
