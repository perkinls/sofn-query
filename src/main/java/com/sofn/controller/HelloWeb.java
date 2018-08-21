package com.sofn.controller;



import com.alibaba.fastjson.JSONObject;
import com.sofn.service.HelloWebService;
import com.sofn.utils.ESUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private HelloWebService helloWebService;

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
        //读取请求参数
        String keyword = request.getParameter("keyword");
        String pageIndex_info = request.getParameter("pageIndex");
        int pageIndex = null==pageIndex_info?0:Integer.parseInt(pageIndex_info);

        //查询ES索引库
        SearchResponse response = helloWebService.searchResponse(keyword, pageIndex);

        //封装返回结果
        Map result = new HashMap();
        List list = new ArrayList();
        for(SearchHit hit:response.getHits()){
            Map<String,Object> map=new HashMap<>();
            String resource = hit.getSourceAsString();
            JSONObject jsonObject = JSONObject.parseObject(resource);

            //关键字高亮处理
            String keywordFiled = null;
            for(String key:jsonObject.keySet()){
                if(null == jsonObject.get(key)) continue;

                String value = jsonObject.get(key).toString();
                if(value.contains(keyword)){
                    value = value.replace(keyword, "<span style='color:red;'>" + keyword + "</span>");
                    resource = resource.replace(keyword,"<span style='color:red;'>" + keyword + "</span>");
                    keywordFiled = key + ": " + value;
                    break;
                }
            }
            map.put("keywordFiled",keywordFiled);
            map.put("source",resource);
            map.put("index",hit.getIndex());
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
