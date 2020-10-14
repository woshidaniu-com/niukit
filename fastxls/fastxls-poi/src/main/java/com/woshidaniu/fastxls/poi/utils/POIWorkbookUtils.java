/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.poi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.fastxls.core.Suffix;
import com.woshidaniu.fastxls.core.model.ArgumentsModel;
import com.woshidaniu.fastxls.core.utils.ExtensionUtils;
import com.woshidaniu.io.utils.FileUtils;

/**
 * 
 *@类名称	: POIWorkbookUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 28, 2016 9:04:11 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class POIWorkbookUtils {

	public static Workbook getWorkbook(Suffix suffix){
		//支持97 ~2003 
		if(Suffix.XLS.equals(suffix)){
			return new HSSFWorkbook();
		}else if(Suffix.XLSX.equals(suffix)){
			//支持2007
			return new SXSSFWorkbook();
		}else{
			return new HSSFWorkbook();
		}
	}
	
	public static Workbook getWorkbook(ArgumentsModel arguments) throws IOException{
		//优先从模板加载Workbook
		String filePath = arguments.getTemplatePath();
		if(!BlankUtils.isBlank(filePath)&&FileUtils.isExists(filePath)){
			return getWorkbook(filePath);
		}else{
			return getWorkbook(arguments.getSuffix());
		}
	} 
	
	/**
	 * 
	 * @description: 根据文件的路径创建Workbook对象
	 * @author : kangzhidong
	 * @date 上午10:37:24 2014-11-22
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @return Workbook 返回类型
	 * @throws
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static Workbook getWorkbook(String filePath) throws IOException {
		Workbook workbook = null;
		if(FileUtils.isExists(filePath)){
			if(ExtensionUtils.isXls(filePath)||ExtensionUtils.isXlsx(filePath)){
				try {
					InputStream ins = new FileInputStream(filePath);
					workbook = WorkbookFactory.create(ins);
					ins.close();
				} catch (InvalidFormatException e) {
					throw new RuntimeException(e.getCause());
				}
			}
		}
		return workbook;
	}
	
	public static Workbook getWorkbook(File file) throws IOException {
		Workbook workbook = null;
		if(file.exists()){
			if(ExtensionUtils.isXls(file.getAbsolutePath())||ExtensionUtils.isXlsx(file.getAbsolutePath())){
				try {
					workbook = WorkbookFactory.create(file);
				} catch (InvalidFormatException e) {
					throw new RuntimeException(e.getCause());
				}
			}
		}
		return workbook;
	}
	
	
	public static Workbook getWorkbook(InputStream inp) throws IOException {
		Workbook workbook = null;
		if(inp != null){
			try {
				workbook = WorkbookFactory.create(inp);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getCause());
			}
		}
		return workbook;
	}
}
