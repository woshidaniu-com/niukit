package com.woshidaniu.orm.mybatis.utils;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

public class MetaObjectUtils{
	
	/* 默认对象工厂 */
	public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();  
	/* 默认对象包装工厂 */
	public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();  
	
	public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

	private MetaObjectUtils() {
		// Prevent Instantiation of Static Class
	}

	private static class NullObject {
	}

	public static MetaObject forObject(Object object) {
		return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,	DEFAULT_OBJECT_WRAPPER_FACTORY);
	}

	public static MetaObject forObject(Object object,
			ObjectFactory objectFactory) {
		return MetaObject.forObject(object, objectFactory,DEFAULT_OBJECT_WRAPPER_FACTORY);
	}

	public static MetaObject forObject(Object object,
			ObjectFactory objectFactory,
			ObjectWrapperFactory objectWrapperFactory) {
		return MetaObject.forObject(object, objectFactory,objectWrapperFactory);
	}
	
}
