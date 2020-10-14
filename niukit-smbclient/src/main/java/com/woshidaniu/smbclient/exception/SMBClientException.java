package com.woshidaniu.smbclient.exception;

public class SMBClientException extends RuntimeException {
    
    private static final long serialVersionUID = 142468800110101833L;

    public SMBClientException() {
        super();
    }

    public SMBClientException(String message) {
        super(message);
    }

    public SMBClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public SMBClientException(Throwable cause) {
        super(cause);
    }

}
