
package com.woshidaniu.orm.ibatis2.handler;

import java.util.Map;

import com.ibatis.sqlmap.client.event.RowHandler;
/** 
 * @package com.woshidaniu.orm.ibatis2.handler
 * @className: MapToBeanRowHandler
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-4-9
 * @time : 下午03:37:57
 */
public class MapToBeanRowHandler implements RowHandler {
	@SuppressWarnings("unchecked")
	public void handleRow(Object row) {
		if(row instanceof Map){
			
		}
	}

}



