
#--------请求对象参数常用取值Key---------------------------

#当前请求的功能有个性化业务逻辑时，通过此key对应的值进行命名空间进行切换达到个性化需求的key
#request.dispatcher.namespace = namespace
#当前请求中传递的用户ID值对应的key
#request.parameter.userkey = userKey
#当前请求中传递的访问功能页面ID对应的key
#request.parameter.funckey = funcKey
#当前请求中传递的访问功能操作ID对应的key
#request.parameter.optkey = optKey
#当前请求中传递的工作流程ID对应的key
#request.workflow.flowid = flowID
#当前请求中传递的工作流节点ID对应的key
#request.workflow.nodeid = nodeID

#--------Session 会话常用Key------------------------------

#当前登录角色sessionKey
#request.session.user = user
#单点登录用户sessionKey
#request.session.ssoUser = ssoUser
#单点登录用户sessionKey
#request.session.ssoRole = ssoRole
#上次登录角色sessionKey
#request.session.preRole = pre_role
#当前登录角色sessionKey
#request.session.role = login_role 

#--------登入登出等常用Key和URL取值Key---------------------

#登录内部转发地址配置取值的key
#request.login.dispatchURL","request.login.dispatchURL"); 
#登录重定向地址配置取值的key
#request.login.redirectURL = request.login.redirectURL 
#登出内部转发地址配置取值的key
#request.loginout.dispatchURL = request.loginout.dispatchURL 
#登出重定向地址配置取值的key
#request.loginout.redirectURL = request.loginout.redirectURL
#会话超时内部转发地址配置取值的key
#request.timeout.dispatchURL = request.timeout.dispatchURL
#会话超时重定向地址配置取值的key
#request.timeout.redirectURL = request.timeout.redirectURL
#恶意刷新内部转发地址配置取值的key
#request.spiteful.dispatchURL = request.spiteful.dispatchURL 
#恶意刷重定向向地址配置取值的key
#request.spiteful.redirectURL = request.spiteful.redirectURL 
#系统错误内部转发地址配置取值的key
#request.error.dispatchURL = request.error.dispatchURL 
#系统错误刷重定向向地址配置取值的key
#request.error.redirectURL = request.error.redirectURL


