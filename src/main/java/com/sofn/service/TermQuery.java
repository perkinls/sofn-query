package com.sofn.service;

import com.sofn.utils.ESUtil;
import com.sofn.utils.QueryUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.Map;

public class TermQuery {
    /**
     * term查询
     */
    public void termQuery(){
        QueryUtil util=new QueryUtil("website",5);
        //构造查询对象
        QueryBuilder qb=QueryBuilders.termQuery("title","vmware");
        util.query(qb).print();
    }
    /**
     * terms查询
     */
    public void termsQuery(){
        QueryUtil util=new QueryUtil("website",5);
        //构造查询对象
        QueryBuilder qb=QueryBuilders.termsQuery("title","centos","yum");
        util.query(qb).print();
    }
    /**
     * range查询
     */
    public void rangeQuery(){
        QueryUtil util=new QueryUtil("website",5);
        //构造查询对象
        QueryBuilder qb=QueryBuilders.rangeQuery("postdate").from("2017-01-01").to("2017-12-31").format("yyyy-MM-dd");
        util.query(qb).print();
    }
    /**
     * prefix查询
     */
    public void prefixQuery(){
        QueryUtil util=new QueryUtil("my-index",5);
        //构造查询对象
        QueryBuilder qb=QueryBuilders.prefixQuery("name","程");
        util.query(qb).print();
    }
    /**
     * wildcard查询
     */
    public void wildcardQuery(){
        QueryUtil util=new QueryUtil("website",5);
        //构造查询对象
        QueryBuilder qb=QueryBuilders.wildcardQuery("title","*yum*");
        util.query(qb).print();
    }
    /**
     * regexp查询
     */
    public void regexpQuery(){
        QueryUtil util=new QueryUtil("website",5);
        //构造查询对象
        QueryBuilder qb=QueryBuilders.regexpQuery("title","gc.*");
        util.query(qb).print();
    }
    /**
     * fuzzy查询
     */
    public void fuzzyQuery(){
        QueryUtil util=new QueryUtil("website",5);
        //构造查询对象
        QueryBuilder qb=QueryBuilders.fuzzyQuery("title","vmwere");
        util.query(qb).print();
    }
    /**
     * type查询
     */
    public void typeQuery(){
        QueryUtil util=new QueryUtil("website",2);
        //构造查询对象
        QueryBuilder qb=QueryBuilders.typeQuery("blog");
        util.query(qb).print();
    }
    /**
     * ids查询
     */
    public void idsQuery(){
        QueryUtil util=new QueryUtil("subj_detection_detail",2);
        //构造查询对象
        QueryBuilder qb=QueryBuilders.idsQuery().addIds("1","3");
        util.query(qb).print();
    }
    public static void main(String[] args) {
        TransportClient client = ESUtil.getClient();
        //构造查询对象
        QueryBuilder query= QueryBuilders.matchAllQuery();
        //搜索结果存入SearchResponse
        SearchResponse response=client.prepareSearch("subj_detection_detail")
                .setQuery(query) //设置查询器
                .setSize(3)      //一次查询文档数
                .get();
        SearchHits hits=response.getHits();
        for(SearchHit hit:hits){
            System.out.println("source:"+hit.getSourceAsString());
            System.out.println("index:"+hit.getIndex());
            System.out.println("type:"+hit.getType());
            System.out.println("id:"+hit.getId());
            //遍历文档的每个字段
            Map<String,Object> map=hit.getSourceAsMap();
            for(String key:map.keySet()){
                System.out.println(key+"="+map.get(key));
            }
            System.out.println("--------------------");
        }
    }
}
