package com.woshidaniu.security.algorithm;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyPairCodec {
	
	interface KeyPairEntry {
		 
		PublicKey getPublic();
		
		PrivateKey getPrivate();
		
		String getPublicKey();
		
		String getPrivateKey();
		
	}
	
	/**
	 * 
	 * @description: 初始化 钥匙对
	 * @author : kangzhidong
	 * @date 下午1:57:18 2014-10-9 
	 * @return
	 * @throws GeneralSecurityException
	 * @return  Map<String,Object> 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	 public KeyPair initKey() throws GeneralSecurityException;
	 public KeyPair initKey(int keysize) throws GeneralSecurityException;
	 
	 public KeyPairEntry initKeyEntry() throws GeneralSecurityException;
	 public KeyPairEntry initKeyEntry(int keysize) throws GeneralSecurityException;
	 
	 /**
	  * 
	  * @description: 还原公钥
	  * @author : kangzhidong
	  * @date 下午1:57:08 2014-10-9 
	  * @param key
	  * @return
	  * @throws GeneralSecurityException
	  * @return  PublicKey 返回类型
	  * @throws  
	  * @modify by:
	  * @modify date :
	  * @modify description : TODO(描述修改内容)
	  */
	 public PublicKey toPublicKey(byte[] base64PublicKeyBytes) throws GeneralSecurityException;
	 
	 public PublicKey toPublicKey(String base64PublicKeyText) throws GeneralSecurityException;
	 
	 /**
	  * 
	  * @description: 还原私钥
	  * @author : kangzhidong
	  * @date 下午1:56:39 2014-10-9 
	  * @param key
	  * @return
	  * @throws GeneralSecurityException
	  * @return  PrivateKey 返回类型
	  * @throws  
	  * @modify by:
	  * @modify date :
	  * @modify description : TODO(描述修改内容)
	  */
	 public PrivateKey toPrivateKey(byte[] base64PrivateKeyBytes) throws GeneralSecurityException;
	 
	 public PrivateKey toPrivateKey(String base64PrivateKeyText) throws GeneralSecurityException;
	 
}
