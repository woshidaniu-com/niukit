package com.woshidaniu.spring.web.servlet.mvc.bind;

import java.util.Map;

/**
 * 
 *@类名称		： BoundMap.java
 *@类描述		： Map对象绑定请求参数比较灵活，但是需要绑定在对象上，而不能直接写在Controller方法的参数中。
 *@创建人		：kangzhidong
 *@创建时间	：2017年4月13日 上午9:10:22
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 *@see http://www.cnblogs.com/HD/p/4107674.html
 */
public class BindMap {

	private Map<String, Object> data;

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
}
