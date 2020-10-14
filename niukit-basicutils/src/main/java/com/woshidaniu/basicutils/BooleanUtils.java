package com.woshidaniu.basicutils;
/**
 * 
 *@类名称: BooleanUtil.java
 *@类描述：布尔类型转换工具
 *@创建人：kangzhidong
 *@创建时间：2015-3-31 上午08:45:19
 *@修改人：
 *@修改时间：
 *@版本号:v1.0
 */
public abstract class BooleanUtils extends org.apache.commons.lang.BooleanUtils{

	public static boolean parse(String str){
		//如果此字段为必选项,则验证此字段对应的列是否为空
		if(str!=null){
			if("yes".equalsIgnoreCase(str.trim())||"true".equalsIgnoreCase(str.trim())||"1".equalsIgnoreCase(str.trim())){
				return true;
			}else if("no".equalsIgnoreCase(str.trim())||"false".equalsIgnoreCase(str.trim())||"0".equalsIgnoreCase(str.trim())){
				return false;
			}else{
				throw new RuntimeException(str.trim() + " can't convert to a boolean value!");
			}
		}else{
			return false;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(BooleanUtils.parse("0"));
	}
	
}



