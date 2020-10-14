package com.woshidaniu.authz.ticket.time;

import java.util.Date;

/**
 * 
 * @className	： TimeProvider
 * @description	： 时间提供者
 * @author 		：康康（1571）
 * @date		： 2018年5月17日 下午2:29:04
 * @version 	V1.0
 */
public interface TimeProvider{

	/**
	 * 
	 * @description	： 获得当前时间
	 * @author 		： 康康（1571）
	 * @date 		：2018年5月17日 下午2:29:18
	 * @return
	 */
	public Date get();
}
