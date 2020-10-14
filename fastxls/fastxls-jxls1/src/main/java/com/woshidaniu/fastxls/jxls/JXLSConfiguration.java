package com.woshidaniu.fastxls.jxls;

import net.sf.jxls.transformer.Configuration;

/**
 *@类名称	: JXLSConfiguration.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 28, 2016 9:09:23 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class JXLSConfiguration extends Configuration {
	
	public JXLSConfiguration() {
		super();
		//registerTagLib(new JxTaglib(), "jx");
    }

    public JXLSConfiguration(String startExpressionToken, String endExpressionToken, String startFormulaToken, String endFormulaToken, String metaInfoToken) {
    	super(startExpressionToken, endExpressionToken, startFormulaToken, endFormulaToken, metaInfoToken);
    }

    public JXLSConfiguration(String startExpressionToken, String endExpressionToken, String startFormulaToken, String endFormulaToken, String metaInfoToken, boolean isUTF16) {
    	super(startExpressionToken, endExpressionToken, startFormulaToken, endFormulaToken, metaInfoToken, isUTF16);
    }
    
}
