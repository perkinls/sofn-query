package com.sofn.controller;



import com.alibaba.fastjson.JSONObject;
import com.sofn.utils.ESUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HelloWeb {
    private static final int pageSize=10;

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("advanced")
    public String advanced(){
        return "advanced";
    }

    @RequestMapping("result")
    public String result(HttpServletRequest request, Model model){
        String keyword = request.getParameter("keyword");
        String pageIndex_info = request.getParameter("pageIndex");
        int pageIndex = null==pageIndex_info?0:Integer.parseInt(pageIndex_info);
        TransportClient client = ESUtil.getClient();
        QueryBuilder query= QueryBuilders.queryStringQuery(keyword).defaultOperator(Operator.AND);

        //搜索结果存入SearchResponse
        SearchResponse response=client.prepareSearch("*")
                .setQuery(query) //设置查询器
                .setSize(pageSize).setFrom(pageSize*pageIndex)    //一次查询文档数
                .get();
        Map result = new HashMap();
        List list = new ArrayList();
        for(SearchHit hit:response.getHits()){
            Map<String,Object> map=new HashMap<>();
            String resource = hit.getSourceAsString().replace(keyword,"<span style='color:red;'>" + keyword + "</span>");
            map.put("source",resource);
            map.put("index",hit.getIndex());
            JSONObject jsonObject = JSONObject.parseObject(resource);

            String keywordFiled = null;
            for(String key:jsonObject.keySet()){
                if(null == jsonObject.get(key)) continue;
                String value = jsonObject.get(key).toString();
                if(value.contains(keyword)){
                    keywordFiled = key+": "+value;
                    break;
                }
            }

            map.put("keywordFiled",keywordFiled);
            list.add(map);
        }
        result.put("list",list);
        result.put("took",response.getTook());
        result.put("size",response.getHits().totalHits);
        result.put("keyword",keyword);
        result.put("pageIndex",pageIndex);
        model.addAttribute("result",result);
        return "result";
    }

    @RequestMapping("detail")
    public String detail(){
        return "detail";
    }


}
