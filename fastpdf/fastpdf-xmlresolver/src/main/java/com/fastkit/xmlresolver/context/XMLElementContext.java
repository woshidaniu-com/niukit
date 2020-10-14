package com.fastkit.xmlresolver.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.JDOMException;

import com.fastkit.xmlresolver.context.constants.ConfigConstants;
import com.fastkit.xmlresolver.xml.XMLElement;
import com.fastkit.xmlresolver.xml.resolver.XMLElementResolver;
import com.woshidaniu.basicutils.ConfigUtils;
import com.woshidaniu.configuration.config.AbstractContext;
import com.woshidaniu.configuration.config.Config;
/**
 * 
 * @description:XMLElement 上下文 
 * @author kangzhidong
 * @date 2012-4-23
 */
public class XMLElementContext extends AbstractContext {

	// 日志记录
	private static final Log log = LogFactory.getLog(XMLElementContext.class);
	// 上下文本地线程
	private static final ThreadLocal<XMLElementContext> localContext = new ThreadLocal<XMLElementContext>();
	// 初始化配置文件
	private static Properties defaults = ConfigUtils.getProperties(XMLElementContext.class , "default-config.properties");
	
	// 初始化配置文件
	private Properties configs = null;

	private String[] keys = { 
			ConfigConstants.KEY_CONFIG_FILE_PATH,
			ConfigConstants.KEY_CONFIG_XML_PATH,
			ConfigConstants.KEY_CONFIG_ENCODING,
			ConfigConstants.KEY_ELEMENT_KEY
	};
	
	//初始化配置文件
	private static Map<String,XMLElement> documents = Collections.synchronizedMap(new HashMap<String, XMLElement>());
	private static List<String> links = Collections.synchronizedList(new ArrayList<String>());
	private static Map<String,Map<String,String>> font_faces =  Collections.synchronizedMap(new HashMap<String, Map<String,String>>());
	private static Map<String,Map<String,Map<String,Object>>> medias = Collections.synchronizedMap(new HashMap<String, Map<String,Map<String,Object>>>());
	private static Map<String,Object> pages =Collections.synchronizedMap( new HashMap<String, Object>());
	private static Map<String,Map<String,Object>> styles = Collections.synchronizedMap(new HashMap<String, Map<String,Object>>());
	
	// 无参构造
	private XMLElementContext() {
		this.setConfigs(defaults);
		localContext.set(this);
	}

	private XMLElementContext(Properties configs) {
		this.setConfigs(configs);
		localContext.set(this);
	}

	private XMLElementContext(Map<String, String> parameters) {
		this.setParameters(parameters);
		localContext.set(this);
	}

	public static XMLElementContext getInstance() {
		return getInstance(defaults);
	}

	public static XMLElementContext getInstance(Properties configs) {
		XMLElementContext ctx = (XMLElementContext) localContext.get();
		if (ctx == null) {
			ctx = new XMLElementContext(configs);
		} else {
			ctx.setConfigs(configs);
		}
		return ctx;
	}

	public static XMLElementContext getInstance(Map<String, String> multipartParameters) {
		XMLElementContext ctx = (XMLElementContext) localContext.get();
		if (ctx == null) {
			ctx = new XMLElementContext(multipartParameters);
		} else {
			ctx.setParameters(multipartParameters);
		}
		return ctx;
	}
	
	//------抽象method实现--------------------------------------------------------------------------------------------
	
	@Override
	public void initialize(Config config) throws ServletException {
		log.info("Init JReportContext ...");
		setServletContext(config.getServletContext());
    	for (int i = 0; i < keys.length; i++) {
        	configs.setProperty(keys[i],safeGet(config.getInitParameter(keys[i]), defaults.getProperty(keys[i])));
        	System.setProperty(keys[i],configs.getProperty(keys[i]));
        	log.info("config:["+keys[i]+"]:"+configs.getProperty(keys[i]));
		}
    	
		log.info("read Property from config file ："+this.getConfigPath());
		Properties configs = ConfigUtils.getNewProperties(this.getClass(), this.getConfigPath());
		Iterator<Entry<Object,Object>> ite = configs.entrySet().iterator();
		while(ite.hasNext()){
			Entry<Object,Object> entry = ite.next();
			String key = String.valueOf(entry.getKey());
			String value = String.valueOf(entry.getValue());
			configs.setProperty(key,safeGet(value,defaults.getProperty(key)));
			log.info("config:["+key+"]:"+value);
			System.setProperty(key,value);
		}
		log.info("resolver xml document ："+this.getConfigXmlPath());
		try {
			XMLElementResolver.getInstance().resolver(this.getConfigXmlPath());
		} catch (JDOMException e) {
			
		} catch (IOException e) {
			
		}
	}
	
	/**
	 *@描述		：
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 30, 201611:41:21 AM
	 *@param initParameter
	 *@param property
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	: 
	 */
	private String safeGet(String initParameter, String property) {
		return null;
	}

