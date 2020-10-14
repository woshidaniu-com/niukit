package com.woshidaniu.beanutils.reflection;

import java.util.Date;

//import com.sun.beans.ObjectHandler;
/**
 * 
 *@类名称	: ClassTypeUtils.java
 *@类描述	：Class类型判断工具
 *@创建人	：kangzhidong
 *@创建时间	：Dec 30, 2015 9:36:19 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings({"restriction" })
public abstract class ClassTypeUtils {

	public static void main(String[] args) throws Exception {
		
        System.out.println(isWrapClass(Integer.class));
        System.out.println(isWrapClass(Date.class));
        System.out.println(isWrapClass(String.class));
        
        System.out.println("---------------------------------------");
        
        System.out.println(isJavaClass(Integer.class));
        System.out.println(isJavaClass(Date.class));
        System.out.println(isJavaClass(String.class));
        System.out.println(isJavaClass(java.lang.Object.class));
        
        System.out.println("---------------------------------------");
        
        System.out.println(isCustomClass(Integer.class));
        System.out.println(isCustomClass(Date.class));
        System.out.println(isCustomClass(String.class));
        System.out.println(isCustomClass(java.lang.Object.class));
        
    }

/*	byte(字节) 	    8         -128 - 127                                           0
	shot(短整型)      16      -32768 - 32768                                         0
	int(整型)        32   -2147483648-2147483648                                    0
	long(长整型)      64   -9233372036854477808-9233372036854477808                  0        
	float(浮点型)    32  -3.40292347E+38-3.40292347E+38                            0.0f
	double(双精度)	   64  -1.79769313486231570E+308-1.79769313486231570E+308        0.0d
	char(字符型)     16         ‘ \u0000 - u\ffff ’                             ‘\u0000 ’
	boolean(布尔型)  1         true/false                                         false*/
	
//	public static Class typeToClass(Class type) {
//		return type.isPrimitive() ? ObjectHandler.typeNameToClass(type.getName()) : type;
//	}
	
	public static boolean isPrimitive(Class type) {
		return primitiveTypeFor(type) != null;
	}

	public static Class primitiveTypeFor(Class wrapper) {
		if (wrapper == Boolean.class){
			return Boolean.TYPE;
		}
		if (wrapper == Byte.class){
			return Byte.TYPE;
		}
		if (wrapper == Character.class){
			return Character.TYPE;
		}
		if (wrapper == Short.class){
			return Short.TYPE;
		}
		if (wrapper == Integer.class){
			return Integer.TYPE;
		}
		if (wrapper == Long.class){
			return Long.TYPE;
		}
		if (wrapper == Float.class){
			return Float.TYPE;
		}
		if (wrapper == Double.class){
			return Double.TYPE;
		}
		if (wrapper == Void.class){
			return Void.TYPE;
		}
		return null;
	}
	
	/**
	 * 
	 * @description: 判断类型是否是封装类：包括java的基本类库的类，用户自定义的类型
	 * @author : kangzhidong
	 * @date 下午3:22:37 2013-9-18 
	 * @param clazz
	 * @return
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static boolean isWrapClass(Class clazz){
        try {
            return !clazz.isPrimitive()&&!((Class)clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e){
        	//e.printStackTrace();
            return true;
        }
    }
    
    /**
     * 
     * @description: 判断一个class 是否是java类库的类
     * 				如果是lang包或者util包下面的类，可以跳过
	 *				这里不严谨，不过对于实体而言可以接受，			
	 *				因为实体的属性一般不是这两个包下面的就是自定义的包
     * @author : kangzhidong
     * @date 上午11:44:37 2013-9-27 
     * @param clazz
     * @return
     * @throws  
     * @modify by:
     * @modify date :
     * @modify description : TODO(描述修改内容)
     */
    public static boolean isJavaClass(Class<?> clazz) {  
		if (clazz != null &&clazz.getPackage().getName().equals("java.lang")){
			return true;
		}
		if (clazz != null && clazz.getPackage().getName().equals("java.util")) {
			return true;
		}
        return clazz != null && clazz.getClassLoader() == null;  
    }  
    
    /**
     * 
     * @description: 判断一个类型是否是用户自定义的类型，需要满足条件：
     * 				1.不是java类库的类
     * 				2.非基本类型的封装类
     * @author : kangzhidong
     * @date 上午11:46:03 2013-9-27 
     * @param clazz
     * @return
     * @throws  
     * @modify by:
     * @modify date :
     * @modify description : TODO(描述修改内容)
     */
	public static boolean isCustomClass(Class clazz){
        try {
            return !isJavaClass(clazz)&&isWrapClass(clazz);
        } catch (Exception e){
            return false;
        }
    }
	
}
