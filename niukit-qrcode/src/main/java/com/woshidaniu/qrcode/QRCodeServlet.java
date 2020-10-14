/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.qrcode;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.woshidaniu.qrcode.utils.QRCodeUtils;

/**
 * 
 *@类名称:QRCodeServlet.java
 *@类描述：二维码生成Servlet
 *@创建人：kangzhidong
 *@创建时间：2014-9-29 上午11:33:00
 *@版本号:v1.0
 */
@SuppressWarnings("serial")
public class QRCodeServlet extends HttpServlet{

	/**
	 * 
	 *@描述：
	 *@创建人:kangzhidong
	 *@创建时间:2014-9-29上午11:28:50
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@return
	 *@throws IOException
	 *@throws WriterException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//二维码内容
		String content = request.getParameter("content");
		//二维码图片宽度
		String width   = request.getParameter("w");
		//二维码图片高度
		String height  = request.getParameter("h");
		
		if(content==null||content.equals("")){
			response.setContentType("text/plain;charset=UTF-8");
			response.getOutputStream().write("二维码内容不可为空!".getBytes("utf-8"));
			response.getOutputStream().close();
		}
		
		int imgWidth  = 110;
		int imgHeight = 110;
		
		if(width!=null&&!width.equals("")){
			try {
				imgWidth = Integer.parseInt(width);
			} catch (Exception e) {}
		}
		if(height!=null&&!height.equals("")){
			try {
				imgHeight = Integer.parseInt(height);
			} catch (Exception e) {}
		}
		try {
			
			BitMatrix byteMatrix = QRCodeUtils.getMatrix(new String(content.getBytes("UTF-8"),"ISO-8859-1"), imgWidth, imgHeight);
			
			response.setContentType("image/png");
			
			MatrixToImageWriter.writeToStream(byteMatrix , "png", response.getOutputStream());
			
		} catch (WriterException e) {
			e.printStackTrace();
		}
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		doGet(request, response);
	}
	
}
