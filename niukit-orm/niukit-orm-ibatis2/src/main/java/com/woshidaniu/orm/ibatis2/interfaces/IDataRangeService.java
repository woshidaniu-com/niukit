package com.woshidaniu.orm.ibatis2.interfaces;

import java.util.List;

import com.woshidaniu.basemodel.datarange.DataRangeModel;

/**
 * 
 *@类名称	: IDataRangeService.java
 *@类描述	：定义数据范围查询接口，需要用户实现此接口
 *@创建人	：kangzhidong
 *@创建时间	：Mar 18, 2016 9:32:23 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface IDataRangeService {

	/**
	 * 
	 * @description: 查询数据范围结果
	 * @author : kangzhidong
	 * @date : 2014-5-14
	 * @time : 上午11:36:57 
	 * @return
	 */
	public List<DataRangeModel> getDataRanges();
	
}
