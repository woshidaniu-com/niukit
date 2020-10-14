package com.woshidaniu.niuca.tp.cas.util;



import com.woshidaniu.niuca.tp.cas.validation.Assertion;

public class AssertionHolder {
    private static final ThreadLocal threadLocal = new ThreadLocal();

    public AssertionHolder() {
    }

    public static Assertion getAssertion() {
        return (Assertion)threadLocal.get();
    }

    public static void setAssertion(Assertion assertion) {
        threadLocal.set(assertion);
    }

    public static void clear() {
        threadLocal.set((Object)null);
    }
}
