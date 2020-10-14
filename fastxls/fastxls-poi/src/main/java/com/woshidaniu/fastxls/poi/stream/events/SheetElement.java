/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.poi.stream.events;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * 
 *@类名称	: SheetEvent.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 9:15:27 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface SheetElement extends XLSEvent{
 
	public Sheet getSheet();
  
}
