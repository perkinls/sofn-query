package com.sofn.controller;

import com.alibaba.fastjson.JSONArray;
import com.sofn.service.HelloWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
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
        List list = helloWebService.searchES(keyword, pageIndex);
        long totalSize = helloWebService.keywordRecordCount(keyword);
        result.put("list",list);
        result.put("totalSize",totalSize);
        result.put("keyword",keyword);
        result.put("pageIndex",pageIndex);
        model.addAttribute("result",result);
        return "result";
    }



    @RequestMapping("detail")
    public String detail(HttpServletRequest request, Model model){
        //读取请求参数
        int index = Integer.parseInt(request.getParameter("index"));
        String keyword = request.getParameter("keyword");
        int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));

        Map currMap = helloWebService.getRecordsByIndex(keyword, index, pageIndex);
        model.addAttribute("currMap",currMap);
        return "detail";
    }


}
