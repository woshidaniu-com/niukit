package com.woshidaniu.fastxls.core.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.woshidaniu.fastxls.core.Suffix;
/**
 * 
 *@类名称	: WorkBookModel.java
 *@类描述	：Excel 电子表格 抽象化数据 对象 
 *@创建人	：kangzhidong
 *@创建时间	：Mar 22, 2016 5:28:44 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 *@param <T>
 */
@SuppressWarnings("serial")
public class WorkBookModel<T extends CellModel> implements Serializable {
	
	/**
	 * 抽象化的excel表格对象  String(sheet名称),SheetModel<T> (sheet数据)
	 */
	private Map<String,SheetModel<T>> workBook = new HashMap<String,SheetModel<T>>(0);
	
	private Suffix suffix = Suffix.XLS;
	
	private String targetPath = null;
	
	public WorkBookModel(){this.workBook.clear();}
	
	public WorkBookModel(Suffix suffix){
		this.suffix = suffix;
		this.workBook.clear();
	}
	
	public WorkBookModel(Suffix suffix,Map<String,SheetModel<T>> workBook){
		this.suffix = suffix;
		this.workBook.clear();
		this.workBook.putAll(workBook);
	}
	
	public int getNumberOfSheets(){
		return (workBook.size()>0)?workBook.size():0;
	}
	
	/**
	 * 
	 * @description: 得到第一个工作表数据对象
	 * @author : kangzhidong
	 * @date 下午2:51:31 2014-11-21 
	 * @return
	 * @return  Entry<String,SheetModel<T>> 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public Entry<String,SheetModel<T>> getFirstSheet(){
		return getSheet(0);
	}
	
	/**
	 * 
	 * @description: 得到指定索引的工作表对象
	 * @author : kangzhidong
	 * @date 下午2:51:21 2014-11-21 
	 * @param index
	 * @return
	 * @return  Entry<String,SheetModel<T>> 返回类型
	 * @throws  
	 */
	public Entry<String, SheetModel<T>> getSheet(int index){
		Entry<String, SheetModel<T>> entry = null;
		if(workBook.size()>0){
			Iterator<Entry<String, SheetModel<T>>> ite = workBook.entrySet().iterator();
			int i = 0;
			while(ite.hasNext()){
				if(index==i){
					entry = ite.next();
					break;
				}
				i++;
			}
		}
		return entry;
	}
	
	/**
	 * 
	 * @description: 设置指定名称的工作表数据对象
	 * @author : kangzhidong
	 * @date 下午2:51:11 2014-11-21 
	 * @param sheetName
	 * @param sheetModel
	 * @return  void 返回类型
	 * @throws  
	 */
	public void setSheet(String sheetName,SheetModel<T> sheetModel){
		workBook.remove(sheetName);
		workBook.put(sheetName, sheetModel);
	}
	
	/**
	 * 
	 * @description: 得到指定名称的工作表数据对象
	 * @author : kangzhidong
	 * @date 下午2:50:44 2014-11-21 
	 * @param sheetName
	 * @return
	 * @return  SheetModel<T> 返回类型
	 * @throws  
	 */
	public SheetModel<T> getSheet(String sheetName){
		return workBook.get(sheetName);
	}
	
	/**
	 * 
	 * @description: 得到指定索引的工作表对象的指定行索引对应的行对象
	 * @author : kangzhidong
	 * @date 下午2:51:00 2014-11-21 
	 * @param sheetIndex
	 * @param rownum
	 * @return
	 * @return  RowModel<T> 返回类型
	 * @throws  
	 */
	public RowModel<T> getRow(int sheetIndex,int rownum){
		RowModel<T> row = null;
		if(workBook.size()>0){
			Iterator<Entry<String, SheetModel<T>>> ite = workBook.entrySet().iterator();
			int i = 0;
			while(ite.hasNext()){
				if(sheetIndex==i){
					row = ite.next().getValue().getRow(rownum);
					break;
				}
				i++;
			}
		}
		return row;
	}
	
	/**
	 * 
	 * @description: 设置指定索引的工作表对象的指定行索引对应的行对象
	 * @author : kangzhidong
	 * @date 下午2:51:58 2014-11-21 
	 * @param sheetIndex
	 * @param rowIndex
	 * @param element
	 * @return  void 返回类型
	 * @throws  
	 */
	public void setRow(int sheetIndex,int rowIndex,RowModel<T> element){
		if(workBook.size()>0){
			Iterator<Entry<String, SheetModel<T>>> ite = workBook.entrySet().iterator();
			int i = 0;
			while(ite.hasNext()){
				if(sheetIndex==i){
					SheetModel<T> sheet = ite.next().getValue();
					sheet.setRow(rowIndex, element);
					break;
				}
				i++;
			}
		}
	}
	
	public Map<String, SheetModel<T>> getWorkBook() {
		return workBook;
	}

	public void setWorkBook(Map<String, SheetModel<T>> workBook) {
		this.workBook = workBook;
	}
	
	public Suffix getSuffix() {
		return suffix;
	}
	
	public void setSuffix(Suffix suffix) {
		this.suffix = suffix;
	}

	public String getTargetPath() {
	
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
	
		this.targetPath = targetPath;
	}
	
	
}
