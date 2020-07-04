package com.easy.ls.oracle.service;

import com.easy.ls.oracle.util.StringSqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MySqlMethodService {
    @Autowired
    private SqlService sqlService;
    @Autowired
    private OracleService testService;
    public Map<String,Object> myMethod(String url,Map<String,Object> paramMap) throws Exception{
        Map<String,Object> result = null;
        //根据路径查询对应的接口
        paramMap.put("requestUrl",url);//请求路径,用于日志定位
        System.out.println("当前请求路:"+url);
        Map<String,Object> paramMap0 = new HashMap();
        paramMap0.put("URL",url);
        List<Map<String,Object>> gnList = testService.getGnListByUrl(paramMap0);//根据路径获取接口信息
        Map<String,Object> gnMap = gnList!=null&&gnList.size()!=0?gnList.get(0):null;
        if(gnMap==null){
            result = new HashMap<>();
            result.put("code",200);
            result.put("info","接口不存在或接口信息未完善!");
            return result;
        }
        Integer gnid =Integer.parseInt(gnMap.get("ID").toString());
        String fflx = gnMap.get("FFLX").toString();//方法类型
        paramMap0.put("GNID",gnid);
        List<Map<String,Object>> sqlList = testService.getSqlListByGnId(paramMap0);//该功能对应的sql脚本信息
        List<Map<String,Object>> list1 = sqlList ==null?null:sqlList;

        //将前端的参数放到其中
        for (Map<String,Object> m:list1){
            //解析if标签
            String sql = m.get("SQLCONTENT").toString();
            sql = StringSqlUtil.getSql(sql,paramMap);
            m.put("SQLCONTENT",sql);
            for(String key:paramMap.keySet()){
                if(m.get(key)==null){//防止覆盖内置参数
                    m.put(key,paramMap.get(key));
                }
            }
        }
        if(fflx.equals("querys")){//查询
            result =sqlService.querys(list1);
        }else if (fflx.equals("updates")){//更新
            result =sqlService.updates(list1);
        }else{
            result = new HashMap<>();
            result.put("code",200);
            result.put("info","该接口不符合规范！");
        }
        return result;
    }


}
