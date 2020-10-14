package com.woshidaniu.ftpclient.exception;

public class FTPClientException extends RuntimeException {
    
    private static final long serialVersionUID = 142468800110101833L;

    public FTPClientException() {
        super();
    }

    public FTPClientException(String message) {
        super(message);
    }

    public FTPClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public FTPClientException(Throwable cause) {
        super(cause);
    }

}
