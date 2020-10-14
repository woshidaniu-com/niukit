/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.web.sitemesh3.content.tagrules.decorate;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.content.tagrules.TagRuleBundle;
import org.sitemesh.tagprocessor.State;

/**
 *@类名称	: ExtendDecoratorTagRuleBundle.java
 *@类描述	： {@code <sitemesh:children property='body:script'/>}
 *@创建人	：kangzhidong
 *@创建时间	：Aug 2, 2016 8:44:10 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class ExtendDecoratorTagRuleBundle implements TagRuleBundle {

	@Override
	public void install(State defaultState, ContentProperty contentProperty,
			SiteMeshContext siteMeshContext) {
		
		 defaultState.addRule("sitemesh:children", new SiteMeshChildrenRule(siteMeshContext));
		 defaultState.addRule("sitemesh:scripts", new SiteMeshScriptRule(siteMeshContext));
		 defaultState.addRule("sitemesh:links", new SiteMeshLinkRule(siteMeshContext));
		 defaultState.addRule("sitemesh:styles", new SiteMeshStyleRule(siteMeshContext));
		 
	}

	@Override
	public void cleanUp(State defaultState, ContentProperty contentProperty,
			SiteMeshContext siteMeshContext) {

	}

}
