package com.woshidaniu.db.core.dialect.impl;


/**
 * A dialect compatible with the H2 database.
 * @author Thomas Mueller
 *
 */
public class H2Dialect extends MySql5Dialect {

    public boolean supportsLimit() {
        return true;
    }

	public String getLimitString(String originalSQL, int offset,String offsetPlaceholder, int limit, String limitPlaceholder) {
		return new StringBuffer(originalSQL.length() + 40).
			append(originalSQL).
			append((offset > 0) ? " limit "+limitPlaceholder+" offset "+offsetPlaceholder : " limit "+limitPlaceholder).
			toString();
	}

	@Override
	public boolean supportsLimitOffset() {
		return true;
	}
    
//    public boolean bindLimitParametersInReverseOrder() {
//        return true;
//    }    
//
//    public boolean bindLimitParametersFirst() {
//        return false;
//    }

    

}