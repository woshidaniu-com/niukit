/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.poi;

import com.woshidaniu.fastxls.core.Cell;

/**
 *@类名称	: POICell.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Aug 26, 2016 4:06:36 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class POICell implements Cell {

	protected org.apache.poi.ss.usermodel.Cell target;
	
	public POICell(org.apache.poi.ss.usermodel.Cell cell){
		this.target = cell;
	}
	
	@Override
	public int getRowIndex() {
		return this.target.getRowIndex();
	}

	@Override
	public int getColumnIndex() {
		return this.target.getColumnIndex();
	}
	
	public static POICell wrap(org.apache.poi.ss.usermodel.Cell cell) {
		return new POICell(cell);
	}

}
