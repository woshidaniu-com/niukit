//package com.woshidaniu.fastpdf.struts2.result;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.struts2.ServletActionContext;
//import org.apache.struts2.dispatcher.StrutsResultSupport;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.opensymphony.xwork2.ActionInvocation;
//import com.opensymphony.xwork2.util.ValueStack;
//import com.oreilly.servlet.ServletUtils;
//
///**
// *
// * <b>Example:</b>
// *
// * <pre>
// * <!-- START SNIPPET: example -->
// * &lt;result name="success" type="itextpdf"&gt;
// *   &lt;param name="inputName"&gt;imageStream&lt;/param&gt;
// *   &lt;param name="attrName"&gt;inputAttr&lt;/param&gt;
// *   &lt;param name="dataName"&gt;inputData&lt;/param&gt;
// *   &lt;param name="contentDisposition"&gt;attachment;filename="${fileName}"&lt;/param&gt;
// *   &lt;param name="bufferSize"&gt;1024&lt;/param&gt;
// * &lt;/result&gt;
// * <!-- END SNIPPET: example -->
// * </pre>
// *
// */
//@SuppressWarnings("serial")
//public class XMLTemplateDocumentResult extends StrutsResultSupport {
//
//	private final static Logger LOG = LoggerFactory.getLogger(XMLTemplateDocumentResult.class);
//
//	protected String contentType = "application/pdf";
//	protected String contentCharSet = "UTF-8";
//	protected String contentDisposition = "inline";
//	protected String inputName = "inputStream";
//	protected InputStream inputStream = null; // the PDF Document to render
//	protected String document_id = "documentID";
//	protected String attrName = "inputAttr";
//	protected Map<String, String> attrs;
//	protected String dataName = "inputData";
//	protected Object data;
//	protected int bufferSize = 1024;
//	protected boolean allowCaching = true;
//
//	protected String template = "jsp";
//
//	protected String velocityDir = "velocity";
//	protected String freemarkerDir = "freemarker";
//
//	// CONSTRUCTORS ----------------------------
//
//	public XMLTemplateDocumentResult() {
//		super();
//	}
//
//	public XMLTemplateDocumentResult(ByteArrayInputStream inputStream) {
//		super();
//		this.inputStream = inputStream;
//	}
//
//	// ACCESSORS ----------------------------
//
//	/**
//	 * @return Returns the whether or not the client should be requested to
//	 *         allow caching of the data stream.
//	 */
//	public boolean getAllowCaching() {
//		return allowCaching;
//	}
//
//	/**
//	 * Set allowCaching to <tt>false</tt> to indicate that the client should be
//	 * requested not to cache the data stream. This is set to <tt>false</tt> by
//	 * default
//	 *
//	 * @param allowCaching
//	 *            Enable caching.
//	 */
//	public void setAllowCaching(boolean allowCaching) {
//		this.allowCaching = allowCaching;
//	}
//
//	/**
//	 * @return Returns the bufferSize.
//	 */
//	public int getBufferSize() {
//		return (bufferSize);
//	}
//
//	/**
//	 * @param bufferSize
//	 *            The bufferSize to set.
//	 */
//	public void setBufferSize(int bufferSize) {
//		this.bufferSize = bufferSize;
//	}
//
//	/**
//	 * @return Returns the Content-disposition header value.
//	 */
//	public String getContentDisposition() {
//		return contentDisposition;
//	}
//
//	/**
//	 * @param contentDisposition
//	 *            the Content-disposition header value to use.
//	 */
//	public void setContentDisposition(String contentDisposition) {
//		this.contentDisposition = contentDisposition;
//	}
//
//	/**
//	 * @return Returns the charset specified by the user
//	 */
//	public String getContentCharSet() {
//		return contentCharSet;
//	}
//
//	/**
//	 * @param contentCharSet
//	 *            the charset to use on the header when sending the stream
//	 */
//	public void setContentCharSet(String contentCharSet) {
//		this.contentCharSet = contentCharSet;
//	}
//
//	public String getDocument_id() {
//		return document_id;
//	}
//
//	public void setDocument_id(String document_id) {
//		this.document_id = document_id;
//	}
//
//	public String getAttrName() {
//		return attrName;
//	}
//
//	public void setAttrName(String attrName) {
//		this.attrName = attrName;
//	}
//
//	public String getDataName() {
//		return dataName;
//	}
//
//	public void setDataName(String dataName) {
//		this.dataName = dataName;
//	}
//
//	/**
//     * @return Returns the inputName.
//     */
//    public String getInputName() {
//        return (inputName);
//    }
//
//    /**
//     * @param inputName The inputName to set.
//     */
//    public void setInputName(String inputName) {
//        this.inputName = inputName;
//    }
//	// OTHER METHODS -----------------------
//
//	// Required by com.opensymphony.xwork2.Result
//
//	/**
//	 * Executes the result. Writes the given chart as a PNG or JPG to the
//	 * servlet output stream.
//	 *
//	 * @param invocation
//	 *            an encapsulation of the action execution state.
//	 * @throws Exception
//	 *             if an error occurs when creating or writing the chart to the
//	 *             servlet output stream.
//	 */
//	@SuppressWarnings("unchecked")
//	public void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
//
//        // Override any parameters using values on the stack
//        resolveParamsFromStack(invocation.getStack(), invocation);
//
//        ByteArrayOutputStream output = null;
//    	try {
//
//    		// Find the Response in context
//            HttpServletResponse response = ServletActionContext.getResponse();
//            // Set the content type
//            response.setContentType(contentType+";charset="+contentCharSet);
//            // Set the content-disposition
//            if (contentDisposition != null) {
//            	response.addHeader("Content-Disposition", conditionalParse(contentDisposition, invocation));
//            }
//            // Set the cache control headers if neccessary
//            if (!allowCaching) {
//            	response.addHeader("Pragma", "no-cache");
//            	response.addHeader("Cache-Control", "no-cache");
//            }
//
//			String documentID =  conditionalParse(document_id, invocation);
//    		ItextXMLElement element = ItextContext.getElement(documentID);
//    		//生成标准html后，保存到指定地址，通过第三方开源工具取得该地址的html文档转换成pdf文档
//    		if("xml".equalsIgnoreCase(element.attr("model"))){
//    			if (this.inputStream == null) {
//        			// Find the inputstream from the invocation variable stack
//    				this.inputStream = (InputStream) invocation.getStack().findValue(conditionalParse(inputName, invocation));
//                }
//                if (this.inputStream == null) {
//                    String msg = ("Can not find a java.io.InputStream with the name [" + inputName + "] in the invocation stack. will use the document and data !");
//                    LOG.error(msg);
//
//                   if (attrs == null) {
//                   	 	// Find the attrs from the invocation variable stack
//                    	attrs = ((Map<String, String>) invocation.getStack().findValue(conditionalParse(attrName, invocation)));
//                    	if (attrs == null) {
//                    		attrs = new HashMap<String, String>();
//                    	}
//                   }
//                   if (data == null) {
//                	   	// Find the datas from the invocation variable stack
//                	   data = (Map<String, Object>) invocation.getStack().findValue(conditionalParse(dataName, invocation));
//                   }
//                   if (data == null) {
//                       msg = ("Can not find a java.util.List<Map<String, Map<String, Object>>> with the name [" + dataName + "] in the invocation stack. " + "Check the <param name=\"dataName\"> tag specified for this action.");
//                       LOG.error(msg);
//                       throw new IllegalArgumentException(msg);
//                   }
//                   //自定义转换样式，手动的增加表格信息
//                   this.inputStream = PDFDocumentRender.getInstance().render(documentID,attrs, data);
//                }
//                //判断是否需要存储
//                ItextContext ctx = ItextContext.getInstance();
//                boolean storeEnable = ctx.getStoreEnable();
//                if(storeEnable){
//                	//如果需要存储，先存储再将文件写到客户端
//            		String result = ctx.getStorePrefix()+documentID+"-"+ctx.getStoreSuffix()+".pdf";
//                	File out = new File(ItextContext.getRealPath(ctx.getStoreDir())+File.separator+result);
//            		out.deleteOnExit();
//            		//把pdf写到FileOutputStream
//                	this.writeStream(new FileOutputStream(out));
//                	//利用cos的工具类写文件到响应流
//                	ServletUtils.returnFile(out.getAbsolutePath(), response.getOutputStream());
//                	LOG.info("Path:"+out.getAbsolutePath());
//                }else{
//                	//把pdf写到ServletOutputStream
//                    this.writeStream(response.getOutputStream());
//                }
//                if (LOG.isDebugEnabled()) {
//                    LOG.debug("Streaming result [" + documentID + "] type=[" + contentType + "] content-disposition=[" + contentDisposition + "] charset=[" + contentCharSet + "]");
//                }
//    		}
//        }finally {
//            if (inputStream != null) inputStream.close();
//            if (output != null) output.close();
//        }
//    }
//
//	protected void writeStream(OutputStream output) throws IOException {
//		// Copy input to output
//        if (LOG.isDebugEnabled()) {
//        	LOG.debug("Streaming to output buffer +++ START +++");
//        }
//        byte[] oBuff = new byte[bufferSize];
//        int iSize;
//        while (-1 != (iSize = inputStream.read(oBuff))) {
//        	output.write(oBuff, 0, iSize);
//        }
//        if (LOG.isDebugEnabled()) {
//        	LOG.debug("Streaming to output buffer +++ END +++");
//        }
//        // Flush
//        output.flush();
//	}
//
//	/**
//	 * Tries to lookup the parameters on the stack. Will override any existing
//	 * parameters
//	 *
//	 * @param stack The current value stack
//	 */
//	protected void resolveParamsFromStack(ValueStack stack, ActionInvocation invocation) {
//		//手动设置输入流时需要的参数
//		String inputName = stack.findString("inputName");
//        if (inputName != null) {
//            setInputName(inputName);
//        }
//		//根据给定的文档名和数据时的参数
//        String document_id = stack.findString("documentID");
//		if (document_id != null) {
//			setDocument_id(document_id);
//		}
//		String attrName = stack.findString("attrName");
//		if (attrName != null) {
//			setAttrName(attrName);
//		}
//		String dataName = stack.findString("dataName");
//		if (dataName != null) {
//			setDataName(dataName);
//		}
//		//输出数据流需要的参数
//		String disposition = stack.findString("contentDisposition");
//		if (disposition != null) {
//			setContentDisposition(disposition);
//		}
//		Integer bufferSize = (Integer) stack.findValue("bufferSize", Integer.class);
//		if (bufferSize != null) {
//			setBufferSize(bufferSize.intValue());
//		}
//		if (contentCharSet != null) {
//			contentCharSet = conditionalParse(contentCharSet, invocation);
//		} else {
//			contentCharSet = stack.findString("contentCharSet");
//		}
//	}
//
//}
