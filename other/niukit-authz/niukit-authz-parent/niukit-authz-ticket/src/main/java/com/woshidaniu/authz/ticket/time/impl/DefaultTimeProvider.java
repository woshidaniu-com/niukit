/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.ticket.time.impl;

import java.util.Calendar;
import java.util.Date;

import com.woshidaniu.authz.ticket.time.TimeProvider;

/**
 * 
 * @className	： DefaultTimeProvider
 * @description	： 默认时间提供者
 * @author 		：康康（1571）
 * @date		： 2018年5月17日 下午2:28:47
 * @version 	V1.0
 */
public class DefaultTimeProvider implements TimeProvider{

	@Override
	public Date get() {
		long time = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar.getTime();
	}
}
