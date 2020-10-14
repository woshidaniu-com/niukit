package com.woshidaniu.niuca.tp.cas.validation;

import com.woshidaniu.niuca.tp.cas.authentication.AttributePrincipal;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface Assertion extends Serializable {
    Date getValidFromDate();

    Date getValidUntilDate();

    Map getAttributes();

    AttributePrincipal getPrincipal();
}
