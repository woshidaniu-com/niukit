package com.woshidaniu.sms.client.factory;


import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.woshidaniu.sms.client.SmsClient;
import com.woshidaniu.sms.client.def.NoneSmsClient;
import com.woshidaniu.sms.client.provider.SmsPropertiesProvider;
/**
 * 
 *@类名称		： SmsClientFactory.java
 *@类描述		：Spring集成SmsClient实现
 *@创建人		：kangzhidong
 *@创建时间	：2017年5月11日 上午9:15:03
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class SmsClientFactory implements ApplicationContextAware {

	public static final String SMS_PROVIDER = "sms.provider";
	protected SmsPropertiesProvider propsProvider;
	/** ApplicationContext this object runs in */
	protected ApplicationContext applicationContext;
	
	public SmsClient getClient() throws Exception {
		//获取配置文件中的短信提供商标记
		String sms_provider = getPropsProvider().props().getProperty(SMS_PROVIDER);
		//查找上下文中配置的短信客户端实现，并根据提供商标记返回对应的实现
		Collection<SmsClient> matchingBeans = getApplicationContext().getBeansOfType(SmsClient.class).values();
		if(matchingBeans != null){
			for (SmsClient smsClient : matchingBeans) {
				if(smsClient.name().equalsIgnoreCase(sms_provider)){
					smsClient.setPropsProvider(getPropsProvider());
					return smsClient;
				}
			}
		}
		/*if(SmsEnum.ZDT_SMS.getName().equalsIgnoreCase(sms_provider)){
			smsClient = new ZdtSmsClient(getPropsProvider());
		}else if (SmsEnum.BAIDU_106_SMS.getName().equalsIgnoreCase(sms_provider)) {
			smsClient = new Baidu106SmsClient(getPropsProvider());
		}
		return smsClient;*/
		return new NoneSmsClient();
	}

	public SmsPropertiesProvider getPropsProvider() {
		return propsProvider;
	}

	public void setPropsProvider(SmsPropertiesProvider propsProvider) {
		this.propsProvider = propsProvider;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
}