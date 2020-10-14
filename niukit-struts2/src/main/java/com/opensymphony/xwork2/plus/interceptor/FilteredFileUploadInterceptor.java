package com.opensymphony.xwork2.plus.interceptor;

import org.apache.struts2.interceptor.FileUploadInterceptor;
import org.apache.struts2.plus.StrutsConstants;

import com.opensymphony.xwork2.inject.Inject;

@SuppressWarnings("serial")
public class FilteredFileUploadInterceptor extends FileUploadInterceptor {

	@Inject(value = StrutsConstants.STRUTS_MULTIPART_ALLOWED_EXTENSIONS, required = false)
	@Override
	public void setAllowedExtensions(String allowedExtensions) {
		super.setAllowedExtensions(allowedExtensions);
	}
	
	@Inject(value = StrutsConstants.STRUTS_MULTIPART_ALLOWED_TYPES, required = false)
	@Override
	public void setAllowedTypes(String allowedTypes) {
		super.setAllowedTypes(allowedTypes);
	}

	@Inject(value = StrutsConstants.STRUTS_MULTIPART_MAXSIZE, required = false)
	public void setMaximumSize(String maximumSize) {
		super.setMaximumSize(Long.valueOf(maximumSize));
	}
	
}
