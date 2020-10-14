/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.web.sitemesh3.content;

import java.io.IOException;

import org.sitemesh.content.Content;
import org.sitemesh.content.ContentProperty;

import com.woshidaniu.web.utils.TagRuleBundleUtils;

/**
 *@类名称	: ChildrenContentProperty.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Aug 2, 2016 9:55:38 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public class ChildrenContentProperty implements ContentProperty {

	private ContentProperty current;
	private String propertyPath;
	
	public ChildrenContentProperty(ContentProperty property,String propertyPath) {
		this.current = property;
		this.propertyPath = propertyPath;
	}
	
	@Override
	public String getName() {
		return current.getName();
	}

	@Override
	public ContentProperty[] getFullPath() {
		return current.getFullPath();
	}

	@Override
	public ContentProperty getParent() {
		return current.getParent();
	}

	@Override
	public boolean hasChildren() {
		return current.hasChildren();
	}

	@Override
	public boolean hasChild(String name) {
		return current.hasChild(name);
	}

	@Override
	public ContentProperty getChild(String name) {
		return current.getChild(name);
	}

	@Override
	public Iterable<ContentProperty> getChildren() {
		return TagRuleBundleUtils.getChildren(current, propertyPath);
	}

	@Override
	public Iterable<ContentProperty> getDescendants() {
		return current.getDescendants();
	}

	@Override
	public boolean hasValue() {
		return current.hasValue();
	}

	@Override
	public String getValue() {
		return current.getValue();
	}
 
	@Override
	public String getNonNullValue() {
		return current.getNonNullValue();
	}

	@Override
	public void writeValueTo(Appendable out) throws IOException {
		current.writeValueTo(out);
	}

	@Override
	public void setValue(CharSequence value) {
		current.setValue(value);
	}

	@Override
	public Content getOwningContent() {
		return current.getOwningContent();
	}

	/**
	 * @return the current ContentProperty
	 */
	public ContentProperty getCurrentProperty() {
		return current;
	}

}
