package com.easy.ls.oracle.service;

import com.easy.ls.oracle.dao.SqlDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by wmh on 2018/12/13.
 * 所有的返回结果默认放在data中,只有当sqltype的值为total的时候,值存在total中
 */
@Slf4j
@Service
public class SqlService {
    @Autowired
    private SqlDao sqlDao;
    //增删改(带有查询)
    @Transactional
    public Map<String,Object> updates(List<Map<String,Object>> list){
        String url = "";
        if(null != list && list.size() !=0 && null != list.get(0).get("requestUrl")){
            url = list.get(0).get("requestUrl").toString();
        }
        log.info("-----请求的url:"+url+"-------");
        Map<String,Object> result = new HashMap<>();
        result.put("code",200);
        try {
            if(list != null && list.size() != 0){
                for (int i=0;i<list.size();i++){
                    Map<String,Object> paramMap = list.get(i);
                    String sqlType = paramMap.get("SQLTYPE")==null?"":paramMap.get("SQLTYPE").toString();
                    if(sqlType.equals("insert")){
                        sqlDao.insert(paramMap);
                        log.info("执行insert");
                    }else if(sqlType.equals("delete")){
                        sqlDao.delete(paramMap);
                        log.info("执行delete");
                    }else if(sqlType.equals("update")){
                        sqlDao.update(paramMap);
                        log.info("执行update");
                    }else if(sqlType.equals("oneResult")){
                        result.put("data",sqlDao.oneResult(paramMap));
                        log.info("返回一个结果");
                    }else if(sqlType.equals("select")){
                        result.put("data",sqlDao.select(paramMap));
                        log.info("返回一个结果集");
                    }else if(sqlType.equals("total")){
                        result.put("total",sqlDao.oneResult(paramMap));
                        log.info("查询返回一个值(总行数)");
                    }else if(sqlType.equals("procedure")){//存储过程
                        Set<String> keys = paramMap.keySet();
                        sqlDao.procedure(paramMap);
                        for (String key:keys){
                            paramMap.remove(key);//移除参数
                        }
                        Map<String,Object> data = new HashMap<>();
                        for (String key:paramMap.keySet()){
                            data.put(key,paramMap.get(key));//结果
                        }
                        result.put("data",data);
                        log.info("调用存储过程");
                    }else{
                        log.info("未执行任何操作");
                    }
                }
                result.put("code",200);
                result.put("info","请求成功!");
                log.info("数据更新成功！");
            }else{
                result.put("code",500);
                result.put("info","请求失败!");
                log.info("请求失败！");
            }
        } catch (Exception e) {
            result.put("code",500);
            result.put("info","请求异常!");
            log.error(url+"请求异常!",list,e);
        }

        return result;
    }
    //查询
    public Map<String,Object> querys(List<Map<String,Object>> list){
        String url = "";
        if(null != list && list.size() !=0 && null != list.get(0).get("requestUrl")){
            url = list.get(0).get("requestUrl").toString();
        }
        log.info("-----请求的url:"+url+"-------");
        Map<String,Object> result = new HashMap<>();
        result.put("code",500);
        try {
            if(list != null && list.size() != 0){
                for (int i=0;i<list.size();i++){
                    Map<String,Object> paramMap = list.get(i);
                    String sqlType = paramMap.get("SQLTYPE")==null?"":paramMap.get("SQLTYPE").toString();
                    if(sqlType.equals("select")){
                        List<Map<String,Object>> list0 = sqlDao.select(paramMap);
                        result.put("data",list0);
                        log.info("普通查询返回list");
                    }else if(sqlType.equals("oneResult")){
                        Object obj = sqlDao.oneResult(paramMap);
                        log.info("查询返回一个值");
                        result.put("data",obj);
                    }else if(sqlType.equals("total")){
                        Object obj = sqlDao.oneResult(paramMap);
                        Integer count = Integer.parseInt(obj.toString());
                        result.put("total",count);
                        log.info("查询返回一个值(总行数)");
                    }else if(sqlType.equals("procedure")){//存储过程
                        List<String> keys = new ArrayList<>();
                        for (String key:paramMap.keySet()){
                                keys.add(key);
                        }
                        sqlDao.procedure(paramMap);
                        for (String key:keys){
                            paramMap.remove(key);//移除参数
                        }
                        if(paramMap!=null&&paramMap.keySet().size()!=0){
                            Map<String,Object> data = new HashMap<>();
                            for (String key:paramMap.keySet()){
                                data.put(key,paramMap.get(key));//结果
                            }
                            result.put("data",data);
                        }
                        log.info("调用存储过程");
                    }else{
                        log.info("未执行任何操作");
                    }
                }
                result.put("code",200);
                result.put("info","请求成功!");
                log.info("查询成功！");
            }else{
                result.put("code",500);
                result.put("info","请求失败!");
                log.info("请求失败！");
            }
        } catch (Exception e) {
            result.put("code",500);
            result.put("info","请求异常!");
            log.error(url+"请求异常",list,e);
        }
        return result;
    }

}
