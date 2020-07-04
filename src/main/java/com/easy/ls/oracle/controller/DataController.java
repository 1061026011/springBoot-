package com.easy.ls.oracle.controller;

import com.easy.ls.oracle.service.SqlService;
import com.easy.ls.oracle.service.OracleService;
import com.easy.ls.oracle.util.ExcelContrast;
import com.easy.ls.oracle.util.FileUtil;
import com.easy.ls.oracle.util.XmlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Administrator on 2018/12/13.
 */
@RestController
@RequestMapping("/data")
public class DataController {
    @Value("${filepath}")
    private String filepath;

    @Autowired
    private SqlService sqlService;
    @Autowired
    private OracleService testService;

    /**
     * 下载Excel模板
     * @param title,column,response,response
     * @return
     */
    @RequestMapping("/download")
    @ResponseBody
    public void downloadExcel(@RequestParam("title") String title, @RequestParam("column") String column, HttpServletResponse response){
        ExcelContrast.download(title,column,response);
    }
    /**
     * 下载文件
     * @param newName,oldName,filepath,response
     * @return
     */
    @RequestMapping(value="/downloadFile",method= RequestMethod.GET)
    @ResponseBody
    public void downloadFile(String newName,String oldName,HttpServletResponse response) {
        System.out.println("oldName:"+oldName);
        try {
            FileUtil.download(newName,oldName,filepath,response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 对数据库操作的公共方法
     */
    @RequestMapping("/{a1}/{a2}")
    @ResponseBody
    public Map<String,Object> myMethod(@PathVariable("a1") String a1, @PathVariable("a2") String a2, @RequestParam Map<String,Object> paramMap, @RequestParam(value = "file",required = false) MultipartFile file, HttpServletResponse response,HttpServletRequest request) throws Exception{
        Map<String,Object> result= null;
        //根据路径查询对应的接口
        String url ="/"+a1+"/"+a2;
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
        String gnid =gnMap.get("ID").toString();
        String fflx = gnMap.get("FFLX").toString();//方法类型
        paramMap0.put("GNID",gnid);
        List<Map<String,Object>> sqlList = testService.getSqlListByGnId(paramMap0);//该功能对应的sql脚本信息
        List<Map<String,Object>> list1 = sqlList ==null?null:sqlList;

        //将前端的参数放到其中
        for (Map<String,Object> m:list1){
            //解析if标签
            String sql = m.get("SQLCONTENT").toString();
            sql = XmlParser.getMySql(sql,paramMap);
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
        }else if (fflx.equals("import")){//导入
            //获取字段信息
            String column = paramMap.get("column").toString();//必需参数
            String name = paramMap.get("name").toString();//必需参数
            String[] columns = column.split(",");
            String[] names = name.split(",");
            Map<String,String> m = new HashMap<>();
            for (int i=0;i<columns.length;i++){
                m.put(columns[i],names[i]);//m.put("中文字段","对应的英文字段");
            }
            //读取Excel
            List<Map<String,Object>> excelList = ExcelContrast.FileExcel(file,m);
            //整理参数(将读取到的值进行处理)
            Map<String,Object> m0 = list1.get(0);
            for (int i=0;i<excelList.size();i++){//遍历参数
                for(String key:m0.keySet()){
                    excelList.get(i).put(key,m0.get(key));
                }
            }
            result =sqlService.updates(excelList);//执行导入
        }else if (fflx.equals("export")){//导出
            result =sqlService.querys(list1);//从数据库中取出数据
            //获取字段信息
            String column = paramMap.get("column").toString();//必需参数
            String name = paramMap.get("name").toString();//必需参数
            String title = paramMap.get("title").toString();//必需参数
            ExcelContrast.export((ArrayList<Map<String,Object>>)result.get("data"),title,column,name,response);
        }else if(fflx.equals("upload")){//文件上传
            Map<String,String> m0= FileUtil.upload(file,filepath);//上传到磁盘
            String oldName =m0.get("oldName");//必需参数
            String newName =m0.get("newName");//必需参数
            //将参数放到其中
            for (Map<String,Object> m:list1){
                m.put("OLDNAME",oldName);//文件表的字段值
                m.put("NEWNAME",newName);//文件表的字段值
            }
            result=sqlService.updates(list1);//文件信息入库
        }else{
            result = new HashMap<>();
            result.put("code",500);
            result.put("info","该接口不不符合规范！");
        }
        return result;
    }

}
