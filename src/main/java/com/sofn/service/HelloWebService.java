package com.sofn.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sofn.utils.ESUtil;
import com.sofn.utils.MysqlUtil;
import com.sofn.utils.RedisUtil;
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


    public List<String> getHeadsByIndex(String keyword, int pageIndex) {
        // TODO
        return null;
    }

    public List<Object> getRecordsByIndex(String keyword, int pageIndex, List<String> heads) {
        //TODO
        return null;
    }

    public List<Object> searchES(String keyword, int pageIndex) {
        //先去redis缓存查找
        if (jedis.exists(REDIS_KEY_KEYWORD_PREFIX + keyword)) {
            jedis.expire(REDIS_KEY_KEYWORD_PREFIX + keyword, REDIS_KEYWORD_EXPIRE_SECONDS);
            List<String> redisRes = jedis.lrange(REDIS_KEY_KEYWORD_PREFIX + keyword, pageIndex * pageSize, pageIndex * pageSize + pageSize - 1);
            return parseObj(redisRes);
        }

        QueryBuilder query = QueryBuilders.queryStringQuery(keyword).defaultOperator(Operator.AND);
        List list = new ArrayList<>();
        for (String indexName : indices) {
            SearchResponse response = client.prepareSearch(indexName)
                    .setQuery(query)
                    .setSize(QUERY_SIZE)
                    .get();
            if (response.getHits().totalHits == 0) continue;

            JSONObject curr = new JSONObject();
            List records = new ArrayList();
            for (SearchHit hit : response.getHits()) {
                records.add(hit.getSourceAsString());
            }
            curr.put("indexName", indexName);
            curr.put("records", JSON.toJSONString(records));
            curr.put("tableName", jedis.get(REDIS_KEY_TABLE_PREFIX + indexName));

            list.add(curr);
            jedis.rpush(REDIS_KEY_KEYWORD_PREFIX + keyword, curr.toJSONString());
        }
        jedis.expire(REDIS_KEY_KEYWORD_PREFIX + keyword, REDIS_KEYWORD_EXPIRE_SECONDS);
        List<String> redisRes = jedis.lrange(REDIS_KEY_KEYWORD_PREFIX + keyword, pageIndex * pageSize, pageIndex * pageSize + pageSize - 1);
        return parseObj(redisRes);
    }

    public long keywordRecordCount(String keyword) {
        return jedis.llen(REDIS_KEY_KEYWORD_PREFIX + keyword);
    }


    private List<Object> parseObj(List<String> jsonInfo) {
        List<Object> list = new ArrayList<>();
        for (String str : jsonInfo) {
            JSONObject jsonObject = JSONObject.parseObject(str);
            list.add(jsonObject);
        }
        return list;
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
