package org.apache.struts2.plus.dispatcher.multipart;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsConstants;
import org.apache.struts2.dispatcher.multipart.MultiPartRequest;

import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/**
 * 
 *@类名称	: CosMultiPartRequest.java
 *@类描述	：基于COS
 *@创建人	：kangzhidong
 *@创建时间	：Mar 5, 2016 9:29:01 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("unchecked")
public class CosMultiPartRequest implements MultiPartRequest {

	private static final Logger LOG = LoggerFactory.getLogger(CosMultiPartRequest.class);
	private com.oreilly.servlet.MultipartRequest multi;
	private String defaultEncoding;
	private boolean maxSizeProvided;
	private int maxSize;

	@Inject(StrutsConstants.STRUTS_I18N_ENCODING)
	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	@Inject(StrutsConstants.STRUTS_MULTIPART_MAXSIZE)
	public void setMaxSize(String maxSize) {
		this.maxSizeProvided = true;
		this.maxSize = Integer.parseInt(maxSize);
	}

	@Override
	public String[] getContentType(String fieldName) {
		return new String[] { multi.getContentType(fieldName) };
	}

	@Override
	public List getErrors() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public File[] getFile(String fieldName) {
		return new File[] { multi.getFile(fieldName) };
	}

	@Override
	public String[] getFileNames(String fieldName) {
		return new String[] { multi.getFile(fieldName).getName() };
	}

	@Override
	public Enumeration<String> getFileParameterNames() {
		return multi.getFileNames();
	}

	@Override
	public String[] getFilesystemName(String name) {
		return new String[] { multi.getFilesystemName(name) };
	}

	@Override
	public String getParameter(String name) {
		return multi.getParameter(name);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return multi.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		return multi.getParameterValues(name);
	}

	@Override
	public void parse(HttpServletRequest request, String saveDir) throws IOException {
		if (maxSizeProvided) {
			multi = new com.oreilly.servlet.MultipartRequest(request, saveDir, maxSize, defaultEncoding);
		} else {
			multi = new com.oreilly.servlet.MultipartRequest(request, saveDir, defaultEncoding);
		}
	}

	@Override
	public void cleanUp() {
		Enumeration fileParameterNames = multi.getFileNames();
		while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
			String inputValue = (String) fileParameterNames.nextElement();
			File[] files = getFile(inputValue);
			for (File currentFile : files) {
				if (LOG.isInfoEnabled()) {
					String msg = LocalizedTextUtil.findText(this.getClass(), "struts.messages.removing.file", Locale.ENGLISH, "no.message.found", new Object[] { inputValue, currentFile });
					LOG.info(msg);
				}
				if ((currentFile != null) && currentFile.isFile()) {
					if (!currentFile.delete()) {
						if (LOG.isWarnEnabled()) {
							LOG.warn("Resource Leaking:  Could not remove uploaded file [#0]", currentFile.getAbsolutePath());
						}
					}
				}
			}
		}
	}

}
