package com.woshidaniu.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.woshidaniu.security.algorithm.Algorithm;
import com.woshidaniu.security.algorithm.SMS4Codec;
/**
 * 
 *@类名称:SecretKeyUtils.java
 *@类描述：秘钥工具类
 *@创建人：kangzhidong
 *@创建时间：2014-12-31 上午10:15:39
 *@版本号:v1.0
 */
public class SecretKeyUtils {

	public static final int KEY_SIZE = 128;
	public static final int CACHE_SIZE = 1024;
	
	static {
		// 加入bouncyCastle支持
		Security.addProvider(new BouncyCastleProvider());
	}

	public static SecretKey getSecretKey(String base64Key) throws Exception{
		DESKeySpec dks = new DESKeySpec(Base64.decodeBase64(base64Key));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		return  keyFactory.generateSecret(dks);
	}
	
	public static KeyPair genKeyPair(String algorithm) throws GeneralSecurityException {
		// 定义密钥长度1024位
		return SecretKeyUtils.genKeyPair(algorithm, 1024);
	}
	
	public static KeyPair genKeyPair(String algorithm,int keySize) throws GeneralSecurityException {
		// 通过KeyPairGenerator产生密钥,注意：这里的key是一对钥匙！！
		return SecretKeyUtils.genKeyPair(null,algorithm, 1024);
	}
	
