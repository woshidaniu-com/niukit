package com.woshidaniu.struts2.provider;

import org.apache.struts2.components.UIBean;

public interface ITagDataProvider {

	TagDataPair getBindData(UIBean uiBean,String stackKey);	
	
}
