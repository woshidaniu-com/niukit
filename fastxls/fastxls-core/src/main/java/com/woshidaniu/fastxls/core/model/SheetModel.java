package com.woshidaniu.fastxls.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *@类名称	: SheetModel.java
 *@类描述	：Excel 电子表格sheet抽象化 对象
 *@创建人	：kangzhidong
 *@创建时间	：Mar 22, 2016 5:28:19 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 *@param <T>
 */
public class SheetModel<T extends CellModel> implements Serializable {

	private static final long serialVersionUID = 1L;

	private int sheetIndex = 0;
	//sheet的名称 
	private String sheetName = null;
	//是否选中
	private boolean selected = false;
	//默认列宽
	private int defaultColumnWidth = 100;
	//默认行高
	private short defaultRowHeight = 30;
	//sheet的行数据
	private List<RowModel<T>> rows = new ArrayList<RowModel<T>>(0);
	//数据校验规则
	private ValidateModel[] validates;
	
	public SheetModel() {
		this.sheetIndex = 0;
	}
	
	public SheetModel(List<RowModel<T>> rows) {
		this.rows = rows;
		this.sheetIndex = 0;
	}

	public SheetModel(int sheetIndex, List<RowModel<T>> rows) {
		this.rows = rows;
		this.sheetIndex = sheetIndex;
	}

	public SheetModel(String sheetName, List<RowModel<T>> rows) {
		this.rows = rows;
		this.sheetName = sheetName;
	}

	public SheetModel(int sheetIndex, String sheetName, List<RowModel<T>> rows) {
		this.rows = rows;
		this.sheetIndex = sheetIndex;
		this.sheetName = sheetName;
	}

	/**
	 * 
	 * @description: TODO(描述这个方法的作用)
	 * @author : kangzhidong
	 * @date 下午2:16:29 2014-11-21
	 * @return
	 * @return RowModel<T> 返回类型
	 * @throws
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public RowModel<T> getFirstRow() {
		return getRow(0);
	}

	/**
	 * 
	 * @description: 获得指定行数据
	 * @author : kangzhidong
	 * @date 下午2:20:52 2014-11-21
	 * @param rownum
	 * @return
	 * @return RowModel<T> 返回类型
	 * @throws
	 */
	public RowModel<T> getRow(int rownum) {
		RowModel<T> entry = null;
		if (rows.size() > 0) {
			entry = rows.get(rownum);
		}
		return entry;
	}

	/**
	 * 
	 * @description:设置指定行数据
	 * @author : kangzhidong
	 * @date 下午2:21:47 2014-11-21
	 * @param rownum
	 * @param rowModel
	 * @return void 返回类型
	 * @throws
	 */
	public void setRow(int rownum, RowModel<T> rowModel) {
		rows.remove(rownum);
		rows.set(rownum, rowModel);
	}

	public int getSheetIndex() {

		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {

		this.sheetIndex = sheetIndex;
	}

	public String getSheetName() {

		return sheetName;
	}

	public void setSheetName(String sheetName) {

		this.sheetName = sheetName;
	}

	public List<RowModel<T>> getRows() {

		return rows;
	}

	public void setRows(List<RowModel<T>> rows) {

		this.rows = rows;
	}

	public boolean isSelected() {
	
		return selected;
	}

	public void setSelected(boolean selected) {
	
		this.selected = selected;
	}

	public int getDefaultColumnWidth() {
	
		return defaultColumnWidth;
	}

	public void setDefaultColumnWidth(int defaultColumnWidth) {
	
		this.defaultColumnWidth = defaultColumnWidth;
	}

	public short getDefaultRowHeight() {
	
		return defaultRowHeight;
	}

	public void setDefaultRowHeight(short defaultRowHeight) {
	
		this.defaultRowHeight = defaultRowHeight;
	}

	public ValidateModel[] getValidates() {
		return validates;
	}

	public void setValidates(ValidateModel[] validates) {
		this.validates = validates;
	}
	
}
