package com.woshidaniu.fastxls.jxls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.rowset.CachedRowSet;

import net.sf.jxls.transformer.Configuration;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.ExecutorUtils;
import com.woshidaniu.compress.ZipUtils;
import com.woshidaniu.fastxls.core.SplitType;
import com.woshidaniu.fastxls.core.constant.ConfigConstants;
import com.woshidaniu.fastxls.core.model.ArgumentsModel;
import com.woshidaniu.fastxls.jxls.task.CollectionTransformTask;
import com.woshidaniu.fastxls.jxls.utils.JXLSConfigUtils;

/**
 * 
 * @className: JXLSWorkbookTransformer
 * @description: 基于POI和Jxls组件的Excel标签数据生成技术
 * </pre>
 *  
 *  transformer.transformMultipleSheetsList(is, objects, newSheetNames, beanName, beanParams, startSheetNum)
 *	transformer.transformWorkbook(hssfWorkbook, beanParams)
 *	transformer.transformXLS(is, beanParams)
 *	transformer.transformXLS(srcFilePath, beanParams, destFilePath)
 *	transformer.transformXLS(is, templateSheetNameList, sheetNameList, beanParamsList)
 *  transformer.transformXLS(arguments, rowSet, outpath)
 *  transformer.transformXLS(arguments, rowSet, outStream)
 *  transformer.transformXLS(arguments, list, outpath)
 *  transformer.transformXLS(arguments, list, outStream)
 *  
 * </pre>
 * 
 * @author : kangzhidong
 * @date : 下午2:45:27 2014-11-22
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class JXLSWorkbookTransformer extends XLSTransformer {

	protected static Logger LOG = LoggerFactory.getLogger(JXLSWorkbookTransformer.class);
	protected static ThreadLocal<JXLSWorkbookTransformer> threadLocal = new ThreadLocal<JXLSWorkbookTransformer>(){
		@Override
		protected JXLSWorkbookTransformer initialValue() {
			return  new JXLSWorkbookTransformer();
		}
	};

	private JXLSWorkbookTransformer() {
	}
	
	private JXLSWorkbookTransformer(Configuration configuration) {
		this.setConfiguration(configuration);
	}
	
	public static JXLSWorkbookTransformer getInstance() {
		return threadLocal.get();
	}
	
	public static JXLSWorkbookTransformer getInstance(Configuration configuration) {
		JXLSWorkbookTransformer transformer = threadLocal.get();
		transformer.setConfiguration(configuration);
		return transformer;
	}

	public void transformXLS(ArgumentsModel arguments, CachedRowSet rowSet,File tmpDir,String outpath) throws Exception {
		this.transformXLS(arguments, rowSet,tmpDir, new FileOutputStream(outpath));
	}
	
	public void transformXLS(ArgumentsModel arguments, CachedRowSet rowSet,File tmpDir,OutputStream outStream) throws Exception {
		if(StringUtils.isBlank(arguments.getTemplatePath())){
			return;
		}
		//判断是否超出一页的数据量
		boolean overPage = (rowSet.size()/rowSet.getPageSize()>1);
		List<String> newSheetNames = new ArrayList<String>();
		//所有sheet的数据
		List<Object> objects = new ArrayList<Object>();
		//提取行配置信息，生成参数
		Map<String,Object> beanParams = new HashMap<String, Object>();
		//表头
		beanParams.put("titles", arguments.getSheetTitles());
		
		//处理列信息
		int columnCount = rowSet.getMetaData().getColumnCount();
		String[] columnNames = new String[columnCount];
		for (int i = 0; i < columnCount; i++) {
			columnNames[i] = rowSet.getMetaData().getColumnLabel(i+1);
		}
		beanParams.put("columns", columnNames);
		
		//处理每页的数据
		//单页数据
		List<Map<String,Object>> pageObjects = null;
		//单行数据
		Map<String,Object> rowObj = null;
		//分页
		if(overPage){
			//逐个创建Sheet,一页的结果集代表着一个sheet
			int sheetIndex = 0;
			while (rowSet.nextPage()) {
				pageObjects = new ArrayList<Map<String,Object>>();
				//sheet名称
				newSheetNames.add(arguments.getSheetName()+sheetIndex);
				//组织每个sheet的数据
				while (rowSet.next()) {
					rowObj = new HashMap<String, Object>();
					for (String key : columnNames) {
						Object value = rowSet.getObject(key);
						rowObj.put(key.toLowerCase(), BlankUtils.isBlank(value)?"":value);
					}
					pageObjects.add(rowObj);
				}
				sheetIndex++;
			}
		}else{
			pageObjects = new ArrayList<Map<String,Object>>();
			newSheetNames.add(arguments.getSheetName());
			//组织每个sheet的数据
			while (rowSet.next()) {
				rowObj = new HashMap<String, Object>();
				for (String key : columnNames) {
					Object value = rowSet.getObject(key);
					rowObj.put(key.toLowerCase(), BlankUtils.isBlank(value)?"":value);
				}
				pageObjects.add(rowObj);
			}
			objects.add(pageObjects);
		}
		this.transformXLS(arguments, objects, tmpDir, outStream);
	}
	
	public void transformXLS(ArgumentsModel arguments,List<Object> list,File tmpDir,String outPath) throws Exception {
		this.transformXLS(arguments, list, tmpDir,new FileOutputStream(outPath));
	}
	
	public void transformXLS(ArgumentsModel arguments,List<Object> list,File tmpDir,OutputStream outStream) throws Exception  {
		if(StringUtils.isBlank(arguments.getTemplatePath())){
			return;
		}
		
		long currentMs = System.currentTimeMillis();
		
		List<String> newSheetNames = new ArrayList<String>();
		//所有sheet的数据
		List<List<Object>> objects = new ArrayList<List<Object>>();
		//提取行配置信息，生成参数
		Map<String,Object> beanParams = new HashMap<String, Object>();
		//表头
		beanParams.put("titles", arguments.getSheetTitles());
		//取得sheet允许容量
		int sheet_limit = arguments.getSheet_limit();
		//取得需要的sheet数量
		int sheet_book_num = ExecutorUtils.getThreadCount(list.size(), sheet_limit);
		//逐个创建Sheet,一页的结果集代表着一个sheet
		for (int sheetIndex = 0; sheetIndex < sheet_book_num; sheetIndex++) {
			//sheet名称
			newSheetNames.add(arguments.getSheetName()+sheetIndex);
			//计算出当前页的数据行数
			int page_size = ExecutorUtils.getPageSize(list.size(), sheet_book_num, sheetIndex);
			//向线程池增加一个线程任务
			//计算起始位置
			int fromIndex = sheetIndex * sheet_limit;
				fromIndex = fromIndex>=page_size?page_size:fromIndex;
			arguments.setSheetIndex(sheetIndex);
			//设置起始位置
			arguments.setOffset(fromIndex);
			arguments.setLimit(page_size);
			//计算结束位置
			int toIndex = arguments.getOffset()+arguments.getLimit();
				toIndex = toIndex>list.size()?list.size():toIndex;
			//组织每个sheet数据
			objects.add(list.subList(arguments.getOffset(), toIndex));
		}
		
		//判断数据过多时候采用拆分方式sheet:多个工作簿|wookbook：多个xls文件，默认：sheet 即 拆分为多个工作簿
		if(SplitType.SHEET.equals(arguments.getSplitType())){
			/*
			1）is：即Template文件的一个输入流
	        2）newSheetNames：即形成Excel文件的时候Sheet的Name
	        3）objects：即我们传入的对应每个Sheet的一个Java对象，这里传入的List的元素为一个Map对象
	        4）beanName：这个参数在jxls对我们传入的List进行解析的时候使用，而且，该参数还对应Template文件中的Tag，例如，beanName为map，那么在Template文件中取值的公式应该定义成${map.get("property1")}；如果beanName为payslip，公式应该定义成${payslip.get("property1")}
	        5）beanParams：这个参数在使用的时候我的代码没有使用到，这个参数是在如果传入的objects还与其他的对象关联的时候使用的，该参数是一个HashMap类型的参数，如果不使用的话，直接传入new HashMap()即可
	        6）startSheetNum：传入0即可，即SheetNo从0开始
	        */
			//xls模板的文件输入流
			FileInputStream input = new FileInputStream(arguments.getTemplatePath());
			Workbook wb = null;
			try {
				wb = this.transformMultipleSheetsList(input,objects,newSheetNames,"rowModels", beanParams, 0);
			} catch (Exception e) {
				wb = new HSSFWorkbook();
			}
			input.close();
			//输出文档
			wb.write(outStream);
			outStream.flush();
			
		}else if(SplitType.WOOKBOOK.equals(arguments.getSplitType())){
			//记录状态日志
			LOG.info(JXLSConfigUtils.getText(ConfigConstants.XLS_THREAD_INFO, new Object[]{arguments.getSheetName(),list.size(),sheet_limit,objects.size()}));
			//文件集合
			List<File> tempFiles = Collections.synchronizedList(new ArrayList<File>());
			//固定大小的创建线程池
			ExecutorService executor = Executors.newFixedThreadPool(objects.size());
			//逐个创建Wookbook,一页的结果集代表着一个Wookbook；循环每个Workbook的数据
			for (int index = 0; index < objects.size(); index++) {
				//向线程池增加一个线程任务
				executor.execute(new CollectionTransformTask(this,tempFiles,tmpDir,index,arguments,beanParams,objects.get(index)));
			}
			//等待所有任务完成Workbook wb
			ExecutorUtils.shutdown(executor, new Object[]{arguments.getTaskName()});
		    //压缩打包文档，并输出
			ZipUtils.compressFiles(tempFiles,outStream);
			//记录状态日志
			LOG.info(JXLSConfigUtils.getText(ConfigConstants.XLS_THREAD_TIME, new Object[]{arguments.getTaskName(),(System.currentTimeMillis() - currentMs)/1000}));
		}
	}

}
