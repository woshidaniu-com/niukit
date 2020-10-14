package com.fastkit.xmlresolver.color;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @className: ColorResolver
 * @description: 颜色解析器
 * @author : kangzhidong
 * @date : 上午10:31:53 2013-8-16
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class ColorParser {

	private String[] hexArray = new String[256];
	private Map<String,String> baseColor = new HashMap<String, String>();
	
	private static ColorParser instance = null;
	
	private ColorParser(){
		for (int i = 0; i < 256; i++) {
			String hex = Integer.toHexString(i).toUpperCase();
				hex = hex.length()<2?"0"+hex:hex;
				hexArray[i] = hex;
			//System.out.println(hex);
		}
		
		baseColor.put("black", "#000000");
		baseColor.put("green", "#008000");
		baseColor.put("silver", "#C0C0C0");
		baseColor.put("lime", "#00FF00");
		baseColor.put("gray", "#808080");
		baseColor.put("olive", "#808000");
		baseColor.put("white", "#FFFFFF");
		baseColor.put("yellow", "#FFFF00");
		baseColor.put("maroon", "#800000");
		baseColor.put("navy", "#000080");
		baseColor.put("red", "#FF0000");
		baseColor.put("blue", "#0000FF");
		baseColor.put("purple", "#800080");
		baseColor.put("teal", "#008080");
		baseColor.put("fuchsia", "#FF00FF");
		baseColor.put("aqua", "#00FFFF");
		
	}
	
	public static ColorParser getInstance(){
		instance = instance==null?new ColorParser():instance;
		return instance;
	}
	
	/**
	 * 
	 * @description: 取得基本颜色[17个基本值对应的颜色]的16进制表达值
	 * @author : kangzhidong
	 * @date 上午10:36:07 2013-8-16 
	 * @param color
	 * @return
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public String hex(String color){
		String bc = baseColor.get(color.toLowerCase());
		return bc!=null?bc:color;
	}
	
	/**
	 * 
	 * @description: 将一个RGB数组转成对应的16进制表达的颜色值
	 * @author : kangzhidong
	 * @date 上午10:36:40 2013-8-16 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public String hex(int r,int g,int b){
		return "#" + hexArray[r] + hexArray[g] + hexArray[b];
	}
	
	/**
	 * 
	 * @description: 将基本颜色[17个基本值对应的颜色]，16进制表达的颜色转成RGB值数组
	 * @author : kangzhidong
	 * @date 上午10:34:56 2013-8-16 
	 * @param color
	 * @return
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public int[] rgb(String color){
		//进行一次基本样式转换
		color  = hex(color);
		//最终的16进制表达格式进行匹配分解
		Pattern p = Pattern.compile("^#([A-Za-z0-9]{1,2})([A-Za-z0-9]{1,2})([A-Za-z0-9]{1,2})");
		Matcher m = p.matcher(color);
		int[] rgb = new int[3];
		if(m.matches()){
			for (int i = 1; i <= m.groupCount(); i++) {
				String str = m.group(i).length()==1?m.group(i)+m.group(i):m.group(i);
				rgb[i-1] = Integer.parseInt(str,16);
			}
		}
		return rgb;
	}
	
	public static void main(String[] args) {
		System.out.println(ColorParser.getInstance().hex("blue"));
		System.out.println(ColorParser.getInstance().hex(255,200,200));
		int[] rgb =ColorParser.getInstance().rgb("#f00");
		System.out.println(rgb[0]+","+rgb[1]+","+rgb[2]);
		
	}

}
