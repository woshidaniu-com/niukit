package org.springframework.enhanced.web.servlet.mvc.bind;

import java.util.Map;

/**
 *Map对象绑定请求参数比较灵活，但是需要绑定在对象上，而不能直接写在Controller方法的参数中。
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
