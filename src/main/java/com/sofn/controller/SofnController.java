package com.sofn.controller;

import com.sofn.service.SofnService;
import com.sofn.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class SofnController {

    @Autowired
    private SofnService sofnService;

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("advanced")
    public String advanced() {
        return "advanced";
    }

    @RequestMapping("result")
    public String result(@RequestParam String keyword,
                         @RequestParam int pageIndex,
                         @RequestParam String startDate,
                         @RequestParam String endDate, Model model) {
        Jedis jedis = RedisUtil.getJedis();
        //查询ES索引库
        Map searchResult = sofnService.searchES(jedis, keyword, pageIndex, startDate, endDate);

        //封装返回结果
        Map result = new HashMap();
        result.put("list", searchResult.get("list"));
        result.put("indexCount", searchResult.get("indexCount"));
        result.put("recordCount", searchResult.get("recordCount"));
        result.put("keyword", keyword);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("pageIndex", pageIndex);
        model.addAttribute("result",result);
        jedis.close();
        return "result";
    }

    @ResponseBody
    @RequestMapping("getResult")
    public Map getResult(@RequestParam String keyword,
                         @RequestParam int pageIndex,
                         @RequestParam String startDate,
                         @RequestParam String endDate) {
        Jedis jedis = RedisUtil.getJedis();
        //查询ES索引库
        Map searchResult = sofnService.searchES(jedis, keyword, pageIndex, startDate, endDate);

        //封装返回结果
        Map result = new HashMap();
        result.put("list", searchResult.get("list"));
        result.put("indexCount", searchResult.get("indexCount"));
        result.put("recordCount", searchResult.get("recordCount"));
        result.put("keyword", keyword);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("pageIndex", pageIndex);
        jedis.close();
        return result;
    }

    @RequestMapping("detail")
    public String detail(@RequestParam String keyword,
                         @RequestParam int pageIndex,
                         @RequestParam int index, //当前页的第index条
                         @RequestParam String startDate,
                         @RequestParam String endDate, Model model) {
        Jedis jedis = RedisUtil.getJedis();
        Map currMap = sofnService.getRecordsByIndex(jedis, keyword, index, pageIndex, startDate, endDate);
        model.addAttribute("currMap", currMap);
        jedis.close();
        return "detail";
    }

    @ResponseBody
    @RequestMapping("getDetail")
    public Map getDetail(@RequestParam String keyword,
                         @RequestParam int pageIndex,
                         @RequestParam int index, //当前页的第index条
                         @RequestParam String startDate,
                         @RequestParam String endDate) {
        Jedis jedis = RedisUtil.getJedis();
        Map result = sofnService.getRecordsByIndex(jedis, keyword, index, pageIndex, startDate, endDate);
        jedis.close();
        return result;
    }

    @ResponseBody
    @RequestMapping("export")
    public Map Export(@RequestParam String exportItems,
                      @RequestParam int pageIndex,
                      @RequestParam String keyword,
                      @RequestParam String startDate,
                      @RequestParam String endDate) {
        Map result = new HashMap();
        try {
            Jedis jedis = RedisUtil.getJedis();
            String filePath = sofnService.exportXls(jedis, keyword, startDate, endDate, pageIndex, exportItems);
            result.put("flag", "success");
            result.put("filePath", filePath);
            jedis.close();
        }catch (Exception e){
            result.put("flag", "error");
        }
        return result;
    }
}
