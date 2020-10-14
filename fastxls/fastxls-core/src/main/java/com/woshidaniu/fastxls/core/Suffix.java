package com.woshidaniu.fastxls.core;

 /**
  * 
  * @className: Suffix
  * @description: 支持的Excel版本后缀枚举对象
  * @author : kangzhidong
  * @date : 上午10:44:23 2014-11-22
  * @modify by:
  * @modify date :
  * @modify description :
  */
public enum Suffix {


	XLS{
		public String toString() {
			return ".xls";
		}
	},
	XLSX{
		public String toString() {
			return ".xlsx";
		}
	},
	//数据较大时使用此对象
	SXLSX{
		public String toString() {
			return ".xlsx";
		}
	}
	
}



