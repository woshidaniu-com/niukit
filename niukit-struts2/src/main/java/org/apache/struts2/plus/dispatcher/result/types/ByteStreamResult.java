package org.apache.struts2.plus.dispatcher.result.types;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.woshidaniu.io.utils.IOUtils;
/**
 * 
 * @className: ByteStreamResult
 * @description: 输出二进制数据
 * @author : kangzhidong
 * @date : 上午10:18:00 2013-10-24
 * <b>Example:</b>
 *
 * <pre><!-- START SNIPPET: example -->
 * &lt;result name="success" type="byte"&gt;
 *   &lt;param name="contentType"&gt;image/jpeg&lt;/param&gt;
 *   &lt;param name="contentCharSet"&gt;UTF-8&lt;/param&gt;
 *   &lt;param name="contentDisposition"&gt;attachment;filename="document.jpg"&lt;/param&gt;
 *   &lt;param name="inputName"&gt;inputBytes&lt;/param&gt;
 *   &lt;param name="allowCaching"&gt;false&lt;/param&gt;
 *   &lt;param name="bufferSize"&gt;1021&lt;/param&gt;
 * &lt;/result&gt;
 * <!-- END SNIPPET: example --></pre>
 */
public class ByteStreamResult extends StrutsResultSupport {

    private static final long serialVersionUID = -1468409635999059850L;

    protected static final Logger LOG = LoggerFactory.getLogger(ByteStreamResult.class);

    protected String contentType = "text/plain";
    protected String contentDisposition = "inline";
    protected String contentCharSet ;
    protected String inputName = "inputBytes";
    protected byte[] inputBytes;
    protected boolean allowCaching = true;
    protected int bufferSize = 1024;
    
    public ByteStreamResult() {
        super();
    }

    public ByteStreamResult(byte[] in) {
        this.inputBytes = in;
    }

     /**
     * @return Returns the whether or not the client should be requested to allow caching of the data stream.
     */
    public boolean getAllowCaching() {
        return allowCaching;
    }

    /**
     * Set allowCaching to <tt>false</tt> to indicate that the client should be requested not to cache the data stream.
     * This is set to <tt>false</tt> by default
     *
     * @param allowCaching Enable caching.
     */
    public void setAllowCaching(boolean allowCaching) {
        this.allowCaching = allowCaching;
    }


    /**
     * @return Returns the contentType.
     */
    public String getContentType() {
        return (contentType);
    }

    /**
     * @param contentType The contentType to set.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return Returns the Content-disposition header value.
     */
    public String getContentDisposition() {
        return contentDisposition;
    }

    /**
     * @param contentDisposition the Content-disposition header value to use.
     */
    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    /**
     * @return Returns the charset specified by the user
     */
    public String getContentCharSet() {
        return contentCharSet;
    }

    /**
     * @param contentCharSet the charset to use on the header when sending the stream
     */
    public void setContentCharSet(String contentCharSet) {
        this.contentCharSet = contentCharSet;
    }

    /**
     * @return Returns the inputName.
     */
    public String getInputName() {
        return (inputName);
    }

    /**
     * @param inputName The inputName to set.
     */
    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    /**
     * @return Returns the bufferSize.
     */
    public int getBufferSize() {
        return (bufferSize);
    }

    /**
     * @param bufferSize The bufferSize to set.
     */
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    
    /**
     * @see org.apache.struts2.dispatcher.StrutsResultSupport#doExecute(java.lang.String, com.opensymphony.xwork2.ActionInvocation)
     */
    protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {

        // Override any parameters using values on the stack
        resolveParamsFromStack(invocation.getStack(), invocation);

        OutputStream oOutput = null;

        try {
            if (inputBytes == null) {
                // Find the byte[] from the invocation variable stack
            	inputBytes = (byte[]) invocation.getStack().findValue(conditionalParse(inputName, invocation));
            }

            if (inputBytes == null) {
                String msg = ("Can not find a byte[] with the name [" + inputName + "] in the invocation stack. " +"Check the <param name=\"inputName\"> tag specified for this action.");
                LOG.error(msg);
                throw new IllegalArgumentException(msg);
            }

            // Find the Response in context
 			HttpServletResponse response = ServletActionContext.getResponse();

            // Set the content type
            if (contentCharSet != null && ! contentCharSet.equals("")) {
                response.setContentType(conditionalParse(contentType, invocation)+";charset="+contentCharSet);
            }
            else {
                response.setContentType(conditionalParse(contentType, invocation));
            }


            // Set the content-disposition
            if (contentDisposition != null) {
                response.addHeader("Content-Disposition", conditionalParse(contentDisposition, invocation));
            }

            // Set the cache control headers if neccessary
            if (!allowCaching) {
                response.addHeader("Pragma", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
            }

            // Get the outputstream
            oOutput = response.getOutputStream();

            LOG.debug("Streaming result [" + inputName + "] type=[" + contentType + "]  content-disposition=[" + contentDisposition + "] charset=[" + contentCharSet + "]");

        	// Copy input to output
			IOUtils.copy(new ByteArrayInputStream(inputBytes), oOutput, bufferSize);
        				
			LOG.debug("Streaming to output buffer +++ END +++");
        }catch (Exception e) {
		}finally {
        }
    }

    /**
     * Tries to lookup the parameters on the stack.  Will override any existing parameters
     *
     * @param stack The current value stack
     */
    protected void resolveParamsFromStack(ValueStack stack, ActionInvocation invocation) {
       
        String contentType = stack.findString("contentType");
        if (contentType != null) {
            setContentType(contentType);
        }

        if (contentCharSet != null ) {
            contentCharSet = conditionalParse(contentCharSet, invocation);
        }
        else {
            contentCharSet = stack.findString("contentCharSet");
        }
        
        String disposition = stack.findString("contentDisposition");
        if (disposition != null) {
            setContentDisposition(disposition);
        }

        String inputName = stack.findString("inputName");
        if (inputName != null) {
            setInputName(inputName);
        }
        
        String allowCaching = stack.findString("allowCaching");
        if (allowCaching != null) {
        	setAllowCaching(Boolean.getBoolean(allowCaching));
        }
        
        Integer bufferSize = (Integer) stack.findValue("bufferSize", Integer.class);
        if (bufferSize != null) {
            setBufferSize(bufferSize.intValue());
        }

    }

}