	public static KeyPair genKeyPair(String seed, String algorithm,int keySize) throws GeneralSecurityException {
		//产生一个RSA密钥生成器KeyPairGenerator(顾名思义：一对钥匙生成器)
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm.toString());
		// 初始化密钥生成器
		keyGen.initialize(keySize, SecretKeyUtils.getSecureRandom(seed));
		// 通过KeyPairGenerator产生密钥,注意：这里的key是一对钥匙！！
		return keyGen.generateKeyPair();
	}
	
	/**
	 * 
	 * @description: 生成共有key
	 * @author : kangzhidong
	 * @date : 2014-9-26
	 * @time : 下午6:21:13
	 * @param pubkey_bytes
	 * @param algorithm
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static PublicKey genPublicKey(byte[] pubkey_bytes,String algorithm) throws GeneralSecurityException {
		return KeyFactory.getInstance(algorithm.toString()).generatePublic(new X509EncodedKeySpec(pubkey_bytes));
	}

	/**
	 * 
	 * @description: 生成私有key
	 * @author : kangzhidong
	 * @date : 2014-9-26
	 * @time : 下午6:21:57
	 * @param prikey_bytes
	 * @param algorithm
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static PrivateKey genPrivateKey(byte[] prikey_bytes,String algorithm) throws GeneralSecurityException {
		return KeyFactory.getInstance(algorithm.toString()).generatePrivate(new PKCS8EncodedKeySpec(prikey_bytes));
	}
	
	public static SecretKey genSecretKey(String key,String algorithm) throws GeneralSecurityException {
		return SecretKeyUtils.genSecretKey(key.getBytes(), algorithm);
	}

	/**
	 * 
	 * @description: 秘密（对称）密钥(SecretKey继承(key))
	 * @author : kangzhidong
	 * @date : 2014-9-29
	 * @time : 上午10:01:42 
	 * @param key
	 * @param algorithm
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static SecretKey genSecretKey(byte[] key,String algorithm) throws GeneralSecurityException {
		return new SecretKeySpec(key, algorithm);
	}
	
	/**
	 * 
	 * @description: <p> 根据秘钥种子生成随机密钥</p>
	 * @author : kangzhidong
	 * @date : 2014-9-28
	 * @time : 上午10:16:29
	 * @param seed 密钥种子
	 * @param algorithm 生成密匙的算法
	 * @param keySize 密匙长度
	 * @return 二进制密钥
	 * @throws GeneralSecurityException
	 */
	public static SecretKey genSecretKey(String seed, String algorithm,int keySize) throws GeneralSecurityException {
		/*
			 如果要生成密钥，必须使用"真正的随机"数。
			例如，在Random类中的常规的随机数发生器，是根据当前的日期和时间来产生随机数的，因此它不够随 机。
			例如，假设计算机时钟可以精确到1/10秒，那么，每天最多存在864,000个种子。如果攻击者知道发布密钥的日期（通常可以由截止日期推算出 来），
			那么就可以很容易地生成那一天所有可能的种子。
			SecureRandom类产生的随机数，远比由Random类产生的那些数字安全得多。
			你仍然需要提供一个种子，以便在一个随机点上开始生成数字 序列。
			要这样做，最好的方法是从一个诸如白噪声发生器之类的硬件设备那里获取输入。
			另一个合理的随机输入源是请用户在键盘上进行随心所欲的盲打，但是每次 敲击键盘只为随机种子提供1位或者2位。
			一旦你在字节数组中收集到这种随机位后，就可以将它传递给setSeed方法或者构造器。
		*/
		KeyGenerator keygen = KeyGenerator.getInstance(algorithm.toString());
		// 初始化密钥生成器，AES要求密钥长度为128位、192位、256位；DES密匙长度为:56位
		keygen.init(keySize, SecretKeyUtils.getSecureRandom(seed));
		// 生成密钥
		return keygen.generateKey();
	}
	
	/**
	 * 
	 * @description: 生成相应算法密钥
	 * @author : kangzhidong
	 * @date : 2014-9-28
	 * @time : 下午6:54:55 
	 * @param algorithm
	 * @return SecretKey 密钥
	 * @throws GeneralSecurityException
	 */
	public static SecretKey genSecretKey(String algorithm) throws GeneralSecurityException {
		//初始化KeyGenerator
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm.toString());
		//产生密钥
		return keyGenerator.generateKey();
	}
	
	/**
	 * 
	 * @description: 生成相应算法密钥
	 * @author : kangzhidong
	 * @date : 2014-9-28
	 * @time : 下午6:54:55 
	 * @param algorithm
	 * @return byte[] 密钥
	 * @throws GeneralSecurityException
	 */
	public static byte[] genBinarySecretKey(String algorithm) throws GeneralSecurityException {
		// 获取二进制密钥编码形式
		return SecretKeyUtils.genSecretKey(algorithm).getEncoded();
	}

	public static byte[] genBinarySecretKey(String seed, String algorithm,int keySize) throws GeneralSecurityException {
		// 获取二进制密钥编码形式
		return SecretKeyUtils.genSecretKey(seed, algorithm, keySize).getEncoded();
	}

	/**
	 * 
	 * @description: 生成随机密钥，java6只支持56位密钥，bouncycastle支持64位密钥
	 * @author : kangzhidong
	 * @date : 2014-9-26
	 * @time : 下午2:48:52
	 * @param algorithm 生成密匙的算法
	 * @param keySize 密匙长度
	 * @return byte[] 二进制密钥
	 * @throws Exception
	 */
	public static SecretKey genSecretKey(String algorithm, int keySize) throws GeneralSecurityException {
		return SecretKeyUtils.genSecretKey(null, algorithm, keySize);
	}

	public static byte[] genBinarySecretKey(String algorithm, int keySize) throws GeneralSecurityException {
		return SecretKeyUtils.genBinarySecretKey(null, algorithm, keySize);
	}

	
	/**
	 * 
	 * @description: 加密解密第一步：从一组固定的原始数据（也许是由口令或者随机击键产生的）来生成一个密钥
	 * @author : kangzhidong
	 * @date : 2014-9-28
	 * @time : 上午10:44:05 
	 * @param keySpec
	 * @param algorithm
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static SecretKey genSecretKey(KeySpec keySpec, String algorithm) throws GeneralSecurityException {
		// 生成指定秘密密钥算法的 SecretKeyFactory 对象。  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm.toString());  
        // 根据提供的密钥规范（密钥材料）生成 SecretKey 对象,利用密钥工厂把KeySpec转换成一个SecretKey对象  
		return keyFactory.generateSecret(keySpec);
	}

	public static SecretKey  genPBEKey(String password, String algorithm) throws GeneralSecurityException {
		return SecretKeyUtils.genPBEKey(password.toCharArray(), algorithm);
	}

	public static SecretKey genPBEKey(char[] password, String algorithm) throws GeneralSecurityException {
		// 实例化PBE密钥
		PBEKeySpec keySpec = new PBEKeySpec(password);
		// 生成密钥
		return SecretKeyUtils.genSecretKey(keySpec, algorithm);
	}

	public static SecretKey  genDESKey(String key) throws GeneralSecurityException {
		return SecretKeyUtils.genDESKey(key.getBytes());
	}
	
	public static SecretKey  genDESKey(byte[] key) throws GeneralSecurityException {
		// 实例化Des密钥
		DESKeySpec dks = new DESKeySpec(key);
		// 生成密钥
		return SecretKeyUtils.genSecretKey(dks, Algorithm.KEY_DES);
	}

	public static SecretKey  genDESedeKey(String key) throws GeneralSecurityException {
		return SecretKeyUtils.genDESedeKey(key.getBytes());
	}
	
	/**
	 * 转换密钥
	 * 
	 * @param  key 二进制密钥
	 * @return Key 密钥
	 * */
	public static SecretKey genDESedeKey(byte[] key) throws GeneralSecurityException {
		// 实例化Des密钥
		DESedeKeySpec dks = new DESedeKeySpec(key);
		// 生成密钥
		return SecretKeyUtils.genSecretKey(dks, Algorithm.KEY_DESEDE);
	}

	public static SecureRandom getSecureRandom(){
		return SecretKeyUtils.getSecureRandom(null);
	}
	
	public static SecureRandom getSecureRandom(String seed){
		//实例化安全随机数
		SecureRandom secureRandom;
		if (seed != null && !"".equals(seed)) {
			secureRandom = new SecureRandom(seed.getBytes());
		} else {
			secureRandom = new SecureRandom();
		}
		return secureRandom;
	}

	public static byte[] getRandomKey(int keysize){
		//生成随机数
		return SecretKeyUtils.getRandomKey(null,keysize);
	}
	
	public static byte[] getRandomKey(String seed,int keysize){
		//生成随机数
		return SecretKeyUtils.getSecureRandom(seed).generateSeed(keysize);
	}
	
	
	/**
	 * 
	 * @description: 从 *.key 文件中读取 SecretKey 对象
	 * @author : kangzhidong
	 * @date : 2014-9-28
	 * @time : 上午11:25:08 
	 * @param inStream
	 * @return
	 */
	public static SecretKey readKey(InputStream inStream){
		SecretKey key = null;
		try {
			ObjectInputStream keyIn = new ObjectInputStream(inStream);
			//读取SecretKey对象
			key = (SecretKey) keyIn.readObject();
			//关闭输入流
			keyIn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return key;
	}
	
	/**
	 * 
	 * @description: 将SecretKey 对象 写到 *.key 文件中 
	 * @author : kangzhidong
	 * @date : 2014-9-28
	 * @time : 上午11:25:43 
	 * @param key
	 * @param outStream
	 */
	public static void writeKey(Key key,OutputStream outStream){
		try {
			ObjectOutputStream out = new ObjectOutputStream (outStream);
			out.writeObject(key);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) throws Exception {
		
		
	/*	//AES要求密钥长度为128位、192位、256位；DES密匙长度为:56位
		//生成AES密匙
		Key key2 = SecretKeyUtils.genSecretKey(String.KEY_AES, 128);
		//保存AES密匙
		SecretKeyUtils.writeKey(key2, new FileOutputStream("D:/secret.key"));*/
		
		
	 /* for (Provider p : Security.getProviders()) { //System.out.println(p);
		  for (Map.Entry<Object, Object> entry : p.entrySet()) {
			  System.out.println("\t"+entry.getKey()); 
		  } 
	  }
		
		*/
		String input = "01D644043343D9A2E050007F01002ECA01D644043343D9A2E050007F01002ECAsss";
		
		//RSAPublicKey publicKey = (RSAPublicKey) SecretKeyUtils.genKeyPair(Algorithm.KEY_RSA).getPublic();
		
		//根据学生号ID,课程ID生成32位key
		byte[] key = input.getBytes();
		System.out.println(key.length);
		byte[] in = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab,
				(byte) 0xcd, (byte) 0xef, (byte) 0xfe, (byte) 0xdc,
				(byte) 0xba, (byte) 0x98, 0x76, 0x54, 0x32, 0x10 };
		
		System.out.println();
		// =================================
		System.out.println("\r加密前：");
		for (int i = 0; i < in.length; i++){
			System.out.print(Integer.toHexString(in[i] & 0xff) );
		}
		// =================================
		System.out.println("\r加密后：");
		byte[] out1 = SMS4Codec.getInstance().encrypt(in, key );
		for (int i = 0; i < out1.length; i++){
			System.out.print(Integer.toHexString(out1[i] & 0xff) );
		}
		// =================================
		System.out.println("\r解密后：");
		byte[] out2 = SMS4Codec.getInstance().encrypt(out1, key );
		for (int i = 0; i < out2.length; i++){
			System.out.print(Integer.toHexString(out2[i] & 0xff) );
		}
		
		//System.out.println(SecretKeyUtils.genSecretKeyString(String.KEY_SM3, 1024));
		
		/*
		byte[] key = Base64.decodeBase64("123456");
		
		//new MD5Digest().update(key,0,key.length)
		SM3Digest digest = new SM3Digest();
		
		digest.update(key,0,key.length)
		digest.doFinal(arg0, arg1)*/
		
		
		
		//System.out.println(SecretKeyUtils.getSecretKeyHex("132", String.KEY_AES,128));

		
	}

}
