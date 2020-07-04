package com.easy.ls.oracle.controller;

import com.easy.ls.oracle.service.OracleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by wmh on 2018/12/14.
 */
@RequestMapping("/oracle")
@RestController
public class OracleController {
    @Autowired
    private OracleService testService;

    @RequestMapping("/getGnById")
    @ResponseBody
    public Map<String, Object> getGnById(@RequestParam Map<String,Object> paramMap){
        return testService.getGnById(paramMap);
    }
    @RequestMapping("/getSqlById")
    @ResponseBody
    public Map<String, Object> getSqlById(@RequestParam Map<String,Object> paramMap){
        return testService.getGnById(paramMap);
    }
    @RequestMapping("/getGnList")
    @ResponseBody
    public Map<String, Object> getGnList(@RequestParam Map<String,Object> paramMap){
        return testService.getGnList(paramMap);
    }
    @RequestMapping("/getSqlList")
    @ResponseBody
    public Map<String, Object> getSqlList(@RequestParam Map<String,Object> paramMap){
        return testService.getSqlList(paramMap);
    }
    @RequestMapping("/addGn")
    @ResponseBody
    public Map<String, Object> addGn(@RequestParam Map<String,Object> paramMap){
        return testService.addGn(paramMap);
    }
    @RequestMapping("/addSql")
    @ResponseBody
    public Map<String, Object> addSql(@RequestParam Map<String,Object> paramMap){
        return testService.addSql(paramMap);
    }
    @RequestMapping("/updateGn")
    @ResponseBody
    public Map<String, Object> updateGn(@RequestParam Map<String,Object> paramMap){
        return testService.updateGn(paramMap);
    }
    @RequestMapping("/updateSql")
    @ResponseBody
    public Map<String, Object> updateSql(@RequestParam Map<String,Object> paramMap){
        return testService.updateSql(paramMap);
    }
    @RequestMapping("/delGnById")
    @ResponseBody
    public Map<String, Object> delGnById(@RequestParam Map<String,Object> paramMap){
        return testService.delGnById(paramMap);
    }
    @RequestMapping("/delSqlById")
    @ResponseBody
    public Map<String, Object> delSqlById(@RequestParam Map<String,Object> paramMap){
        return testService.delSqlById(paramMap);
    }
}
