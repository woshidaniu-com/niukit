/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.struts2.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.struts2.StrutsException;
import org.apache.struts2.util.ComponentUtils;
import org.apache.struts2.util.TextProviderHelper;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.ValueStack;

/**
 *@类名称:I81nUtils.java
 *@类描述：
 *@创建人：kangzhidong
 *@创建时间：Feb 15, 2016 4:35:07 PM
 *@修改人：
 *@修改时间：
 *@版本号:v1.0
 */

public class I18nUtils {
	
	public boolean hasKey(String key) {
		boolean hasKey = TextProviderHelper.getText(key, null, getValueStack()) != null;
     	if(!hasKey){
     		//hasKey = ModuleMessageFactory.getText(key) != null; 
     	}
     	return hasKey;
    }

    public static String getText(String key) {
    	String message = TextProviderHelper.getText(key, null, getValueStack());
     	if(message == null || key.equalsIgnoreCase(message)){
     		//message =  ModuleMessageFactory.getText(key, null , new String[]{});
     	}
     	return message;
    }
     
    public static String getText(String key, String defaultValue) {
    	String message = TextProviderHelper.getText(key, defaultValue, getValueStack());
     	if(message == null || key.equalsIgnoreCase(message)){
     		//message =  ModuleMessageFactory.getText(key, defaultValue , new String[]{});
     	}
     	return message;
    } 

    public static String getText(String key, List<Object> args) {
    	return getText(key,null,args);
    }

    public static String getText(String key, String defaultValue, List<Object> args) {
    	return getText(key,defaultValue,args,getValueStack());
    }

    public static String getText(String key, String defaultValue, List<Object> args, ValueStack stack) {
        String message = TextProviderHelper.getText(key, defaultValue, args, stack);
    	if(message == null || key.equalsIgnoreCase(message)){
    		//message =  ModuleMessageFactory.getText(key, defaultValue, args.toArray(new String[args.size()]));
    	}
    	return message;
    }
    
    public static String getText(String key, String[] args) {
    	return getText(key, null,args);
    }

    public static String getText(String key, String defaultValue, String[] args) {
    	return getText(key,defaultValue,args,getValueStack());
    }
    
    public static String getText(String key, String defaultValue, String[] args, ValueStack stack) {
    	List<Object> args2 = Collections.emptyList();
    	args2.addAll(Arrays.asList(args));
    	String message = TextProviderHelper.getText(key, defaultValue, args2 , stack);
     	if(message == null || key.equalsIgnoreCase(message)){
     		//message =  ModuleMessageFactory.getText(key, defaultValue, args);
     	}
     	return message;
    }

	/**
	 * 返回值栈对象
	 */
	public static ValueStack getValueStack() {
		ActionContext actionContext = ActionContext.getContext();
		return actionContext.getValueStack();
	}

	
   /**
     * Evaluates the OGNL stack to find a String value.
     * @param expr  OGNL expression.
     * @return  the String value found.
     */
    public static String findString(ValueStack stack,String expr) {
        return (String) findValue(stack, expr, String.class);
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
    public static String findString(ValueStack stack,String expr, String field, String errorMsg) {
        if (expr == null) {
            throw fieldError(field, errorMsg, null);
        } else {
            return findString(stack, expr);
        }
    }

    /**
     * Constructs a <code>RuntimeException</code> based on the given information.
     * <p/>
     * A message is constructed and logged at ERROR level before being returned
     * as a <code>RuntimeException</code>.
     * @param field   field name used when throwing <code>RuntimeException</code>.
     * @param errorMsg  error message used when throwing <code>RuntimeException</code>.
     * @param e  the caused exception, can be <tt>null</tt>.
     * @return  the constructed <code>StrutsException</code>.
     */
    public static StrutsException fieldError(String field, String errorMsg, Exception e) {
        throw new StrutsException(" field '" + field + "': " + errorMsg, e);
    }

    /**
     * Finds a value from the OGNL stack based on the given expression.
     * Will always evaluate <code>expr</code> against stack except when <code>expr</code>
     * is null. If altsyntax (%{...}) is applied, simply strip it off.
     *
     * @param expr  the expression. Returns <tt>null</tt> if expr is null.
     * @return the value, <tt>null</tt> if not found.
     */
    public static Object findValue(ValueStack stack,String expr) {
        if (expr == null) {
            return null;
        }
        expr = stripExpressionIfAltSyntax(stack, expr);
        return stack.findValue(expr, false);
    }

    /**
     * If altsyntax (%{...}) is applied, simply strip the "%{" and "}" off. 
     * @param expr the expression (must be not null)
     * @return the stripped expression if altSyntax is enabled. Otherwise
     * the parameter expression is returned as is.
     */
	public static String stripExpressionIfAltSyntax(ValueStack stack,String expr) {
		return ComponentUtils.stripExpressionIfAltSyntax(stack, expr);
	}

    /**
     * Is the altSyntax enabled? [TRUE]
     * <p/>
     * See <code>struts.properties</code> where the altSyntax flag is defined.
     */
    public static boolean altSyntax(ValueStack stack) {
        return ComponentUtils.altSyntax(stack);
    }

    /**
     * Adds the sorrounding %{ } to the expression for proper processing.
     * @param expr the expression.
     * @return the modified expression if altSyntax is enabled, or the parameter 
     * expression otherwise.
     */
	public static String completeExpressionIfAltSyntax(ValueStack stack,String expr) {
		if (altSyntax(stack)) {
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
	public static String findStringIfAltSyntax(ValueStack stack,String expr) {
		if (altSyntax(stack)) {
		    return findString(stack,expr);
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
    public static Object findValue(ValueStack stack,String expr, String field, String errorMsg) {
        if (expr == null) {
            throw fieldError(field, errorMsg, null);
        } else {
            Object value = null;
            Exception problem = null;
            try {
                value = findValue(stack,expr);
            } catch (Exception e) {
                problem = e;
            }

            if (value == null) {
                throw fieldError(field, errorMsg, problem);
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
    public static Object findValue(ValueStack stack,String expr, Class toType) {
        if (altSyntax(stack) && toType == String.class) {
        	return TextParseUtil.translateVariables('%', expr, stack);
        } else {
            expr = stripExpressionIfAltSyntax(stack,expr);
            return stack.findValue(expr, toType, false);
        }
    }
}
