package org.springframework.enhanced.aop;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.enhanced.aop.aware.BeanSelfAware;

/**
 * 
 * @className	： InjectBeanSelfProcessor
 * @description	： TODO(描述这个类的作用)
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午9:13:39
 * @version 	V1.0
 */
public class InjectBeanSelfProcessor implements BeanPostProcessor {

	 public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {  
        if(bean instanceof BeanSelfAware){  
            BeanSelfAware myBean = (BeanSelfAware)bean;  
            myBean.setSelf(bean);  
            return myBean;  
        }  
        return bean;  
    }  
   
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {  
        return bean;  
    }  

}
