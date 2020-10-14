package org.apache.struts2.plus.dispatcher.result;

/**
 * 
 * @package com.woshidaniu.common.action.result
 * @className: Result
 * @description: 
 * <pre>
 * &lt;result-type name="chain" class="com.opensymphony.xwork2.ActionChainResult"/&gt;
 * &lt;result-type name="dispatcher" class="org.apache.struts2.dispatcher.ServletDispatcherResult" default="true"/&gt;
 * &lt;result-type name="freemarker" class="org.apache.struts2.views.freemarker.FreemarkerResult"/&gt;
 * &lt;result-type name="httpheader" class="org.apache.struts2.dispatcher.HttpHeaderResult"/&gt;
 * &lt;result-type name="redirect" class="org.apache.struts2.dispatcher.ServletRedirectResult"/&gt;
 * &lt;result-type name="redirectAction" class="org.apache.struts2.dispatcher.ServletActionRedirectResult"/&gt;
 * &lt;result-type name="stream" class="org.apache.struts2.dispatcher.StreamResult"/&gt;
 * &lt;result-type name="velocity" class="org.apache.struts2.dispatcher.VelocityResult"/&gt;
 * &lt;result-type name="xslt" class="org.apache.struts2.views.xslt.XSLTResult"/&gt;
 * &lt;result-type name="plainText" class="org.apache.struts2.dispatcher.PlainTextResult" /&gt;
 * <!-- struts-plugin -->
 * &lt;result-type name="json" class="org.apache.struts2.json.JSONResult"/&gt;
 * &lt;result-type name="jasper" class="org.apache.struts2.views.jasperreports.JasperReportsResult"/&gt;
 * &lt;result-type name="chart" class="org.apache.struts2.dispatcher.ChartResult"/&gt;
 * &lt;result-type name="jsf" class="org.apache.struts2.jsf.FacesResult"/&gt;
 * <!-- struts-portlet-plugin -->
 * &lt;result-type name="dispatcher" class="org.apache.struts2.portlet.result.PortletResult" default="true"/&gt;
 * &lt;result-type name="redirect" class="org.apache.struts2.portlet.result.PortletResult"/&gt;
 * &lt;result-type name="redirectAction" class="org.apache.struts2.portlet.result.PortletActionRedirectResult"/&gt;
 * &lt;result-type name="freemarker" class="org.apache.struts2.views.freemarker.PortletFreemarkerResult"/&gt;
 * &lt;result-type name="velocity" class="org.apache.struts2.portlet.result.PortletVelocityResult"/&gt;
 * <!-- struts-custom-plugin -->
 * &lt;result-type name="byte" class="com.ant4j.struts2.result.types.ByteStreamResult"/&gt;
 * &lt;result-type name="file" class="com.ant4j.struts2.result.types.FileStreamResult"/&gt;
 * &lt;result-type name="ajax" class="com.woshidaniu.common.action.result.types.ServletAjaxResult"/&gt;
 * </pre>
 * @author : kangzhidong
 * @date : 2014-4-10
 * @time : 上午10:54:55
 */
public abstract class Result {

	/**strtus2-result */
	
	public static final String DISPATCHER = "dispatcher";

	public static final String HTTPHEADER = "httpheader";

	public static final String REDIRECT = "redirect";

	public static final String REDIRECT_ACTION = "redirectAction";
	
	public static final String FREEMARKER = "freemarker";

	public static final String VELOCITY = "velocity";
	
	public static final String CHAIN = "chain";
	
	public static final String STREAM = "stream";

	public static final String XSLT = "xslt";

	public static final String PLAIN_TEXT = "plainText";
	
	public static final String DATA = "data";
	
	public static final String QUERY = "query";

	/**返回JasperReport报表结果*/
	public static final String JASPER = "jasper"; 
	/**返回JFreeChart图表结果*/
	public static final String CHART = "chart"; 
	/**返回JSF 模板*/
	public static final String JSF = "jsf";
	/**返回TEXT格式数据*/
	public static final String TEXT = "text";
	/**返回HTML格式数据*/
	public static final String HTML = "html"; 
	/**返回XML格式数据*/
	public static final String XML = "xml"; 
	/**返回JSON格式数据*/
	public static final String JSON = "json"; 
	/**返回Byte字节流数据*/
	public static final String BYTE = "byte";
	/**返回ZIP 文件流*/
	public static final String ZIP = "zip";
	/**返回FILE文件数据*/
	public static final String FILE = "file";
	/**返回FILE文件数据,返回后,删除源文件*/
	public static final String FILE_TEMP = "file-tmp";
	/**返回FILE文件数据*/
	public static final String FILEPATH = "filepath";
	/**返回FILE文件数据,返回后,删除源文件*/
	public static final String FILEPATH_TEMP = "filepath-tmp";
	/**返回断点续传字节*/
	public static final String BREAKPOINT_BYTE = "breakpoint-bytes";
	/**返回断点续传文件*/
	public static final String BREAKPOINT_FILEPATH = "breakpoint-filepath";
	/**返回断点续传文件,返回后,删除源文件*/
	public static final String BREAKPOINT_FILEPATH_TEMP = "breakpoint-filepath-tmp";
	/**返回断点续传文件*/
	public static final String BREAKPOINT_FILE = "breakpoint-file";
	/**返回断点续传文件,返回后,删除源文件*/
	public static final String BREAKPOINT_FILE_TEMP = "breakpoint-file-tmp";
	/**返回断点续传输入流*/
	public static final String BREAKPOINT_STREAM = "breakpoint-stream";
	/**返回JFreeChart图表图片*/
	public static final String JCHART = "jchart";
	/**返回JasperReports报表结果文件*/
	public static final String JREPORT = "jreport";
	/**返回PDF 文件流*/
	public static final String PDF = "pdf";
	public static final String PDF_INLINE = "pdf-inline"; 
	/**返回EXCEL 文件流*/
	public static final String EXCEL = "excel"; 
	public static final String EXCEL_INLINE = "excel-inline"; 
	/**返回DOC 文件流*/
	public static final String DOC_ATTACHMENT = "doc-attachment"; 
	public static final String DOC_INLINE = "doc-inline"; 
	
