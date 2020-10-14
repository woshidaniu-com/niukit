package com.woshidaniu.component.bpm.management;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：com.woshidaniu.component.bpm.management.BPMMessageUtil.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public final class BPMMessageUtil {
private static final Logger LOG = LoggerFactory.getLogger(BPMMessageUtil.class);

	//资源池
	private static Properties prop = new Properties();
	
	/*
	 * 加载classpath目录下的 *.properties资源文件
	 */

	static {
		load();
	}
	
	//加载资源文件
	private static boolean load(){
		InputStream inStream = null;
		try{
			//File classPath = new File(BPMMessageUtil.class.getResource("/").getFile());
			//File[] files = classPath.listFiles();
			
			//for (File file : files){
				//将以.bpm.properties结尾的文件加入资源池
				//if (file.isFile() && file.getName().endsWith(".bpm.properties")){
					inStream = BPMMessageUtil.class.getResourceAsStream("/message.bpm.properties");
					prop.load(inStream);
			//	}
			//}
			
			if(LOG.isDebugEnabled()){
				LOG.debug("[************************BPMMessageUtil Property************************]");
				if(prop != null){
					Iterator<Object> iterator = prop.keySet().iterator();
					while (iterator.hasNext()) {
						Object key = iterator.next();
						Object value = prop.get(key);
						LOG.debug(String.format("{key= %s, value= %s}", key,value));
					}
				}
				LOG.debug("[************************BPMMessageUtil Property************************]");
			}
			
			return true;
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			if(inStream != null){
				try {
					inStream.close();
				} catch (IOException e) {
					inStream = null;
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * <p>方法说明：刷新资源文件<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月13日上午10:32:20<p>
	 * @return boolean
	 */
	public static boolean reload(){
		LOG.info("************************Reload MessageUtil Property************************");	

		if(!prop.isEmpty()){
			prop.clear();
		}
		return load();
	}
	

	/**
	 * 
	 * <p>方法说明：获取message.properties中的消息<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月13日上午10:32:54<p>
	 * @param key 键
	 * @param params 参数列表
	 * @return Content
	 */
	public static String getText(String key,Object[] params){
		
		String message = prop.getProperty(key);
		
		return setParams(params, message);
	}
	

	
	/**
	 * 
	 * <p>方法说明：为资源文件中的参数设值<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月13日上午10:34:59<p>
	 * @param params 参数列表
	 * @param message 转换前Message
	 * @return 转换后Message
	 */
	private static String setParams(Object[] params, String message) {
		if (null != params && params.length > 0){
			for (int i = 0 ; i < params.length ; i++){
				message = message.replace(String.format("{%s}", i), String.valueOf(params[i]));
			}
		}
		return message;
	}
	
	
	/**
	 * 
	 * <p>方法说明：获取message.properties中的消息<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月13日上午10:37:21<p>
	 * @param key 键
	 * @return 值
	 */
	public static String getText(String key){
		return getText(key,null);
	}
	

	/**
	 * 
	 * <p>方法说明：只获取属性，不进行参数设置<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月13日上午10:37:51<p>
	 * @param key 键
	 * @return 值
	 */
	public static String getTextOnly(String key){
		return prop.getProperty(key);
	}
	
	
	
	/**
	 * 
	 * <p>方法说明：获取int类型属性<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月13日上午10:38:29<p>
	 * @param key 键
	 * @return 值
	 */
	public static Integer getInt(String key){
		String textOnly = getTextOnly(key);
		if(textOnly == null || "".equals(textOnly)){
			return null;
		}else{
			return new Integer(textOnly);
		}
	}
}
