package com.woshidaniu.taglibs.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import com.woshidaniu.basicutils.RegexUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.beanutils.reflection.ReflectionUtils;
import com.woshidaniu.taglibs.data.TagDataProvider;
import com.woshidaniu.taglibs.factory.ServiceFactory;

import net.sf.json.JSONObject;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：自定义标签库-支持List数据集的抽象类
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2017年2月21日下午4:25:58
 */
public abstract class  AbstractListTagSupport extends AbstractTagSupport {


	private static final long serialVersionUID = 796977563908258490L;
	
	private String list;
	private String provider;
	private String providerData;
	private String listKey;
	private String listValue;

	protected static final String KEY_NAME = "key";
	protected static final String VALUE_NAME = "value";
	private static final String PAGE_LIST_PATTEN = "(#\\(){1,}+(\\w+\\:.*{1,}+,*)+(\\))$";
	private static final String PAGE_LIST_VALUE_SPLIT= ":";
	private static final String PAGE_LIST_SPLIST = ",";
	
	/**
	 * 
	 * <p>方法说明：根据参数解析List<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2017年2月22日下午2:02:17<p>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String,Object>> parseList(){
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		//匹配正则表达式，如 #(xh:学号,xm:姓名)
		if (!StringUtils.isEmpty(list) && RegexUtils.isContentMatche(list, PAGE_LIST_PATTEN)){
			String[] arr = list.substring(2, list.length()-1).split(PAGE_LIST_SPLIST);
			
			for (String str : arr){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put(KEY_NAME, str.split(PAGE_LIST_VALUE_SPLIT)[0]);
				String value = str.split(PAGE_LIST_VALUE_SPLIT)[1];
				map.put(VALUE_NAME, value);
				resultList.add(map);
			}
			return resultList;
		}
		
		List<?> valueList = null;
		
		if (!StringUtils.isEmpty(provider)){
			JSONObject json = JSONObject.fromObject(providerData);
			TagDataProvider dataProvider = (TagDataProvider) ServiceFactory.getService(provider);
			valueList = dataProvider.getDataList(json);
		} else {
			//从request中取list属性
			ServletRequest request = pageContext.getRequest();
			valueList = (List<?>) request.getAttribute(list);
		}
		
		for (Object o : valueList){
			Map<String,Object> map = new HashMap<String,Object>();
			if (o instanceof Map){
				map.put(KEY_NAME, ((Map<String,Object>)o).get(listKey));
				String value = String.valueOf(((Map<String,Object>)o).get(listValue));
				map.put(VALUE_NAME, value);
				
			} else {
				map.put(KEY_NAME, ReflectionUtils.invokeGetterMethod(o, listKey));
				String value = String.valueOf(ReflectionUtils.invokeGetterMethod(o, listValue));
				map.put(VALUE_NAME, value);
			}
			resultList.add(map);
		}
		
		return resultList;
	}
	
	
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getProviderData() {
		return providerData;
	}
	public void setProviderData(String providerData) {
		this.providerData = providerData;
	}
	public String getListKey() {
		return listKey;
	}
	public void setListKey(String listKey) {
		this.listKey = listKey;
	}
	public String getListValue() {
		return listValue;
	}
	public void setListValue(String listValue) {
		this.listValue = listValue;
	}
	
	
}
