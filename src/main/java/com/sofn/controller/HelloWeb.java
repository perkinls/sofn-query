package com.sofn.controller;

import com.sofn.service.HelloWebService;
import com.sofn.utils.RedisUtil;
import org.elasticsearch.action.search.SearchResponse;
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

        //封装返回结果
        Map result = new HashMap();

        //查询ES索引库
        List list = helloWebService.searchES(keyword);
        result.put("list",list);
        result.put("size",list.size());
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
