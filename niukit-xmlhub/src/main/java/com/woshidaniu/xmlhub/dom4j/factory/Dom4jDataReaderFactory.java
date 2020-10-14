package com.woshidaniu.xmlhub.dom4j.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.CollectionUtils;
import com.woshidaniu.xmlhub.core.model.BaseDataModel;
import com.woshidaniu.xmlhub.utils.Dom4jUtils;

/**
 * 
 *@类名称	: BaseDataReaderFactory.java
 *@类描述	：通用基础数据集配置，增加国际化支持
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 6:54:58 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class Dom4jDataReaderFactory {

	protected static Logger LOG = LoggerFactory.getLogger(Dom4jDataReaderFactory.class);
	private ConcurrentMap<String,Map<String,List<BaseDataModel>>> baseDataOptionList = new ConcurrentHashMap<String,Map<String,List<BaseDataModel>>>();
	private ConcurrentMap<String,Map<String,List<HashMap<String,String>>>> mapOptionList = new ConcurrentHashMap<String,Map<String,List<HashMap<String,String>>>>();
	private String location;

	public static Dom4jDataReaderFactory newFactory(String location) {
		return new Dom4jDataReaderFactory(location);
	}
	
	private Dom4jDataReaderFactory(String location){
		this.location = location;
	}

	public Document getDoc(String locale){
		try {
			////File classPath = ResourceUtils.getFile("classpath:\\");
			return Dom4jUtils.read(locale , location );
		}catch(Exception e){
			LOG.error("Load Basicdata Error",e);
		}  
		return null;
	}
	

	/**
	 * 
	 *@描述：xml中读取下拉列表选项；优先从缓存中获取
	 *@创建人:kangzhidong
	 *@创建时间:Dec 15, 20159:07:53 AM
	 *@param id  baseData.xml中data节点的id
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public List<HashMap<String,String>> getCachedOptionList(String locale,String id){
		Map<String, List<HashMap<String, String>>>  localeOptionMap = mapOptionList.get(locale);
		if(localeOptionMap == null){
			localeOptionMap =  new HashMap<String,List<HashMap<String,String>>>(); 
		}
		List<HashMap<String,String>> list = localeOptionMap.get(id);
		if(!CollectionUtils.isEmpty(list)){
			return list;
		}
		list = getOptionMapList(locale,id);
		localeOptionMap.put(id, list);
		mapOptionList.put(locale, localeOptionMap);
		return list;
	}
	
	/**
	 * 
	 *@描述：xml中读取下拉列表选项
	 *@创建人:kangzhidong
	 *@创建时间:Dec 15, 20159:07:53 AM
	 *@param id  baseData.xml中data节点的id
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	@SuppressWarnings({ "unchecked" })
	public List<HashMap<String,String>> getOptionMapList(String locale,String id){
		Element root = getDoc(locale).getRootElement();
		List<Element> dataList = root.elements("data");
		List<HashMap<String,String>> optionList = new ArrayList<HashMap<String,String>>();
		for (int i = 0 ; i < dataList.size() ; i++) {
			Element element = dataList.get(i);
			if (id.equalsIgnoreCase(element.attributeValue("id"))) {
				List<Element> options = element.elements("option");
				HashMap<String,String> option = null;
				for (int j = 0 ; j < options.size() ; j++) {
					option = new HashMap<String,String>();
					option.put("key", options.get(j).attributeValue("key"));
					option.put("value", options.get(j).getText());
					optionList.add(option);
				}
				break;
			}
		}
		return optionList;
	}
	
	/**
	 * 
	 *@描述：xml中读取下拉列表选项；优先从缓存中获取
	 *@创建人:kangzhidong
	 *@创建时间:Dec 15, 20159:07:53 AM
	 *@param id  baseData.xml中data节点的id
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public List<BaseDataModel> getCachedBaseDataModelList(String locale,String id){
		 Map<String,List<BaseDataModel>>  localeOptionMap = baseDataOptionList.get(locale);
		if(localeOptionMap == null){
			localeOptionMap =  new HashMap<String,List<BaseDataModel>>(); 
		}
		List<BaseDataModel> list = localeOptionMap.get(id);
		if(!CollectionUtils.isEmpty(list)){
			return list;
		}
		list = getBaseDataList(locale,id);
		localeOptionMap.put(id, list);
		baseDataOptionList.put(locale, localeOptionMap);
		return list;
	}
	
	/**
	 * 
	 *@描述：xml中读取下拉列表选项
	 *@创建人:kangzhidong
	 *@创建时间:Dec 15, 20159:07:53 AM
	 *@param id  baseData.xml中data节点的id
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	@SuppressWarnings("unchecked")
	public List<BaseDataModel> getBaseDataList(String locale,String id){
		List<BaseDataModel> list = null;
		Element root = getDoc(locale).getRootElement();
		List<Element> dataList = root.elements("data");
		for (int i = 0 ; i < dataList.size() ; i++) {
			Element element = dataList.get(i);
			if (id.equalsIgnoreCase(element.attributeValue("id"))) {
				List<Element> options = element.elements("option");
				BaseDataModel data = null;
				list = new ArrayList<BaseDataModel>();
				for (int j = 0 ; j < options.size() ; j++) {
					data = new BaseDataModel();
					data.setKey(options.get(j).attributeValue("key"));
					data.setValue(options.get(j).getText());
					list.add(data);
				}
				break;
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		Dom4jDataReaderFactory.newFactory("basedara.xml").getBaseDataList("ch_CN","isNot");
		 
	}
}
