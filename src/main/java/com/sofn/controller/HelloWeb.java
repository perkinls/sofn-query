package com.sofn.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sofn.service.HelloWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        int pageIndex = null==pageIndex_info?0:Integer.parseInt(pageIndex_info);

        //封装返回结果
        Map result = new HashMap();

        //查询ES索引库
        Map searchResult = helloWebService.searchES(keyword, pageIndex, startDate, endDate);
        result.put("list",searchResult.get("list"));
        result.put("indexCount",searchResult.get("indexCount"));
        result.put("recordCount",searchResult.get("recordCount"));
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
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        Map currMap = helloWebService.getRecordsByIndex(keyword, index, pageIndex, startDate, endDate);
        model.addAttribute("currMap",currMap);
        return "detail";
    }


}
