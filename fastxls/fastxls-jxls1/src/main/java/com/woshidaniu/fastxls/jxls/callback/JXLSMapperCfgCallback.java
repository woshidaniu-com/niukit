package com.woshidaniu.fastxls.jxls.callback;

import net.sf.jxls.transformer.Configuration;
import net.sf.jxls.transformer.XLSTransformer;


public interface JXLSMapperCfgCallback {

	public void doInJXLSMapper(XLSTransformer transformer);
	
	public void doBeforeJXLSMapper(Configuration config);
	
}
