package org.apache.struts2.plus.dispatcher.result.types;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.io.utils.FileUtils;
import com.woshidaniu.io.utils.FilemimeUtils;
import com.woshidaniu.io.utils.IOUtils;

/**
 * 
 * @className: FileStreamResult
 * @description: 文件结果流处理，结合COS的稳定与struts2的便利，实现文件的下载，从而替换直接访问文件的方式
 * <b>Example:</b>
 * <pre>
 * <!-- START SNIPPET: example -->
 *   &lt;result name="success" type="file"&gt;
 *   &lt;param name="inputPath"&gt;filePath&lt;/param&gt;
 *   &lt;param name="contentType"&gt;image/jpeg&lt;/param&gt;
 *   &lt;param name="contentCharSet"&gt;UTF-8&lt;/param&gt;
 *   &lt;param name="contentFilePath"&gt;${path}&lt;/param&gt;
 *   &lt;param name="contentDisposition"&gt;attachment;filename="document.jpg"&lt;/param&gt;
 * &lt;/result&gt;
 * <!-- END SNIPPET: example -->
 * </pre>
 * @author :kangzhidong
 * @date : 下午5:48:58 2013-10-23
 */
public class FileStreamResult extends StrutsResultSupport {

	private static final long serialVersionUID = -1468409635999059850L;

	protected static final Logger LOG = LoggerFactory.getLogger(FileStreamResult.class);
	
	protected String contentType = null;
	protected String contentCharSet = "UTF-8";
	protected String contentDisposition = "inline";
	protected String inputName = "inputFile";
	protected boolean allowClear = false;
	protected int bufferSize = 1024;
	protected File inputFile;
	
	public FileStreamResult() {
		super();
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
	 * @return Returns the contentType.
	 */
	public String getContentType() {
		return (contentType);
	}

	/**
	 * @param contentType
	 *            The contentType to set.
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
	 * @param contentDisposition
	 *            the Content-disposition header value to use.
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
	 * @param contentCharSet
	 *            the charset to use on the header when sending the stream
	 */
	public void setContentCharSet(String contentCharSet) {
		this.contentCharSet = contentCharSet;
	}

	public boolean isAllowClear() {
		return allowClear;
	}

	public void setAllowClear(boolean allowClear) {
	
		this.allowClear = allowClear;
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
	 * @see org.apache.struts2.dispatcher.StrutsResultSupport#doExecute(java.lang.String,com.opensymphony.xwork2.ActionInvocation)
	 */
	protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
		
		// Override any parameters using values on the stack
		resolveParamsFromStack(invocation.getStack(), invocation);
		
		InputStream input = null;
		OutputStream output = null;
		try {
			if (inputFile == null) {
				// Find the inputFile from the invocation variable stack
				inputFile = (File) invocation.getStack().findValue(conditionalParse(inputName, invocation));
			}
			if (inputFile == null) {
				String msg = ("Can not find a file with the name ["+ inputName + "] in the invocation stack. " + "Check the <param name=\"inputName\"> tag specified for this action.");
				LOG.error(msg);
				throw new IllegalArgumentException(msg);
			}
			// Find the Response in context
			HttpServletResponse response = ServletActionContext.getResponse();
			//invocation.getStack().set("fileName", StringUtils.getSafeStr(invocation.getStack().findString("fileName"), inputFile.getName()));
			// get the content type by fileName
			if( null == contentType)   {
				contentType = FilemimeUtils.getFileMimeType(inputFile.getName());
				contentType = BlankUtils.isBlank(contentType) ? "application/octet-stream" : contentType; 
	        }
			// Set the content type
			if (contentCharSet != null && !contentCharSet.equals("")) {
				response.setContentType(conditionalParse(contentType,invocation) + ";charset=" + contentCharSet);
			} else {
				response.setContentType(conditionalParse(contentType,invocation));
			}

			// Set the content-disposition
			if (contentDisposition != null) {
				/*
					response.setHeader(name,value) :如果Header中没有定义则添加，如果已定义则用新的value替换原用value值。
					response.addHeader(name,value) :如果Header中没有定义则添加，如果已定义则原有value不改变。
				 */
				response.setHeader("Content-Disposition",conditionalParse(contentDisposition, invocation));
			}

        	// Get the outputstream
			output = response.getOutputStream();
			
			/*// Copy input to output
			LOG.info("Use COS to write file +++ START +++");
			ServletUtils.returnFile(inputFile.getAbsolutePath(), output);
			//output.flush();
			//
			LOG.info("Use COS to write file  +++ END +++");
			*/
			// Copy input to output
			LOG.info("Copy file to output buffer +++ START +++");
			
			// Copy file to memory
			byte[] bytes = FileUtils.toByteArray(inputFile);
			// Copy memory to response
			input = IOUtils.toByteArrayInputStream(bytes);
			IOUtils.copy(input, output, bufferSize);
			bytes = null;
			
			LOG.info("Copy file to output buffer +++ END +++");
			
		}catch (Exception e) {
		}finally {
			IOUtils.closeQuietly(input);
			if(allowClear){
				invocation.getStack().setValue(conditionalParse(inputName, invocation), null);
				LOG.info("Delete file when allowClear == true;  +++ START +++");
				FileUtils.deleteQuietly(inputFile);
	        	LOG.info("Delete file when allowClear == true;  +++ END +++");
			}
		}
	}

	/**
	 * Tries to lookup the parameters on the stack. Will override any existing
	 * parameters
	 * 
	 * @param stack  The current value stack
	 */
	protected void resolveParamsFromStack(ValueStack stack,ActionInvocation invocation) {
		
		String contentType = stack.findString("contentType");
		if (contentType != null) {
			setContentType(contentType);
		}
		
		if (contentCharSet != null) {
			contentCharSet = conditionalParse(contentCharSet, invocation);
		} else {
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
        
		String allowClear = stack.findString("allowClear");
        if (allowClear != null) {
        	setAllowClear(Boolean.getBoolean(allowClear));
        }
		
        Integer bufferSize = (Integer) stack.findValue("bufferSize", Integer.class);
        if (bufferSize != null) {
            setBufferSize(bufferSize.intValue());
        }
        
	}

}
