package com.woshidaniu.fastxls.core;

/**
 * @title: SplitType.java
 * @package com.woshidaniu.fastpoi.workbook.document.poi
 * @fescription: 生成Excel文件时,数据过多时候采用拆分方式sheet:多个工作簿|wookbook：多个xls文件，默认：sheet 即 拆分为多个工作簿
 * @author: kangzhidong
 * @date : 上午10:56:04 2014-11-22
 */
public enum SplitType {
	
	SHEET{
		public String toString() {
			return "sheet";
		}
	},
	WOOKBOOK{
		public String toString() {
			return "wookbook";
		}
	}
	
}
