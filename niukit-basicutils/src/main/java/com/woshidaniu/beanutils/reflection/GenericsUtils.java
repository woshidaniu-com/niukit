package com.woshidaniu.beanutils.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *@类名称	: GenericsUtils.java
 *@类描述	：Generics的工具类.
 *@创建人	：kangzhidong
 *@创建时间	：Dec 30, 2015 9:37:52 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("unchecked")
public class GenericsUtils {
    /**
     * 日志.
     */
	protected static Logger LOG = LoggerFactory.getLogger(GenericsUtils.class);

    /**
     * 构造方法.
     */
    protected GenericsUtils() {
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.
     * 如public BookManager extends GenricManager&lt;Book&gt;
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if cannot be determined
     */
   
	public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager&lt;Book&gt;
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or <code>Object.class</code> if cannot be determined
     */
    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            LOG.warn(clazz.getSimpleName()  + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if ((index >= params.length) || (index < 0)) {
        	throw new IndexOutOfBoundsException("Index: " + index + ", Size of Parameterized Type: " + params.length);
        }

        if (!(params[index] instanceof Class)) {
            LOG.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }
}
