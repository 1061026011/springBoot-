package com.easy.ls.oracle.util;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by wmh on 2018/9/29.
 */
public class FileUtil {
    /**
     * 文件上传（返回新文件名newName和原文件名oldName）
     * @param file
     * @param filepath
     * @return
     */
    public static Map<String, String> upload(MultipartFile file,String filepath){
        Map<String, String> map = new HashMap<String, String>();
        map.put("bool","true");
        UUID id = UUID.randomUUID();//随机生成的16位数
        String name = file.getOriginalFilename();//获取文件名称
        String type = name.substring(name.lastIndexOf("."));//截取.xls
        String fileName = id + type;//拼接成文件的新的名字
        //创建文件对象
        File f = new File(filepath);
        try {
            //判断路径是否存在，不存在就创建
            if (!f.exists()) {
                f.mkdir();
            }
            //创建文件对象，名称和路径
            File file1=new File(filepath,fileName);
            //将文件对象写入磁盘
            file.transferTo(file1);

            map.put("oldName", name);
            map.put("newName", fileName);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("bool","false");
        }
        return map;
    }
    /**
     * 文件下载
     * @param newName
     * @param oldName
     * @param filepath
     * @param response
     * @throws Exception
     */
    public static void download(String newName,String oldName,String filepath,HttpServletResponse response) throws Exception{
        //创建文件对象，指定读取文件路径
        File file = new File(filepath, newName);
        if (file.exists()) {
        	/*oldName = new String(oldName.getBytes("UTF-8"), "ISO-8859-1");*/
        	//System.out.println(oldName);
            response.setContentType("application/octet-stream");// 设置强制下载不打开
            response.addHeader("Content-Disposition",
//            		"attachment;fileName="+new String(oldName.replaceAll(" ","").getBytes(),"iso-8859-1"));// 设置文件名
                    "attachment;fileName="+oldName.replaceAll(" ",""));// 设置文件名
            		
            		
            //创建字节流数组，准备将文件流中的数据传给字节流数组
            byte[] buffer = new byte[1024];
            //创建为文件输入流对象，指定要读取的文件对象
            FileInputStream fis = fis = new FileInputStream(file);
            try {
                OutputStream os = response.getOutputStream();
                //将字节流中的数据传递给字节数组
                int i = fis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = fis.read(buffer);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
