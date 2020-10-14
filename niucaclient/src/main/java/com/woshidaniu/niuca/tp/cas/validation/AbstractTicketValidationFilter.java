package com.woshidaniu.niuca.tp.cas.validation;


import com.woshidaniu.niuca.tp.cas.util.AbstractCasFilter;
import com.woshidaniu.niuca.tp.cas.util.CommonUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractTicketValidationFilter extends AbstractCasFilter {
    private TicketValidator ticketValidator;
    private boolean redirectAfterValidation = false;
    private boolean exceptionOnValidationFailure = true;
    private boolean useSession = true;

    public AbstractTicketValidationFilter() {
    }

    protected TicketValidator getTicketValidator(FilterConfig filterConfig) {
        return this.ticketValidator;
    }

    protected void initInternal(FilterConfig filterConfig) throws ServletException {
        super.initInternal(filterConfig);
        this.setExceptionOnValidationFailure(Boolean.valueOf(this.getPropertyFromInitParams(filterConfig, "exceptionOnValidationFailure", "true").toString()));
        this.log.trace("Setting exceptionOnValidationFailure parameter: " + this.exceptionOnValidationFailure);
        this.setRedirectAfterValidation(Boolean.valueOf(this.getPropertyFromInitParams(filterConfig, "redirectAfterValidation", "false").toString()));
        this.log.trace("Setting redirectAfterValidation parameter: " + this.redirectAfterValidation);
        this.setUseSession(Boolean.valueOf(this.getPropertyFromInitParams(filterConfig, "useSession", "true").toString()));
        this.log.trace("Setting useSession parameter: " + this.useSession);
        this.setTicketValidator(this.getTicketValidator(filterConfig));
    }

    public void init() {
        super.init();
        CommonUtils.assertNotNull(this.ticketValidator, "ticketValidator cannot be null.");
    }

    protected boolean preFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        return true;
    }

    protected void onSuccessfulValidation(HttpServletRequest request, HttpServletResponse response, Assertion assertion) {
    }

    protected void onFailedValidation(HttpServletRequest request, HttpServletResponse response) {
    }

    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (this.preFilter(servletRequest, servletResponse, filterChain)) {
            HttpServletRequest request = (HttpServletRequest)servletRequest;
            HttpServletResponse response = (HttpServletResponse)servletResponse;
            String ticket = request.getParameter(this.getArtifactParameterName());
            if (CommonUtils.isNotBlank(ticket)) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Attempting to validate ticket: " + ticket);
                }

                try {
                    Assertion assertion = this.ticketValidator.validate(ticket, this.constructServiceUrl(request, response));
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Successfully authenticated user: " + assertion.getPrincipal().getName());
                    }

                    request.setAttribute("_const_cas_assertion_", assertion);
                    if (this.useSession) {
                        request.getSession().setAttribute("_const_cas_assertion_", assertion);
                    }

                    this.onSuccessfulValidation(request, response, assertion);
                } catch (TicketValidationException var8) {
                    response.setStatus(403);
                    this.log.warn(var8, var8);
                    this.onFailedValidation(request, response);
                    if (this.exceptionOnValidationFailure) {
                        throw new ServletException(var8);
                    }
                }

                if (this.redirectAfterValidation) {
                    this.log.debug("Redirecting after successful ticket validation.");
                    response.sendRedirect(response.encodeRedirectURL(this.constructServiceUrl(request, response)));
                    return;
                }
            }

            filterChain.doFilter(request, response);
        }
    }

    public final void setTicketValidator(TicketValidator ticketValidator) {
        this.ticketValidator = ticketValidator;
    }

    public final void setRedirectAfterValidation(boolean redirectAfterValidation) {
        this.redirectAfterValidation = redirectAfterValidation;
    }

    public final void setExceptionOnValidationFailure(boolean exceptionOnValidationFailure) {
        this.exceptionOnValidationFailure = exceptionOnValidationFailure;
    }

    public final void setUseSession(boolean useSession) {
        this.useSession = useSession;
    }
}
