 package com.woshidaniu.security.algorithm;
 import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

import org.bouncycastle.util.encoders.Base64;

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
 public class RSABase64Codec implements KeyPairCodec {
	/**
     * RSA最大加密明文大小 
     */  
    private static final int MAX_ENCRYPT_BLOCK = 117;  
      
    /**
     * RSA最大解密密文大小 
     */  
    private static final int MAX_DECRYPT_BLOCK = 128;  
    
	public final static String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCtS6EXfulk2AH/4k72fH4nG2rskLdzatmmOUWT/EHaCxQg5v8doO3fPOZgLsk9SfOiY8rUXPtHUowC+YmfQSxHTCZWGeVFb94F/Oa+SzT38g8gfwg9YXCSVbW84O7mUkRZ/MkdA8BpXcLvUMQ4qUQM5yQ7Rv8DjxtzzaoeuU8t0QIDAQAB";

	public final static  String private_key = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAK1LoRd+6WTYAf/iTvZ8ficbauyQt3Nq2aY5RZP8QdoLFCDm/x2g7d885mAuyT1J86JjytRc+0dSjAL5iZ9BLEdMJlYZ5UVv3gX85r5LNPfyDyB/CD1hcJJVtbzg7uZSRFn8yR0DwGldwu9QxDipRAznJDtG/wOPG3PNqh65Ty3RAgMBAAECgYANmhoS1gtLtwixJPo9rpfYSfaiitlbXc+nlGc5rlsc2jHCQ19S3AbPtt/+PK3e+ab94AdTk9jFSF1k/mEm9t6auL7vn5pWf+e71po0Z3MjWx6sAKJRlF7e2tPY1h+CxxE/zkwmJeHBpTNDw1BSoz6p8h7H5Z7OTwj+JaJLAq2ZYQJBAOxEevSVFDMacOYbMU9Kbs0Vdx715b5twX70EMCTQGndH2NkVBAp8ghuf98soDp8q92r02nMk+rm0VNkqz3fAicCQQC7xMYeDJqhlGqSUGC1eCG3YzXzdvXC1txM3ifZEySVygWrgtJn2PpfCe+2qgL0fKty8IquSCNOpECa5XgDK+NHAkAsyi6k/z39Eh4lATKv3WRz7IXQkL0lBJmR/6LNXxzu9MRmizaEee8wOgJzxHn7nHbYZyKFBAtmlKtKK+Nqr0QxAkBkePp6AcyMQVp1SEU3VTzVYGTyDembhFUFXKp94VxmOl+mUq47m0L9r9dxKq/CGyOysTvD3h3masWXHMqHB+jpAkB2oWnHXBIfw2f0qf8xssuAZqHMOdza1hKPCkK+AFc+6txdCaS0JJhbKbEZFIcLl41sLsfx8pXpEpFPAlJRe+Az";
	
 	private static RSABase64Codec instance = null;
	private RSABase64Codec(){};
	public static RSABase64Codec getInstance(){
		instance= (instance==null)?instance=new RSABase64Codec():instance;
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
			return CharsetUtils.newStringUtf8(Base64.encode(SignatureUtils.sign(Base64.decode(encryptedBytes), signature, privateKey)));
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
			return SignatureUtils.verify(Base64.decode(encryptedBytes), Base64.decode(sign), signature, publicKey);
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
			// 对数据Base64解密
	 		encryptedBytes = Base64.decode(encryptedBytes);
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
			// 对数据Base64解密
	 		encryptedBytes = Base64.decode(encryptedBytes);
	 		// 对数据分段解密  
			return CipherUtils.segment(cipher, encryptedBytes, MAX_DECRYPT_BLOCK);
 		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*private byte[] decode(Cipher cipher,byte[] encryptedBytes) throws GeneralSecurityException{
 		// 对数据Base64解密
 		encryptedBytes = Base64.decode(encryptedBytes);
 		
 		int inputLen = encryptedBytes.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段解密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {  
                cache = cipher.doFinal(encryptedBytes, offSet, MAX_DECRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(encryptedBytes, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_DECRYPT_BLOCK;  
        } 
        try {
			byte[] decryptedData = out.toByteArray();  
			out.close();  
			return decryptedData;
		} catch (IOException e) {
			return null;
		} 
 	}*/
	
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
	 		return Base64.encode(encryptedData);
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
	 		return Base64.encode(encryptedData);
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
	 		return Base64.encode(encryptedData);
		} catch (IOException e) {
			return null;
		}  
	}*/
	
 	
 	public static void main(String[] args) throws Exception {
 		 
 		String publicKey = RSABase64Codec.public_key;
 	 	String privateKey = RSABase64Codec.private_key;

 	 	RSABase64Codec codec = RSABase64Codec.getInstance();

 		String s = codec.encodeByPublicKey("aq_03,android,123456",publicKey);
 		System.out.println("密文: " + s);
 		System.out.println("明文: " + codec.decodeByPrivateKey(s,privateKey));

 		System.out.println("---------------------------------------------------------------- ");
 		// ----------------------------------------------------------------

 		String s3 = codec.encodeByPublicKey("jwglxt/jxjh/xqzxjh/xqzxjhcj/XqzxjhrwBjck.jsp",publicKey);
 		System.out.println("密文: " + s3);
 		System.out.println("明文: " + codec.decodeByPrivateKey(s3,privateKey));
 		
 	 		
 		System.out.println("---------------------------------------------------------------- ");
 		
 	 		KeyPairEntry keyPair = RSABase64Codec.getInstance().initKeyEntry(512);
 	 		publicKey = keyPair.getPublicKey();
 	 		privateKey = keyPair.getPrivateKey();
 	 		System.out.println("公钥: \n\r" + publicKey);
 	 		System.out.println("私钥： \n\r" + privateKey);

 	 		System.out.println("公钥加密——私钥解密");
 	 		String inputStr = "abc";
 	 		byte[] data = inputStr.getBytes();

 	 		byte[] encodedData = RSABase64Codec.getInstance().encodeByPublicKey(data, publicKey);

 	 		byte[] decodedData = RSABase64Codec.getInstance().decodeByPrivateKey(encodedData,privateKey);

 	 		String outputStr = new String(decodedData);
 	 		System.out.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);


 	 		System.out.println("私钥加密——公钥解密");
 	 		String inputStr1 = "sign";
 	 		byte[] data1 = inputStr1.getBytes();

 	 		byte[] encodedData1 = RSABase64Codec.getInstance().encodeByPrivateKey(data1, privateKey);

 	 		byte[] decodedData1 = RSABase64Codec.getInstance().decodeByPublicKey(encodedData1, publicKey);

 	 		String outputStr1 = new String(decodedData1);
 	 		System.out.println("加密前: " + inputStr1 + "\n\r" + "解密后: " + outputStr1);

 	 		System.out.println("私钥签名——公钥验证签名");
 	 		// 产生签名
 	 		String sign = RSABase64Codec.getInstance().sign(encodedData1, privateKey);
 	 		System.out.println("签名:\r" + sign);

 	 		// 验证签名
 	 		boolean status = RSABase64Codec.getInstance().verify(encodedData1, publicKey, sign);
 	 		System.out.println("状态:\r" + status);

	}
 }

