/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package org.apache.struts2.plus;

/**
 *@类名称	: StrutsConstants.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 10, 2016 3:24:28 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public final class StrutsConstants {
	
	/** Whether Struts is in development mode or not */
    public static final String STRUTS_DEVMODE = "struts.devMode";
    
	/** Location of additional localization properties files to load */
    public static final String STRUTS_CUSTOM_I18N_MODULE_RESOURCES = "struts.custom.i18n.moduleResources";
    
    /** 个性化功能专属命名空间 */
    public static final String STRUTS_ACTION_NAMESPACE = "struts.action.namespace";

    /** The default Style Path */
    public static final String STRUTS_UI_STYLE_PATH = "struts.ui.stylePath";
    /** The default Report Path */
    public static final String STRUTS_UI_REPORT_PATH = "struts.ui.reportath";
    /** The default Html Store Path */
    public static final String STRUTS_UI_HTML_DIR = "struts.ui.htmlDir";
    /** The default Javascript Store Path */
    public static final String STRUTS_UI_JAVASCRIPT_DIR = "struts.ui.jsDir";
    /** The default UI Configuration Object ID */
    public static final String STRUTS_UI_CONFIG = "struts.ui.config";
    
    public static final String STRUTS_ACTION_VALIDATOR_MANAGER = "struts.action.validator.manager";

    /** The allowed mimetypes of a multipart request (file upload) */
    public static final String STRUTS_MULTIPART_ALLOWED_TYPES = "struts.multipart.allowedTypes";
    
    /** The allowed extensions of a multipart request (file upload) */
    public static final String STRUTS_MULTIPART_ALLOWED_EXTENSIONS = "struts.multipart.allowedExtensions";
    
    /** The maximize size of a multipart request (file upload) */
    public static final String STRUTS_MULTIPART_MAXSIZE = "struts.multipart.maxSize";
    
}
