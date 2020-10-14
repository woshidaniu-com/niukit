package com.woshidaniu.httpclient.utils;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.conn.DefaultHttpResponseParser;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.util.CharArrayBuffer;

public class HttpMessageFactoryUtils {

	// Use custom message parser / writer to customize the way HTTP
    // messages are parsed from and written out to the data stream.
	private static final HttpMessageParserFactory<HttpResponse> RESPONSE_PARSER_FACTORY = new DefaultHttpResponseParserFactory() {

        public HttpMessageParser<HttpResponse> create(
            SessionInputBuffer buffer, MessageConstraints constraints) {
            LineParser lineParser = new BasicLineParser() {

                @Override
                public Header parseHeader(final CharArrayBuffer buffer) {
                    try {
                        return super.parseHeader(buffer);
                    } catch (ParseException ex) {
                        return new BasicHeader(buffer.toString(), null);
                    }
                }

            };
            return new DefaultHttpResponseParser(
                buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {

                @Override
                protected boolean reject(final CharArrayBuffer line, int count) {
                    // try to ignore all garbage preceding a status line infinitely
                    return false;
                }

            };
        }

    };

    private static final HttpMessageWriterFactory<HttpRequest> REQUEST_WRITER_FACTORY = new DefaultHttpRequestWriterFactory(){
    	
    	
    };

	public static HttpMessageParserFactory<HttpResponse> getResponseParserFactory() {
		return RESPONSE_PARSER_FACTORY;
	}
	
	public static HttpMessageWriterFactory<HttpRequest> getRequestWriterFactory() {
		return REQUEST_WRITER_FACTORY;
	}
    
}
