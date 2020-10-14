package com.woshidaniu.security.algorithm;

import org.apache.commons.lang.StringUtils;



/**
 * 
 *@类名称:	KNJMCodec.java
 *@类描述：	调用鹘音系统时的可逆加密方法
 *@创建人：	zyw
 *@创建时间：2015-11-12 
 *@版本号:v1.0
 *@see 
 */
public class KNJMCodec {
	//加密
	public static String encrypt(String plainStr,String key){
		char strChar, keyChar;
		String newStr="",side1="",side2="";
		int pos=0,keyLen=key.length();
		for(int i=0;i<plainStr.length();i++){
			strChar = plainStr.charAt(i);
			keyChar = key.charAt(pos);
            if ((((int)(strChar) ^ (int)(keyChar)) < 32) || (((int)(strChar) ^ (int)(keyChar)) > 126) 
    				|| ((int)(strChar) < 0) || ((int)(strChar) > 255)){
				newStr += strChar;
			}else{
                newStr += (char)((int)(strChar) ^ (int)(keyChar));
            }
			pos++;
            if (pos == keyLen){ 
				pos = 0;
			}			
		}
		int newStrLen=newStr.length();
        if (newStrLen%2==0){
            side1 = StringUtils.reverse(StringUtils.substring(newStr,0,cInt((float)newStrLen/2)));
            side2 = StringUtils.reverse(StringUtils.substring(newStr,newStrLen-cInt((float)newStrLen/2),newStrLen));
            newStr = side1 + side2;
		}			
		return newStr;
	}
	//解密
	public static String decrypt(String plainStr,String key){
		char strChar, keyChar;
		String newStr="",side1="",side2="";
		int pos=0,keyLen=key.length();
		int plainStrLen=plainStr.length();
        if (plainStrLen%2==0){
            side1 = StringUtils.reverse(StringUtils.substring(plainStr,0,cInt(plainStrLen/2)));
            side2 = StringUtils.reverse(StringUtils.substring(plainStr,plainStrLen-cInt(plainStrLen/2),plainStrLen));
            plainStr = side1 + side2;
		}			
		for(int i=0;i<plainStrLen;i++){
			strChar = plainStr.charAt(i);
			keyChar = key.charAt(pos);
            if ((((int)(strChar) ^ (int)(keyChar)) < 32) || (((int)(strChar) ^ (int)(keyChar)) > 126) 
				|| ((int)(strChar) < 0) || ((int)(strChar) > 255)){
				newStr = newStr + strChar;
			}else{
                newStr = newStr + (char)((int)(strChar) ^ (int)(keyChar));
            }
			pos++;
            if (pos == keyLen){ 
				pos = 0;
			}			
		}
		return newStr;
	}
	/**
	 * @描述:cInt的作用是四舍六入五留双.意思是说,四舍六入后取整，如果要取整的浮点数小数部分恰好是0.5的情况，则向最接近的偶数取整。
	 * @param num
	 * @return int
	 */
	public static int cInt(float num){
		int result;
		if((Math.floor(num)+0.6)-num>0 && (Math.floor(num)+0.6)-num<=0.1){
			//小数部分为X.5XX时
			result = (int)Math.floor(num);
			if(result%2==1){
				result+=1;
			}			
		}else{
			//四舍六入
			result = (int)Math.round(num);
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(KNJMCodec.encrypt("23sdffsf", "niutal"));
	}
}