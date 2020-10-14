package com.woshidaniu.basemodel.bundle;

import java.util.List;
import java.util.ListResourceBundle;

import com.woshidaniu.basemodel.PairModel;
/**
 * 
 *@类名称		： PairListResourceBundle.java
 *@类描述		：基于键值对对象计划的国际化资源对象
 *@创建人		：kangzhidong
 *@创建时间	：Feb 21, 2017 9:08:47 AM
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class PairListResourceBundle extends ListResourceBundle {
	
	/**国际化信息集合*/
	protected List<PairModel> i18nList;
	
	 public PairListResourceBundle(List<PairModel> i18nList) {
		this.i18nList = i18nList;
	}

	protected Object[][] getContents() {
		Object[][] objects = new Object[this.i18nList.size()][0];
		for (int i = 0; i < this.i18nList.size(); i++) {
			PairModel model = this.i18nList.get(i);
			objects[i] = new Object[]{model.getKey(), model.getValue()};
		}
	    return objects;
	}
};