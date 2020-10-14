package com.woshidaniu.sms.client.http;

import java.net.HttpURLConnection;

/**
 * @className	： HttpStatus
 * @description	：  Constants enumerating the HTTP status codes.
 * @date		： 2017年6月13日 下午9:20:54
 * @version 	V1.0
 */
public class HttpStatus {

    // -------------------------------------------------------- Class Variables

    /** Reason phrases lookup table. */
    private static final String[][] REASON_PHRASES = new String[][]{
        new String[0],
        new String[3],
        new String[8],
        new String[8],
        new String[25],
        new String[8]
    };


    // --------------------------------------------------------- Public Methods

    /**
     * Get the reason phrase for a particular status code.
     * 
     * This method always returns the English text as specified in the
     * relevent RFCs and is not internationalized.
     * 
     * @param statusCode the numeric status code
     * @return the reason phrase associated with the given status code
     * or null if the status code is not recognized.
     * 
     * TODO: getStatusText should be called getReasonPhrase to match RFC
     */
    public static String getStatusText(int statusCode) {

        if (statusCode < 0) {
            throw new IllegalArgumentException("status code may not be negative");
        }
        int classIndex = statusCode / 100;
        int codeIndex = statusCode - classIndex * 100;
        if (classIndex < 1 || classIndex > (REASON_PHRASES.length - 1)  || codeIndex < 0 || codeIndex > (REASON_PHRASES[classIndex].length - 1)) {
            return null;
        }
        return "HTTP Status-Code " + statusCode + ": " + REASON_PHRASES[classIndex][codeIndex];
    }


    // -------------------------------------------------------- Private Methods

    /**
     * Store the given reason phrase, by status code.
     * @param statusCode The status code to lookup
     * @param reasonPhrase The reason phrase for this status code
     */
    private static void addStatusCodeMap(int statusCode, String reasonPhrase) {
        int classIndex = statusCode / 100;
        REASON_PHRASES[classIndex][statusCode - classIndex * 100] = reasonPhrase;
    }

    // ----------------------------------------------------- Static Initializer

    /** Set up status code to "reason phrase" map. */
    static {
    	
        // The response codes for HTTP, as of version 1.1.

        /* 2XX: generally "OK" */
    	
        addStatusCodeMap(HttpURLConnection.HTTP_OK, "OK.");
        addStatusCodeMap(HttpURLConnection.HTTP_CREATED, "Created.");
        addStatusCodeMap(HttpURLConnection.HTTP_ACCEPTED, "Accepted.");
        addStatusCodeMap(HttpURLConnection.HTTP_NOT_AUTHORITATIVE, "Non-Authoritative Information.");
        addStatusCodeMap(HttpURLConnection.HTTP_NO_CONTENT, "No Content.");
        addStatusCodeMap(HttpURLConnection.HTTP_RESET, "Reset Content.");
        addStatusCodeMap(HttpURLConnection.HTTP_PARTIAL, "Partial Content.");
        
        /* 3XX: relocation/redirect */

        addStatusCodeMap(HttpURLConnection.HTTP_MULT_CHOICE, "Multiple Choices.");
        addStatusCodeMap(HttpURLConnection.HTTP_MOVED_PERM, "Moved Permanently.");
        addStatusCodeMap(HttpURLConnection.HTTP_MOVED_TEMP, "Temporary Redirect.");
        addStatusCodeMap(HttpURLConnection.HTTP_SEE_OTHER, "See Other.");
        addStatusCodeMap(HttpURLConnection.HTTP_NOT_MODIFIED, "Not Modified.");
        addStatusCodeMap(HttpURLConnection.HTTP_USE_PROXY, "Use Proxy.");
        
        /* 4XX: client error */
        
        addStatusCodeMap(HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request.");
        addStatusCodeMap(HttpURLConnection.HTTP_UNAUTHORIZED, "Unauthorized.");
        addStatusCodeMap(HttpURLConnection.HTTP_PAYMENT_REQUIRED, "Payment Required.");
        addStatusCodeMap(HttpURLConnection.HTTP_FORBIDDEN, "Forbidden.");
        addStatusCodeMap(HttpURLConnection.HTTP_NOT_FOUND, "Not Found.");
        addStatusCodeMap(HttpURLConnection.HTTP_BAD_METHOD, "Method Not Allowed.");
        addStatusCodeMap(HttpURLConnection.HTTP_NOT_ACCEPTABLE, "Not Acceptable.");
        addStatusCodeMap(HttpURLConnection.HTTP_PROXY_AUTH, "Proxy Authentication Required.");
        addStatusCodeMap(HttpURLConnection.HTTP_CLIENT_TIMEOUT, "Request Timeout.");
        addStatusCodeMap(HttpURLConnection.HTTP_CONFLICT, "Conflict.");
        addStatusCodeMap(HttpURLConnection.HTTP_GONE,"Gone.");
        addStatusCodeMap(HttpURLConnection.HTTP_LENGTH_REQUIRED,"Length Required.");
        addStatusCodeMap(HttpURLConnection.HTTP_PRECON_FAILED, "Precondition Failed.");
        addStatusCodeMap(HttpURLConnection.HTTP_ENTITY_TOO_LARGE, "Request Entity Too Large.");
        addStatusCodeMap(HttpURLConnection.HTTP_REQ_TOO_LONG, "Request-URI Too Large.");
        addStatusCodeMap(HttpURLConnection.HTTP_UNSUPPORTED_TYPE, "Unsupported Media Type.");
        
        /* 5XX: server error */
        
        addStatusCodeMap(HttpURLConnection.HTTP_INTERNAL_ERROR, "Internal Server Error.");
        addStatusCodeMap(HttpURLConnection.HTTP_NOT_IMPLEMENTED, "Not Implemented.");
        addStatusCodeMap(HttpURLConnection.HTTP_BAD_GATEWAY, "Bad Gateway.");
        addStatusCodeMap(HttpURLConnection.HTTP_UNAVAILABLE, "Service Unavailable.");
        addStatusCodeMap(HttpURLConnection.HTTP_GATEWAY_TIMEOUT, "Gateway Timeout.");
        addStatusCodeMap(HttpURLConnection.HTTP_VERSION, "HTTP Version Not Supported.");
        
    }


}
