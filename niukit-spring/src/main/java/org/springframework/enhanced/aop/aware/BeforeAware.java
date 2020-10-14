package org.springframework.enhanced.aop.aware;


/**
 * 
 * @className: BeforeAware
 * @description: Aop 前置通知要调用的方法接口
 * @author : kangzhidong
 * @date : 下午06:52:10 2014-6-7
 * @modify by:
 * @modify date :
 * @modify description :
 */
public abstract interface BeforeAware {

	/**
	 * 
	 * @description: Aop 前置通知调用的方法
	 * @author : kangzhidong
	 * @date 下午06:56:15 2014-6-7 
	 * @return  void 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public abstract void doBefore(Invocation invocation)  throws Exception;
	
}
