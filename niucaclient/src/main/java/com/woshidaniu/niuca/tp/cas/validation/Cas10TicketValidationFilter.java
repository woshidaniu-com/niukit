package com.woshidaniu.niuca.tp.cas.validation;

import javax.servlet.FilterConfig;

public class Cas10TicketValidationFilter extends AbstractTicketValidationFilter {
    public Cas10TicketValidationFilter() {
    }

    protected final TicketValidator getTicketValidator(FilterConfig filterConfig) {
        String casServerUrlPrefix = this.getPropertyFromInitParams(filterConfig, "casServerUrlPrefix", (String)null);
        Cas10TicketValidator validator = new Cas10TicketValidator(casServerUrlPrefix);
        validator.setRenew(Boolean.valueOf(this.getPropertyFromInitParams(filterConfig, "renew", "false").toString()));
        return validator;
    }
}
