package com.easy.ls.oracle.util;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;

import org.dom4j.Element;

import org.dom4j.io.SAXReader;

public class XmlParser{


    public static void main(String[] args) throws Exception {
        String sql = "select * from user " +
                "<where>and id=#{id}<if test='userName != null'>and userName=#{userName}</if></where>"+
                "<set>age=#{age},phone=#{phone},</set>"+
                "";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("userName","张三");
        System.out.println(getMySql(sql,paramMap));

        //getNodeByName(sql);
        // 解析books.xml文件
        // 创建SAXReader的对象reader


    }
    public static String getMySql(String sql,Map<String,Object> paramMap){
        sql = "<root>"+sql+"</root>";
        // 解析books.xml文件
        // 创建SAXReader的对象reader
        SAXReader reader = new SAXReader();
        try {
            // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
            Document document = reader.read(new ByteArrayInputStream(sql.getBytes("utf-8")));
            // 通过document对象获取根节点rootstore
            Element rootstore = document.getRootElement();

                ifOparate(rootstore,paramMap);
                whereOparate(rootstore);
                setOparate(rootstore);

            sql = rootstore.getStringValue();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sql;
    }
    //获取if标签判断的结果
    public static boolean  ifBoolean(Element element,Map<String,Object> paramMap) throws Exception{
        return StringSqlUtil.getif(element.attribute("test").getValue(),paramMap);
    }

    //if递归操作
    public static void ifOparate(Element element,Map<String,Object> paramMap) throws Exception{
        // 通过element对象的elementIterator方法获取迭代器
        Iterator it = element.elementIterator();
        while (it.hasNext()) {
            Element node = (Element) it.next();
            if(node.getName().equals("if")&&!ifBoolean(node,paramMap)){
                    //node.setText("");
                    node.clearContent();
            }else{
                //递归
                ifOparate(node,paramMap);
            }
        }

    }
    //where解析
    public static void whereOparate(Element element){
        // 通过element对象的elementIterator方法获取迭代器
        Iterator it = element.elementIterator();
        while (it.hasNext()) {
            Element node = (Element) it.next();
            if(node.getName().equals("where")){
                String text = node.getStringValue().trim();
                if(text.length()>="and".length() && text.substring(0,"and".length()).toLowerCase().equals("and")){
                    text = text.substring("and".length(),text.length());
                    node.clearContent();
                    node.setText(" where "+text+" ");
                }
            }
        }
    }
    //set解析
    public static void setOparate(Element element){
        // 通过element对象的elementIterator方法获取迭代器
        Iterator it = element.elementIterator();
        while (it.hasNext()) {
            Element node = (Element) it.next();
            if(node.getName().equals("set")){
                String text = node.getStringValue().trim();
                if(text.endsWith(",")){
                    text = text.substring(0,text.length()-",".length());
                    node.clearContent();
                    node.setText(" set "+text+" ");
                }
            }
        }
    }
}
