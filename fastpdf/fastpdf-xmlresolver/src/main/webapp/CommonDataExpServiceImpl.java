package com.woshidaniu.service.common.impl;

import static org.apache.commons.collections.MapUtils.getObject;
import static org.apache.commons.collections.MapUtils.getString;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;
import static org.apache.poi.ss.usermodel.CellStyle.VERTICAL_CENTER;
import static org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD;
import static org.apache.poi.ss.usermodel.Font.DEFAULT_CHARSET;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.collectionToDelimitedString;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.sql.rowset.CachedRowSet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.util.StringUtils;

import com.woshidaniu.dao.common.CommonDataExpDao;
import com.woshidaniu.dao.common.CommonSqlDao;
import com.woshidaniu.domain.common.ColumnBean;
import com.woshidaniu.service.common.CommonDataExpService;
import com.woshidaniu.util.BlankUtil;
@SuppressWarnings("unchecked")
public class CommonDataExpServiceImpl implements CommonDataExpService {
	
	private CommonDataExpDao commonDataExpDao;
	
	private CommonSqlDao commonSqlDao;
	
	private Map table_map;
	
	private Map table_name_map;
	
	private List table_name_list;
	
	private Map table_upd;          //需要更新的表数据
	
	private Map table_upd_view;     //根据传入的表，来查询所对应的视图
	
	private Map table_upd_primary_key;  //需要更新字段后面的提交（必须选择）
	
	private List cj_cjd_list;           //成绩段的集合
	
	//数据检查结果信息
	public static Map<String,String> checkRasult;
	//存储没有加入到数据库的数据集合
	public static List<Integer> addedList = null;
	//存储新增出现异常到数据库的数据集合
	public static List<Integer> errorList = null;
	
	public List getCj_cjd_list() {
		return cj_cjd_list;
	}

	public void setCj_cjd_list(List cj_cjd_list) {
		this.cj_cjd_list = cj_cjd_list;
	}

	public Map getTable_upd_primary_key() {
		return table_upd_primary_key;
	}

	public void setTable_upd_primary_key(Map table_upd_primary_key) {
		this.table_upd_primary_key = table_upd_primary_key;
	}

	public Map getTable_upd_view() {
		return table_upd_view;
	}

	public void setTable_upd_view(Map table_upd_view) {
		this.table_upd_view = table_upd_view;
	}

	public List getTable_name_list() {
		return table_name_list;
	}

	public void setTable_name_list(List table_name_list) {
		this.table_name_list = table_name_list;
	}

	/**
	 * @param arg0 表名或视图名
	 * @param arg1 主键名
	 * @param arg2 主键值数组
	 * @param arg3 需要导出的字段数组
	 */
	public void exportDataExcelPlus(Map map, OutputStream out) throws Exception{
		String table = getString(map, "table");
		String title = getString(map, "title");
		String condition = getString(map, "condition");
		String[] column_name = (String[]) getObject(map, "column_name");
		//生成workbook（POI）
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		CreationHelper helper = wb.getCreationHelper();
		//定义标题单元格格式（加粗，15磅字）
		Font font1 = wb.createFont();
		font1.setFontName("黑体");
		font1.setBoldweight(BOLDWEIGHT_BOLD);
		font1.setCharSet(DEFAULT_CHARSET);
		CellStyle style1 = wb.createCellStyle();
		style1.setFont(font1);
		style1.setAlignment(ALIGN_CENTER);
		style1.setVerticalAlignment(VERTICAL_CENTER);
		
		//定义普通单元格格式
		Font font2 = wb.createFont();
		font2.setFontName("宋体");	
		font2.setCharSet(DEFAULT_CHARSET);
		CellStyle style2 = wb.createCellStyle();
		style2.setFont(font2);
		style2.setVerticalAlignment(VERTICAL_CENTER);
		
		Cell cell = null;
		Row row = null;
		
		//生成列标题
		
		row = sheet.createRow(0);
		List column_name_list = Arrays.asList(column_name);
		List list = (List) getObject(table_map, table);
		Iterator<Map> iterator = list.iterator();
		int k = 0;//列序号
		while (iterator.hasNext()) {
			Map item = iterator.next();
			String cn = getString(item, "column_name");
			if (column_name_list.contains(cn)) {
				cell = row.createCell(k++);
				cell.setCellValue(helper.createRichTextString(getString(item, "comments")));
				cell.setCellStyle(style1);
			}
		}
		
		 int index = 1;
		 CachedRowSet rowSet = this.commonDataExpDao.findDataByProperty(table, column_name, condition);
		 while (rowSet.next()) {
			 row = sheet.createRow(index++);
			 for (int i = 0;i < column_name.length;i++) {
				 if (rowSet.getObject(column_name[i]) == null) {
					 row.createCell(i, CELL_TYPE_BLANK);
				 } else {
					 cell = row.createCell(i, CELL_TYPE_STRING);
					 cell.setCellValue(helper.createRichTextString(rowSet.getString(column_name[i])));
					 cell.setCellStyle(style2);
				 }
			 }
			 
		 }
		 
		wb.write(out);
	}
	
