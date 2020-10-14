package com.woshidaniu.regexp.factory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.regexp.annotation.Validates;

/**
 * 
 *@类名称	: ValidateWrap.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 4:55:02 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 *@param <R>
 */
public class ValidateWrap<R>{

	/**
	 * 要被验证的bean对象
	 */
	private R t;
	public ValidateWrap(R r){
		this.t = r;
	}
		 
	public boolean validate(){
		boolean temp = false;
		// 获取类属性
		Class<?> clazz = this.t.getClass();
		//clazz.getMethods()
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Validates vl = field.getAnnotation(Validates.class);
			if(!BlankUtils.isBlank(vl)){
				String property = field.getName();
				String value = null;
				try {
					value = BeanUtils.getProperty(this.t,property);
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				} catch (NoSuchMethodException e) {
				}
				//如果不允许为空
				if(!vl.blank()){
					if(BlankUtils.isBlank(value)){
						temp = false;
					}else{
						temp = true;
					}
				}
				//进行其他验证的前提是不为空
				if(!BlankUtils.isBlank(value)){
					//如果length不为-1表示进行长度验证
					if(vl.length()!=-1){
						if(value.length()<=0){
							temp = false;
						}else{
							temp = true;
						}
					}
					//如果min()>=Integer.MIN_VALUE表示进行最小值验证
					if(vl.min()>=Integer.MIN_VALUE){
						if(Integer.valueOf(value)<=vl.min()){
							temp = false;
						}else{
							temp = true;
						}
					}
					//如果max()<=Integer.MAX_VALUE表示进行最大值验证
					if(vl.max()<=Integer.MAX_VALUE){
						if(Integer.valueOf(value)>=vl.max()){
							temp = false;
						}else{
							temp = true;
						}
					}
					//如果in()的数组长度大于0表示进行in的范围验证
					if(!BlankUtils.isBlank(vl.in())){
						for (String in : vl.in()) {
							if(value.equals(in)){
								temp = true;
								break;
							}
						}
					}
					//如果not()的数组长度大于0表示进行not的排除约束验证
					if(!BlankUtils.isBlank(vl.not())){
						for (String not : vl.not()) {
							if(value.equals(not)){
								temp = false;
								break;
							}
						}
					}
					//如果contain()的数组长度大于0表示进行contain的包含约束验证
					if(!BlankUtils.isBlank(vl.contain())){
						//数组类型才会有此验证
						
						for (String contain : vl.contain()) {
							if(value.equals(contain)){
								temp = false;
								break;
							}
						}
					}
					//如果equals()不为空表示进行equals的比较约束验证
					if(!BlankUtils.isBlank(vl.equals())){
						if(!value.equals(vl.equals())){
							temp = false;
						}else{
							temp = true;
						}
					}
					//如果equalsIgnoreCase()不为空表示进行equalsIgnoreCase的比较约束验证
					if(!BlankUtils.isBlank(vl.equalsIgnoreCase())){
						temp = (value.equalsIgnoreCase(vl.equalsIgnoreCase()))?true:false;
					}
					
					//如果regex()不为空表示进行regex的正则格式平匹配约束验证
					if(!BlankUtils.isBlank(vl.regexp())){
						temp =  PatternMatcherFactory.getInstance().matches(value, vl.regexp())?true:false;
					}
				}else{
					temp = true;
				}
			}
		}
		return temp;
	}
	
}
