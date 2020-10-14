package com.woshidaniu.niuca.tp.cas.validation;


import com.woshidaniu.niuca.tp.cas.authentication.AttributePrincipal;
import com.woshidaniu.niuca.tp.cas.authentication.AttributePrincipalImpl;
import com.woshidaniu.niuca.tp.cas.util.CommonUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class AssertionImpl implements Assertion {
    private static final long serialVersionUID = -7767943925833639221L;
    private final Date validFromDate;
    private final Date validUntilDate;
    private final Map attributes;
    private final AttributePrincipal principal;

    public AssertionImpl(String name) {
        this((AttributePrincipal)(new AttributePrincipalImpl(name)));
    }

    public AssertionImpl(AttributePrincipal principal) {
        this(principal, new HashMap());
    }

    public AssertionImpl(AttributePrincipal principal, Map attributes) {
        this(principal, new Date(), (Date)null, attributes);
    }

    public AssertionImpl(AttributePrincipal principal, Date validFromDate, Date validUntilDate, Map attributes) {
        this.principal = principal;
        this.validFromDate = validFromDate;
        this.validUntilDate = validUntilDate;
        this.attributes = attributes;
        CommonUtils.assertNotNull(this.principal, "principal cannot be null.");
        CommonUtils.assertNotNull(this.validFromDate, "validFromDate cannot be null.");
        CommonUtils.assertNotNull(this.attributes, "attributes cannot be null.");
    }

    public Date getValidFromDate() {
        return this.validFromDate;
    }

    public Date getValidUntilDate() {
        return this.validUntilDate;
    }

    public Map getAttributes() {
        return this.attributes;
    }

    public AttributePrincipal getPrincipal() {
        return this.principal;
    }
}
