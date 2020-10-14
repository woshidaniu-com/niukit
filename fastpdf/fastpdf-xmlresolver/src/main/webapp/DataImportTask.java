package com.woshidaniu.service.common.impl;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.StringUtils;

import com.woshidaniu.dao.common.CommonDataExpDao;
/**
 * 数据导入线程处理类
 * @author Administrator
 *
 */
public class DataImportTask extends Thread {

	private Sheet sheet;
	private int startRowIndex;
	private int lastRow;
	private int count = 500;
	private String table_name;
	private String main_column;
	private String[] columns_name;
	
	private CommonDataExpDao commonDataExpDao;
	private Map table_map;
	private List table_name_list;
	
	public DataImportTask(String thread_name,Sheet sheet,int startRowIndex,String table_name,String main_column,String[] columns_name,CommonDataExpDao commonDataExpDao,List table_name_list,Map table_map){
		this.setName(thread_name);
		this.sheet = sheet;
		this.startRowIndex = startRowIndex;
		this.table_name = table_name;
		this.main_column = main_column;
		this.columns_name = columns_name;
		this.lastRow = sheet.getLastRowNum();
		this.commonDataExpDao = commonDataExpDao;
		this.table_name_list = table_name_list;
		this.table_map = table_map;
	}
	
	public void run() {
		int lastIndex = (startRowIndex+count) + 1;
		if(lastIndex>lastRow){
			lastIndex=lastRow;
		}
		startRowIndex = startRowIndex==0?1:startRowIndex;
		for (int r = startRowIndex;r < lastIndex ;r++) {
			Row row = sheet.getRow(r);
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
				}
				prefix = prefix.deleteCharAt(prefix.length() - 1).append(")");
				suffix = suffix.deleteCharAt(suffix.length() - 1).append(")");
				try {
					System.out.println(" INSERT SQL: ["+prefix.toString() + suffix.toString()+"]");
					commonDataExpDao.addData(prefix.toString() + suffix.toString());
					CommonDataExpServiceImpl.addedList.add(r);
				} catch (RuntimeException e) {
					CommonDataExpServiceImpl.errorList.add(r);
					System.out.println(" ERROR-MESSAGE:["+e.getMessage() +"] \nERROR-CASE:  [" + e.getCause()+"]");
				}
			}
		}
	}
	
	
	
	
	
}
