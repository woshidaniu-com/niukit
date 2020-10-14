/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.poi.stream.events;

import org.apache.poi.ss.usermodel.Cell;

/**
 *@类名称	: CellElementImpl.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 10:28:06 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public class CellElementImpl extends XLSEventImpl implements CellElement {

	public CellElementImpl(Cell cell) {
		super(CELL_ELEMENT);
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

}
