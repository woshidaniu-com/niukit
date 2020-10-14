package com.woshidaniu.db.core.mapper;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.beanutils.reflection.GenericsUtils;

@SuppressWarnings("unchecked")
public class Map2BeanRowMapper<T> implements MapRowMapper<T> {
	
	protected static Logger LOG = LoggerFactory.getLogger(Map2BeanRowMapper.class);
	protected Class<T> clazz = GenericsUtils.getSuperClassGenricType(this.getClass());
	
	public T mapperRow(Map<String, Object> row) {
		T object = null;
		try {
			object = (T) clazz.newInstance();
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);// 获取类属性
			Iterator<Map.Entry<String, Object>> ite = row.entrySet().iterator();
			while (ite.hasNext()) {
				Entry<String, Object> type = ite.next();
				// 给JavaBean对象的属性赋值
				for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
					String propertyName = descriptor.getName();
					try {
						if (type.getKey().equals(propertyName)) {
							descriptor.getWriteMethod().invoke(object,type.getValue());
						}
					} catch (Exception e) {
						LOG.error(e.getMessage(), e.getCause());
					}
				}

			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.getCause());
		}
		return object;
	}

}
