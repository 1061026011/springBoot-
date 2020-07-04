package com.easy.ls.oracle.service;

import com.easy.ls.oracle.dao.SqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2018/12/14.
 */
@Service
public class OracleService {

    @Autowired
    private SqlDao sqlDao;

    public Map<String,Object> getGnById(Map<String,Object> paraMap){
        Map<String,Object> result = new HashMap<>();
        result.put("code",200);
        try{
            List<Map<String,Object>> list = sqlDao.getGnList(paraMap);
            Map<String,Object> data = list.size()==0?null:list.get(0);
            result.put("data",data);
            result.put("code",200);
        }catch(Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("info","系统异常!");
        }
        return result;
    }
    public Map<String,Object> getSqlById(Map<String,Object> paraMap){
        Map<String,Object> result = new HashMap<>();
        result.put("code",200);
        try{
            List<Map<String,Object>> list = sqlDao.getSqlList(paraMap);
            Map<String,Object> data = list.size()==0?null:list.get(0);
            result.put("data",data);
            result.put("code",200);
        }catch(Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("info","系统异常!");
        }
        return result;
    }
    public Map<String,Object> getGnList(Map<String,Object> paraMap){
        Map<String,Object> result = new HashMap<>();
        result.put("code",500);
        try{
            List<Map<String,Object>> list = sqlDao.getGnList(paraMap);
            Integer count = sqlDao.getGnCount(paraMap);
            result.put("data",list);
            result.put("total",count);
            result.put("code",200);
        }catch(Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("info","系统异常!");
        }
        return result;
    }
    public List<Map<String,Object>> getGnListByUrl(Map<String,Object> paraMap){
        return sqlDao.getGnList(paraMap);
    }
    public List<Map<String,Object>> getSqlListByGnId(Map<String,Object> paraMap){
        return sqlDao.getSqlList(paraMap);
    }
    public Map<String,Object> getSqlList(Map<String,Object> paraMap){
        Map<String,Object> result = new HashMap<>();
        result.put("code",500);
        try{
            List<Map<String,Object>> list = sqlDao.getSqlList(paraMap);
            Integer count = sqlDao.getSqlCount(paraMap);
            result.put("data",list);
            result.put("total",count);
            result.put("code",200);
        }catch(Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("info","系统异常!");
        }
        return result;
    }
    public Map<String,Object> addGn(Map<String,Object> paraMap){
        Map<String,Object> result = new HashMap<>();
        result.put("code",500);
        try{
            String id = UUID.randomUUID().toString();
            paraMap.put("ID",id);
            sqlDao.addGn(paraMap);
            result.put("data",id);
            result.put("code",200);
        }catch(Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("info","系统异常!");
        }
        return result;
    }
    public  Map<String,Object> addSql(Map<String,Object> paraMap){
        Map<String,Object> result = new HashMap<>();
        result.put("code",500);
        try{
            String id = UUID.randomUUID().toString();
            paraMap.put("ID",id);
            sqlDao.addSql(paraMap);
            result.put("data",id);
            result.put("code",200);
        }catch(Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("info","系统异常!");
        }
        return result;
    }
    public Map<String,Object> updateGn(Map<String,Object> paraMap){
        Map<String,Object> result = new HashMap<>();
        result.put("code",500);
        try{
            sqlDao.updateGn(paraMap);
            result.put("code",200);
        }catch(Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("info","系统异常!");
        }
        return result;
    }
    public Map<String,Object> updateSql(Map<String,Object> paraMap){
        Map<String,Object> result = new HashMap<>();
        result.put("code",500);
        try{
            sqlDao.updateSql(paraMap);
            result.put("code",200);
        }catch(Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("info","系统异常!");
        }
        return result;
    }
    public Map<String,Object> delGnById(Map<String,Object> paraMap){
        Map<String,Object> result = new HashMap<>();
        result.put("code",500);
        try{
            sqlDao.delGnById(paraMap);
            result.put("code",200);
        }catch(Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("info","系统异常!");
        }
        return result;
    }
    public Map<String,Object> delSqlById(Map<String,Object> paraMap){
        Map<String,Object> result = new HashMap<>();
        result.put("code",500);
        try{
            sqlDao.delSqlById(paraMap);
            result.put("code",200);
        }catch(Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("info","系统异常!");
        }
        return result;
    }
}
