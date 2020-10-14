 package com.woshidaniu.security.algorithm;
 import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import com.woshidaniu.basicutils.CharsetUtils;
import com.woshidaniu.security.CipherUtils;
import com.woshidaniu.security.SecretKeyUtils;
import com.woshidaniu.security.SignatureUtils;




 /**
  * 
  * @className: RSACodec
  * @description: RSA安全编码组件
  * @author : kangzhidong
  * @date : 下午2:35:41 2014-10-9
  * @modify by:
  * @modify date :
  * @modify description :
  */
 public class RSACodec implements KeyPairCodec {

   /**
     * RSA最大加密明文大小 
     */  
    private static final int MAX_ENCRYPT_BLOCK = 117;  
      
    /**
     * RSA最大解密密文大小 
     */  
    private static final int MAX_DECRYPT_BLOCK = 128; 
    
	public final static String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCIyFsRh3vbNB4GrYCsmw2UdNAq5QrnHrml8JXM"
			+ "KRhCCGF99wDLT3bhoL8YvBywP3eWBX3IMHL6DKbDp3l1sqLT0LQ0TFRMwnvnLXsmzRjubeJiEfyY"
			+ "47FdtZGji4rllrekloqohypkcivHac7HOeuCsWd9vxD7gGZEig5T9Tu8QwIDAQAB";

	public final static  String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIjIWxGHe9s0HgatgKybDZR00Crl"
			+ "CuceuaXwlcwpGEIIYX33AMtPduGgvxi8HLA/d5YFfcgwcvoMpsOneXWyotPQtDRMVEzCe+cteybN"
			+ "GO5t4mIR/JjjsV21kaOLiuWWt6SWiqiHKmRyK8dpzsc564KxZ32/EPuAZkSKDlP1O7xDAgMBAAEC"
			+ "gYA/22nIsTu9hoNOEsozyt94I2Db5bpFVC7PrZYBjl3o1gjNyfbw89RIWcddQNUT401tGHs0bon+"
			+ "+tEQHquxqwggs4tr9DqWVttdOk+diSnmuneyahBRpgh42jXkf5NBZZZcIRx4Ry2TwtxHgcfx+Bqx"
			+ "gEjsyQKw6f/g1UceZftqQQJBAM5bF4aNHbeg35tijZ40yv8ldc0x5jLJnUTQGer+LtCrm+GIMmhw"
			+ "bE5clZJZV7qDK7pMjJvP7INz+/EWXEZGSAsCQQCpsG+K/Ho732E39WQP8AVGQR2nRDlqTXi6h+pw"
			+ "ZAIan66ucd29wL0hojF9XcaD4wWC+6PXyYbVLeaPD/eS5aepAkEAnUlfYCZ1rT6I0aZH7Xut8tZ5"
			+ "uQK8xJ9aKVY5Ox2tT05OjZRDX8m5M+1r8FX7AWXz0ZeBYU4Vp4ijU3rIsKPnSwJAZg3F1+omvZGI"
			+ "D7aW2nr5QRpyciG3AjbbsBuEJNoQ5eA5l5LF0JR1ax/38bUPakyECRW8oVADtnxnmIz60a8rGQJA"
			+ "MvDY4TlvOsvQcYJQMHHbvSeKgAP8mOJBM+QG4YOyBxAjDhxS3k6jkTWSqZh5xECpNQo+Exjlzgjf"
			+ "NCxBVSCuHQ==";
	
 	private static RSACodec instance = null;
	protected RSACodec(){};
	public static RSACodec getInstance(){
		instance= (instance==null)?instance=new RSACodec():instance;
		return  instance;
	}
	
 	/**
 	 * 初始化密钥
 	 * 
 	 * @return
 	 * @throws Exception
 	 */
	
 	public KeyPair initKey()  {
 		return this.initKey(512);
 	}
 	
 	public KeyPair initKey(int keysize) {
 		try {
			return SecretKeyUtils.genKeyPair(Algorithm.KEY_RSA,keysize);
 		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
 	}
 	
 	public KeyPairEntry initKeyEntry()  {
 		return this.initKeyEntry(512);
 	}
 	
 	class DefaultKeyPairEntry implements KeyPairEntry{
 		
 		KeyPair keyPair = null;
 		private DefaultKeyPairEntry(KeyPair keyPair){
 			this.keyPair = keyPair;
 		}
 		
 		public String getPublicKey() {
			return CharsetUtils.newStringUtf8(Base64.encode(keyPair.getPublic().getEncoded()));
		}
		
		public PublicKey getPublic() {
			return keyPair.getPublic();
		}
		
		public String getPrivateKey() {
			return CharsetUtils.newStringUtf8(Base64.encode(keyPair.getPrivate().getEncoded()));
		}
		
		public PrivateKey getPrivate() {
			return keyPair.getPrivate();
		}
 	}
 	
 	public KeyPairEntry initKeyEntry(final int keysize) {
 		try {
			KeyPair keyPair = SecretKeyUtils.genKeyPair(Algorithm.KEY_RSA,keysize);
			return new DefaultKeyPairEntry(keyPair);
 		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
 	}
 	
 	
	public PublicKey toPublicKey(String base64PublicKeyText)  {
 		return this.toPublicKey(base64PublicKeyText.getBytes());
	}

 	
	public PublicKey toPublicKey(byte[] base64PublicKeyBytes)  {
 		try {
			// 解密公钥
			byte[] pubkey_bytes = Base64.decode(base64PublicKeyBytes);
			// 取公钥匙对象
			return SecretKeyUtils.genPublicKey(pubkey_bytes, Algorithm.KEY_RSA);
 		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public PrivateKey toPrivateKey(String base64PrivateKeyText)  {
		return this.toPrivateKey(base64PrivateKeyText.getBytes());
	}
	
	
	public PrivateKey toPrivateKey(byte[] base64PrivateKeyBytes)  {
		try {
			// 解密私钥
			byte[] prikey_bytes = Base64.decode(base64PrivateKeyBytes);
			// 取私钥匙对象
			return SecretKeyUtils.genPrivateKey(prikey_bytes, Algorithm.KEY_RSA);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
 	
 	/**
 	 * 用私钥对信息生成数字签名
 	 * 
 	 * @param encryptedBytes 加密数据
 	 * @param base64PrivateKeyText  私钥
 	 * 
 	 * @return
 	 * @throws Exception
 	 */
 	public String sign(byte[] encryptedBytes, String base64PrivateKeyText)  {
 		try {
			// 取私钥匙对象
			PrivateKey privateKey =  toPrivateKey(base64PrivateKeyText);
			//获取签名对象
			Signature signature = SignatureUtils.getSignature(Algorithm.KEY_SIGNATURE_RSA);
			//用私钥对信息生成数字签名
			return CharsetUtils.newStringUtf8(Hex.encode(SignatureUtils.sign(Hex.decode(encryptedBytes), signature, privateKey)));
 		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
 	}


 	/**
 	 * 
 	 * @description:  校验数字签名
 	 * @author : kangzhidong
 	 * @date 下午1:42:32 2014-10-9 
 	 * @param plainBytes 加密数据
 	 * @param base64PublicKeyText 公钥
 	 * @param sign 数字签名
 	 * @return
 	 * @throws Exception
 	 * @return  boolean  校验成功返回true 失败返回false
 	 * @throws  
 	 * @modify by:
 	 * @modify date :
 	 * @modify description : 
 	 */
 	public boolean verify(byte[] encryptedBytes, String base64PublicKeyText, String sign)  {
 		return verify(encryptedBytes,base64PublicKeyText.getBytes(),sign);
 	}
 	
 	public boolean verify(byte[] encryptedBytes, byte[] base64PublicKeyText, String sign)  {
 		try {
			// 取公钥匙对象
			PublicKey publicKey = toPublicKey(base64PublicKeyText);
			//获取签名对象
			Signature signature = SignatureUtils.getSignature(Algorithm.KEY_SIGNATURE_RSA);
			// 验证签名是否正常
			return SignatureUtils.verify(Hex.decode(encryptedBytes), Hex.decode(sign), signature, publicKey);
 		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
 	}

 	/**
 	 * 解密<br>
 	 * 用私钥解密
 	 * 
 	 * @param plainBytes
 	 * @param key
 	 * @return
 	 * @throws Exception
 	 */
 	public String decodeByPrivateKey(String encryptedText, String base64PrivateKeyText)  {
 		// 取得私钥
		PrivateKey privateKey =  toPrivateKey(base64PrivateKeyText);
		//使用私钥解密
		return this.decode(encryptedText, privateKey);
	}
	
	public String decode(String encryptedText, PrivateKey privateKey)  {
		// 对数据加密
		return CharsetUtils.newStringUtf8(this.decode(encryptedText.getBytes(), privateKey));
	}
	
	public byte[] decodeByPrivateKey(byte[] encryptedBytes, String base64PrivateKeyText)  {
		// 取得私钥
		PrivateKey privateKey =  toPrivateKey(base64PrivateKeyText);
		//使用私钥解密
		return this.decode(encryptedBytes, privateKey);
	}
 	
	
	public byte[] decode(byte[] encryptedBytes, PrivateKey privateKey)  {
		try {
			// 取得Cipher对象
			Cipher cipher = CipherUtils.getDecryptCipher(Algorithm.KEY_RSA, privateKey);
			// 对数据Hex解密
			encryptedBytes = Hex.decode(encryptedBytes);
			// 对数据分段解密  
			return CipherUtils.segment(cipher, encryptedBytes, MAX_DECRYPT_BLOCK);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

 	/**
 	 * 解密<br>
 	 * 用公钥解密
 	 * 
 	 * @param plainBytes
 	 * @param key
 	 * @return
 	 * @throws Exception
 	 */
	public String decodeByPublicKey(String encryptedText, String base64PublicKeyText)  {
		// 取得公钥
 		PublicKey publicKey = toPublicKey(base64PublicKeyText);
 		//使用公钥解密
 		return this.decode(encryptedText, publicKey);
	}
	
	public String decode(String encryptedText, PublicKey publicKey)  {
		// 对数据加密
		return CharsetUtils.newStringUtf8(this.decode(encryptedText.getBytes(), publicKey));
	}
	
	public byte[] decodeByPublicKey(byte[] encryptedBytes, String base64PublicKeyText) {
 		// 取得公钥
 		PublicKey publicKey = toPublicKey(base64PublicKeyText);
 		//使用公钥解密
 		return this.decode(encryptedBytes, publicKey);
 	}
 
 	
	public byte[] decode(byte[] encryptedBytes, PublicKey publicKey)  {
 		try {
			// 取得Cipher对象
			Cipher cipher = CipherUtils.getDecryptCipher(Algorithm.KEY_RSA, publicKey);
			// 对数据Hex解密
			encryptedBytes = Hex.decode(encryptedBytes);
			// 对数据分段解密  
			return CipherUtils.segment(cipher, encryptedBytes, MAX_DECRYPT_BLOCK);
 		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
 	/**
 	 * 加密<br>
 	 * 用公钥加密
 	 * 
 	 * @param plainBytes
 	 * @param key
 	 * @return
 	 * @ 
 	 * @throws Exception
 	 */
	public String encodeByPublicKey(String plainText, String base64PublicKeyText)  {
		// 取得公钥
 		PublicKey publicKey = toPublicKey(base64PublicKeyText);
 		// 对数据加密
 		return this.encode(plainText, publicKey);
	}
	
	public String encode(String plainText, PublicKey publicKey)  {
		// 对数据加密
		return CharsetUtils.newStringUtf8(this.encode(plainText.getBytes(), publicKey));
	}
	
 	public byte[] encodeByPublicKey(byte[] plainBytes, String base64PublicKeyText) {
 		// 取得公钥
 		PublicKey publicKey = toPublicKey(base64PublicKeyText);
 		// 对数据加密
 		return this.encode(plainBytes, publicKey);
 	}

	public byte[] encode(byte[] plainBytes, PublicKey publicKey) {
 		try {
			// 取得Cipher对象
			Cipher cipher = CipherUtils.getEncryptCipher(Algorithm.KEY_RSA, publicKey);
			// 对数据分段加密  
			byte[] encryptedData = CipherUtils.segment(cipher, plainBytes, MAX_ENCRYPT_BLOCK);
			// 对数据加密
	 	 	return new String(Hex.encode(encryptedData)).getBytes();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
 	}
	
 	/**
 	 * 加密<br>
 	 * 用私钥加密
 	 * 
 	 * @param plainBytes
 	 * @param key
 	 * @return
 	 * @throws Exception
 	 */
	public String encodeByPrivateKey(String plainText, String base64PrivateKeyText)  {
		// 取得私钥
 		PrivateKey privateKey = toPrivateKey(base64PrivateKeyText);
 		// 对数据加密
		return this.encode(plainText, privateKey);
	}
	
	public String encode(String plainText, PrivateKey privateKey) { 
		// 对数据加密
		return CharsetUtils.newStringUtf8(this.encode(plainText.getBytes(), privateKey));
	}
	
 	public byte[] encodeByPrivateKey(byte[] plainBytes, String base64PrivateKeyText)  {
 		// 取得私钥
 		PrivateKey privateKey = toPrivateKey(base64PrivateKeyText);
 		// 对数据加密
 		return this.encode(plainBytes, privateKey);
 	}
 	
 	
 	public byte[] encode(byte[] plainBytes, PrivateKey privateKey)  {
 		try {
			// 取得Cipher对象
			Cipher cipher = CipherUtils.getEncryptCipher(Algorithm.KEY_RSA, privateKey);
			// 对数据分段加密  
			byte[] encryptedData = CipherUtils.segment(cipher, plainBytes, MAX_ENCRYPT_BLOCK);
			// 对数据加密
	 	 	return new String(Hex.encode(encryptedData)).getBytes();
 		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
 	}
 	
 	/*private byte[] encode(Cipher cipher,byte[] plainBytes) throws GeneralSecurityException{
		//分段加密
 	 	int inputLen = plainBytes.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段加密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(plainBytes, offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(plainBytes, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        try {
        	byte[] encryptedData = out.toByteArray();  
			out.close();
			// 对数据加密
	 	 	return new String(Hex.encode(encryptedData)).getBytes();
		} catch (IOException e) {
			return null;
		}  
	}*/
	
 	
 	public static void main(String[] args) throws Exception {
 		 
 		String publicKey = RSACodec.public_key;
 	 	String privateKey = RSACodec.private_key;

 	 	RSACodec codec = RSACodec.getInstance();

 		String s = codec.encodeByPublicKey("aq_03,android,123456",publicKey);
 		System.out.println("密文: " + s);
 		System.out.println("明文: " + codec.decodeByPrivateKey(s,privateKey));

 		System.out.println("---------------------------------------------------------------- ");
 		// ----------------------------------------------------------------

 		String s3 = codec.encodeByPublicKey("jwglxt/jxjh/xqzxjh/xqzxjhcj/XqzxjhrwBjck.jsp",publicKey);
 		System.out.println("密文: " + s3);
 		System.out.println("明文: " + codec.decodeByPrivateKey(s3,privateKey));
 		
 	 		
 		System.out.println("---------------------------------------------------------------- ");
 		
 	 		KeyPairEntry keyPair = RSACodec.getInstance().initKeyEntry(512);
 	 		publicKey = keyPair.getPublicKey();
 	 		privateKey = keyPair.getPrivateKey();
 	 		System.out.println("公钥: \n\r" + publicKey);
 	 		System.out.println("私钥： \n\r" + privateKey);

 	 		System.out.println("公钥加密——私钥解密");
 	 		String inputStr = "abc";
 	 		byte[] data = inputStr.getBytes();

 	 		byte[] encodedData = RSACodec.getInstance().encodeByPublicKey(data, publicKey);

 	 		byte[] decodedData = RSACodec.getInstance().decodeByPrivateKey(encodedData,privateKey);

 	 		String outputStr = new String(decodedData);
 	 		System.out.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);


 	 		System.out.println("私钥加密——公钥解密");
 	 		String inputStr1 = "sign";
 	 		byte[] data1 = inputStr1.getBytes();

 	 		byte[] encodedData1 = RSACodec.getInstance().encodeByPrivateKey(data1, privateKey);

 	 		byte[] decodedData1 = RSACodec.getInstance().decodeByPublicKey(encodedData1, publicKey);

 	 		String outputStr1 = new String(decodedData1);
 	 		System.out.println("加密前: " + inputStr1 + "\n\r" + "解密后: " + outputStr1);

 	 		System.out.println("私钥签名——公钥验证签名");
 	 		// 产生签名
 	 		String sign = RSACodec.getInstance().sign(encodedData1, privateKey);
 	 		System.out.println("签名:\r" + sign);

 	 		// 验证签名
 	 		boolean status = RSACodec.getInstance().verify(encodedData1, publicKey, sign);
 	 		System.out.println("状态:\r" + status);

	}
 }

