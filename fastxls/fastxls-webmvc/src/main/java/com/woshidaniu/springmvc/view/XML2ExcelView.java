package com.fastxls.springmvc.view;


import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractView;

public class XML2ExcelView extends AbstractView {

	private static String content_type = "application/vnd.ms-excel; charset=UTF-8"; 
	private static String extension = ".xls";
	
	private String fileName;
	private String location;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public XML2ExcelView() {
		setContentType(content_type);
	}
	 
	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Feb 21, 2016 10:32:17 PM
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LocalizedResourceHelper helper = new LocalizedResourceHelper(getApplicationContext());
		Locale userLocale=RequestContextUtils.getLocale(request);
		Resource inputFile=helper.findLocalizedResource(location, extension, userLocale);
		
		/*XLSTransformer transformer=new XLSTransformer();
	        workbook=transformer.transformXLS(inputFile.getInputStream(), map);


		
		response.setContentType(getContentType());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		
		ServletOutputStream out=response.getOutputStream();
		workbook.write(out);
		out.flush();		*/
		
	}

}
