package com.woshidaniu.db.core.dialect;

public enum NativeDialect {
	
	MYSQL {
		public String toString() {
			return "MYSQL";
		}
	},
	SQLSERVER{
		public String toString() {
			return "SQLSERVER";
		}
	},
	DB2{
		public String toString() {
			return "DB2";
		}
	},
	ORACLE{
		public String toString() {
			return "ORACLE";
		}
	}
			
}
