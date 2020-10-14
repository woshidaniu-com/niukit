package com.woshidaniu.niuca.tp.cas.validation;

public class TicketValidationException extends Exception {
    private static final long serialVersionUID = -7036248720402711806L;

    public TicketValidationException(String string) {
        super(string);
    }

    public TicketValidationException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public TicketValidationException(Throwable throwable) {
        super(throwable);
    }
}
