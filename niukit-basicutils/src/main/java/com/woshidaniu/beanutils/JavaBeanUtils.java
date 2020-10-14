package com.woshidaniu.beanutils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.beanutils.reflection.ClassTypeUtils;


/**
 * 
 * @className: JavaBeanUtils
 * @description: 设置提取javabean的属性：
 * @author : kangzhidong
 * @date : 下午3:46:30 2013-9-18
 * @modify by:
 * @modify date :
 * @modify description :
 */
@SuppressWarnings({  "unchecked", "unused" })
public final class JavaBeanUtils {

	/**
	 * Logging for this instance
	 */
	protected static Logger LOG = LoggerFactory.getLogger(JavaBeanUtils.class);

	private static Pattern pattern = Pattern.compile("^[_a-zA-Z][a-zA-Z0-9_]*$");
	private static Pattern list_pattern = Pattern.compile("(^[_a-zA-Z][a-zA-Z0-9_]*)\\[([0-9]+)\\]$");
	private static Pattern pattern_x = Pattern.compile("((\\$\\{)(.*?)(\\}))+");
	private static Pattern pattern_y = Pattern.compile("((\\#\\{)(.*?)(\\}))+");
	
	public static boolean isFiledName(String name) {
		Matcher m = pattern.matcher(name);
		return m.matches();
	}

	/**
	 * 
	 * @description:
	 * @author: kangzhidong
	 * @date: 2013-9-19
	 * @param bean
	 * @param name
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IntrospectionException 
	 */
	public static Object getProperty(Object bean, String name) throws RuntimeException{
		return JavaBeanUtils.getProperty(bean, name, false);
	}
	
	/**
	 * 
	 * @description: 可取得 字符串表示为 bean.key.key ... / bean.key_4.list[1] 在
	 *               bean的内部某个字段的值
	 * @author : kangzhidong
	 * @date 下午4:33:07 2013-9-18
	 * @param bean
	 * @param name
	 * @param deep : 如果设为true，则递归取值。
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IntrospectionException 
	 * @throws
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	
	public static Object getProperty(Object bean, String name, boolean deep) throws RuntimeException{
		Object value = null;
		if (bean == null) {
			return (null);
		}else{
			try {
				Matcher matcher_x = pattern_x.matcher(name);
				Matcher matcher_y = pattern_y.matcher(name);
				if (matcher_x.matches()) {
					value = BeanUtils.getProperty(bean, matcher_x.group(3));
				}else if (matcher_y.matches()) {
					value = BeanUtils.getProperty(bean, matcher_y.group(3));
				}else{
					if (bean instanceof Map) {
						return ((Map)bean).get(name);
					}else if (bean instanceof Properties) {
						return ((Properties)bean).getProperty(name);
					}else if(ClassTypeUtils.isWrapClass(bean.getClass())){
						
						BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
						for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
							String propertyName = descriptor.getName();
						    Class<?> propertyType = descriptor.getPropertyType();
						    Object propertyVlaue = descriptor.getReadMethod().invoke(bean);
							// 相同则说明是一对一的设置
							if (propertyName.equals(name)) {
								value = propertyVlaue;
								break;
							}else if (deep && name.indexOf(".") != -1) {
								String name1 = name.substring(0, name.indexOf(".")), 
									   name2 = name.substring(name.indexOf(".")+1);
								if (propertyName.equals(name1)) {
									// name2:xxx.xxx...
									if (name2.indexOf(".") != -1) {
										if(deep){
											value = JavaBeanUtils.getProperty(propertyVlaue, name2, deep);
										}else{
											break;
										}
									} else {
										// name2:xxx[1] 或者 xxx
										Matcher matcher = list_pattern.matcher(name2);
										if (matcher.matches()) {
											String name3 = matcher.group(1);
											int index = Integer.parseInt(matcher.group(2));
											value = JavaBeanUtils.getArrayProperty(propertyVlaue, name3)[index];
										} else {
											value = BeanUtils.getProperty(propertyVlaue, name2);
										}
									}
									break;
								}
							}else{
								continue;
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getCause());
			}
		}
		return value;
	}
	
	/**
	 * 
	 * @description:获取对象bean的name属性的数组的结果表示
	 * 				1.Array类型则转换成 Object[]
	 * 				2.Collection类型则逐个取值后放到Object[]
	 * 				3.普通的类型放到Object[]
	 * @author: kangzhidong
	 * @date: 2013-9-19
	 * @param bean
	 * @param name
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IntrospectionException 
	 */
	public static Object[] getArrayProperty(Object bean, String name) throws RuntimeException {
		Object value = JavaBeanUtils.getProperty(bean, name);
		if (value == null) {
			return (null);
		} else if (value instanceof Collection) {
			ArrayList<Object> values = new ArrayList<Object>();
			Iterator<Object> items = ((Collection<Object>) value).iterator();
			while (items.hasNext()) {
				Object item = items.next();
				if (item == null) {
					values.add(null);
				} else {
					values.add(item);
				}
			}
			return values.toArray(new Object[values.size()]);
		} else if (value.getClass().isArray()) {
			int length = Array.getLength(value);
			Object results[] = new Object[length];
			for (int i = 0; i < length; i++) {
				Object item = Array.get(value, i);
				if (item == null) {
					results[i] = null;
				} else {
					results[i] = item;
				}
			}
			return (results);
		}  else {
			return (new Object[]{value});
		}
	}

