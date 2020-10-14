/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.poi.stream.events;


/**
 *@类名称	: WorkbookEvent.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 9:02:34 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public interface XLSEvent extends XLSConstants {

	  /**
	   * Returns an integer code for this event.
	   * @see #CELL_ELEMENT
	   * @see #CHARACTERS
	   * @see #COMMENT
	   * @see #ROW_ELEMENT
	   * @see #SHEET_ELEMENT
	   */
	  public int getEventType();
	 
	  public boolean isSheet();
	  
	  public boolean isRow();

	  public boolean isCell();
	  
	  public boolean isComment();
	  
	  public boolean isCharacters();
	  
	  public CellElement asCellElement();
	
	  public Characters asCharacters();
	  
	  public Comment asComment();
	  
	  public RowElement asRowElement();
	  
	  public SheetElement asSheetElement();
}
