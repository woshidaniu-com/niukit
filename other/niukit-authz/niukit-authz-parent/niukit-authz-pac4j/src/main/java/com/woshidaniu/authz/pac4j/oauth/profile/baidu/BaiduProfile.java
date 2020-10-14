package com.woshidaniu.authz.pac4j.oauth.profile.baidu;

import java.net.URI;

import org.pac4j.core.profile.Gender;
import org.pac4j.oauth.profile.OAuth20Profile;

/**
 * http://developer.baidu.com/wiki/index.php?title=docs/oauth/rest/file_data_apis_list
 */
public class BaiduProfile extends OAuth20Profile {

	/** 用户真实姓名，可能为空。 */
	@Override
	public String getDisplayName() {
		return (String) getAttribute(BaiduProfileDefinition.REAL_NAME);
	}

	/** 当前登录用户的用户名，值可能为空。 */
	@Override
	public String getUsername() {
		return (String) getAttribute(BaiduProfileDefinition.USER_NAME);
	}

	/** 用户真实姓名，可能为空。 */
	@Override
	public String getFirstName() {
		return (String) getAttribute(BaiduProfileDefinition.REAL_NAME);
	}

	/**
	 * 当前登录用户的头像 头像的地址具体如下： small image:
	 * http://tb.himg.baidu.com/sys/portraitn/item/{$portrait} large image:
	 * http://tb.himg.baidu.com/sys/portrait/item/{$portrait}
	 */
	@Override
	public URI getPictureUrl() {
		return (URI) getAttribute(BaiduProfileDefinition.PORTRAIT_SMALL_URL);
	}

	public URI getPortraitLargeUrl() {
		return (URI) getAttribute(BaiduProfileDefinition.PORTRAIT_LARGE_URL);
	}

	/** 自我简介，可能为空。 */
	public String getUserdetail() {
		return (String) getAttribute(BaiduProfileDefinition.USER_DETAIL);
	}

	/** 生日，以yyyy-mm-dd格式显示。 */
	public String getBirthday() {
		return (String) getAttribute(BaiduProfileDefinition.BIRTHDAY);
	}

	/** 婚姻状况 */
	public String getMarriage() {
		return (String) getAttribute(BaiduProfileDefinition.MARRIAGE);
	}

	/** 用户性别 */
	@Override
	public Gender getGender() {
		return (Gender) getAttribute(BaiduProfileDefinition.SEX);
	}

	/** 血型 */
	public String getBlood() {
		return (String) getAttribute(BaiduProfileDefinition.BLOOD);
	}

	/** 体型 */
	public String getFigure() {
		return (String) getAttribute(BaiduProfileDefinition.FIGURE);
	}

	/** 星座 */
	public String getConstellation() {
		return (String) getAttribute(BaiduProfileDefinition.CONSTELLATION);
	}

	/** 学历 */
	public String getEducation() {
		return (String) getAttribute(BaiduProfileDefinition.EDUCATION);
	}

	/** 当前职业 */
	public String getTrade() {
		return (String) getAttribute(BaiduProfileDefinition.TRADE);
	}

	/** 职位 */
	public String getJob() {
		return (String) getAttribute(BaiduProfileDefinition.JOB);
	}

}
