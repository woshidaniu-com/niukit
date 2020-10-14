package com.woshidaniu.shiro.authenticator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 我是大牛shiro认证器
 * 
 * 修复shiro的doAuthenticate方法的逻辑错误
 * 
 * 防止配置多个Realm，导致开发者自定义认证异常丢失。
 * 
 * @className ： ZfModularRealmAuthenticator
 * @description ： 对多Realm认证用户登录信息的增强
 * @author ：康康（1571）
 * @date ： 2018年4月11日 下午2:11:37
 * @version V1.0
 */
public class ZFModularRealmAuthenticator extends ModularRealmAuthenticator {

	private static final Logger log = LoggerFactory.getLogger(ZFModularRealmAuthenticator.class);

	/**
	 * 
	 * @description	： 覆盖父类方法，当realm超过1个，根据token类型获得匹配的Realm，防止Realm自定义异常丢失
	 * @author 		：康康（1571）
	 * @date 		：2018年4月11日 下午2:15:56
	 * @param authenticationToken
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		assertRealmsConfigured();
		Collection<Realm> realms = this.getRealms();
		if (realms.size() == 1) {
			return this.doSingleRealmAuthentication(realms.iterator().next(), authenticationToken);
		} else {
			//获得匹配的Realm
			List<Realm> supportRealms = this.filterSupportRealms(realms,authenticationToken);
			if(supportRealms.isEmpty()) {
				throw new IllegalStateException("token类型为"+authenticationToken.getClass().getName()+"没有对应的Realm处理，请检查核对配置文件！！！");
			}else if(supportRealms.size() == 1) {
				//只有一个匹配
				Realm singelSupportRealm = supportRealms.get(0);
				return this.doSingleRealmAuthentication(singelSupportRealm, authenticationToken);
			}else {
				//具有多个匹配，此时提醒开发者有可能会导致验证时的用户自定义异常丢失
				if(log.isWarnEnabled()) {
					log.warn("token类型为"+authenticationToken.getClass().getName()+"有超多一个对应的Realm处理，有可能会导致认证时用户自定义认证异常丢失，请检查核对配置文件！！！");
				}
				return this.doMultiRealmAuthentication(realms, authenticationToken);				
			}
		}
	}

	/**
	 * 
	 * @description	： 获得匹配对应token的Realm集合
	 * @author 		： 康康（1571）
	 * @date 		：2018年4月11日 下午2:23:18
	 * @param realms
	 * @param authenticationToken
	 * @return
	 */
	private List<Realm> filterSupportRealms(Collection<Realm> realms, AuthenticationToken authenticationToken) {
		List<Realm> supportRealms = new ArrayList<Realm>(realms);
		Iterator<Realm> it = supportRealms.iterator();
		while(it.hasNext()) {
			Realm r = it.next();
			if(!r.supports(authenticationToken)) {
				it.remove();
			}
		}
		return supportRealms;
	}
}