	/**
	 * 获取表的注释
	 */
	public String getTableComments(String table_name) {
		return getString(table_name_map, table_name);
	}
	/**
	 * 生成SYS_GUID
	 * @return
	 */
	public String findSysGuid() {
		return commonDataExpDao.findSysGuid();
	}
	/**
	 * 根据表名获得需要导出的字段名和注释
	 * @param tableName
	 * @return
	 */
	public List getColumnList(String tableName) {
		return (List) getObject(table_map, tableName);
	}
	/**
	 * @param arg0 表名或视图名
	 * @param arg1 主键名
	 * @param arg2 主键值数组
	 * @param arg3 需要导出的字段数组
	 * @return
	 */
	public CachedRowSet findDataByProperty(String arg0,String arg1,String[] arg2,String[] arg3) {
		return commonDataExpDao.findDataByProperty(arg0, arg1, arg2, arg3);
	}
	/**
	 * @param arg0 表名或视图名
	 * @param arg1 主键名
	 * @param arg2 主键值数组
	 * @param arg3 需要导出的字段数组
	 */
	public void exportDataExcel(Map map, OutputStream out) throws Exception{
		String table = getString(map, "table");
		String primarykey = getString(map, "primarykey");
		String title = getString(map, "title");
		String[] primarykeyvalue = (String[]) getObject(map, "primarykeyvalue");
		String[] column_name = (String[]) getObject(map, "column_name");
		//生成workbook
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");
//		CreationHelper createHelper = workbook.getCreationHelper();
		//定义标题单元格格式
		Font font1 = workbook.createFont();
		font1.setFontName("黑体");
		font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font1.setFontHeightInPoints((short) 15);
		CellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setFont(font1);
		cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		//定义普通单元格格式
		Font font2 = workbook.createFont();
		font2.setFontName("宋体");
		font2.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		font2.setFontHeightInPoints((short) 10);
		CellStyle cellStyle2 = workbook.createCellStyle();
		cellStyle2.setFont(font2);
		cellStyle2.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		//生成标题
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
		cell.setCellStyle(cellStyle1);
		cell.setCellValue(title);
		////合并
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, column_name.length - 1));
		//生成列标题
		row = sheet.createRow(1);
		List list = (List) getObject(table_map, table);
		Iterator<Map> iterator = list.iterator();
		while (iterator.hasNext()) {
			Map item = iterator.next();
			String cn = getString(item, "column_name");
			for (int i = 0;i < column_name.length;i++) {
				if (cn.equalsIgnoreCase(column_name[i])) {
					cell = row.createCell(i, Cell.CELL_TYPE_STRING);
					cell.setCellStyle(cellStyle2);
					cell.setCellValue(getString(item, "comments"));
					break;
				}
			}
		}
		
		CachedRowSet rowSet = this.findDataByProperty(table, primarykey, primarykeyvalue, column_name);
		int index = 2;
		try {
			while (rowSet.next()) {
				row = sheet.createRow(index);
				for (int i = 0;i < column_name.length;i++) {
					
					if (rowSet.getObject(column_name[i]) == null) {
						cell = row.createCell(i, Cell.CELL_TYPE_BLANK);
					} else {
						try {
							cell = row.createCell(i, Cell.CELL_TYPE_NUMERIC);
							cell.setCellStyle(cellStyle2);
							cell.setCellValue(rowSet.getDouble(column_name[i]));
						} catch (InvalidResultSetAccessException e) {
							cell = row.createCell(i, Cell.CELL_TYPE_STRING);
							cell.setCellStyle(cellStyle2);
							cell.setCellValue(rowSet.getString(column_name[i]));
						}
					}
				}
				index++;
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		workbook.write(out);
		out.flush();
	}
	
	/**
	 * @param arg0 表名或视图名
	 * @param arg1 主键名
	 * @param arg2 主键值数组
	 * @param arg3 需要导出的字段数组
	 */
	public void exportDataExcel(Map map, String path) throws Exception{
		String table = getString(map, "table");
		String primarykey = getString(map, "primarykey");
		String title = getString(map, "title");
		String[] primarykeyvalue = (String[]) getObject(map, "primarykeyvalue");
		String[] column_name = (String[]) getObject(map, "column_name");
		//生成workbook（JXL）
		jxl.write.WritableWorkbook wb = jxl.Workbook.createWorkbook(new java.io.File(path));
		jxl.write.WritableSheet sheet = wb.createSheet("Sheet1", 0);
		//定义标题单元格格式（加粗，15磅字）
		jxl.write.WritableFont wf1 = new jxl.write.WritableFont(jxl.write.WritableFont.TIMES, 15, jxl.write.WritableFont.BOLD);
		jxl.write.WritableCellFormat f1 = new jxl.write.WritableCellFormat(wf1);
		f1.setAlignment(jxl.format.Alignment.CENTRE);
		f1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		//定义普通单元格格式
		jxl.write.WritableFont wf2 = new jxl.write.WritableFont(jxl.write.WritableFont.TIMES, 10);
		jxl.write.WritableCellFormat f2 = new jxl.write.WritableCellFormat(wf2);
		f2.setAlignment(jxl.format.Alignment.CENTRE);
		f2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		//生成标题
		jxl.write.WritableCell label = new jxl.write.Label(0, 0, title, f1);
		sheet.addCell(label);
		//合并
//		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, column_name.length - 1));
		sheet.mergeCells(0, 0, column_name.length - 1, 0);
		//生成列标题
		List column_name_list = Arrays.asList(column_name);
		List list = (List) getObject(table_map, "view_ksfssz_jxrwb");
		Iterator<Map> iterator = list.iterator();
		while (iterator.hasNext()) {
			Map item = iterator.next();
			String cn = getString(item, "column_name");
			if (column_name_list.contains(cn)) {
				label = new jxl.write.Label(column_name_list.indexOf(cn), 1, getString(item, "comments"), f2);
				sheet.addCell(label);
			}
		}
		
		 int index = 2;
		 CachedRowSet rowSet = this.commonDataExpDao.findDataByProperty(table, primarykey, primarykeyvalue, column_name);
		 while (rowSet.next()) {
			 for (int i = 0;i < column_name.length;i++) {
				 if (rowSet.getObject(column_name[i]) == null) {
					 label = new jxl.write.Blank(i, index, f2);
				 } else {
					 label = new jxl.write.Label(i, index, rowSet.getString(column_name[i]), f2);
				 }
				 if (null != label) {
					 sheet.addCell(label);
				 }
			 }
			 index++;
		 }
		 
		wb.write();
		wb.close();
	}
	
	
	/**
	 * 数据导入前的数据检查
	 * @param arg0 导入的表名
	 * @param arg1 匹配字段
	 * @param arg2 手动加入的主键字段名
	 * @update by wuyiqi 增加功能：当数据导入到“xs_xsjbxxlsb”(学生基本信息临时表)时，并且当前学校的名称为“深圳职业技术学院”时，检查原数据库是否数据已经存在
	 */
	public List dataImportCheck(String tableName, String[] arg1, String arg2, Sheet sheet, List ucs) {
		//存储数据库已经存在的数据库的数据集合
		List<Integer> czList = new ArrayList<Integer>();
		//EXCEL行数
		int row_count = sheet.getLastRowNum();
		for (int index = 1;index < row_count + 1;index++) {
			Row row = sheet.getRow(index);
			if (row != null) {
				boolean isUnique = checkDataUnique(tableName, ucs, row, arg1);
				if(!isUnique){
					czList.add(index);
				}
			}
		}
		return czList;
	}
	
	/**
	 * 检查是否有重复列
	 */
	public Map<String,String> dataNumericFieldCheck(String table_name,String[] columns_name, Sheet sheet){
		checkRasult = new HashMap<String, String>();
		//EXCEL行数
		int row_count = sheet.getLastRowNum();
		int thread_num = 1;
		if(row_count>500){
			thread_num = (row_count - row_count%500)/500;
			if(row_count%500>0){
				thread_num = thread_num+1;
			}
		}
		//ThreadPoolExecutor executor = new ThreadPoolExecutor(thread_num,thread_num, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()); 
		ExecutorService executor = Executors.newCachedThreadPool();
		for(int i=0;i<thread_num;i++){
			 executor.submit(new DataCheckTask("数据检查线程-"+i,sheet,500*i,table_name,columns_name,commonDataExpDao,table_name_list,table_map)); 
		}
		try {
			executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务  
			while (!executor.awaitTermination(5, TimeUnit.SECONDS)) {   
			    System.out.println("线程池:数据检查线程正在执行...");   
			}   
			System.out.println("线程池:数据检查线程已经完成！"); 
	   } catch (InterruptedException ie) {
		   executor.shutdownNow();
		   Thread.currentThread().interrupt();
	   }
	   return checkRasult;
		/*
		for (int k = 1;k < row_count + 1;k++) {
			Row row = sheet.getRow(k);
			if (row != null) {
				for (int i = 0; i < columns_name.length; i++) {
					String data = columns_name[i];
					String[] cellNames = tokenizeToStringArray(data, "!#!");
					int cellIndex = Integer.parseInt(cellNames[0]);
					Cell cell = row.getCell(cellIndex);
					String cellText = "";
					if (cell == null){
						cellText = "";
					} else {
						if (cell.getCellType() != Cell.CELL_TYPE_STRING) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
						}
						cellText = cell.getStringCellValue();
						cellText = commonDataExpDao.transformValue(cellNames[1], cellText);
					}
					//遍历配置表中用于导入的表名
					for (Object table : table_name_list) {
						Map<String,String> mpt = (Map)table;
						//匹配表名
						//||"xs_ysxsxxb".equalsIgnoreCase(table_name)
						if(mpt.get("column_name").equalsIgnoreCase(table_name)){
							//取得表xml配置文件的导入字段信息
							List<Map<String,String>> columnsList = (List<Map<String,String>>) table_map.get(table_name);
							//遍历字段信息
							for (Map<String,String> column : columnsList) {
								//匹配字段
								if(column.get("column_name").equalsIgnoreCase(cellNames[1])){
									//判断此字段是否是为必填项
									String requisite = column.get("requisite");
									//如果此字段为必选项,则验证此字段对应的列是否为空
									if(requisite!=null&&"1".equalsIgnoreCase(requisite)){
										//如果为空,则将此列写入临时map
										if(BlankUtil.isBlank(cellText)){
											//判断是否已经包含了此列名的中文名称
											if(!dataRasult.containsKey(cellNames[1])){
												dataRasult.put(cellNames[1], firstRow.getCell(cellIndex).getStringCellValue());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}*/
	}
	
	/**
	 * 数据导入
	 * @param table_name 导入的表名
	 * @param columns_name 匹配字段
	 * @param main_column 手动加入的主键字段名
	 * @update by wuyiqi 增加功能：当数据导入到“xs_xsjbxxlsb”(学生基本信息临时表)时，并且当前学校的名称为“深圳职业技术学院”时，则数据同时导入到“xs_ysxsxxb”（原始学生信息表）中
	 */
	public Map<String,List<Integer>> dataImport(String table_name, String[] columns_name, String main_column, Sheet sheet, List ucs) {
		//存储没有加入到数据库的数据集合
		addedList = new ArrayList<Integer>();
		//存储新增出现异常到数据库的数据集合
		errorList = new ArrayList<Integer>();
		Map<String,List<Integer>> dataRasult = new HashMap<String, List<Integer>>();
		//EXCEL行数
		int row_count = sheet.getLastRowNum();
		int thread_num = 1;
		if(row_count>500){
			thread_num = (row_count - row_count%500)/500;
			if(row_count%500>0){
				thread_num = thread_num+1;
			}
		}
		//ThreadPoolExecutor executor = new ThreadPoolExecutor(thread_num,thread_num, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()); 
		ExecutorService executor = Executors.newCachedThreadPool();
		for(int i=0;i<thread_num;i++){
			 executor.submit(new DataImportTask("数据导入线程-"+i,sheet,500*i,table_name,main_column,columns_name,commonDataExpDao,table_name_list,table_map)); 
		}
		try {
			executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务  
			while (!executor.awaitTermination(5, TimeUnit.SECONDS)) {   
			    System.out.println("线程池:数据导入线程正在执行...");   
			}   
			System.out.println("线程池:数据导入线程已经完成！"); 
	    } catch (InterruptedException ie) {
		   executor.shutdownNow();
		   Thread.currentThread().interrupt();
	    }
		if(!BlankUtil.isBlank(addedList)){
			dataRasult.put("addedList", addedList);
		}
		if(!BlankUtil.isBlank(errorList)){
			dataRasult.put("errorList", errorList);
		}
		return dataRasult;
		
		/*for (int k = 1;k < row_count + 1;k++) {
			Row row = sheet.getRow(k);
			if (row != null) {
				int lastCellNum = row.getLastCellNum();
				StringBuilder prefix = new StringBuilder("INSERT INTO " + table_name + "(");
				StringBuilder suffix = new StringBuilder(" VALUES(");
				//获取ID
				String sys_guid = row.getCell(lastCellNum - 1).getRichStringCellValue().getString();
				prefix.append(main_column + ",");
				suffix.append(StringUtils.quoteIfString(sys_guid) + ",");
				for (int i = 0; i < columns_name.length; i++) {
					String data = columns_name[i];
					String[] cellNames = tokenizeToStringArray(data, "!#!");
					Cell cell = row.getCell(Integer.parseInt(cellNames[0]));
					String content = "";
					if (cell == null){
						content = "";
					} else {
						if (cell.getCellType() != Cell.CELL_TYPE_STRING) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
						}
						content = cell.getStringCellValue();
						content = commonDataExpDao.transformValue(cellNames[1], content);
					}
					boolean tmp = false;
					//遍历配置表中用于导入的表名
					for (Object table : table_name_list) {
						Map<String,String> mpt = (Map)table;
						//匹配表名 ||"xs_ysxsxxb".equalsIgnoreCase(table_name)
						if(mpt.get("column_name").equalsIgnoreCase(table_name)){
							//取得表xml配置文件的导入字段信息
							List<Map<String,String>> columnsList =  (List<Map<String,String>>) table_map.get(table_name);
							//遍历字段信息
							for (Map<String,String> column : columnsList) {
								//匹配字段
								if(column.get("column_name").equalsIgnoreCase(cellNames[1])){
									prefix.append(cellNames[1] + ",");
									suffix.append(StringUtils.quote(content) + ",");
									tmp = true;
									break;
								}
							}
						}
						if(tmp){
							break;
						}
					}
					//prefix.append(cellNames[1] + ",");
					//suffix.append(StringUtils.quote(content) + ",");
				}
				prefix = prefix.deleteCharAt(prefix.length() - 1).append(")");
				suffix = suffix.deleteCharAt(suffix.length() - 1).append(")");
				try {
					System.out.println(" INSERT SQL: ["+prefix.toString() + suffix.toString()+"]");
					commonDataExpDao.addData(prefix.toString() + suffix.toString());
					addedList.add(k);//将加入成功的数据加入到集合中
				} catch (RuntimeException e) {
					errorList.add(k);
					System.out.println(" ERROR-MESSAGE:["+e.getMessage() +"] \nERROR-CASE:  [" + e.getCause()+"]");
				}
			}
		}*/
	}
	
	
	/**
	 * 数据更新
	 * @param arg0
	 * @param ucs
	 * @return
	 */
	public List dataImportUpdate(String arg0, String[] arg1,String[] arg2,  jxl.Sheet sheet, List ucs) {
		List<Integer> notAddedList = new ArrayList<Integer>();
		int row_count = sheet.getRows();
		for (int k = 1;k < row_count;k++) {
				jxl.Cell[] cells = sheet.getRow(k);
				if (cells != null && cells.length > 0) {
						StringBuilder prefix = new StringBuilder("update  " + arg0 + " set ");
						StringBuilder suffix = new StringBuilder(" where 1=1");
						for (int i = 0; i < arg2.length; i++) {
							String data = arg2[i];
							String[] datas = StringUtils.tokenizeToStringArray(data, "!#!");
							prefix.append(datas[1] + "='");
							jxl.Cell cell = sheet.getCell(Integer.parseInt(datas[0]), k);
							String content = "";
							if (cell == null){
								content = "";
							} else {
								content = cell.getContents();
							}
							prefix.append(content+"',");
						}
						for(int i=0;i<arg1.length;i++){
							String data = arg1[i];
							String[] datas = StringUtils.tokenizeToStringArray(data, "!#!");
							jxl.Cell cell = sheet.getCell(Integer.parseInt(datas[0]), k);
							String content = "";
							if (cell == null){
								content = "";
							} else {
								content = cell.getContents();
							}
							suffix.append(" and "+datas[1]+"='"+content+"'");
						}
						prefix = prefix.deleteCharAt(prefix.length() - 1).append("");
						System.out.println(prefix.toString()+suffix.toString());
					} else {
						notAddedList.add(k);
					}
		}
		return notAddedList;
	}
	
	/**
	 * 判断数据的唯一性
	 * @param arg0 表名称
	 * @param ucs 唯一键的字段名集合
	 * @param cells 每行的单元格集合
	 * @param arg1 匹配的字段数组
	 * @return
	 */
	private boolean checkDataUnique(String arg0, List ucs, Row row, String[] arg1) {
		if (!isEmpty(ucs)) {
			List<String> exists = new ArrayList<String>();
			List param = new ArrayList();
			for (int i = 0; i < arg1.length; i++) {
				String[] datas = tokenizeToStringArray(arg1[i], "!#!");
				if (ucs.contains(datas[1].toLowerCase())) {
					exists.add(datas[1] + "=?");
					Cell cell = row.getCell(Integer.parseInt(datas[0]));
					if (cell != null) {
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							param.add(cell.getNumericCellValue());
						} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							param.add(cell.getRichStringCellValue().getString());
						} else {
							param.add("");
						}
					} else {
						param.add("");
					}
				}
			}
			if (!isEmpty(exists)) {
				StringBuilder sql = new StringBuilder();
				sql.append("select count(*) from ");
				sql.append(arg0);
				sql.append(" where ");
				sql.append(collectionToDelimitedString(exists, " and "));
				int count = commonDataExpDao.checkUnique(sql.toString(), param);
				return count == 0;
			}
			return true;
		} else {
			return true;
		}
	}
	
	public CommonDataExpDao getCommonDataExpDao() {
		return commonDataExpDao;
	}

	public void setCommonDataExpDao(CommonDataExpDao commonDataExpDao) {
		this.commonDataExpDao = commonDataExpDao;
	}

	public Map getTable_map() {
		return table_map;
	}
	
	public void setTable_map(Map table_map) {
		this.table_map = table_map;
	}
	public Map getTable_name_map() {
		return table_name_map;
	}
	public void setTable_name_map(Map table_name_map) {
		this.table_name_map = table_name_map;
	}
	public CommonSqlDao getCommonSqlDao() {
		return commonSqlDao;
	}
	public void setCommonSqlDao(CommonSqlDao commonSqlDao) {
		this.commonSqlDao = commonSqlDao;
	}

	public List GetAllTableNames() {
		return table_name_list;
	}

	public Map getTable_upd() {
		return table_upd;
	}

	public void setTable_upd(Map table_upd) {
		this.table_upd = table_upd;
	}

	public List findByTable(String table_name) {
		return (List)getObject(table_upd,table_name);
	}
	
	
	public String getTableView(String table_name){
		return getString(table_upd_view,table_name);
	}

	public List getAllTableColunm(ColumnBean bean) {
		return this.commonSqlDao.getAllTableColumn(bean);
	}

	public int getAllTableColumnCount(ColumnBean bean) {
		return this.commonSqlDao.getAllTableColumnCount(bean);
	}

	public String getTablePriKey(String table_name) {
		return getString(table_upd_primary_key,table_name);
	}

	public List getCjfd() {
		return this.getCj_cjd_list();
	}

}
