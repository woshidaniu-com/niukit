package com.woshidaniu.xmlhub.xstream.io.json;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;

import com.thoughtworks.xstream.io.AbstractDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;

/**
 * *******************************************************************
 * @className	： FastJsonStreamDriver
 * @description	： TODO(描述这个类的作用)
 * @author 		： kangzhidong
 * @date		： Mar 12, 2016 2:07:26 PM
 * @version 	V1.0 
 * *******************************************************************
 */

public class FastJsonStreamDriver extends AbstractDriver {

    /**
     * Construct a FastJsonStreamDriver.
     */
    public FastJsonStreamDriver() {
        super();
    }
    
    /**
     * Construct a FastJsonStreamDriver with name coding.
     * @param nameCoder the coder to encode and decode the JSON labels.
     * @since 1.4.2
     */
    public FastJsonStreamDriver(NameCoder nameCoder) {
        super(nameCoder);
    }
    
	@Override
	public HierarchicalStreamReader createReader(Reader in) {
		throw new UnsupportedOperationException("The JsonHierarchicalStreamDriver can only write JSON");
	}

	@Override
	public HierarchicalStreamReader createReader(InputStream in) {
		throw new UnsupportedOperationException("The JsonHierarchicalStreamDriver can only write JSON");
	}
	
	@Override
    public HierarchicalStreamReader createReader(URL in) {
        throw new UnsupportedOperationException("The JsonHierarchicalStreamDriver can only write JSON");
    }
	
	@Override
    public HierarchicalStreamReader createReader(File in) {
        throw new UnsupportedOperationException("The JsonHierarchicalStreamDriver can only write JSON");
    }
	
	/**
     * Create a HierarchicalStreamWriter that writes JSON.
     */
	@Override
    public HierarchicalStreamWriter createWriter(Writer out) {
        return new FastJsonWriter(out);
    }
	
    @Override
    public HierarchicalStreamWriter createWriter(OutputStream out) {
        try {
            // JSON spec requires UTF-8
            return createWriter(new OutputStreamWriter(out, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new StreamException(e);
        }
    }

}
