package com.woshidaniu.struts2.provider.def;

import org.apache.struts2.components.UIBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.beanutils.reflection.ReflectionUtils;
import com.woshidaniu.struts2.provider.ITagDataProvider;
import com.woshidaniu.struts2.provider.TagDataPair;

/**
 * 
 *@类名称		： DefaultDataProvider.java
 *@类描述		：
 *@创建人		：kangzhidong
 *@创建时间	：2017年3月27日 下午5:27:59
 *@修改人		：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class DefaultTagDataProvider implements ITagDataProvider {

	protected static Logger LOG = LoggerFactory.getLogger(DefaultTagDataProvider.class);

	@Override
	public TagDataPair getBindData(UIBean uiBean,String stackKey) {
		try {
			Object ret = ReflectionUtils.getAccessibleMethod(uiBean, "findValue", String.class).invoke(uiBean, stackKey);
			return new TagDataPair(ret);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return null;
	}
	
	
}
