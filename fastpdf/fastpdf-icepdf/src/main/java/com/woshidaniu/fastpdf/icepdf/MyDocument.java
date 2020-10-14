package com.woshidaniu.fastpdf.icepdf;

import icepdf.cr;
import java.lang.reflect.Field;
import org.icepdf.core.application.ProductInfo;
import org.icepdf.core.pobjects.Document;

public class MyDocument extends Document
{
	// 通过反射去掉水印文字资源，使其打印不出
	{
		Class<?> clazzA = cr.class;
        Field name;
        Field tip;
        
        Class<?> clazzB = ProductInfo.class;
        Field ver_c;
        Field ver_d;
        Field ver_e;
        Field ver_f;
        
		try 
		{
			name = clazzA.getDeclaredField("a");
			name.setAccessible(true); 
	        
	        byte[] x = {};
	        name.set(name, x);
	        
	        tip = clazzA.getDeclaredField("b");
	        tip.setAccessible(true); 
	        tip.set(tip, x);
	        
	        ver_c = clazzB.getDeclaredField("c");
	        ver_d = clazzB.getDeclaredField("d");
	        ver_e = clazzB.getDeclaredField("e");
	        ver_f = clazzB.getDeclaredField("f");
	        
	        ver_c.setAccessible(true); 
	        ver_d.setAccessible(true); 
	        ver_e.setAccessible(true); 
	        ver_f.setAccessible(true); 
	        
	        ver_c.set(ver_c, "");
	        ver_d.set(ver_d, "");
	        ver_e.set(ver_e, "");
	        ver_f.set(ver_f, "");
		} 
		catch (NoSuchFieldException e) 
		{
			e.printStackTrace();
		} 
		catch (SecurityException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) 
		{
			e.printStackTrace();
		}
	}
}
