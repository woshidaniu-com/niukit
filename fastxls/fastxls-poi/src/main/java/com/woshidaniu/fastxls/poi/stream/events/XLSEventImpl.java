/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.poi.stream.events;

/**
 *@类名称	: XLSEventImpl.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 10:19:18 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class XLSEventImpl implements XLSEvent {

	 private int fEventType;
	 
	 /**
     * Constructor.
     */
	XLSEventImpl(final int eventType) {
        fEventType = eventType;
    }
	
	/**
     * @see com.woshidaniu.fastxls.poi.stream.events.XLSEvent#getEventType()
     */
    public final int getEventType() {
        return fEventType;
    }

	@Override
	public final CellElement asCellElement() {
		return (CellElement) this;
	}

	@Override
	public final Characters asCharacters() {
		return (Characters) this;
	}

	@Override
	public final Comment asComment() {
		return (Comment) this;
	}

	@Override
	public final RowElement asRowElement() {
		return (RowElement) this;
	}

	@Override
	public final SheetElement asSheetElement() {
		return (SheetElement) this;
	}

	@Override
	public final boolean isCell() {
		 return CELL_ELEMENT == fEventType;
	}

	@Override
	public final boolean isCharacters() {
		 return CHARACTERS == fEventType;
	}

	@Override
	public final boolean isComment() {
		 return COMMENT == fEventType;
	}

	@Override
	public final boolean isRow() {
		 return ROW_ELEMENT == fEventType;
	}

	@Override
	public final boolean isSheet() {
		 return SHEET_ELEMENT == fEventType;
	}
	 
}
