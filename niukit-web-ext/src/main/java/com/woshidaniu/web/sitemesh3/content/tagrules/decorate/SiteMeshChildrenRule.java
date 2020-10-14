/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.web.sitemesh3.content.tagrules.decorate;

import java.io.IOException;
import java.util.Iterator;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.Content;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.tagprocessor.BasicBlockRule;
import org.sitemesh.tagprocessor.Tag;

import com.woshidaniu.web.utils.TagRuleBundleUtils;

/**
 *@类名称	: SiteMeshChildrenRule.java
 *@类描述	：替换 {@code <sitemesh:children property='script'/>} 此类标签为  {@link ContentProperty} 合并到当前文档后的内容,标签自身内的内容将被丢弃.
 *@创建人	：kangzhidong
 *@创建时间	：Aug 2, 2016 8:43:12 AM
 *@修改人		：
 *@修改时间	：
 *@版本号	:v1.0
 * @see org.sitemesh.content.tagrules.decorate.SiteMeshWriteRule
 * @see SiteMeshContext#getContentToMerge()
 */
public class SiteMeshChildrenRule extends BasicBlockRule<String> {

    private final SiteMeshContext siteMeshContext;

    public SiteMeshChildrenRule(SiteMeshContext siteMeshContext) {
        this.siteMeshContext = siteMeshContext;
    }

    /*
     * <sitemesh:children property='body.footer:script'/>
     * <sitemesh:children property='body:script'/>
     */
    @Override
    protected String processStart(Tag tag) throws IOException {
        String propertyPath = tag.getAttributeValue("property", true);
        Content contentToMerge = siteMeshContext.getContentToMerge();
        if (contentToMerge != null) {
        	//根节点路径取值
        	String rootPath = TagRuleBundleUtils.getRootPath(propertyPath);
        	String tagName = TagRuleBundleUtils.getTagName(propertyPath);
        	//获取指定的根节点
        	ContentProperty targetProperty = TagRuleBundleUtils.getTargetProperty(contentToMerge, rootPath);
	    	if(targetProperty != null){
	    		//循环子节点
	    		Iterator<ContentProperty> childIterator = targetProperty.getChildren().iterator();
			 	while (childIterator.hasNext()) {
					ContentProperty contentProperty = childIterator.next();
					if(contentProperty.getName().equalsIgnoreCase(tagName)){
						contentProperty.writeValueTo(tagProcessorContext.currentBuffer());
					}
				}
	    	}
        }
        tagProcessorContext.pushBuffer();
        return null;
    }

    @Override
    protected void processEnd(Tag tag, String property) throws IOException {
        CharSequence defaultContents = tagProcessorContext.currentBufferContents();
        tagProcessorContext.popBuffer();
        if (siteMeshContext.getContentToMerge() == null) {
            tagProcessorContext.currentBuffer().append(defaultContents);
        }
    }
    
}