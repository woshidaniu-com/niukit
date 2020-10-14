package com.woshidaniu.component.bpm.common;

import java.io.Serializable;


/**
 * 
* 类描述：所有model的基类 
* 创建人：hhy 
* 创建时间：2011-12-20 上午10:51:28 
* 修改人：caozf 
* 修改时间：2012-07-04 上午13:51:28 
* 修改备注：  
* @version 
*
 */
public class ModelBase  implements Serializable {

	public String getDoType() {
		return doType;
	}

	public void setDoType(String doType) {
		this.doType = doType;
	}

	public BPMQueryModel getQueryModel() {
		return queryModel;
	}

	public void setQueryModel(BPMQueryModel queryModel) {
		this.queryModel = queryModel;
	}

	private static final long serialVersionUID = -1172945282247419062L;

	private String doType;
	
	public BPMQueryModel queryModel = new BPMQueryModel();
	
	
}
