package com.woshidaniu.fastxls.core;

 /**
  * 
  *@类名称	: Version.java
  *@类描述	：
  *@创建人	：kangzhidong
  *@创建时间	：Mar 22, 2016 5:29:36 PM
  *@修改人	：
  *@修改时间	：
  *@版本号	:v1.0
  */
public enum Version {


	VERSION_2003{
		public String toString() {
			return ".xls";
		}
	},
	VERSION_2007{
		public String toString() {
			return ".xlsx";
		}
	},
	VERSION_2010{
		public String toString() {
			return ".xlsx";
		}
	},
	VERSION_2013{
		public String toString() {
			return ".xlsx";
		}
	}
	
	
}



