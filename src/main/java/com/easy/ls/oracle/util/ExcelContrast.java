package com.easy.ls.oracle.util;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public class ExcelContrast {
	/**
	 * 获取Excel列表数据(无论是否有标题)
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> FileExcel(MultipartFile file, Map<String, String> param){
		Map<String, Object> map = null;
		Workbook wb =null;
		Sheet sheet = null;
		Row row = null;
		Row row0 = null;
		List<Map<String,Object>> list = null;
		wb = readExcel0(file);
		if(wb != null){
			//用来存放表中数据
			list = new ArrayList<Map<String,Object>>();
			//获取第一个sheet
			sheet = wb.getSheetAt(0);
			//获取最大行数
			int rownum = sheet.getPhysicalNumberOfRows();
			//获取最大列
//		        int colnum = sheet.getRow(0).getPhysicalNumberOfCells();
			//最大列
			int colnum=10;
			//开始读取数据
			int index=0;//起始行
			int col=0;//起始列
			for(int i = 0; i <rownum; i++){
				//判断是否是表头所在行
				row = sheet.getRow(i);
				if(null != row){
					for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
						String str = row.getCell(j)==null?"":row.getCell(j).toString();
						if(!str.equals("") && param.get(str)!=null){
							colnum=row.getPhysicalNumberOfCells();
							index=i;
							col=j;
							break;
						}
					}
				}

			}
			System.out.println("表头所在行："+index+"-------最大列:"+colnum);
			System.out.println("起始行："+(index+1)+"-------起始列:"+col);
			row0 = sheet.getRow(index);
			String str="";
			for (int i = index+1; i <rownum; i++) {
				row = sheet.getRow(i);
				map = new HashMap<>();
				for (int j = col; j <colnum; j++) {
					str=row0.getCell(j).toString();
					if(!str.equals("")&&null != param.get(str)){
						map.put(param.get(str), getCellFormatValue(row.getCell(j)).toString());
					}

				}
	            	/*System.out.println(map);*/
				list.add(map);
			}
		}
		return list;
	}
	/**
	 * exce导出
	 * @param list11
	 * @param title
	 * @param column
	 * @param name
	 * @param response
	 */
	public static void export(List<Map<String, Object>> list11,String title,String column,String name,HttpServletResponse response){
		if (list11.size()>0) {
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = wb.createSheet(title);
			String[] columns=column.split(",");
			String[] names =name.split(",");
			for (int i = 0; i < columns.length; i++) {
				sheet.setColumnWidth(i, 20 * 256); // 单独设置每列的宽
			}
			sheet.setVerticallyCenter(true);
			// 第三步，在sheet中添加表头第0行
			HSSFRow row = sheet.createRow(0);
			// 第四步，创建单元格，并设置值表头 设置表头居中
			HSSFCellStyle style = wb.createCellStyle();
			HSSFCell cell = row.createCell(0);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			Font baseFont = wb.createFont();
			// 字体类型
			baseFont.setFontName("宋体");
			// 字体大小
			baseFont.setFontHeightInPoints((short) 14);
			style.setFont(baseFont);
			row = sheet.createRow(0);
			row.setHeight((short) (5 * 100));
			row = sheet.createRow(0);
			// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
			cell = row.createCell(0);
			// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columns.length-1));
			// 设置单元格标题
			cell.setCellValue(title);
			cell.setCellStyle(style);
			//设置列名
			row = sheet.createRow(1);
			for (int i = 0; i < columns.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(style);
			}
			for (int i = 0; i < list11.size(); i++) {
				row = sheet.createRow((int) i + 2);
				for (int j=0;j<columns.length;j++) {
					cell=row.createCell(j);
					cell.setCellValue(list11.get(i).get(names[j])==null?"":list11.get(i).get(names[j]).toString());
					cell.setCellStyle(style);
				}
					/*cell=row.createCell(columns.length-1);
					cell.setCellValue(rows.get(i).get("problem")==null?"":rows.get(i).get("problem").toString());
					cell.setCellStyle(style);*/
			}
			// 第六步，将文件存到指定位置
			try {
				response.setCharacterEncoding("UTF-8");
				OutputStream output;
				output = response.getOutputStream();
				response.reset();

				//response.setContentType("application/vnd.ms-excel;charset=UTF-8"); // 下载文版类型
				// response.setContentType("application/x-download");
				//response.setHeader("Content-disposition", "attachment; filename=" + title + ".xls");
				response.setContentType("application/octet-stream;charset=utf-8");
				response.setHeader("Content-Disposition", "attachment;filename="
						+ new String(title.getBytes(),"iso-8859-1") + ".xls");

				wb.write(output);
				output.close();
//					FileOutputStream fout = new FileOutputStream("F:/哈哈表zzzxx.xls");
//					wb.write(fout);
//					fout.close();
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}
	/**
	 * 下载exce模板
	 * @param
	 * @param title
	 * @param column
	 * @param response
	 */
	public static void download(String title,String column,HttpServletResponse response){
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(title);
		String[] columns=column.split(",");
		for (int i = 0; i < columns.length; i++) {
			sheet.setColumnWidth(i, 20 * 256); // 单独设置每列的宽
		}
		sheet.setVerticallyCenter(true);
		// 第三步，在sheet中添加表头第0行
		HSSFRow row = sheet.createRow(0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		HSSFCell cell = row.createCell(0);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		// 设置字体
		Font baseFont = wb.createFont();
		// 字体类型
		baseFont.setFontName("宋体");
		// 字体大小
		baseFont.setFontHeightInPoints((short) 14);
		style.setFont(baseFont);
		row = sheet.createRow(0);
		row.setHeight((short) (5 * 100));
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		cell = row.createCell(0);
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columns.length-1));
		// 设置单元格标题
		cell.setCellValue(title);
		cell.setCellStyle(style);
		//设置列名
		row = sheet.createRow(1);
		row.setHeight((short) (5 * 100));
		for (int i = 0; i < columns.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(style);
		}
		// 第五步，写入实体数据 ，


		// 第六步，将文件存到指定位置
		try {
			response.setCharacterEncoding("UTF-8");
			OutputStream output;
			output = response.getOutputStream();
			response.reset();
			String str = new String(title.getBytes("UTF-8"), "ISO-8859-1");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8"); // 下载文版类型
			// response.setContentType("application/x-download");
			response.setHeader("Content-disposition", "attachment; filename=" + str + ".xls");

			wb.write(output);
			output.close();
		} catch (Exception e) {


		}
	}
	//读取excel
	public static Workbook readExcel(String filePath){
		Workbook wb = null;
		if(filePath==null){
			return null;
		}
		String extString = filePath.substring(filePath.lastIndexOf("."));
		InputStream is = null;
		try {
			is = new FileInputStream(filePath);
			if(".xls".equals(extString)){
				return wb = new HSSFWorkbook(is);
			}else if(".xlsx".equals(extString)){
				return wb = new XSSFWorkbook(is);
			}else{
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}
	//读取excel
	public static Workbook readExcel0(MultipartFile file){
		Workbook wb = null;
		if(file==null){
			return null;
		}
		String filename = file.getOriginalFilename();
		String extString = filename.substring(filename.lastIndexOf("."));
		InputStream is = null;
		try {
			is = file.getInputStream();
			if(".xls".equals(extString)){
				return wb = new HSSFWorkbook(is);
			}else if(".xlsx".equals(extString)){
				return wb = new XSSFWorkbook(is);
			}else{
				return null;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}
	//特殊数据进行处理
	public static Object getCellFormatValue(Cell cell){
		Object cellValue = null;
		if(cell!=null){
			//判断cell类型
			if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
				//判断cell是否为日期格式
				if(DateUtil.isCellDateFormatted(cell)){
					//转换为日期格式YYYY-mm-dd
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					cellValue = sdf.format(cell.getDateCellValue());
				}else{
					//数字
					DecimalFormat df = new DecimalFormat("0");
					cellValue = df.format(cell.getNumericCellValue());
				}
			}
			else if(cell.getCellType() == Cell.CELL_TYPE_STRING){
				cellValue = cell.toString();
			}
			else{

				cellValue = "";
			}
		}else{
			cellValue = "";
		}
		return cellValue;
	}

	public static void export(List<Map<String, Object>> list11){
		if (list11.size()>0) {
			HSSFWorkbook wb = new HSSFWorkbook();
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
			HSSFSheet sheet = wb.createSheet("测试");

			for (int i = 0; i <list11.get(0).keySet().size(); i++) {
				sheet.setColumnWidth(i, 20 * 256); // 单独设置每列的宽
			}
			sheet.setVerticallyCenter(true);

			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			Font baseFont = wb.createFont();
			// 字体类型
			baseFont.setFontName("宋体");
			// 字体大小
			baseFont.setFontHeightInPoints((short) 14);
			style.setFont(baseFont);
			HSSFRow row=null;
			HSSFCell cell=null;
			for (int i = 0; i < list11.size(); i++) {
				row = sheet.createRow(i);
				for (int j=0;j<list11.get(0).keySet().size();j++) {
					cell=row.createCell(j);
					cell.setCellValue(list11.get(i).get("key"+j)==null?"":list11.get(i).get("key"+j).toString());
					cell.setCellStyle(style);
				}
			}
			// 第六步，将文件存到指定位置
			try {
				FileOutputStream fout = new FileOutputStream("F:/a.xls");
				wb.write(fout);
				fout.close();
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	public static List<Map<String,Object>> fileExcel1(@RequestParam(value="file")MultipartFile file, Map<String,Integer> map, String dpc) throws Exception{
		Workbook wb =null;
		Sheet sheet = null;
		Row row = null;
		Row row0 = null;
		List<Map<String,Object>> list = null;
		//定义数组集合
		List<String[]> csvList = null;
		wb = readExcel0(file);
		if(wb != null){
			csvList = new ArrayList<>();
			//用来存放表中数据
			list = new ArrayList<Map<String,Object>>();
			//获取第一个sheet
			sheet = wb.getSheetAt(0);
			//获取最大行数
			int rownum = sheet.getPhysicalNumberOfRows();
			//获取最大列
			int colnum = sheet.getRow(0).getPhysicalNumberOfCells();

			//获取字段所在行
			row0 = sheet.getRow(0);
			for (int i = 1; i <rownum; i++) {
				row = sheet.getRow(i);
				String[] str = new String[colnum];
				boolean b = false;
				for (int j = 0; j < colnum; j++) {
					str[j]=getCellFormatValue(row.getCell(j)).toString();
					//验证是否所有字段值为空
					if(null != str[j] && !str[j].equals("")){
						b = true;
					}
				}
				if(b){
					csvList.add(str);
				}
			}
		}
		for(String[] s:csvList){
			System.out.println("= = = == = = = = = =");
			for (String string : s) {
				System.out.print(string+",");
			}
			System.out.println("s= = ="+s.length);
			System.out.println("= = = == = = = = = =");
			Map<String,Object> map2=new HashMap<>();
			for(String str:map.keySet()){
				System.out.println("= = = == = = = = = =");
				System.out.println("map = ="+map.get(str));
				System.out.println("= = = == = = = = = =");
				if(s.length<=map.get(str)){
					map2.put(str, "");
				}else{
					map2.put(str, s[map.get(str)]);
				}
				map2.put("PH",dpc);
			}
			list.add(map2);
		}
		return list;
	}
}
