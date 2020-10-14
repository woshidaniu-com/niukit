 package com.woshidaniu.web.servlet.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.woshidaniu.web.servlet.http.io.ServletByteArrayOutputStream;
 /**
  * 
  *@类名称	: HttpServletResourceCachedResponseWrapper.java
  *@类描述	：
  *@创建人	：kangzhidong
  *@创建时间	：Mar 8, 2016 9:08:02 AM
  *@修改人	：
  *@修改时间	：
  *@版本号	:v1.0
  */
public class HttpServletResourceCachedResponseWrapper  extends HttpServletResponseWrapper{
   
	private ByteArrayOutputStream bout = new ByteArrayOutputStream();  //捕获输出的缓存
    private PrintWriter pw;
    private HttpServletResponse response;
   
    public HttpServletResourceCachedResponseWrapper(HttpServletResponse response) {
        super(response);
        this.response = response;
    }
    
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletByteArrayOutputStream(bout);
    }
    
    @Override
    public PrintWriter getWriter() throws IOException {
        pw = new PrintWriter(new OutputStreamWriter(bout,this.response.getCharacterEncoding()));
        return pw;
    }
    
    public byte[] getBuffer(){
        try{
            if(pw!=null){
                pw.close();
            }
            return bout.toByteArray();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
