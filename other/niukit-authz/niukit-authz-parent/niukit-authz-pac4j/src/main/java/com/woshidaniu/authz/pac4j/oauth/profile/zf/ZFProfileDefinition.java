/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.pac4j.oauth.profile.zf;

import org.pac4j.core.exception.HttpAction;
import org.pac4j.oauth.profile.OAuth20Profile;
import org.pac4j.oauth.profile.generic.GenericOAuth20ProfileDefinition;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @className	： ZFProfileDefinition
 * @description	： 获取id属性并设置profile
 * @author 		：康康（1571）
 * @date		： 2018年5月8日 下午3:08:10
 * @version 	V1.0
 */
public class ZFProfileDefinition extends GenericOAuth20ProfileDefinition{

	@Override
	public OAuth20Profile extractUserProfile(String body) throws HttpAction {
		OAuth20Profile profile = super.extractUserProfile(body);
		//id属性设置
		JSONObject jsonObject = JSONObject.parseObject(body);
		String id = jsonObject.getString("id");
		profile.setId(id);
		return profile;
	}
}
