package com.woshidaniu.web;

import java.util.Locale;

import org.apache.commons.lang.CharEncoding;

public enum Parameter {
	
	/**
	 * 默认字符集
	 */
	APPLICATION_NAME("application.name", ""),
	 
	/**
	 * Application Environment Locale
	 */
	APPLICATION_LOCALE("application.locale", Locale.getDefault().toString()),

	/**
	 * 默认字符集
	 */
	APPLICATION_CHARSET("application.charset", CharEncoding.UTF_8),
	
	/**
	 * 临时存储目录
	 */
	STORAGE_DIRECTORY("storage-directory", "tmpdir"),

	/**
	 * 排除目录表达式
	 */
	URL_EXCLUDE_PATTERN("url-exclude-pattern","*"),

	/**
	 * true | false, true will return localhost/127.0.0.1 for hostname/hostaddress, false will attempt dns lookup for hostname (default: false).
	 */
	DNS_LOOKUPS_DISABLED("dns-lookups-disabled", "false"),
	
	/*--------请求对象参数常用取值Key-------------------------------------------------------------------------------------------------------*/
	
	/**
	 * 当前请求的功能有个性化业务逻辑时，通过此key对应的值进行命名空间进行切换达到个性化需求的key
	 */
	REQUEST_NAMESPACE_KEY("request.dispatcher.namespace", "namespace"),
	/**
	 * 当前请求中传递的用户ID值对应的key
	 */
	REQUEST_USER_KEY("request.parameter.userkey", "userKey"),
	/**
	 * 当前请求中传递的访问功能页面ID对应的key
	 */
	REQUEST_FUNC_KEY("request.parameter.funckey", "funcKey"),
	/**
	 * 当前请求中传递的访问功能操作ID对应的key
	 */
	REQUEST_OPT_KEY("request.parameter.optkey", "optKey"),
	/**
	 * 当前请求中传递的工作流程ID对应的key
	 */
	REQUEST_WORKFLOW_FLOWID_KEY("request.workflow.flowid", "flowID"),
	/**
	 * 当前请求中传递的工作流节点ID对应的key
	 */
	REQUEST_WORKFLOW_NODEID_KEY("request.workflow.nodeid", "nodeID"),
	
	/*--------Session 会话常用Key-------------------------------------------------------------------------------------------------------*/
	
	/**
	 * 当前登录用户sessionKey
	 */
	SESSION_USER_KEY("request.session.user", "user"),
	/**
	 * 单点登录用户sessionKey
	 */
	SESSION_SSO_USER_KEY("request.session.ssoUser", "ssoUser"),
	/**
	 * 单点登录角色sessionKey
	 */
	SESSION_SSO_ROLE_KEY("request.session.ssoRole", "ssoRole"),
	/**
	 * 上次登录角色sessionKey
	 */
	SESSION_ROLE_PRE_KEY("request.session.preRole", "pre_role"),
	/**
	 * 当前登录角色sessionKey
	 */
	SESSION_ROLE_KEY("request.session.role", "login_role"),
	
	/*--------登入登出等常用Key和URL取值Key-------------------------------------------------------------------------------------------------------*/
	
	/**
	 * 用户登录类型session记录值的取值key
	 */
	LOGIN_TYPE_KEY("request.session.login_type", "LOGIN_TYPE"),
	/**
	 * 登录内部转发地址配置取值的key
	 */
	LOGIN_DISPATCH_KEY("request.login.dispatchURL","request.login.dispatchURL"), 
	/**
	 * 登录重定向地址配置取值的key
	 */
	LOGIN_REDIRECT_KEY("request.login.redirectURL","request.login.redirectURL"), 
	/**
	 * 登出内部转发地址配置取值的key
	 */
	LOGIN_OUT_DISPATCH_KEY("request.loginout.dispatchURL","request.loginout.dispatchURL"), 
	/**
	 * 登出重定向地址配置取值的key
	 */
	LOGIN_OUT_REDIRECT_KEY("request.loginout.redirectURL","request.loginout.redirectURL"), 
	/**
	 * 会话超时内部转发地址配置取值的key
	 */
	TIMEOUT_DISPATCH_URL_KEY("request.timeout.dispatchURL","request.timeout.dispatchURL"),
	/**
	 * 会话超时重定向地址配置取值的key
	 */
	TIMEOUT_REDIRECT_URL_KEY("request.timeout.redirectURL","request.timeout.redirectURL"),
	/**
	 * 恶意刷新内部转发地址配置取值的key
	 */
	SPITEFUL_DISPATCH_URL_KEY("request.spiteful.dispatchURL","request.spiteful.dispatchURL"), 
	/**
	 * 恶意刷重定向向地址配置取值的key
	 */
	SPITEFUL_REDIRECT_URL_KEY("request.spiteful.redirectURL","request.spiteful.redirectURL"), 
	/**
	 * 系统错误内部转发地址配置取值的key
	 */
	ERROR_DISPATCH_URL_KEY("request.error.dispatchURL","request.error.dispatchURL"),
	/**
	 * 系统错误刷重定向向地址配置取值的key
	 */
	ERROR_REDIRECT_URL_KEY("request.error.redirectURL","request.error.redirectURL"); 

	protected String name;
	protected String defaultValue;

	private Parameter(String name,String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public String getDefault() {
		return defaultValue;
	}

	static Parameter valueOfIgnoreCase(String parameter,String defaultValue) {
		Parameter parm = valueOf(parameter.toUpperCase(Locale.ENGLISH).trim());
		parm.defaultValue = defaultValue;
		return parm;
	}
}
