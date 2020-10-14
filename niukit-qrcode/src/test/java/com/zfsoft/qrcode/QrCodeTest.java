package com.woshidaniu.qrcode;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import com.woshidaniu.qrcode.utils.QRCodeUtils;

import junit.framework.TestCase;

public class QrCodeTest extends TestCase {
	
	public void testEncode() {

		StringBuilder bulder = new StringBuilder();
		
		bulder.append("{");
			bulder.append("jxb_id:01D644043343D9A2E050007F01002ECA,");
			bulder.append("kch_id:01D644043343D9A2E050007F01002ECA,");
			bulder.append("xn:2014,");
			bulder.append("xq:'春',");
			bulder.append("xslist[");
			for (int i = 0; i < 300; i++) {
				for (int j = 0; j < 20; j++) {
					bulder.append("{");
					bulder.append("xh_id:01D644043343D9A2E050007F01002ECA,");
					bulder.append("xm_id:01D644043343D9A2E050007F01002ECA,");
					bulder.append("zpcj:'不及格',");
					bulder.append("bfzcj:100");
					bulder.append("},");
				}				
				bulder.deleteCharAt(bulder.length() -1);
			}
			bulder.deleteCharAt(bulder.length() -1);
			bulder.append("]}");
		
		// /String str = "{xh:3333333,kcdm:333333,cj:33333}";//二维码内容。
		String imgPath = "d:/test.png"; // 保存文件名。

		try {
			QRCodeUtils.encode(bulder.toString(), new FileOutputStream(imgPath));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void atestDecode() throws Exception{
		String imgPath = "D:/test.png"; 

		try { 
			String encryptedText = QRCodeUtils.decode(imgPath);
			
			System.out.println("解压后字符串:\t" + encryptedText);
			System.out.println("解压后长度:\t" + encryptedText.length());
			
		} catch (Exception e) { 
		    System.out.println(e.toString()); 
		} 
		
	}
	
	public void atestEncodeWithLogo() {

		StringBuilder bulder = new StringBuilder();
		
		bulder.append("{");
			bulder.append("jxb_id:01D644043343D9A2E050007F01002ECA,");
			bulder.append("kch_id:01D644043343D9A2E050007F01002ECA,");
			bulder.append("xn:2014,");
			bulder.append("xq:'春',");
			bulder.append("xslist[");
			for (int i = 0; i < 300; i++) {
				for (int j = 0; j < 20; j++) {
					bulder.append("{");
					bulder.append("xh_id:01D644043343D9A2E050007F01002ECA,");
					bulder.append("xm_id:01D644043343D9A2E050007F01002ECA,");
					bulder.append("zpcj:'不及格',");
					bulder.append("bfzcj:100");
					bulder.append("},");
				}				
				bulder.deleteCharAt(bulder.length() -1);
			}
			bulder.deleteCharAt(bulder.length() -1);
			bulder.append("]}");
		
		// /String str = "{xh:3333333,kcdm:333333,cj:33333}";//二维码内容。
		String imgPath = "d:/test2.png"; // 保存文件名。
		String logoPath = "F:/pic/1.jpg"; // logo文件名。
		try {
			Image src = ImageIO.read(new File(logoPath));
			QRCodeUtils.encode(bulder.toString(),src, new FileOutputStream(imgPath),true);

		} catch (Exception e) {
			  System.out.println(e.toString()); 
		}
	}
	
	public void atestDecodeWithLogo() throws Exception{
		String imgPath = "D:/test2.png"; 

		try { 
			String encryptedText = QRCodeUtils.decodeWithLogo(imgPath);
			
			System.out.println("解压后字符串:\t" + encryptedText);
			System.out.println("解压后长度:\t" + encryptedText.length());
			
		} catch (Exception e) { 
		    System.out.println(e.toString()); 
		} 
		
	}

}