	public static void populate(Object bean, Map properties, boolean deep)
			throws RuntimeException {

		// Do nothing unless both arguments have been specified
		if ((bean == null) || (properties == null)) {
			return;
		}
		LOG.debug("BeanUtils.populate(" + bean + ", " + properties + ")");
		// Loop through the property name/value pairs to be set
		Iterator names = properties.keySet().iterator();
		while (names.hasNext()) {

			// Identify the property name and value(s) to be assigned
			String name = (String) names.next();
			if (name == null) {
				continue;
			}
			Object value = properties.get(name);

			// Perform the assignment for this property
			JavaBeanUtils.setProperty(bean, name, value, deep);

		}

	}
	
	public static void setProperty(Object bean, String name, Object value)
			throws Exception {
		JavaBeanUtils.setProperty(bean, name, value, false);
	}

	/**
	 * 
	 * @description: 可设置如bean的内部类型的某个字段的值 bean.key.key ... / bean.key_4.list[1]
	 * @author : kangzhidong
	 * @date 下午3:49:05 2013-9-18
	 * @param bean
	 * @param name
	 * @param value
	 * @param deep
	 *            : 如果设为true，则递归赋值。
	 * @throws IntrospectionException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static void setProperty(Object bean, String name, Object value,
			boolean deep) throws RuntimeException {
		// Do nothing unless both arguments have been specified
		if ((bean == null) || (name == null)) {
			return;
		}
		LOG.debug("JavaBeanUtils.setProperty(" + bean + ", " + name +", " + value + ")");
		if (bean instanceof Map) {
			((Map)bean).put(name, value);
		}else if (bean instanceof Properties) {
			((Properties)bean).setProperty(name,value.toString());
		}else if(ClassTypeUtils.isWrapClass(bean.getClass())){
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
				for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
					String propertyName = descriptor.getName();
				    Class<?> propertyType = descriptor.getPropertyType();
					// 相同则说明是一对一的设置
					if (propertyName.equals(name)) {
						descriptor.getWriteMethod().invoke(bean, value);
						break;
					}else if (deep && name.indexOf(".") != -1) {
						String name1 = name.substring(0, name.indexOf(".")), 
							   name2 = name.substring(name.indexOf(".")+1);
						if (propertyName.equals(name1)) {
							Object propertyVlaue = descriptor.getReadMethod().invoke(bean);
							if (propertyVlaue == null&& ClassTypeUtils.isCustomClass(propertyType)) {
								propertyVlaue = propertyType.newInstance();
								BeanUtils.setProperty(bean, propertyName, propertyVlaue);
							}
							//System.out.println("field: " + descriptor.getReadMethod().invoke(bean));
							// name2:xxx.xxx...
							if (name2.indexOf(".") != -1) {
								if (deep) {
									JavaBeanUtils.setProperty(propertyVlaue, name2, value, deep);
								}
							} else {
								// name2:xxx[1] 或者 xxx
								Matcher matcher = list_pattern.matcher(name2);
								if (matcher.matches()) {
									String name3 = matcher.group(1);
									int index = Integer.parseInt(matcher.group(2));
									System.out.println(matcher.group(0));
									Object value3 = BeanUtils.getProperty(propertyVlaue, name3);
									if (value3 != null) {
										if (value3 instanceof Collection) {
											ArrayList<Object> values = new ArrayList<Object>();
											Iterator<Object> items = ((Collection<Object>) value).iterator();
											while (items.hasNext()) {
												Object item = items.next();
												if (item == null) {
													values.add(null);
												} else {
													values.add(item);
												}
											}
											BeanUtils.setProperty(propertyVlaue, name3,values);
										} else if (value.getClass().isArray()) {
											int length = Array.getLength(value);
											Object[] results = new Object[length];
											for (int i = 0; i < length; i++) {
												Object item = Array.get(value, i);
												if (item == null) {
													results[i] = null;
												} else {
													results[i] = item;
												}
											}
											BeanUtils.setProperty(propertyVlaue, name3,results);
										}  else {
											BeanUtils.setProperty(propertyVlaue, name3,value);	
										}
									} 
								} else {
									BeanUtils.setProperty(propertyVlaue,name2,value);
								}
							}
							break;
						}
					}else{
						continue;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getCause());
			}
		}
	}
	

	/**
	 * Gets the <code>ConvertUtilsBean</code> instance used to perform the
	 * conversions.
	 */
	public static ConvertUtilsBean getConvertUtils() {
		return BeanUtilsBean.getInstance().getConvertUtils();
	}

	/**
	 * Gets the <code>PropertyUtilsBean</code> instance used to access
	 * properties.
	 */
	public static PropertyUtilsBean getPropertyUtils() {
		return BeanUtilsBean.getInstance().getPropertyUtils();
	}

}
