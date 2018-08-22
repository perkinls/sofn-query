package com.sofn.service;

import com.sofn.entity.TableFieldInfo;
import com.sofn.entity.TableInfo;
import com.sofn.utils.ESUtil;
import com.sofn.utils.RedisUtil;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.stats.IndexStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.*;

@Service("helloWebService")
public class HelloWebService {
    private static final int QUERY_SIZE=10000;
    private static String REDIS_KEY_TABLE_PREFIX="TABLE_INFO: ";
    private static String REDIS_KEY_FIELD_PREFIX="FIELD_INFO: ";
    private static int REDIS_KEY_EXPIRE_SECONDS=3600;

    public List<SearchResponse> searchES(String keyword) {
        TransportClient client = ESUtil.getClient();
        QueryBuilder query= QueryBuilders.queryStringQuery(keyword).defaultOperator(Operator.AND);
        ActionFuture<IndicesStatsResponse> isr = client.admin().indices().stats(new IndicesStatsRequest().all());
        Set<String> set = isr.actionGet().getIndices().keySet();

        List list = new ArrayList<>();
        for(String indexName: set){
            SearchResponse response = client.prepareSearch(indexName)
                    .setQuery(query)
                    .setSize(QUERY_SIZE)
                    .get();
            Map curr = new HashMap();
            List records = new ArrayList();
            for(SearchHit hit:response.getHits()){
                records.add(hit.getSourceAsString());
            }
            if(null != indexName){
                curr.put("indexName",indexName);
                curr.put("records",records);
                if(!RedisUtil.getJedis().exists("TABLE_INFO:"+indexName)){
                    loadTableInfo();
                }
                curr.put("tableName",RedisUtil.getJedis().get(REDIS_KEY_TABLE_PREFIX+indexName));
                list.add(curr);
            }
            list.add(response);
        }
        return list;
    }

    private void loadTableInfo(){
        // TODO: 查询hive元数据库
        List<TableInfo> list = null;
        Jedis jedis = RedisUtil.getJedis();
        for(TableInfo ele:list){
            jedis.setex(REDIS_KEY_TABLE_PREFIX+ele.getTableName(),REDIS_KEY_EXPIRE_SECONDS,ele.getTableDesc());
        }
    }

    private void loadTableFieldInfo(String tableName){
        // TODO: 查询hive元数据库
        List<TableFieldInfo> list = null;
        Jedis jedis = RedisUtil.getJedis();
        for(TableFieldInfo ele:list){
            jedis.setex(REDIS_KEY_TABLE_PREFIX+tableName+"_"+ele.getTableFieldName(),REDIS_KEY_EXPIRE_SECONDS,ele.getTableFieldDesc());
        }
    }
}
