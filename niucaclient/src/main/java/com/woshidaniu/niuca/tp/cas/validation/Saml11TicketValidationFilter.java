package com.woshidaniu.niuca.tp.cas.validation;


import javax.servlet.FilterConfig;

public class Saml11TicketValidationFilter extends AbstractTicketValidationFilter {
    public Saml11TicketValidationFilter() {
        this.setArtifactParameterName("SAMLart");
        this.setServiceParameterName("TARGET");
    }

    protected final TicketValidator getTicketValidator(FilterConfig filterConfig) {
        Saml11TicketValidator validator = new Saml11TicketValidator(this.getPropertyFromInitParams(filterConfig, "casServerUrlPrefix", (String)null));
        String tolerance = this.getPropertyFromInitParams(filterConfig, "tolerance", "1000");
        validator.setTolerance(Long.parseLong(tolerance));
        return validator;
    }
}
