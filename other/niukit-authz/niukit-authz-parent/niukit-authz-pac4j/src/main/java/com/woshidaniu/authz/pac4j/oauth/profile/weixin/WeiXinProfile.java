package com.woshidaniu.authz.pac4j.oauth.profile.weixin;

import java.net.URI;
import java.util.Locale;

import org.pac4j.core.profile.Gender;
import org.pac4j.oauth.profile.OAuth20Profile;

/**
 * 用于添加返回用户信息
 */
public class WeiXinProfile extends OAuth20Profile {

	/**
	 * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的
	 */
	public String getOpenid() {
		return (String) getAttribute(WeiXinProfileDefinition.OPEN_ID);
	}
	
	@Override
	public String getFamilyName() {
		return null;
	}

	/**
	 * 用户昵称
	 */
	@Override
	public String getFirstName() {
		return (String) getAttribute(WeiXinProfileDefinition.NICK_NAME);
	}

	/**
	 * 友好显示名称
	 */
	@Override
	public String getDisplayName() {
		return (String) getAttribute(WeiXinProfileDefinition.NICK_NAME);
	}

	@Override
	public String getUsername() {
		return (String) getAttribute(WeiXinProfileDefinition.NICK_NAME);
	}
	
	@Override
	public Gender getGender() {
		return (Gender) getAttribute(WeiXinProfileDefinition.SEX);
	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public URI getProfileUrl() {
		return null;
	}

	/**
	 * 用户头像，最后一个数值代表我是大牛形头像大小（有0、46、64、96、132数值可选，0代表640*640我是大牛形头像），用户没有头像时该项为空
	 */
	@Override
	public URI getPictureUrl() {
		return (URI) getAttribute(WeiXinProfileDefinition.HEAD_IMG_URL);
	}


	/**
	 * 国家，如中国为CN
	 */
	public String getCountry() {
		return (String) getAttribute(WeiXinProfileDefinition.COUNTRY);
	}
	
	/**
	 * 普通用户个人资料填写的省份
	 */
	public String getProvince() {
		return (String) getAttribute(WeiXinProfileDefinition.PROVINCE);
	}
	
	/**
	 * 普通用户个人资料填写的城市
	 */
	public String getCity() {
		return (String) getAttribute(WeiXinProfileDefinition.CITY);
	}
	
	/**
	 * 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
	 */
	public String getPrivilege() {
		return (String) getAttribute(WeiXinProfileDefinition.PRIVILEGE);
	}

}
