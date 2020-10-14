package org.apache.struts2.plus.dispatcher.mapper;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.RequestUtils;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/** 
 * 
 *@类名称	: DefaultActionMapper.java
 *@类描述	：重新DefaultActionMapper实现个性化处理
 *@创建人	：kangzhidong
 *@创建时间	：Mar 4, 2016 1:48:02 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class DefaultActionMapper extends org.apache.struts2.dispatcher.mapper.DefaultActionMapper {

	protected static final Logger LOG = LoggerFactory.getLogger(DefaultActionMapper.class);
    private String idParameterName = null;
    
    public DefaultActionMapper() {
    	super();
    	setSlashesInActionNames("true");
    }

    /*
    * @see org.apache.struts2.dispatcher.mapper.ActionMapper#getMapping(javax.servlet.http.HttpServletRequest)
    */
    @Override
    public ActionMapping getMapping(HttpServletRequest request, ConfigurationManager configManager) {
    	setSlashesInActionNames("true");
    	if (!isSlashesInActionNames()) {
    		throw new IllegalStateException("This action mapper requires the setting 'slashesInActionNames' to be set to 'true'");
    	}
    	ActionMapping mapping = new ActionMapping();
        String uri = RequestUtils.getUri(request);

        int indexOfSemicolon = uri.indexOf(";");
        uri = (indexOfSemicolon > -1) ? uri.substring(0, indexOfSemicolon) : uri;

        uri = dropExtension(uri, mapping);
        if (uri == null) {
        	return null;
        }
        //根据默认请求设置 namespace,actionName
        parseNameAndNamespace(uri, mapping, configManager);
        //处理特殊参数
        handleSpecialParameters(request, mapping);
        //进行自定义的扩展处理
        doActionMappingInternal(request, uri, mapping, configManager);
        //还回处理后的 ActionMapping对象
        return parseActionName(mapping);
    }
    
    protected abstract void doActionMappingInternal(HttpServletRequest request,String uri, ActionMapping mapping, ConfigurationManager configManager);
    
	public String getIdParameterName() {
		return idParameterName;
	}

	@Inject(required=false,value=StrutsConstants.STRUTS_ID_PARAMETER_NAME)
	public void setIdParameterName(String idParameterName) {
		this.idParameterName = idParameterName;
	}
}
