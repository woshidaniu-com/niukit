package org.apache.struts2.plus.components;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.struts2.components.UIBean;
import org.apache.struts2.components.template.Template;
import org.apache.struts2.components.template.TemplateEngine;
import org.apache.struts2.components.template.TemplateRenderingContext;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.plus.StrutsConstants;
import org.apache.struts2.plus.views.jsp.TaglibConfig;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.BooleanUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.io.utils.DirectoryUtils;

/**
 * 
 *@类名称	: AbstractStrutsUIBean.java
 *@类描述	：抽象的 Struts UI Bean
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 8:47:59 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractStrutsUIBean extends UIBean  {

	protected Logger LOG = LoggerFactory.getLogger(AbstractStrutsUIBean.class);
	protected SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyyMMdd");
	protected SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * 功能代码
	 */
	protected String func_code;
	/**
	 * 操作的代码
	 */
	protected String opt_code;
	/**
	 * 组件初始化JSON
	 */
	protected String widget;
	/**
	 * 组件是否使用缓存
	 */
	protected String cacheable;
	/**
	 * 组件是否静态化
	 */
	protected String staticable;
	/**
	 * 参数对象值栈取值key 
	 */
	protected String paramKey;
	/**
	 * 对象值栈取值key 
	 */
	protected String stackKey;
	/**
	 * 应用根路径
	 */
	protected String webRootDir;
	/**
	 * 样式服务访问地址
	 */
	protected String stylePath = null;
	/**
	 * 报表服务访问地址
	 */
	protected String reportPath = null;
	/**
	 * 生成的Html保存路径（相对应用根路径）
	 */
	protected String htmlDir = "/WEB-INF/dynamic/html";
	/**
	 * 生成的Javascript保存路径（相对应用根路径）
	 */
	protected String jsDir = "/WEB-INF/dynamic/js";
	/**
	 * 初始化参数获取接口
	 */
	protected TaglibConfig config = null;
	
	public AbstractStrutsUIBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
		//应用程序根目录
		webRootDir = request.getSession().getServletContext().getRealPath(File.separator);
	}

	protected File getHtmlFile(){
		//动态生成的静态文件存储目录
		File dynamicDir = DirectoryUtils.getExistDir(webRootDir + getHtmlDir() );
		return new File(dynamicDir, getFunc_code() + "-" + getOpt_code() + ".html");
	}
	
	protected File getJsFile(String suffix){
		//动态生成的静态文件存储目录
		File dynamicDir = DirectoryUtils.getExistDir(webRootDir + getJsDir() );
		return new File(dynamicDir, getFunc_code() + "-" + getOpt_code() + ( !BlankUtils.isBlank(suffix) ? "-" + suffix : "") + ".js");
	}
	
	public String renderTemplate(Template template){
		//创建目标输出缓存writer
        StringBuilderWriter builderWriter = new StringBuilderWriter();
		try {
			
			TemplateEngine engine = templateEngineManager.getTemplateEngine(template, templateSuffix);
	        if (engine == null) {
	            throw new ConfigurationException("Unable to find a TemplateEngine for template " + template);
	        }
	        if (LOG.isDebugEnabled()) {
	            LOG.debug("Rendering template " + template);
	        }
	        //执行目标处理逻辑
	        TemplateRenderingContext context = new TemplateRenderingContext(template, builderWriter, getStack(), getParameters(), this);
	        //渲染模板
	        engine.renderTemplate(context);
	        
		} catch (Exception e) {
			LOG.warn("Rendering template error:",e);
		} finally {
			IOUtils.closeQuietly(builderWriter);
		}
		//返回渲染后的结果
		return builderWriter.toString();
	}
	
	/**
	 * 覆写evaluateExtraParams（）方法，在UIBean初始化后会调用这个方法来初始化设定参数，
	 * 如addParameter方法，会在freemarker里的parameters里加入一个key value。
	 * 这里要注意findString，还有相关的findxxxx方法，它们是已经封装好了的解释ognl语法的工具，具体可看一下UIBean的api doc
	 */
	@Override
    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (null != func_code) {
        	this.setFunc_code(findString(func_code));
        	this.addParameter("func_code", getFunc_code() );
        }
        if (null != opt_code) {
        	this.setOpt_code(findString(opt_code));
        	this.addParameter("opt_code", getOpt_code());
        }else{
        	this.setOpt_code("cx");
        	this.addParameter("opt_code", "cx");
        }
        
        if (null != widget) {
        	this.setWidget(findString(widget));
        	this.addParameter("widget", getWidget());
        }else{
        	this.setWidget("{}");
        	this.addParameter("widget", "{}");
        }
        this.addParameter("stylePath", getStylePath());
		this.addParameter("reportPath", getReportPath());
		this.addParameter("systemPath", request.getContextPath());
		this.addParameter("timestamp", new Date().getTime());
		this.addParameter("timeNow", FORMAT_TIME.format(new Date()));
		this.addParameter("dateNow", FORMAT_DATE.format(new Date()));
		
		//全局初始化参数
		if(!BlankUtils.isBlank(getConfig())){
			Map<String, Object> parameterMap = getConfig().getParameterMap();
			if( parameterMap != null){
				for (String key : parameterMap.keySet()) {
					this.addParameter( key, parameterMap.get(key));
				}
			}
		}
		
		//当前上下文中ValueStack中可能存在的参数对象：对象可能是map或者model对象
		if(!BlankUtils.isBlank(getParamKey())){
			//从struts上下文取值
			Object stack_obj = super.findValue(getParamKey());
			if( stack_obj != null){
				Map stackMap = null;
				if(stack_obj instanceof Map){
					stackMap = (Map) stack_obj;
				}else{
					try {
						stackMap = BeanUtils.describe(stack_obj);
					} catch (Exception e) {
						LOG.warn("stack params Object describe error:",e);
						stackMap = new HashMap<String, String>();
					}
				}
				if( stackMap != null){
					for (Object key : stackMap.keySet()) {
						this.addParameter( StringUtils.getSafeObj(key), stackMap.get(key));
					}
				}
			}
		}
    }
	
	protected String getStackKey(String suffix) {
		return getFunc_code()+ getOpt_code() + suffix;
	}
	
	
	public String getFunc_code() {
		return func_code;
	}

	public String getOpt_code() {
		return opt_code;
	}
	
	/**
	 * 设置UIBean的属性，@StrutsTagAttribute(description="set message", type="String")注解，说明该属性是字符串（也可以是其它），这一步很重要
	 */
    @StrutsTagAttribute(description="set func_code", type="String")
	public void setFunc_code(String funcCode) {
		func_code = funcCode;
	}
    
    /**
	 * 设置UIBean的属性，@StrutsTagAttribute(description="set message", type="String")注解，说明该属性是字符串（也可以是其它），这一步很重要
	 */
    @StrutsTagAttribute(description="set opt_code", type="String")
	public void setOpt_code(String optCode) {
		opt_code = optCode;
	}
    
    @StrutsTagAttribute(description="set widget", type="String")
	public void setWidget(String widget) {
		this.widget = widget;
	}

	public String getWidget() {
		return widget;
	}
	
	public boolean isCacheable(){
		return BooleanUtils.parse(getCacheable());
	}
	
	public String getCacheable() {
		return cacheable;
	}
	
	@StrutsTagAttribute(description="set cacheable", type="String")
	public void setCacheable(String cacheable) {
		this.cacheable = cacheable;
	}

	public boolean isStaticable(){
		return BooleanUtils.parse(getStaticable());
	}
	
	public String getStaticable() {
		return staticable;
	}

	@StrutsTagAttribute(description="set staticable", type="String")
	public void setStaticable(String staticable) {
		this.staticable = staticable;
	}

	public String getParamKey() {
		return paramKey;
	}

	@StrutsTagAttribute(description="set paramKey", type="String")
	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getStackKey() {
		return stackKey;
	}

	@StrutsTagAttribute(description="set stackKey", type="String")
	public void setStackKey(String stackKey) {
		this.stackKey = stackKey;
	}

	/**
	 * 取得HttpSession的简化方法.
	 */
	public HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 取得HttpRequest的简化方法.
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * 取得HttpResponse的简化方法.
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	public String getStylePath() {
		return stylePath;
	}

	@Inject(StrutsConstants.STRUTS_UI_STYLE_PATH)
	public void setStylePath(String stylePath) {
		this.stylePath = stylePath;
	}

	public String getReportPath() {
		return reportPath;
	}

	@Inject(StrutsConstants.STRUTS_UI_REPORT_PATH)
	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public String getHtmlDir() {
		return htmlDir;
	}

	@Inject(StrutsConstants.STRUTS_UI_HTML_DIR)
	public void setHtmlDir(String htmlDir) {
		this.htmlDir = htmlDir;
	}

	public String getJsDir() {
		return jsDir;
	}

	@Inject(StrutsConstants.STRUTS_UI_JAVASCRIPT_DIR)
	public void setJsDir(String jsDir) {
		this.jsDir = jsDir;
	}

	public TaglibConfig getConfig() {
		return config;
	}

	@Inject(value = StrutsConstants.STRUTS_UI_CONFIG, required = false)
	public void setConfig(String taglibConfig) {
		this.config = Dispatcher.getInstance().getContainer().getInstance(TaglibConfig.class, taglibConfig );
	}
	
}