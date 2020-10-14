package com.woshidaniu.service.common.impl;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.woshidaniu.dao.common.CommonDataExpDao;
import com.woshidaniu.util.BlankUtil;
/**
 * 数据导入前的数据检查线程处理类
 * @author Administrator
 *
 */
public class DataCheckTask extends Thread {

	private Sheet sheet;
	private Row firstRow = null;
	private int startRowIndex;
	private int lastRow;
	private int count = 500;
	private String table_name;
	private String[] columns_name;
	
	private CommonDataExpDao commonDataExpDao;
	private Map table_map;
	private List table_name_list;
	
	public DataCheckTask(String thread_name,Sheet sheet,int startRowIndex,String table_name,String[] columns_name,CommonDataExpDao commonDataExpDao,List table_name_list,Map table_map){
		this.setName(thread_name);
		this.sheet = sheet;
		this.firstRow = sheet.getRow(0);
		this.startRowIndex = startRowIndex;
		this.table_name = table_name;
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
											if(!CommonDataExpServiceImpl.checkRasult.containsKey(cellNames[1])){
												CommonDataExpServiceImpl.checkRasult.put(cellNames[1], firstRow.getCell(cellIndex).getStringCellValue());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
