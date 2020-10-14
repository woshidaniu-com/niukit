package com.woshidaniu.struts2.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.struts2.StrutsException;
import org.apache.struts2.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 *@类名称 : ValueStatckUtils.java
 *@类描述 ：
 *@创建人 ：kangzhidong
 *@创建时间 ：Mar 28, 2016 9:03:22 AM
 *@修改人 ：
 *@修改时间 ：
 *@版本号 :v1.0
 */
public class ValueStatckUtils {

	public static Logger LOG = LoggerFactory.getLogger(ValueStatckUtils.class);
	
	public static String conditionalParse(ValueStack valueStack,String param){
		return ValueStatckUtils.conditionalParse(true,true,valueStack,param);
	}
	
	
		/**
	 * Parses the parameter for OGNL expressions against the valuestack
	 *
	 * @param param The parameter value
	 * @param invocation The action invocation instance
	 * @return The resulting string
	 */
	 public static String conditionalParse(boolean parse,final boolean encode,ValueStack valueStack,String param) {
	    if (parse && param != null && valueStack != null) {
	        return TextParseUtil.translateVariables(param, valueStack,
	                new TextParseUtil.ParsedValueEvaluator() {
	                    public Object evaluate(String parsedValue) {
	                        if (encode) {
	                            if (parsedValue != null) {
	                                try {
	                                    // use UTF-8 as this is the recommended encoding by W3C to
	                                    // avoid incompatibilities.
	                                    return URLEncoder.encode(parsedValue, "UTF-8");
	                                }
	                                catch(UnsupportedEncodingException e) {
	                                    LOG.warn("error while trying to encode ["+parsedValue+"]", e);
	                                }
	                            }
	                        }
	                        return parsedValue;
	                    }
	        });
	    } else {
	        return param;
	    }
	}
	 
	/**
	 * Evaluates the OGNL stack to find a String value.
	 * @param expr  OGNL expression.
	 * @return  the String value found.
	 */
	public static String findString(String expr) {
	    return (String) findValue(expr, String.class);
	}
	
	/**
	 * Evaluates the OGNL stack to find a String value.
	 * <p/>
	 * If the given expression is <tt>null</tt/> a error is logged and a <code>RuntimeException</code> is thrown
	 * constructed with a messaged based on the given field and errorMsg paramter.
	 *
	 * @param expr  OGNL expression.
	 * @param field   field name used when throwing <code>RuntimeException</code>.
	 * @param errorMsg  error message used when throwing <code>RuntimeException</code>.
	 * @return  the String value found.
	 * @throws StrutsException is thrown in case of expression is <tt>null</tt>.
	 */
	public static String findString(String expr, String field, String errorMsg) {
	    if (expr == null) {
	    	throw new StrutsException(errorMsg, null);
	    } else {
	        return findString(expr);
	    }
	}
	
	/**
	 * Finds a value from the OGNL stack based on the given expression.
	 * Will always evaluate <code>expr</code> against stack except when <code>expr</code>
	 * is null. If altsyntax (%{...}) is applied, simply strip it off.
	 *
	 * @param expr  the expression. Returns <tt>null</tt> if expr is null.
	 * @return the value, <tt>null</tt> if not found.
	 */
	public static Object findValue(String expr) {
	    if (expr == null) {
	        return null;
	    }
	
	    expr = stripExpressionIfAltSyntax(expr);
	
	    return getStack().findValue(expr, false);
	}
	
	/**
	 * If altsyntax (%{...}) is applied, simply strip the "%{" and "}" off. 
	 * @param expr the expression (must be not null)
	 * @return the stripped expression if altSyntax is enabled. Otherwise
	 * the parameter expression is returned as is.
	 */
	public static String stripExpressionIfAltSyntax(String expr) {
		return ComponentUtils.stripExpressionIfAltSyntax(getStack(), expr);
	}
	
	/**
	 * Is the altSyntax enabled? [TRUE]
	 * <p/>
	 * See <code>struts.properties</code> where the altSyntax flag is defined.
	 */
	public static boolean altSyntax() {
	    return ComponentUtils.altSyntax(getStack());
	}
	
	/**
	 * Adds the sorrounding %{ } to the expression for proper processing.
	 * @param expr the expression.
	 * @return the modified expression if altSyntax is enabled, or the parameter 
	 * expression otherwise.
	 */
	public static String completeExpressionIfAltSyntax(String expr) {
		if (altSyntax()) {
			return "%{" + expr + "}";
		}
		return expr;
	}
	
	/**
	 * This check is needed for backwards compatibility with 2.1.x
	 * @param expr the expression.
	 * @return the found string if altSyntax is enabled. The parameter
	 * expression otherwise.
	 */
	public static String findStringIfAltSyntax(String expr) {
		if (altSyntax()) {
		    return findString(expr);
		}
		return expr;
	}
	
	/**
	 * Evaluates the OGNL stack to find an Object value.
	 * <p/>
	 * Function just like <code>findValue(String)</code> except that if the
	 * given expression is <tt>null</tt/> a error is logged and
	 * a <code>RuntimeException</code> is thrown constructed with a
	 * messaged based on the given field and errorMsg paramter.
	 *
	 * @param expr  OGNL expression.
	 * @param field   field name used when throwing <code>RuntimeException</code>.
	 * @param errorMsg  error message used when throwing <code>RuntimeException</code>.
	 * @return  the Object found, is never <tt>null</tt>.
	 * @throws StrutsException is thrown in case of not found in the OGNL stack, or expression is <tt>null</tt>.
	 */
	public static Object findValue(String expr, String field, String errorMsg) {
	    if (expr == null) {
	    	throw new StrutsException(errorMsg, null);
	    } else {
	        Object value = null;
	        Exception problem = null;
	        try {
	            value = findValue(expr);
	        } catch (Exception e) {
	            problem = e;
	        }
	
	        if (value == null) {
	        	throw new StrutsException(errorMsg, problem);
	        }
	
	        return value;
	    }
	}
	
	/**
	 * Evaluates the OGNL stack to find an Object of the given type. Will evaluate
	 * <code>expr</code> the portion wrapped with altSyntax (%{...})
	 * against stack when altSyntax is on, else the whole <code>expr</code>
	 * is evaluated against the stack.
	 * <p/>
	 * This method only supports the altSyntax. So this should be set to true.
	 * @param expr  OGNL expression.
	 * @param toType  the type expected to find.
	 * @return  the Object found, or <tt>null</tt> if not found.
	 */
	public static Object findValue(String expr, Class<?> toType) {
	    if (altSyntax() && toType == String.class) {
	        if (ComponentUtils.containsExpression(expr)) {
	            return TextParseUtil.translateVariables('%', expr, getStack());
	        } else {
	            return expr;
	        }
	    } else {
	        expr = stripExpressionIfAltSyntax(expr);
	
	        return getStack().findValue(expr, toType, false );
	    }
	}
	
	/**
	 * Gets the OGNL value stack assoicated with this component.
	 * @return the OGNL value stack assoicated with this component.
	 */
	public static ValueStack getStack() {
	    return ActionContext.getContext().getValueStack();
	}
    
}
