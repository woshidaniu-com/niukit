package com.woshidaniu.beanutils.reflection;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * 
 *@类名称	: JavaAssistUtils.java
 *@类描述	：获得方法的参数名称，使用JDK自带的反射是不行的。但是我们借助第三方包javaassist就可以获得
 *@创建人	：kangzhidong
 *@创建时间	：Dec 30, 2015 9:46:13 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class JavaAssistUtils {

	private JavaAssistUtils() {
	}
			
	/**
	 * 
	 *@描述		：获取方法参数名称
	 *@创建人	: kangzhidong
	 *@创建时间	: Dec 30, 20159:46:23 AM
	 *@param cm
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	protected static String[] getMethodParamNames(CtMethod cm) {
		// CtClass cc = cm.getDeclaringClass();
		MethodInfo methodInfo = cm.getMethodInfo();
		CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
		if (attr == null) {
			// throw new CcException(cc.getName());
		}
		String[] paramNames = null;
		try {
			paramNames = new String[cm.getParameterTypes().length];
		} catch (NotFoundException e) {

		}
		int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
		for (int i = 0; i < paramNames.length; i++) {
			paramNames[i] = attr.variableName(i + pos);
		}
		return paramNames;
	}

	/**
	 * 
	 *@描述		：获取方法参数名称，按给定的参数类型匹配方法
	 *@创建人	: kangzhidong
	 *@创建时间	: Dec 30, 20159:46:32 AM
	 *@param clazz
	 *@param method
	 *@param paramTypes
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static String[] getMethodParamNames(Class<?> clazz, String method,Class<?>... paramTypes) {
		// 用于取得字节码类，必须在当前的classpath中，使用全称
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ClassClassPath(clazz));
		CtClass cc = null;
		CtMethod cm = null;
		try {
			cc = pool.get(clazz.getName());
			String[] paramTypeNames = new String[paramTypes.length];
			for (int i = 0; i < paramTypes.length; i++) {
				paramTypeNames[i] = paramTypes[i].getName();
			}
			cm = cc.getDeclaredMethod(method, pool.get(paramTypeNames));
		} catch (NotFoundException e) {

		}
		return getMethodParamNames(cm);
	}

	/**
	 * 
	 *@描述		：获取方法参数名称，匹配同名的某一个方法
	 *@创建人	: kangzhidong
	 *@创建时间	: Dec 30, 20159:46:39 AM
	 *@param clazz
	 *@param method
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static String[] getMethodParamNames(Class<?> clazz, String method) {
		// 用于取得字节码类，必须在当前的classpath中，使用全称
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ClassClassPath(clazz));
		CtClass cc;
		CtMethod cm = null;
		try {
			cc = pool.get(clazz.getName());
			cm = cc.getDeclaredMethod(method);
		} catch (NotFoundException e) {

		}
		return getMethodParamNames(cm);
	}

}
