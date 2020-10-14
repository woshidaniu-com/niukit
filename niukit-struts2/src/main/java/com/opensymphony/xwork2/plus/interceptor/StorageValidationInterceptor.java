package com.opensymphony.xwork2.plus.interceptor;

import java.lang.reflect.Method;

import org.apache.struts2.plus.StrutsConstants;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.validator.ActionValidatorManager;
import com.opensymphony.xwork2.validator.ValidationInterceptor;

/**
 * Extends the xwork validation interceptor to also check for method, and if found, don't validate this action method
 */
@SuppressWarnings("serial")
public class StorageValidationInterceptor extends ValidationInterceptor {

    private boolean devMode;

    @Inject(StrutsConstants.STRUTS_DEVMODE)
    public void setDevMode(String devMode) {
        this.devMode = "true".equalsIgnoreCase(devMode);
    }

    @Inject(StrutsConstants.STRUTS_ACTION_VALIDATOR_MANAGER)
    public void setActionValidatorManager(ActionValidatorManager mgr) {
        super.setActionValidatorManager(mgr);
    }
    
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        if (action != null) {
            Method method = getActionMethod(action.getClass(), invocation.getProxy().getMethod());
            if(method == null){
            	 return invocation.invoke();
            }
        }
        return super.doIntercept(invocation);
    }

    // FIXME: This is copied from DefaultActionInvocation but should be exposed through the interface
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected Method getActionMethod(Class actionClass, String methodName) throws NoSuchMethodException {
        Method method = null;
        try {
            method = actionClass.getMethod(methodName, new Class[0]);
        } catch (NoSuchMethodException e) {
            // hmm -- OK, try doXxx instead
            try {
                String altMethodName = "do" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                method = actionClass.getMethod(altMethodName, new Class[0]);
            } catch (NoSuchMethodException e1) {
                // throw the original one
                if (devMode) {
                    throw e;
                }
            }
        }
        return method;
    }
}
