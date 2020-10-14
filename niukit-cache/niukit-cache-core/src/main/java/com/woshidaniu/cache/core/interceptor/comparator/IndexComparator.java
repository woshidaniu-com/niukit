package com.woshidaniu.cache.core.interceptor.comparator;

import java.util.Comparator;

import com.woshidaniu.cache.core.interceptor.AspectInterceptor;

/**
 *  
 * @className	： IndexComparator
 * @description	： 排序
 * @author 		： kangzhidong
 * @date		： Dec 25, 2015 4:00:13 PM
 */
public class IndexComparator implements Comparator<AspectInterceptor> {
	
   @Override
   	public int compare(AspectInterceptor i1, AspectInterceptor i2) {
   		if((i1.getIndex()>i2.getIndex())){
   			return 1;
   		}else if((i1.getIndex()==i2.getIndex())){
   			return 0;
   		}else if((i1.getIndex()<i2.getIndex())){
   			return -1;
   		}
   		return 0;
   	}
	   
}