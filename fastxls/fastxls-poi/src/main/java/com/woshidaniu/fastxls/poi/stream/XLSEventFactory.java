/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.poi.stream;

import com.woshidaniu.fastxls.poi.stream.events.CellElement;
import com.woshidaniu.fastxls.poi.stream.events.Characters;
import com.woshidaniu.fastxls.poi.stream.events.Comment;
import com.woshidaniu.fastxls.poi.stream.events.RowElement;
import com.woshidaniu.fastxls.poi.stream.events.SheetElement;

/**
 *@类名称 : XLSEventFactory.java
 *@类描述 ：
 *@创建人 ：kangzhidong
 *@创建时间 ：Mar 29, 2016 9:16:04 AM
 *@修改人 ：
 *@修改时间 ：
 *@版本号 :v1.0
 */
public abstract class XLSEventFactory {

	protected XLSEventFactory() {
	}

	/**
	 * Create a new instance of the factory
	 * 
	 * @throws FactoryConfigurationError
	 *             if an instance of this factory cannot be loaded
	 */
	public static XLSEventFactory newFactory() throws FactoryConfigurationError {
		return (XLSEventFactory) FactoryFinder.find(
				"com.woshidaniu.fastxls.poi.factory.XLSEventFactory",
				"com.woshidaniu.fastxls.poi.factory.XLSEventFactoryImpl");
	}

	/**
	 * Create a new instance of the factory. If the classLoader argument is
	 * null, then the ContextClassLoader is used.
	 * 
	 * Note that this is a new method that replaces the deprecated
	 * newInstance(String factoryId, ClassLoader classLoader) method. No changes
	 * in behavior are defined by this replacement method relative to the
	 * deprecated method.
	 * 
	 * @param factoryId  Name of the factory to find, same as a property name
	 * @param classLoader classLoader to use
	 * @return the factory implementation
	 * @throws FactoryConfigurationError if an instance of this factory cannot be loaded
	 */
	public static XLSEventFactory newFactory(String factoryId,
			ClassLoader classLoader) throws FactoryConfigurationError {
		try {
			// do not fallback if given classloader can't find the class, throw
			// exception
			return (XLSEventFactory) FactoryFinder.newInstance(factoryId, classLoader, false);
		} catch (FactoryFinder.ConfigurationError e) {
			throw new FactoryConfigurationError(e.getException(), e.getMessage());
		}
	}
	
	public abstract CellElement createCellElement(String namespaceURI);
	
	public abstract Characters createCharacters(String content);
	
	public abstract Comment createComment(String namespaceURI);
	
	public abstract RowElement createRowElement(String namespaceURI);
	
	public abstract SheetElement createSheetElement(String namespaceURI);
	 
	  

}
