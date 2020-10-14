package com.woshidaniu.httputils.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.io.IOUtils;

import com.woshidaniu.basicutils.Assert;
import com.woshidaniu.httputils.exception.HttpResponseException;

/**
 * 
 *@类名称	: FileResponseHandler.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：2016年4月27日 下午3:18:07
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class FileResponseHandler implements ResponseHandler<File> {

	private File destFile;
	
	public FileResponseHandler( File destFile) {
		Assert.notNull(destFile, "destFile is null ");
		this.destFile = destFile;
	}

	@Override
	public void handleClient(HttpClient httpclient) {
		
	}
	
	@Override
	public File handleResponse(HttpMethodBase httpMethod) throws IOException {
		StatusLine statusLine = httpMethod.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			
			InputStream input = null;
			FileOutputStream output = null;
			// 先存为临时文件，等全部下完再改回原来的文件名
			File storeFile = new File(destFile.getParent() , destFile.getName()  + ".tmp"); 
			try {
				output = new FileOutputStream(storeFile);
				input = httpMethod.getResponseBodyAsStream();
				IOUtils.copy(input, output);
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
			storeFile.renameTo(destFile);
			return destFile;
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

}
