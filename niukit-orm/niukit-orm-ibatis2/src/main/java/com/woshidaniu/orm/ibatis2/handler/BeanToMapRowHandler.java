package com.woshidaniu.orm.ibatis2.handler;

import java.util.Map;

import com.ibatis.sqlmap.client.event.RowHandler;
/**
 * 
 * @package com.woshidaniu.orm.ibatis2.handler
 * @className: BeanToMapRowHandler
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-4-9
 * @time : 下午03:37:30
 */
public class BeanToMapRowHandler implements RowHandler {

	@SuppressWarnings("unchecked")
	public void handleRow(Object row) {
		if(row instanceof Map){
			//do nothing
		}else{
			
		}
	}

}



