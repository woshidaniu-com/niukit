/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.freemarker.loader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.io.IOUtils;

import freemarker.cache.TemplateLoader;

/**
 *@类名称	: HtmlTemplateLoader.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Jun 23, 2016 11:46:48 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class HtmlTemplateLoader implements TemplateLoader {
    
    private static final String HTML_ESCAPE_PREFIX= "<#escape x as x?html>";
    private static final String HTML_ESCAPE_SUFFIX = "</#escape>";
    
    private final TemplateLoader delegate;

    public HtmlTemplateLoader(TemplateLoader delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object findTemplateSource(String name) throws IOException {
        return delegate.findTemplateSource(name);
    }

    @Override
    public long getLastModified(Object templateSource) {
        return delegate.getLastModified(templateSource);
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        Reader reader = delegate.getReader(templateSource, encoding);
        String templateText = IOUtils.toString(reader);
        return new StringReader(HTML_ESCAPE_PREFIX+templateText + HTML_ESCAPE_SUFFIX);
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {
        delegate.closeTemplateSource(templateSource);
    }

}