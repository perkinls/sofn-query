package com.sofn.utils;


import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.Map;

/**
 * 公共查询类
 * https://blog.csdn.net/chengyuqiang/article/details/79302432
 * 包括Operator和multiMatchQuery
 */
public class QueryUtil {
    private String index = "index";
    private int size = 3;
    private SearchHits hits;
    private TransportClient client = ESUtil.getClient();

    public QueryUtil(String index, int size) {
        this.index = index;
        this.size = size;
    }

    public QueryUtil query(QueryBuilder query) {
        //搜索结果存入SearchResponse
        SearchResponse response = client.prepareSearch(index)
                .setQuery(query) //设置查询器
                .setSize(size)      //一次查询文档数
                .get();
        this.hits = response.getHits();
        return this;
    }

    public void print() {
        if (hits == null) {
            return;
        }
        for (SearchHit hit : hits) {
            System.out.println("source:" + hit.getSourceAsString());
            System.out.println("index:" + hit.getIndex());
            System.out.println("type:" + hit.getType());
            System.out.println("id:" + hit.getId());
            //遍历文档的每个字段
            Map<String, Object> map = hit.getSourceAsMap();
            for (String key : map.keySet()) {
                System.out.println(key + "=" + map.get(key));
            }
        }
    }
}
