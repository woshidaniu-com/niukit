package com.woshidaniu.cache.core.exception;

public class CacheException extends RuntimeException {
    
    private static final long serialVersionUID = 142468800110101833L;

    public CacheException() {
        super();
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }

}