	/**strtus2-forward */
	
	/** 转到登录页面*/
	public static final String RD_LOGIN = "login";
	/** 转到登出页面*/
	public static final String RD_LOGIN_OUT = "loginOut";
	/** 转到oauth2认证页面*/
	public static final String RD_OAUTH2 = "oauth2";
	/** 转到NULL*/
	public static final String RD_NULL = null; 
	/** 转到卡片index页面查询：tab_index */
	public static final String RD_TAB_INDEX  = "tab_index";
	/** 转到index页面查询*/
	public static final String RD_INDEX = "index";
	/** 转到list页面查询：list*/
	public static final String RD_LIST = "list";
	/** 转到display页面查询：display*/
	public static final String RD_DISPLAY = "display";
	/** 转到新增页面：add*/
	public static final String RD_ADD = "add";
	/** 转到修改页面：modify*/
	public static final String RD_MOD = "modify";
	/** 双击查询：line*/
	public static final String RD_LINE = "line";
	/** 转到打印页面：print*/
	public static final String RD_PRINT = "print";
	/** 转到统计页面：query*/
	public static final String RD_QUERY = "query";
	/** 转到信息绑定页面：bind*/
	public static final String RD_BIND = "bind";
	/** 转到信息解绑页面：unbind*/
	public static final String RD_UNBIND = "unbind";
	/** 页面嵌入信息查询*/
	public static final String RD_INFO = "info";
	/** 空白页：blank*/
	public static final String RD_BLANK = "blank";
	/** 友情提示页面：prompt*/
	public static final String RD_PROMPT = "prompt";
	/** 数据导入页面  ：import*/
	public static final String RD_IMPORT = "import";
	/** 数据导出页面：export */
	public static final String RD_EXPORT = "export";
	/** 友情提示信息页面：tooltip */
	public static final String RD_TOOLTIP = "tooltip";
	/** 通告页面：notes */
	public static final String RD_NOTES = "notes";
	/** 系统操作详情首页面 ：index_details*/
	public static final String RD_INDEX_DETAILS = "index_details";
	/** 系统操作详情页面：details */
	public static final String RD_DETAILS = "details";
	/** 转到浏览器下载页面*/
	public static final String RD_BROWSER = "browser";
	/** 返回Workbook 文件流*/
	public static final String EXPORT_WORKBOOK = "exportWorkbook";
	/** 返回Workbook 文件流*/
	public static final String EXPORT_TEMPLATE = "exportTemplate";
	
	/**strtus2-status */
	
	/**成功*/
	public static final String SUCCESS = "success";
	/**失败*/
	public static final String FAILED = "failed";
	/**空*/
	public static final String EMPTY = "empty";
	/**真*/
	public static final String TRUE = "true";
	/**假*/
	public static final String FALSE = "false";
	/**相同*/
	public static final String EQUAL = "equal";
	/**不相同*/
	public static final String UNEQUAL = "unequal"; 
	/**存在*/
	public static final String EXIST = "exist"; 
	/**不存在*/
	public static final String UNEXIST = "unexist";
	
	/**strtus2-exception */

	/**异常：会话超时*/
	public static final String EX_SESSION_OUT = "sessionOut";
	/**异常：功能未开放*/
	public static final String EX_NON_OPEN = "nonOpen";
	/**异常：无功能权限*/
	public static final String EX_NON_ACCESS = "nonAccess";
	/**异常：恶意请求*/
	public static final String EX_SPITEFUL = "spitefulException";
	/**异常：浏览器下载错误*/
	public static final String EX_BROWSER = "browserException";
	/**异常：不存在*/
	public static final String EX_UNEXIST = "unexist";
	/**异常：检查错误 */
	public static final String EX_CHECK = "check";
	/**异常：未知异常*/
	public static final String EX_UNKNOW = "unknow";
	/**异常：提示信息 */
	public static final String EX_INFO = "info";
	/**异常：警告信息*/
	public static final String EX_WARN = "warn";
	/**异常：错误信息 */
	public static final String EX_ERROR = "error";
	/**异常：外部登录信息*/
	public static final String EX_OUTSIDE = "outside";
	
	/**strtus2-key */
	
	/** 消息 */
	public static final String MESSAGE = "message";
	/** 详情 */
	public static final String DETAILS = "details";
	
}
