package com.woshidaniu.security.digest;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Map;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.woshidaniu.security.algorithm.Algorithm;
/**
 * 
 * @package com.ant4j.codec.digest
 * @className: DigestUtils
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-9-28
 * @time : 下午1:49:35
 */
public class DigestUtils {
	
	private static final int STREAM_BUFFER_LENGTH = 1024;

	static {
		// 加入bouncyCastle支持
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public static byte[] md5(String source){
		return md5(source.getBytes());
	}
	
	public static byte[] md5(byte[] source){
		return DigestUtils.getDigest(Algorithm.KEY_MD5).digest(source);
	}
	
	public static byte[] md5(InputStream source) throws IOException{
		return digest(DigestUtils.getDigest(Algorithm.KEY_MD5),source);
	}
	
	public static byte[] digest(MessageDigest digest, InputStream data) throws IOException {
        byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
        int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        while (read > -1) {
            digest.update(buffer, 0, read);
            read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        }
        return digest.digest();
    }
	
	public static MessageDigest getDigest(Algorithm algorithm) {
        return DigestUtils.getDigest(algorithm.toString());
    }
	
	public static MessageDigest getDigest(Algorithm algorithm,String provider) {
        return DigestUtils.getDigest(algorithm.toString(),provider);
    }
	
	public static MessageDigest getDigest(String algorithm) {
        try {
        	return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
	
	public static MessageDigest getDigest(String algorithm,String provider) {
        try {
        	return MessageDigest.getInstance(algorithm, provider);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
	
	public static void main(String[] args) {
		  //加入bouncyCastle支持 
		Security.addProvider(new BouncyCastleProvider());
		  for (Provider p : Security.getProviders()) { //System.out.println(p);
			  for (Map.Entry<Object, Object> entry : p.entrySet()) {
				  System.out.println("\t"+entry.getKey()); 
			  } 
		  }
		
		/*System.out.println(DigestUtils.getDigest(Algorithm.KEY_SM3));
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_RIPEMD128));
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_RIPEMD160));
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_RIPEMD256));
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_RIPEMD320));*/
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_SHA1,"BC").getProvider());
		System.out.println(DigestUtils.getDigest(Algorithm.KEY_SHA1,"BC").digest().length);
		
	}
}
