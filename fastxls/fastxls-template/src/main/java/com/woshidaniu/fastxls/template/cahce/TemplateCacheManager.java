/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.template.cahce;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.guavacache.StreamCacheManager;
import com.woshidaniu.io.utils.ReaderUtils;

/**
 *@类名称	: TemplateCacheManager.java
 *@类描述	：模板内存缓存管理
 *@创建人	：kangzhidong
 *@创建时间	：Mar 25, 2016 10:16:54 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class TemplateCacheManager extends StreamCacheManager{

	protected static Logger LOG = LoggerFactory.getLogger(TemplateCacheManager.class);
	
	public static StringBuffer getTemplate(String filePath) {
		InputStream input = null;
		try {
			input = IOUtils.toBufferedInputStream(getInputStream(filePath));
			return ReaderUtils.readInputStream(input);
		} catch (IOException e) {
			LOG.error("Template Load Error : ", e);
		} finally {
			IOUtils.closeQuietly(input);
		}
		return null;
	}
	
}