	@Override
	public void destroy() {
		defaults.clear();
		defaults = null;
		localContext.remove();
		System.gc();
	}
	
	@Override
	public void setParameters(Map<String,String> params){
		if(null==params){
			params = new HashMap<String, String>();  
		}
		for (int i = 0; i < keys.length; i++) {
			configs.setProperty(keys[i], safeGet(params.get(keys[i]),defaults.getProperty(keys[i])));
		}	
	}
	
	@Override
	public void setConfigs(Properties config) {
		for (int i = 0; i < keys.length; i++) {
			configs.setProperty(keys[i],safeGet(config.getProperty(keys[i]),defaults.getProperty(keys[i])));
		}
	}
	
	@Override
	public XMLElementContext setRequest(HttpServletRequest request, HttpServletResponse response) {
		setRequest(request);
		setResponse(response);
		setSession(request.getSession());
		return this;
	}

	// ----------------预置的参数取值method--------------------------------------------------------
	
	@Override
	public String getConfigProperty(String key) {
		return this.configs.getProperty(key,defaults.getProperty(key));
	}

	@Override
	public Map<String, String> getParameters() {
		Map<String,String> params = new HashMap<String, String>();  
		for (int i = 0; i < keys.length; i++) {
			params.put(keys[i],this.configs.getProperty(keys[i],defaults.getProperty(keys[i])));
		}				
		return params;
	}
	
	
	// ---------初始化---------------------------------------

	/**
	 * IMexportContext 初始化文件路径 . 默认 ： imexport-config.xml
	 */
	@Override
	public String getConfigPath() {
		return getConfigProperty(ConfigConstants.KEY_CONFIG_FILE_PATH);
	}

	@Override
	public String getConfigXmlPath() {
		return getConfigProperty(ConfigConstants.KEY_CONFIG_XML_PATH);
	}
	
	/**
	 * 编码格式 ，默认： UTF-8
	 */
	@Override
	public String getEncoding() {
		return getConfigProperty(ConfigConstants.KEY_CONFIG_ENCODING);
	}
	
	public String getKey() {
		return getConfigProperty(ConfigConstants.KEY_ELEMENT_KEY);
	}
	
	// ----------------预置的参数取值method--------------------------------------------------------
	
	@SuppressWarnings("static-access")
	public static String getRealPath(String path){
		//返回最终路径
		return getInstance().getRealPath(path);
	}

	public static Map<String, XMLElement> getElements() {
		return documents;
	}

	public static XMLElement getElement(String documentID) {
		XMLElement element = XMLElementContext.documents.get(documentID);
		if(element==null){
			try {
				String xmlPath = XMLElementContext.getInstance().getConfigXmlPath();
				element = XMLElementResolver.getInstance().resolver(getRealPath(xmlPath), documentID);
			} catch (JDOMException e) {
				
			} catch (IOException e) {
				
			}
		}
		return element;
	}

	public static void addElement(String name,XMLElement document) {
		XMLElementContext.documents.put(name, document);
	}
	
	public static List<String> getLinks() {
		return links;
	}

	public static void addLink(String link) {
		XMLElementContext.links.add(link);
	}

	public static Map<String, Map<String,Object>> getStyles() {
		return styles;
	}
	
	public static Map<String,Object> getStyle(String className) {
		return styles.get(className);
	}
	
	public static void addStyle(String className,Map<String,Object> style) {
		Map<String,Object> map_old = styles.get(className);
		if(null!=map_old){
			map_old.putAll(style);
			styles.put(className, map_old);
		}else{
			styles.put(className, style);
		}
	}
	
	public static Map<String, Map<String, String>> getFontFaces() {
		return font_faces;
	}
	
	public static Map<String,String> getFontFace(String className) {
		return font_faces.get(className);
	}
	
	public static void addFontFace(String fontFamily,Map<String,String> fontFace) {
		Map<String,String> map_old = font_faces.get(fontFamily);
		if(null!=map_old){
			map_old.putAll(fontFace);
			font_faces.put(fontFamily, map_old);
		}else{
			font_faces.put(fontFamily, fontFace);
		}
	}
	
	public static Map<String, Map<String, Map<String,Object>>> getMedias() {
		return medias;
	}
	
	public static Map<String,Map<String,Object>> getMedia(String mediaName) {
		return medias.get(mediaName);
	}
	
	public static void addMedia(String mediaName,Map<String,Map<String,Object>> media) {
		Map<String,Map<String,Object>> map_old = medias.get(mediaName);
		if(null!=map_old){
			map_old.putAll(media);
			medias.put(mediaName, map_old);
		}else{
			medias.put(mediaName, media);
		}
	}
	
	public static Map<String, Object> getPages() {
		return pages;
	}
	
	public static Object getPageProperty(String className) {
		return pages.get(className);
	}
	
	public static void addPage(Map<String,String> page) {
		XMLElementContext.pages.putAll(page);
	}
	
	
	public static Map<String, Map<String,Object>> merge(Map<String, Map<String,Object>> styles) {
		return getStyles();
	}
	
}