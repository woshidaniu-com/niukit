/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.httputils.multipart;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.methods.multipart.PartSource;

/**
 *@类名称	: InputStreamPartSource.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：2016年4月26日 下午4:32:45
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class InputStreamPartSource implements PartSource {


    /** Stream part input. */
    private InputStream input = null;

    /** File part file name. */
    private String fileName = null;
 
	public InputStreamPartSource( String fileName , InputStream input) {
		super();
		this.fileName = fileName;
		this.input = input;
	}

	@Override
	public long getLength() {
		if (this.input != null) {
            try {
				return this.input.available();
			} catch (IOException e) {
				//	e.printStackTrace();
				return 0;
			}
        } else {
            return 0;
        }
	}

	@Override
	public String getFileName() {
		 return (fileName == null) ? "noname" : fileName;
	}

	@Override
	public InputStream createInputStream() throws IOException {
		return input;
	}

}
