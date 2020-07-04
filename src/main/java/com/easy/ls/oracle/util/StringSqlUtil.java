package com.easy.ls.oracle.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mvel2.MVEL;

import java.util.HashMap;
import java.util.Map;

/**
 * autor:wmh
 * DATE:2019/1/2
 */
public class StringSqlUtil {
    /*public static void main(String[] args) {
        String sql="select * from user where 1=1<if test=\"username != null && username !=''\">and username=#{username}</if><if test=\"usersex != null && usersex !=''\">and usersex=#{usersex}</if>";
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("username", "小明");
        dataMap.put("usersex", "男");
        System.err.println(getSql(sql,dataMap));
        //mvel();
    }*/
    //mvel表达式测试
    public static void mvel(){
        try {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("username", "小明");
            Object obj = MVEL.eval("username0!='小明a'.toString() && username0 !='ss'",dataMap);
            System.out.println(obj);
            System.out.println("obj:"+((boolean)obj==true));
        } catch (Exception e) {

        }

    }
    //jsoup解析xml字符串测试
    public static void jsoup(){
        //字符串文本
        String str = "垃圾垃圾来得及sddfsd<ss test=\"pression\"></ss>";
        //使用Jsoup转化为doc
        Document doc = Jsoup.parse(str);
        //根据标签名来查找节点，此处要查找的是<img>标签
        Elements element =  doc.getElementsByTag("ss");
        if(!element.isEmpty()){
            for(int i = 0; i < element.size(); i++){
                System.out.println("=========");
                Element ele = element.get(i); //获取每一个ss标签
                String src = ele.attr("test"); //获取ss标签的test值
                System.out.println(ele); //ss标签的整个内容
                //在img标签前添加其他html
                //(注：此处jsoup会自动添加闭合标签，如你添加<a>,jsoup会自动添加</a>闭合)
                ele.before("<tttt>测试</tttt>");
                //移除该标签
                ele.remove();
                System.out.println(ele); //ss标签的整个内容

            }
        }
    }
    //sql解析
    public static String getSql(String sql,Map<String,Object> paramMap){
        //使用Jsoup转化为doc
        Document doc = Jsoup.parse(sql);
        //转化后的sql文本;
        String newsql = doc.getElementsByTag("body").get(0).html();
        //根据标签名来查找节点，此处要查找的是<if>标签
        Elements element =  doc.getElementsByTag("if");
        if(!element.isEmpty()){
            for(int i = 0; i < element.size(); i++){
                Element ele = element.get(i); //获取每一个if标签
                String text = ele.text();
                String test = ele.attr("test"); //获取if标签的test值(表达式)
                boolean bool = getif(test,paramMap);
                if(bool){//以标签内的内容替换整个标签
                    newsql = newsql.replace(ele.toString(), " "+text);
                }else{//以空字符串替换标签内容
                    newsql = newsql.replace(ele.toString(), "");
                }
						/*System.out.println(test);
						System.out.println(ele); //if标签的整个内容
*/
            }
        }
        return newsql;
    }
    //表达式有误时默认返回false
    public static boolean getif(String exprission,Map<String, Object> paramMap){
        boolean bool = false;
        try {
            bool = (boolean)MVEL.eval(exprission,paramMap);
        } catch (Exception e) {
            bool = false;
        }
        return bool;

    }
}
