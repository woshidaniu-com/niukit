;(function($){

	/** Map holding bundle keys (if mode: 'niutal') */
	$.i18n = $.i18n || {};
	$.i18n.niutal = $.i18n.niutal || {};
	
	$.extend($.i18n.niutal,{
		//状态码提醒
		"statusCode" 		: {
			//HTTP Status 400（错误请求） 
			"400"		: "Bad Request,Check whether the parameters are correct!",
			//HTTP Status 401（未授权） 
			"401"		: "Access denied, Attempting unauthorized access to password protected pages!",
			//HTTP Status 404（未找到）
			"404"		: "Resource or Web Page is Not Found!",
			//HTTP Status 408（请求超时） 
			"408"		: "The server has timed out waiting for the request. Please check if the network connection is correct!",
			//HTTP Status 500（服务器内部错误） 
			"500"		: "Error occurred on the server, the request cannot be completed!",
			//HTTP Status 502（错误网关） 
			"502"		: "The server receives an invalid response from the upstream server as a gateway or proxy!",
			//HTTP Status 503（服务不可用）
			"503"		: "The server is currently unavailable (Overload or downtime maintenance). Please try again after the service starts!",
			//HTTP Status 504（网关超时） 
			"504"		: "As a gateway or proxy server, request upstream server timeout!",
			  /*--------------自定义响应状态码-----------------*/
	        //HTTP Status 901（HTTP 会话过期）    -> 会话过期或者注销。
			"901"		: "The current user has logged out or the session has expired, Please login again!",
			//HTTP Status 902（未授权） ->请求要求身份验证。 对于需要登录的网页，服务器可能返回此响应。
			"902"		: "The current user login role without the authorization!",
			//HTTP Status 903（HTTP 浏览器会话变更）    -> 当前浏览器同一会话被其他用户登录，导致session变化。
			"903"		: "Only one user is allowed in the same browser!",
			//HTTP Status 904（HTTP 恶意刷新）    -> 相同的两个请求频率超过限制阀值时的状态提醒。
			"904"		: "Don't frequent refresh or click menu!~",
			//HTTP Status 905（HTTP 不一致）    -> 会话用户与指定参数值一致性校验结果不一致，服务器可能返回此响应。
			"905"		: "The current request path parameter value consistency check filter is enabled, and you have no access to data in the non - self permission!",
			//HTTP Status 906（HTTP 非法IP访问）    -> 请求客户端IP地址不在允许的IP白名单内，服务器可能返回此响应。
			"906"		: "Current request path IP address white list filtering is enabled, Illegal IP address client access!",
			//HTTP Status 907（HTTP 非法MAC访问）    -> 请求客户端MAC地址不在允许的MAC白名单内，服务器可能返回此响应。
			"907"		: "Current request path Mac address white list filtering is enabled,Illegal Mac address client access!",
			//HTTP Status 908（HTTP 危险来源）    -> 请求来源不明，服务器为了安全会对象范围来源进行逻辑判断，如果不符合要求则服务器可能返回此响应。
			"908"		: "Current request path dangerous access source filter is enabled, Illegal request source!",
			//HTTP Status 909（HTTP Action未定义）    -> 处理请求的Action对象未定义，则服务器可能返回此响应。
			"909"		: "The Action object that handles the HTTP request is not initialized!",
			//HTTP Status 910（HTTP 方法未定义）    -> 请求的后台方法未定义，则服务器可能返回此响应。
			"910"		: "The requested method is not defined in the Action object!",
			//HTTP Status 911（HTTP 运行异常）    -> 应用程序运行期间发生错误，则服务器可能返回此响应。
			"911"		: "An error occurred during the application running, please contact the administrator to view the exception log!"
		},
		//登录页面提示国际化
		"login" 		: {
			"user_empty"	: "User name cannot be empty！",
			"pwd_empty"		: "Password cannot be empty！",
			"yzm_empty"		: "Verification code cannot be empty！"
		},
		"titles"		: {
			"pwd" : "Modify Password"
		},
		//工作流国际化
		"workFlow" 		: {
			"chooseWorkFlow"	: "Select Process",
			"viewWorkFlow"		: "Process View"
		},
		//工具国际化
		"utils"			: {
			//密码强度组件
			"strength" 		: {
				"label"		: "Password Strength",
				"ratings"	: ["Weak","Weak","Normal","Strong","Strong","Not rated"]
			}
		}
	});

}(jQuery));