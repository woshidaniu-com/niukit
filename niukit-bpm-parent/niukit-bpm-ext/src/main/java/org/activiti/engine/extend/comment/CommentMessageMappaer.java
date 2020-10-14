package org.activiti.engine.extend.comment;

import java.util.HashMap;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：org.activiti.engine.extend.decision.DecisionMapper.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class CommentMessageMappaer extends HashMap<String, CommentMessage>{
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 
	 * <p>方法说明：根据value获取decision对象<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月12日下午2:01:16<p>
	 */
	public CommentMessage getObject(String value){
		if(value.startsWith("BACK_AR")){
			return get("BACK_AR");
		}
		if(value.startsWith("BACK_AC")){
			return get("BACK_AC");
		}
		if(value.startsWith("BACK_IR")){
			return get("BACK_IR");
		}
		if(value.startsWith("BACK_")){
			return get("BACK");
		}
		return get(value);
	}
}
