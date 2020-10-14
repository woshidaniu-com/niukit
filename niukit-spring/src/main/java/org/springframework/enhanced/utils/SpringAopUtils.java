package org.springframework.enhanced.utils;

import org.springframework.aop.framework.Advised;
/**
 * 
 * @className	： SpringAopUtils
 * @description	： 获取最终代理对象
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午9:05:10
 * @version 	V1.0
 */
public class SpringAopUtils {

	public SpringAopUtils() {
	}

	public static Object getProxyTarget(Object proxiedInstance) {
		if (!(proxiedInstance instanceof Advised)) {
		} else {
			try {
				return getProxyTarget(((Advised) proxiedInstance).getTargetSource().getTarget());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return proxiedInstance;
	}
}
