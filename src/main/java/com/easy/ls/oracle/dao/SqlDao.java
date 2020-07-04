package com.easy.ls.oracle.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/13.
 */
@Mapper
public interface SqlDao {
    void insert(Map<String, Object> paramMap);
    void delete(Map<String, Object> paramMap);
    void update(Map<String, Object> paramMap);
    List<Map<String,Object>> select(Map<String, Object> paramMap);
    Object oneResult(Map<String, Object> paramMap);
    void procedure(Map<String, Object> paramMap);

    //本地sql
    Integer getMaxGnId();
    List<Map<String,Object>> getGnList(Map<String, Object> paraMap);
    Integer getGnCount(Map<String, Object> paraMap);
    List<Map<String,Object>> getSqlList(Map<String, Object> paraMap);
    Integer getSqlCount(Map<String, Object> paraMap);
    void addGn(Map<String, Object> paraMap);
    void addSql(Map<String, Object> paraMap);
    void updateGn(Map<String, Object> paraMap);
    void updateSql(Map<String, Object> paraMap);
    void delGnById(Map<String, Object> paraMap);
    void delSqlById(Map<String, Object> paraMap);
}
