package com.woshidaniu.qrcode.utils;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.bouncycastle.util.encoders.Hex;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.woshidaniu.compress.ZLibUtils;
import com.woshidaniu.format.fastjson.FastJSONObject;
import com.woshidaniu.qrcode.io.BufferedImageLuminanceSource;
import com.woshidaniu.qrcode.io.BufferedImageWithLogoLuminanceSource;
import com.woshidaniu.qrcode.io.MatrixToImageWriter;
import com.woshidaniu.security.algorithm.RSACodec;

/**
 * 
 *@类名称	: QRCodeUtils.java
 *@类描述	：二维码工具类:生成和解析二维码；可处理有logo二维码
 *@创建人	：kangzhidong
 *@创建时间	：Mar 24, 2016 11:34:45 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class QRCodeUtils {

	private static final String CHARSET = "utf-8";
	private static final String FORMAT_NAME = "png";
	// 二维码尺寸
	private static final int QRCODE_SIZE = 200;
	// LOGO宽度
	private static final int WIDTH = 60;
	// LOGO高度
	private static final int HEIGHT = 60;

	public static String compress(String content,int times) throws UnsupportedEncodingException{
		//System.out.println("原文：" + content);
		//System.out.println("原文长度:\t" + content.length());
		//进行数据压缩
		byte[] compress1 = content.getBytes(CharEncoding.UTF_8);
		//压缩4次
		for (int i = 0; i < times; i++) {
			 compress1 = ZLibUtils.compress(compress1);
		}
		String encryptedText = new String( Hex.encode(compress1));
		//System.out.println("压缩后加密字符串:\t" + encryptedText);
		//System.out.println("压缩后加密长度:\t" + encryptedText.length());
		return encryptedText;
	}

	public static String decompress(String encryptedText,int times) throws UnsupportedEncodingException{
		//System.out.println("密文：" + encryptedText);
		//System.out.println("密文长度:\t" + encryptedText.length());
		//进行数据解压
		byte[] decompress =  Hex.decode(encryptedText.getBytes());
		//解压4次
		for (int i = 0; i < times; i++) {
			decompress = ZLibUtils.decompress(decompress);
		}
		
		encryptedText = new String(decompress,CharEncoding.UTF_8);
		
		//System.out.println("解压后字符串:\t" + encryptedText);
		//System.out.println("解压后长度:\t" + encryptedText.length());
		
		return encryptedText;
	}
	
	/**
	 * 
	 *@描述：插入LOGO
	 *@创建人:kangzhidong
	 *@创建时间:2014-10-25下午01:11:49
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 * @param source 二维码图片
	 * @param imgPath LOGO图片地址
	 * @param needCompress 是否压缩
	 *@throws Exception
	 */
	public static void drawLogo(BufferedImage source, Image logo, boolean needCompress) throws Exception {
		int width = logo.getWidth(null);
		int height = logo.getHeight(null);
		if (needCompress) { 
			// 压缩LOGO
			if (width > WIDTH) {
				width = WIDTH;
			}
			if (height > HEIGHT) {
				height = HEIGHT;
			}
			Image image = logo.getScaledInstance(width, height,Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			logo = image;
		}
		// 插入LOGO
		Graphics2D graph = source.createGraphics();
		int x = (QRCODE_SIZE - width) / 2;
		int y = (QRCODE_SIZE - height) / 2;
		graph.drawImage(logo, x, y, width, height, null);
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}
	
	public static void drawLogo(BufferedImage source, Image logo) throws Exception {
		QRCodeUtils.drawLogo(source, logo, true);
	}
	
	public static BitMatrix getMatrix(Object element,int width,int height) throws WriterException {
		String content = FastJSONObject.toCleanJSONString(element);
		return QRCodeUtils.getMatrix(content, width, height);
	}
	
	public static BitMatrix getMatrix(String content,int width,int height) throws WriterException {
		// 用于设置QR二维码参数 
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// 设置QR二维码的纠错级别——这里选择最高H级别
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 设置编码方式 
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		//hints.put(EncodeHintType.MARGIN, 1);
		// 写入字节矩阵；参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数  
		return new MultiFormatWriter().encode(content,BarcodeFormat.QR_CODE, width, height, hints); 
	}

	public static byte[] getBitMatrixBytes(String content,int width,int height) throws WriterException, IOException{
		/*
		参数image表示获得的BufferedImage；
		参数format表示图片的格式，比如“gif”等；
		参数out表示输出流，如果要转成Byte数组，则输出流为ByteArrayOutputStream即可；
		*/
		ByteArrayOutputStream byteArray = null;
		byte[] binaryData;
		try {
			binaryData = null;
			byteArray = new ByteArrayOutputStream();
			//生成加密后数据内容的二维码图片
			BitMatrix byteMatrix = QRCodeUtils.getMatrix(content, width, height);
			//写的byte输出流
			MatrixToImageWriter.writeToStream(byteMatrix , FORMAT_NAME, byteArray);
			//返回图标字节内容 byte[];
			binaryData = byteArray.toByteArray();  
		} finally{
			IOUtils.closeQuietly(byteArray);
		}
		return binaryData;
	}
	
	
	/**
	 * 
	 *@描述：对内容进行压缩加密。
	 *@创建人:kangzhidong
	 *@创建时间:2014-9-28下午03:20:09
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param element
	 *@return
	 * @throws WriterException 
	 * @throws GeneralSecurityException 
	 *@throws GeneralSecurityException
	 */
	public static byte[] getBitMatrixBytesByMap(Map<String,Object> element) throws IOException, WriterException, GeneralSecurityException {
		//构造对象的JSON字符对象
		String plainText = FastJSONObject.toCleanJSONString(element);
		//进行数据压缩
		//plainText = compress(plainText, 3);
		//进行RSA公钥加密
		//System.out.println("加密前："+plainText);
		//plainText = RSACodec.getInstance().encodeByPublicKey(plainText,RSACodec.public_key);
		//System.out.println("加密后：" + plainText);
		//返回图标字节内容 byte[];
		return QRCodeUtils.getBitMatrixBytes(plainText, QRCodeUtils.QRCODE_SIZE, QRCodeUtils.QRCODE_SIZE);
	}
	
	/**
	 * 
	 *@描述：解析二维码图片数据
	 *@创建人:kangzhidong
	 *@创建时间:2014-9-28下午03:33:26
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param in
	 *@return
	 *@throws ReaderException
	 *@throws IOException
	 */
	public static Result getResult(InputStream in) throws ReaderException, IOException {
		//将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
		BufferedImage image = ImageIO.read(in);  
		if (image == null) { 
			System.out.println("Could not decode image"); 
			return null;
		} 
		//初始化BinaryBitmap对象
		BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source)); 	
		
		Hashtable<DecodeHintType,String> hints= new Hashtable<DecodeHintType,String>();  
		/*
		 * 指定编码格式:注意这里utf-8一定要小写
		 *  这样就可以解决手机不能识别的问题，而且也能支持中文。
		 * 至于原因，查看了源代码后，发现使用“UTF-8”，会在文本编码前添加一段ECI(扩充解释Extended Channel Interpretation)
		 * 编码，就是这段编码导致手机不能解析。如果使用小写"utf-8"会使这个ECI判断失效而不影响内容编码方式。
		 * 至于详细的ECI解释，可以看《QRCode 编码解码标准》
		 */
		hints.put(DecodeHintType.CHARACTER_SET, CHARSET); 
		
		//使用Google 二维码对象解析二维码
		return new MultiFormatReader().decode(bitmap,hints);
	}
	
	public static Result getResultWithLogo(InputStream in) throws ReaderException, IOException {
		//将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
		BufferedImage image = ImageIO.read(in);  
		if (image == null) { 
			System.out.println("Could not decode image"); 
			return null;
		} 
		//初始化BinaryBitmap对象
		BufferedImageWithLogoLuminanceSource source = new BufferedImageWithLogoLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source)); 	
		
		//使用Google 二维码对象解析二维码
		Hashtable<DecodeHintType,String> hints= new Hashtable<DecodeHintType,String>();  
		/*
		 * 指定编码格式:注意这里utf-8一定要小写
		 *  这样就可以解决手机不能识别的问题，而且也能支持中文。
		 * 至于原因，查看了源代码后，发现使用“UTF-8”，会在文本编码前添加一段ECI(扩充解释Extended Channel Interpretation)
		 * 编码，就是这段编码导致手机不能解析。如果使用小写"utf-8"会使这个ECI判断失效而不影响内容编码方式。
		 * 至于详细的ECI解释，可以看《QRCode 编码解码标准》
		 */
		hints.put(DecodeHintType.CHARACTER_SET, CHARSET); 
		
		return new MultiFormatReader().decode(bitmap,hints);
	}
	
	
	/**
	 * 
	 *@描述：解析二维码图片数据
	 *@创建人:kangzhidong
	 *@创建时间:2014-9-28下午03:31:50
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param bytes
	 *@return
	 *@throws ReaderException
	 *@throws IOException
	 */
	public static Result getResult(byte[] bytes) throws ReaderException, IOException {
		//将bytes作为输入流；
		return QRCodeUtils.getResult(new ByteArrayInputStream(bytes));  
	}
	
	public static Result getResultWithLogo(byte[] bytes) throws ReaderException, IOException {
		//将bytes作为输入流；
		return QRCodeUtils.getResultWithLogo(new ByteArrayInputStream(bytes));  
	}
	
	
	/**
	 * 
	 *@描述：对内容进行压缩加密。
	 *@创建人:kangzhidong
	 *@创建时间:2014-9-28下午03:20:09
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param element
	 *@return
	 * @throws WriterException 
	 * @throws GeneralSecurityException 
	 *@throws GeneralSecurityException
	 */
	public static byte[] encrypt(Map<String,Object> element) throws IOException, WriterException, GeneralSecurityException {
		//构造对象的JSON字符对象
		String plainText = FastJSONObject.toCleanJSONString(element);
		//进行数据压缩
		plainText = compress(plainText, 3);
		//进行RSA公钥加密
		//System.out.println("加密前："+plainText);
		plainText = RSACodec.getInstance().encodeByPublicKey(plainText,RSACodec.public_key);
		//System.out.println("加密后：" + plainText);
		//返回图标字节内容 byte[];
		return QRCodeUtils.getBitMatrixBytes(plainText, QRCodeUtils.QRCODE_SIZE, QRCodeUtils.QRCODE_SIZE);
	}
	
	public static String decrypt(byte[] bytes) throws ReaderException, IOException   {
		return QRCodeUtils.decrypt(new ByteArrayInputStream(bytes));
	}
	
	public static String decrypt(InputStream in) throws ReaderException, IOException   {
		//获取二维码数据内容
		String encryptedText = QRCodeUtils.getResult(in).getText();
		//进行RSA私钥解密
		//System.out.println("解密前："+encryptedText);
		encryptedText = RSACodec.getInstance().decodeByPrivateKey(encryptedText,RSACodec.private_key);
		//进行数据解压
		encryptedText = decompress(encryptedText,3);
		//System.out.println("解密后：" + encryptedText);
		//返回解码后的内容
		return encryptedText;
	}
	
	
	/**
	 * 
	 *@描述：生成二维码(内嵌LOGO)
	 *@创建人:kangzhidong
	 *@创建时间:2014-10-25下午02:24:29
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param content		内容
	 *@param logo			LOGO对象
	 *@param output			输出流
	 *@param needCompress	是否压缩LOGO
	 * @throws Exception 
	 */
	public static void encode(String content,Image logo,OutputStream output, boolean needCompress) throws Exception{
		//进行数据压缩
		content = compress(content, 3);
		//初始化二维码数据位阵
		BitMatrix bitMatrix = QRCodeUtils.getMatrix(content, QRCODE_SIZE, QRCODE_SIZE);
		//生成图片
		BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
		//判断logo是否存在
		if (logo != null ) {
			// 插入logo
			QRCodeUtils.drawLogo(image, logo, needCompress);
		}
		ImageIO.write(image, FORMAT_NAME, output);
	}

	/**
	 * 
	 *@描述：生成二维码(内嵌LOGO)
	 *@创建人:kangzhidong
	 *@创建时间:2014-10-25下午02:23:42
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param content	内容
	 *@param logo		LOGO对象
	 *@param output		输出流
	 *@throws Exception
	 */
	public static void encode(String content,Image logo,OutputStream output) throws Exception {
		QRCodeUtils.encode(content, logo, output, false);
	}
	
	/**
	 * 
	 *@描述：生成二维码(无LOGO)
	 *@创建人:kangzhidong
	 *@创建时间:2014-10-25下午02:31:29
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param content
	 *@param output
	 * @throws Exception 
	 *@throws Exception
	 */
	public static void encode(String content,OutputStream output) throws Exception  {
		QRCodeUtils.encode(content, null, output, false);
	}
	
	/**
	 * 
	 *@描述： 解析二维码
	 *@创建人:kangzhidong
	 *@创建时间:2014-10-25下午02:00:59
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param qrCodePath 二维码图片路径
	 *@return
	 * @throws IOException 
	 * @throws ReaderException 
	 * @throws FileNotFoundException 
	 *@throws Exception
	 */
	public static String decode(String qrCodePath) throws FileNotFoundException, ReaderException, IOException{
		return QRCodeUtils.decode(new FileInputStream(new File(qrCodePath)));
	}
	
	/**
	 * 
	 *@描述：解析二维码
	 *@创建人:kangzhidong
	 *@创建时间:2014-10-25下午02:21:26
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param in 二维码图片输入流
	 *@return
	 * @throws IOException 
	 * @throws ReaderException 
	 *@throws Exception
	 */
	public static String decode(InputStream in) throws ReaderException, IOException  {
		//获取二维码数据内容
		String encryptedText = QRCodeUtils.getResult(in).getText();
		//进行数据解压；返回解码后的内容
		return decompress(encryptedText,3);
	}

	/**
	 * 
	 *@描述： 解析二维码(内嵌LOGO)
	 *@创建人:kangzhidong
	 *@创建时间:2014-10-25下午02:00:59
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param qrCodePath 二维码图片路径
	 *@return
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 *@throws Exception
	 */
	public static String decodeWithLogo(String qrCodePath) throws FileNotFoundException, Exception  {
		return QRCodeUtils.decodeWithLogo(new FileInputStream(new File(qrCodePath)));
	}
	
	/**
	 * 
	 *@描述：解析二维码(内嵌LOGO)
	 *@创建人:kangzhidong
	 *@创建时间:2014-10-25下午02:21:26
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param in 二维码图片输入流
	 *@return
	 *@throws Exception
	 */
	public static String decodeWithLogo(InputStream in) throws Exception {
		//获取二维码数据内容
		String encryptedText = QRCodeUtils.getResultWithLogo(in).getText();
		//进行数据解压；返回解码后的内容
		return decompress(encryptedText,3);
	}

}