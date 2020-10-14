package com.woshidaniu.taglibs.data;

import java.util.List;

import net.sf.json.JSONObject;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：标签库数据提供者接口
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2017年2月21日上午11:55:22
 */
public interface TagDataProvider {
	
	
	/**
	 * 
	 * <p>方法说明：获取数据列表<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2017年2月21日下午1:49:19<p>
	 * @param json 标签参数
	 * @return
	 */
	List<?> getDataList(JSONObject json);

}
