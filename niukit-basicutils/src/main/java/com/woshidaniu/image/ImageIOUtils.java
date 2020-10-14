/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.image;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *@类名称	: ImageIOUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 11, 2016 4:35:31 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public abstract class ImageIOUtils {

	 
	/**
	 * 
	 *@描述：获取图片尺寸（宽、高） 
	 *@创建人:kangzhidong
	 *@创建时间:Dec 16, 20154:29:57 PM
	 *@param file
	 *@return 返回int型数组，第一位宽、第二位高 
	 *@throws IOException
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static int[] getImageSize(File file) throws IOException{
		//BufferedImage  图片越大，消耗的时间越长，针对百K以下的图片速度较快
		//ImageReader    不论图片大小，基本恒定时间，在100ms左右
		//在教务系统中可能对图片处理要求不是太高目前我们采用BufferedImage。
		int[] size = new int[2];
		InputStream inputStrean	 = new BufferedInputStream(new FileInputStream(file),1024);
		BufferedImage image      = javax.imageio.ImageIO.read(inputStrean);
		size[0]  = image.getWidth();//宽
		size[1]	 = image.getHeight();//高
		return size;
	} 
	
	
}
