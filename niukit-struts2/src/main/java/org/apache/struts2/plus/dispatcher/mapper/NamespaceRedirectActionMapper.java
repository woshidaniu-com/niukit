package org.apache.struts2.plus.dispatcher.mapper;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.plus.StrutsConstants;

import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.inject.Inject;
/**
 * 
 *@类名称	: NamespaceDispatchActionMapper.java
 *@类描述	：专属业务命名空间切换
 *@创建人	：kangzhidong
 *@创建时间	：Mar 4, 2016 1:49:09 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class NamespaceRedirectActionMapper extends DefaultActionMapper {
	
	protected String namespace = null;
	 
	public NamespaceRedirectActionMapper(){
		super();
	}
	
	@Override
	protected void doActionMappingInternal(HttpServletRequest request,
			String uri, ActionMapping mapping,
			ConfigurationManager configManager) {
		//专属命名空间
		String namespace = mapping.getNamespace().endsWith(this.getNamespace()) ? mapping.getNamespace() : mapping.getNamespace() + this.getNamespace();
		///System.out.println("namespace:" + namespace + "，name:" + mapping.getName());
		 // Try to find the namespace in those defined, defaulting to ""
        Configuration config = configManager.getConfiguration();
        /*
        <package name="cxsz10291" namespace="/cxsz10291" extends="configuration">
        ...
        </package> 
        */
        /**
         * 如上：命名空间和包名基本相同，可以尝试使用处理后的命名空间名获取包信息
         */
        PackageConfig packageConfig = config.getPackageConfig(namespace.substring(1));
        //没有找到则进行循环查找
        if(packageConfig == null){
        	// Find the longest matching namespace, defaulting to the default
            for (PackageConfig cfg : config.getPackageConfigs().values()) {
                String ns = cfg.getNamespace();
                if (ns != null && namespace.equals(ns) ) {
                	parseIndividuationNamespace(mapping,cfg);
                    break;
                }
            }
        }else{
        	parseIndividuationNamespace(mapping,packageConfig);
        }
	}
	
	/**
	 * 
	 *@描述		：解析专属命名空间
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 4, 20161:54:08 PM
	 *@param mapping
	 *@param packageConfig
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	protected void parseIndividuationNamespace(ActionMapping mapping,PackageConfig packageConfig){
		String name = mapping.getName();
		String namespace = packageConfig.getNamespace();
		//当前命名空间下的action配置
        Map<String, ActionConfig> actionConfigMap = packageConfig.getAllActionConfigs();
        if(actionConfigMap != null && !actionConfigMap.isEmpty()){
        	 for (ActionConfig actionConfig : actionConfigMap.values()) {
        		 String actionName = actionConfig.getName();
        		 if(name != null && actionName != null && name.indexOf(actionName.substring(0, actionName.indexOf("_") + 1)) > -1){
        			 mapping.setNamespace(namespace);
        			 break;
        		 }
             }
        }
	}

	public String getNamespace() {
		return namespace;
	}

	@Inject(required=false,value=StrutsConstants.STRUTS_ACTION_NAMESPACE)
	public void setNamespace(String namespace) {
		this.namespace = (namespace == null ? "" : namespace);
	}
	
}
