package com.woshidaniu.fastpdf.core.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DimensionUtils {

	private static final String[] units = new String[]{"em","px","pt","%",""};
	private static Map<String,Float> powers = new HashMap<String, Float>();
	
	static{
		// Px 像素Pixel；相对长度单位。Pt 点（Point）；绝对长度单位Em 相对长度单位
		powers.put("em", 16f);//1em=16px
		powers.put("px", 1f);//1px
		powers.put("pt", 4/3f);//1pt=4/3px
		powers.put("%", 1f);//换算系数为1直接返回百分值
	}
	
	public static double unitParse(String value) {
		if (value == null){
			return -1;
		}
		for (int i = 0; i < units.length; i++) {
			String regex = "^([0-9]+\\.?[0-9]*)\\s*("+units[i]+")$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(value.toLowerCase());
			if(matcher.matches()){
				if(matcher.group(1)!=null){
					BigDecimal num = new BigDecimal(matcher.group(1));
					if(matcher.group(2)!=null&&matcher.group(2).length()>0){
						BigDecimal mult = BigDecimal.valueOf(powers.get(matcher.group(2)));
						return num.multiply(mult).doubleValue();
					}else{
						BigDecimal mult = BigDecimal.valueOf(1);
						return num.multiply(mult).doubleValue();
					}
				}
				break;
			}
		}
		return 0;
	};
	
	public static double getPercent(String percent){
		if(percent.indexOf("%")>-1){
			return DimensionUtils.unitParse(percent);
		}else{
			return 0;
		}
	}
	
	public static double getWidth(String width){
		return DimensionUtils.unitParse(width);
	}
	
	public static double getHeight(String height){
		return DimensionUtils.unitParse(height);
	}
	
	public static void main(String[] args) {
		System.out.println(DimensionUtils.unitParse("5em"));
		System.out.println(DimensionUtils.unitParse("5px"));
		System.out.println(DimensionUtils.unitParse("5pt"));
		System.out.println(DimensionUtils.unitParse("5"));
		System.out.println(DimensionUtils.unitParse("0.15%"));
	}
	
}



