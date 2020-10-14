package com.woshidaniu.fastxls.jxls.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.report.ResultSetCollection;
import net.sf.jxls.transformer.Configuration;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.woshidaniu.fastxls.jxls.callback.JXLSMapperCallback;
import com.woshidaniu.fastxls.jxls.callback.JXLSMapperCfgCallback;

public class JxlsExcelTransformer{

	public void transformXLSw(String templateFileName,ResultSet rs, String destFileName) throws Exception {
		Map beans = new HashMap();
        ResultSetCollection rsc = new ResultSetCollection(rs, false);
        beans.put( "employee", rsc );
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(templateFileName, beans, destFileName);
	}
	
	public void transformXLS(String templateFileName,Map<String, Object> beans, String destFileName) throws InvalidFormatException {
		XLSTransformer transformer = new XLSTransformer();
		try {
			transformer.transformXLS(templateFileName, beans, destFileName);
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void transformXLS(String templateFileName,Map<String, Object> beans, String destFileName,
			JXLSMapperCallback callBack) {
		XLSTransformer transformer = new XLSTransformer();
		try {
			callBack.doInJXLSMapper(transformer);
			transformer.transformXLS(templateFileName, beans, destFileName);
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void transformXLS(String templateFileName,Map<String, Object> beans, String destFileName,
			JXLSMapperCfgCallback callBack) {
		Configuration config = new Configuration();
		callBack.doBeforeJXLSMapper(config);
		XLSTransformer transformer = new XLSTransformer(config);
		callBack.doInJXLSMapper(transformer);
		try {
			transformer.transformXLS(templateFileName, beans, destFileName);
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public <T> HSSFWorkbook transformMultipleSheetsList(InputStream is,List<T> objects, List<String> newSheetNames, String beanName, Map beanParams,int startSheetNum) throws ParsePropertyException, InvalidFormatException {
		HSSFWorkbook workBook = null;
		XLSTransformer transformer = new XLSTransformer();
		workBook = (HSSFWorkbook) transformer.transformMultipleSheetsList(is, objects,newSheetNames, beanName, beanParams, startSheetNum);
	/*	
		InputStream is = new BufferedInputStream(new FileInputStream("multipleSheetList.xls"));
		XLSTransformer transformer = new XLSTransformer();
		List sheetNames = new ArrayList();
		for(int i = 0; i < departments.size(); i++){
		    Department department = (Department) departments.get( i );
		    sheetNames.add( department.getName() );
		}
		HSSFWorkbook resultWorkbook = transformer.transformMultipleSheetsList(is, departments, sheetNames, "department", new HashMap(), 0);
		      
		*/
		return workBook;
	}

	public HSSFWorkbook transformMultipleSheetsList(InputStream is,	List objects, List newSheetNames, String beanName, Map beanParams,
			int startSheetNum, JXLSMapperCallback callBack) throws ParsePropertyException, InvalidFormatException {
		HSSFWorkbook workBook = null;
		XLSTransformer transformer = new XLSTransformer();
		callBack.doInJXLSMapper(transformer);
		workBook = (HSSFWorkbook) transformer.transformMultipleSheetsList(is, objects,
				newSheetNames, beanName, beanParams, startSheetNum);
		return workBook;
	}

	public HSSFWorkbook transformMultipleSheetsList(InputStream is,
			List objects, List newSheetNames, String beanName, Map beanParams,
			int startSheetNum, JXLSMapperCfgCallback callBack) {
		HSSFWorkbook workBook = null;
		Configuration config = new Configuration();
		callBack.doBeforeJXLSMapper(config);
		XLSTransformer transformer = new XLSTransformer(config);
		callBack.doInJXLSMapper(transformer);
		try {
			workBook = (HSSFWorkbook) transformer.transformMultipleSheetsList(is, objects,
					newSheetNames, beanName, beanParams, startSheetNum);
		} catch (ParsePropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workBook;
	}

	public void transformWorkbook(HSSFWorkbook hssfWorkbook, Map beanParams) {
		XLSTransformer transformer = new XLSTransformer();
		transformer.transformWorkbook(hssfWorkbook, beanParams);
	}

	public void transformWorkbook(HSSFWorkbook hssfWorkbook, Map beanParams,
			JXLSMapperCallback callBack) {
		XLSTransformer transformer = new XLSTransformer();
		callBack.doInJXLSMapper(transformer);
		transformer.transformWorkbook(hssfWorkbook, beanParams);
	}

	public void transformWorkbook(HSSFWorkbook hssfWorkbook, Map beanParams,
			JXLSMapperCfgCallback callBack) {
		Configuration config = new Configuration();
		callBack.doBeforeJXLSMapper(config);
		XLSTransformer transformer = new XLSTransformer(config);
		callBack.doInJXLSMapper(transformer);
		transformer.transformWorkbook(hssfWorkbook, beanParams);
	}

	public HSSFWorkbook transformXLS(InputStream is, Map beanParams) throws ParsePropertyException, InvalidFormatException {
		HSSFWorkbook workBook = null;
		XLSTransformer transformer = new XLSTransformer();
		workBook = (HSSFWorkbook) transformer.transformXLS(is,beanParams);
		return workBook;
	}

	public HSSFWorkbook transformXLS(InputStream is, Map beanParams,
			JXLSMapperCallback callBack) throws ParsePropertyException, InvalidFormatException {
		HSSFWorkbook workBook = null;
		XLSTransformer transformer = new XLSTransformer();
		callBack.doInJXLSMapper(transformer);
		workBook = (HSSFWorkbook) transformer.transformXLS(is,beanParams);
		return workBook;
	}

	public HSSFWorkbook transformXLS(InputStream is, Map beanParams,
			JXLSMapperCfgCallback callBack) {
		HSSFWorkbook workBook = null;
		Configuration config = new Configuration();
		callBack.doBeforeJXLSMapper(config);
		XLSTransformer transformer = new XLSTransformer(config);
		callBack.doInJXLSMapper(transformer);
		try {
			workBook = (HSSFWorkbook) transformer.transformXLS(is,beanParams);
		} catch (ParsePropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workBook;
	}

	public HSSFWorkbook transformXLS(InputStream is, List templateSheetNameList,
			List sheetNameList, List beanParamsList) throws ParsePropertyException, InvalidFormatException {
		XLSTransformer transformer = new XLSTransformer();
		return (HSSFWorkbook) transformer.transformXLS(is,templateSheetNameList,sheetNameList,beanParamsList);
	}

	public HSSFWorkbook transformXLS(InputStream is, List templateSheetNameList,
			List sheetNameList, List beanParamsList, JXLSMapperCallback callBack) throws ParsePropertyException, InvalidFormatException {
		XLSTransformer transformer = new XLSTransformer();
		callBack.doInJXLSMapper(transformer);
		return (HSSFWorkbook) transformer.transformXLS(is,templateSheetNameList,sheetNameList,beanParamsList);
	}

	public HSSFWorkbook transformXLS(InputStream is, List templateSheetNameList,
			List sheetNameList, List beanParamsList,
			JXLSMapperCfgCallback callBack) throws ParsePropertyException, InvalidFormatException {
		Configuration config = new Configuration();
		callBack.doBeforeJXLSMapper(config);
		XLSTransformer transformer = new XLSTransformer();
		callBack.doInJXLSMapper(transformer);
		return (HSSFWorkbook) transformer.transformXLS(is,templateSheetNameList,sheetNameList,beanParamsList);
	}

}
