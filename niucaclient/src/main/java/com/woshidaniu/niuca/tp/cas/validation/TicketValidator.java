package com.woshidaniu.niuca.tp.cas.validation;

public interface TicketValidator {
    Assertion validate(String var1, String var2) throws TicketValidationException;
}
