 package com.woshidaniu.format;

/**
 * 
 * @className: Jsp2HtmlFormat
 * @description: TODO(描述这个类的作用)
 * @author : kangzhidong
 * @date : 上午09:01:46 2014-11-17
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class Jsp2HtmlFormat{
	
	private static Jsp2HtmlFormat instance = null;
	
	private Jsp2HtmlFormat(){};
	
	public static Jsp2HtmlFormat getInstance(){
		instance= (instance==null)?instance=new Jsp2HtmlFormat():instance;
		return  instance;
	}
	
	
	 /**
	  * @description: TODO(描述这个方法的作用)
	  * @author : kangzhidong
	  * @date 下午07:04:49 2014-11-12 
	  * @param requestPageUrl	想要生成html的jsp文件路径（如：/frontStage/articleMenuContent.jsp）,这是实际存在的jsp文件
	  * @param targetHtmlPath			为存放生成html的路径（如：/frontStage/articleMenuContent.html）
	  * @throws Exception
	  * @return  void 返回类型
	  * @throws  
	  * @modify by:
	  * @modify date :
	  * @modify description : TODO(描述修改内容)
	  */
//    public void toStaticHtml(String requestPageUrl,String targetHtmlPath) throws Exception{
//        toStaticHtml(requestPageUrl,new FileOutputStream(targetHtmlPath) );
//    }
    
//    public void toStaticHtml(String requestPageUrl, OutputStream targetHtmlStream) throws Exception{
//        /**
//         * 创建ServletContext对象，用于获取RequestDispatcher对象
//         */
//        ServletContext sc = ServletActionContext.getServletContext();
//         
//        /**
//         * 根据传过来的相对文件路径，生成一个reqeustDispatcher的包装类
//         */
//        RequestDispatcher rd = sc.getRequestDispatcher(requestPageUrl);
//     
//        /**
//         * 获得request,response对象
//         */
//        HttpServletRequest request = ServletActionContext.getRequest();
//        HttpServletResponse response = ServletActionContext.getResponse();
//         
//        /**
//         * 创建一个ByteArrayOutputStream的字节数组输出流,用来存放输出的信息
//         */
//        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
//         
//        /**
//         * ServletOutputStream是抽象类，必须实现write的方法
//         */
//        final ServletOutputStream outputStream = new ServletOutputStream(){
//            
//            public void write(int b) throws IOException {
//                /**
//                 * 将指定的字节写入此字节输出流
//                 */
//                baos.write(b);
//            }
//
//			public boolean isReady() {
//				return false;
//			}
//
//        }; 
//        /**
//         * 通过现有的 OutputStream 创建新的 PrintWriter
//         * OutputStreamWriter 是字符流通向字节流的桥梁：可使用指定的 charset 将要写入流中的字符编码成字节
//         */
//        final PrintWriter pw = new PrintWriter(new OutputStreamWriter(baos,"utf-8"),true);
//         
//        /**
//         * 生成HttpServletResponse的适配器，用来包装response
//         */
//        HttpServletResponse resp = new HttpServletResponseWrapper(response){
//            /**
//             * 调用getOutputStream的方法(此方法是ServletResponse中已有的)返回ServletOutputStream的对象
//             * 用来在response中返回一个二进制输出对象
//             * 此方法目的是把源文件写入byteArrayOutputStream
//             */
//            public ServletOutputStream getOutputStream(){
//                return outputStream;
//            }
//             
//            /**
//             * 再调用getWriter的方法(此方法是ServletResponse中已有)返回PrintWriter的对象
//             * 此方法用来发送字符文本到客户端
//             */
//            public PrintWriter getWriter(){
//                return pw;
//            }
//        }; 
//        /**
//         * 在不跳转下访问目标jsp。 就是利用RequestDispatcher.include(ServletRequest request,
//         * ServletResponse response)。 该方法把RequestDispatcher指向的目标页面写到response中。
//         */
//        rd.include(request, resp);
//        pw.flush();
//        /**
//         * 使用ByteArrayOutputStream的writeTo方法来向文本输出流写入数据，这也是为什么要使用ByteArray的一个原因
//         */
//        baos.writeTo(targetHtmlStream);
//        
//        targetHtmlStream.close();
//    }

	
}

 
