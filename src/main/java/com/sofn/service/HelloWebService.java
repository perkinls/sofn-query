package com.sofn.service;

import com.sofn.utils.ESUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

@Service("helloWebService")
public class HelloWebService {
    private static final int pageSize=10;

    public SearchResponse searchResponse(String keyword, int pageIndex) {
        TransportClient client = ESUtil.getClient();
        QueryBuilder query= QueryBuilders.queryStringQuery(keyword).defaultOperator(Operator.AND);

        //搜索结果存入SearchResponse
        return client.prepareSearch("*")
                .setQuery(query) //设置查询器
                .setSize(pageSize).setFrom(pageSize*pageIndex)    //一次查询文档数
                .get();
    }
}
