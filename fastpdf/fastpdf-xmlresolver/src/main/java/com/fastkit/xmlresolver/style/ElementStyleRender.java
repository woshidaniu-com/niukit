package com.fastkit.xmlresolver.style;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.util.Map;

import com.fastkit.xmlresolver.xml.XMLElement;


 /**
 * @package com.fastkit.xmlresolver.render
 * @className: StyleRender
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-11
 * @time : 下午2:06:02 
 */
@SuppressWarnings("rawtypes")
public class ElementStyleRender {
	
	private StyleTransformer transformer; 
	private static ElementStyleRender instance = null;
	
	private ElementStyleRender(StyleTransformer transformer){
		this.transformer = transformer;
	}
	
	private static ThreadLocal<ElementStyleRender> threadLocal = new ThreadLocal<ElementStyleRender>();
	
	public static ElementStyleRender getInstance(StyleTransformer transformer){
		instance = threadLocal.get();
		if (instance == null) {
			instance = new ElementStyleRender(transformer);
			threadLocal.set(instance);
		}
		return threadLocal.get();
	}
	
	/**
	 * 
	 * @description:对元素进行样式渲染
	 * @author: kangzhidong
	 * @date: 2013-8-19
	 * @param elment 元素
	 * @param style  要渲染的样式
	 */
	@SuppressWarnings("unchecked")
	public <T> void render(T elment,XMLElement domElement){
		Map<String,Object> styles= domElement.styles();
		try {
			if(styles!=null&&!styles.isEmpty()){
				//获得对象的类描述
				BeanInfo beanInfo = Introspector.getBeanInfo(elment.getClass());// 获取类属性
				//获得对象的类属性描述,并循环
				for (MethodDescriptor descriptor : beanInfo.getMethodDescriptors()) {
					//设置为可访问
					descriptor.setHidden(false);
					//得到属性名称
					String methodName = descriptor.getName();
					//得到转换后的值
					Object[] css_values = transformer.getTransform(domElement,methodName);
					if(css_values!=null){
						descriptor.getMethod().invoke(elment,css_values);
					}
				}
				//获得对象的类属性描述,并循环
				for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
					//得到属性名称
					String propertyName = descriptor.getName();
					//设置为可访问
					descriptor.setHidden(false);
					//得到转换后的值
					Object[] css_values = transformer.getTransform(domElement,propertyName);
					if(css_values!=null){
						descriptor.getWriteMethod().invoke(elment, css_values);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}



