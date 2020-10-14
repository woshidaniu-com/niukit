/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

import com.woshidaniu.basicutils.RegexUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.beanutils.reflection.ReflectionUtils;
import com.woshidaniu.format.pinyin4j.PingYinUtils;
import com.woshidaniu.search.core.PinYinComparator;
import com.woshidaniu.taglibs.data.TagDataProvider;
import com.woshidaniu.taglibs.factory.ServiceFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：支持List的公共标签 
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年9月20日下午2:31:17
 */
public abstract class ListTagSupport extends TagSupport{

	private static final long serialVersionUID = -2753373599584845378L;

	private String list;
	private String listKey;
	private String listValue;
	private String name;
	private String text;
	private String relation;//关联关系
	private boolean pinyin;
	private boolean hasBlank;
	private String provider;
	private String providerData;
	
	
	private static final String PAGE_LIST_VALUE_SPLIT= ":";
	private static final String PAGE_LIST_SPLIST = ",";
	protected static final String KEY_NAME = "key";
	protected static final String VALUE_NAME = "value";
	protected static final String PINYIN_NAME = "pinyin";
	private static final String PAGE_LIST_PATTEN = "(#\\(){1,}+(\\w+\\:.*{1,}+,*)+(\\))$";
	/**
	 * 
	 * <p>方法说明：解析List属性值<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年9月20日下午2:46:45<p>
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
				map.put(PINYIN_NAME, PingYinUtils.converterToSpell(value).substring(0, 1));
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
				map.put(PINYIN_NAME, PingYinUtils.converterToSpell(value).substring(0, 1));
				
				if (!StringUtils.isNull(relation)){
					JSONArray json = JSONArray.fromObject(relation);
					Iterator<JSONObject> iteator = json.iterator();
					while (iteator.hasNext()){
						JSONObject object = iteator.next();
						Set<String> keys = object.keySet();
						for (String key : keys){
							map.put(key,  ((Map<String,Object>)o).get(key));
						}
					}
				}
				
			} else {
				map.put(KEY_NAME, ReflectionUtils.invokeGetterMethod(o, listKey));
				String value = String.valueOf(ReflectionUtils.invokeGetterMethod(o, listValue));
				map.put(VALUE_NAME, value);
				map.put(PINYIN_NAME, PingYinUtils.converterToSpell(value).substring(0, 1));
				
				if (!StringUtils.isNull(relation)){
					JSONArray json = JSONArray.fromObject(relation);
					Iterator<JSONObject> iteator = json.iterator();
					while (iteator.hasNext()){
						JSONObject object = iteator.next();
						Set<String> keys = object.keySet();
						for (String key : keys){
							map.put(key,  ReflectionUtils.invokeGetterMethod(o, key));
						}
					}
				}
			}
			resultList.add(map);
		}
		
		//如果需要排序
		if (Boolean.valueOf(pinyin)){
			Collections.sort(resultList,new PinYinComparator());
		}
		return resultList;
	}
	
	/**
	 * 
	 * <p>方法说明：解析关联关系<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年10月9日下午1:45:14<p>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Map<String,String> parseRelation(){
		Map<String,String> map = new HashMap<String,String>();
		if (!StringUtils.isNull(relation)){
			JSONArray json = JSONArray.fromObject(relation);
			Iterator<JSONObject> iteator = json.iterator();
			while (iteator.hasNext()){
				JSONObject object = iteator.next();
				Set<String> keys = object.keySet();
				for (String key : keys){
					map.put(key, object.getString(key));
				}
			}
		}
		return map;
	}
	
	
	
	public boolean isHasBlank() {
		return hasBlank;
	}

	public void setHasBlank(boolean hasBlank) {
		this.hasBlank = hasBlank;
	}

	public boolean isPinyin() {
		return pinyin;
	}

	public void setPinyin(boolean pinyin) {
		this.pinyin = pinyin;
	}

	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
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

}
