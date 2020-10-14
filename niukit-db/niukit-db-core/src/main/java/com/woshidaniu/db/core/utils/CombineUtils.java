package com.woshidaniu.db.core.utils;

/**
 * 合并数组
 */
public class CombineUtils {
	
	public static byte[] combine(byte[] bytesa,byte[] bytesb){
		int tl = bytesa.length+bytesb.length;
		byte[] bytes= new byte[tl];
		for(int i = 0;i<tl;i++){
			if(bytesa.length>0&&i<bytesa.length){
				bytes[i] = bytesa[i];
			}else{
				if(bytesb.length>0){
					int j = 0;
					bytes[i] = bytesb[j];
					j++;
				}
			}
		}
		return bytes;
	}
	
	public static int[] combine(int[] resultIds,int[] ids){
		int tl = resultIds.length+ids.length;
		int[] ints = new int[tl];
		for(int i = 0;i<tl;i++){
			if(resultIds.length>0&&i<resultIds.length){
				ints[i] = resultIds[i];
			}else{
				if(ids.length>0){
					int j = 0;
					ints[i] = ids[j];
					j++;
				}
			}
		}
		return resultIds;
	}
}
