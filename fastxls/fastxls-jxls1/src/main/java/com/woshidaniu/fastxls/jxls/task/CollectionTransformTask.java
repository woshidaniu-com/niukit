package com.woshidaniu.fastxls.jxls.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.fastxls.core.constant.ConfigConstants;
import com.woshidaniu.fastxls.core.model.ArgumentsModel;
import com.woshidaniu.fastxls.jxls.utils.JXLSConfigUtils;
import com.woshidaniu.fastxls.jxls.utils.JXLSWorkbookUtils;

/**
 * 
 * *******************************************************************
 * @className	： CollectionTransformTask
 * @description	： TODO(描述这个类的作用)
 * @author 		： kangzhidong
 * @date		： Mar 27, 2016 10:41:29 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class CollectionTransformTask implements Runnable {
	
	protected static Logger LOG = LoggerFactory.getLogger(CollectionTransformTask.class);
	private XLSTransformer transformer;
	private List<File> tempFiles;
	private File tmpDir;
	private int index;
	private ArgumentsModel arguments;
	private Map<String,Object> beanParams;
	private Collection<Object> rowModels;
	
	public CollectionTransformTask(
			XLSTransformer transformer,
			List<File> tempFiles,
			File tmpDir,
			int index,
			ArgumentsModel arguments,
			Map<String,Object> beanParams,
			Collection<Object> rowModels){
		this.transformer = transformer;
		this.tempFiles = tempFiles;
		this.tmpDir = tmpDir;
		this.index = index;
		this.arguments = arguments;
		this.beanParams = beanParams;
		this.rowModels = rowModels;
	}
	
	public void run() {
		
		try {
			
			//记录状态日志
			LOG.info(JXLSConfigUtils.getText(ConfigConstants.XLS_THREAD_RUNNING, new Object[]{arguments.getTaskName()}));
			
			Map<String,Object> beanMap = new HashMap<String, Object>();
			//复制已有的属性
			BeanUtils.copyProperties(beanMap, beanParams);
			//设置每页数据
			beanMap.put("rowModels", rowModels);
			//创建文件路径
			File tempFile = new File(tmpDir.getAbsolutePath()+File.separator+arguments.getSheetName() + "-" + index + arguments.getSuffix().toString());
			//创建一个workbook（POI）
			Workbook wb = JXLSWorkbookUtils.getWorkbook(arguments.getTemplatePath());
			//调用引擎生成excel报表  
			transformer.transformWorkbook(wb, beanParams);
			//输出文档
			FileOutputStream tempOut = new FileOutputStream(tempFile);
	    	wb.write(tempOut);
	    	tempOut.flush();
			tempOut.close();
			tempFiles.add(tempFile);
			
			//记录状态日志
			LOG.info(JXLSConfigUtils.getText(ConfigConstants.XLS_THREAD_COMPLETE, new Object[]{arguments.getTaskName()}));
			
		} catch (IllegalAccessException e) {
			
		} catch (InvocationTargetException e) {
			
		} catch (IOException e) {
			
		}
				
	}

}
