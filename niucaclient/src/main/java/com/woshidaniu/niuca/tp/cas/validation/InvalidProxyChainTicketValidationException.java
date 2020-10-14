package com.woshidaniu.niuca.tp.cas.validation;


public final class InvalidProxyChainTicketValidationException extends TicketValidationException {
    private static final long serialVersionUID = -7736653266370691534L;

    public InvalidProxyChainTicketValidationException(String string) {
        super(string);
    }

    public InvalidProxyChainTicketValidationException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public InvalidProxyChainTicketValidationException(Throwable throwable) {
        super(throwable);
    }
}
