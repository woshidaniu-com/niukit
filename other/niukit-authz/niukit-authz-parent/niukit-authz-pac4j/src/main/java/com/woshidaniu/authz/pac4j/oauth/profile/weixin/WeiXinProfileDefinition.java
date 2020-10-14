package com.woshidaniu.authz.pac4j.oauth.profile.weixin;

import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.converter.Converters;
import org.pac4j.oauth.config.OAuth20Configuration;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.definition.OAuth20ProfileDefinition;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.woshidaniu.authz.pac4j.oauth.OAuth2Constants;

/**
 * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419316518&lang=zh_CN
 */
public class WeiXinProfileDefinition extends OAuth20ProfileDefinition<WeiXinProfile>  { 
	
	private final static String WEIXIN_PROFILE_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";

	/** 普通用户的标识，对当前开发者帐号唯一 */
    public static final String OPEN_ID = "openid";
    /** 普通用户昵称*/
    public static final String NICK_NAME = "nickname";
    /** 普通用户性别，1为男性，2为女性 */
    public static final String SEX = "sex";
    /** 国家，如中国为CN */
    public static final String COUNTRY = "country";
    /** 普通用户个人资料填写的省份 */
    public static final String PROVINCE = "province";
    /** 普通用户个人资料填写的城市 */
    public static final String CITY = "city";
    /** 用户头像，最后一个数值代表我是大牛形头像大小（有0、46、64、96、132数值可选，0代表640*640我是大牛形头像），用户没有头像时该项为空 */
    public static final String HEAD_IMG_URL = "headimgurl";
    /** 用户特权信息，json数组，如微信沃卡用户为（chinaunicom） */
    public static final String PRIVILEGE = "privilege";
    /** 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。 */
    public static final String UNION_ID = "unionid";
    // appended
    /**  */
    public static final String APP_NAME = "appName";
    /**  */
    public static final String SUID = "suid";

    public WeiXinProfileDefinition(){
        primary(OPEN_ID, Converters.STRING);
        primary(NICK_NAME, Converters.STRING);
        primary(SEX, new WeiXinGenderConverter());
        primary(COUNTRY, Converters.STRING);
        primary(PROVINCE, Converters.STRING);
        primary(CITY, Converters.STRING);
        primary(HEAD_IMG_URL, Converters.URL);
        primary(UNION_ID, Converters.STRING);
        primary(APP_NAME, Converters.STRING);
        primary(SUID, Converters.INTEGER);
    }

	@Override
	public String getProfileUrl(OAuth2AccessToken accessToken, OAuth20Configuration configuration) {
		return String.format(WEIXIN_PROFILE_URL, accessToken.getAccessToken(), accessToken.getParameter(OAuth2Constants.OPENID));
	}

	/**
	 * 
	 * 正确的Json返回结果：
		{
			"openid":"OPENID",
			"nickname":"NICKNAME",
			"sex":1,
			"province":"PROVINCE",
			"city":"CITY",
			"country":"COUNTRY",
			"headimgurl": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
			"privilege":[
				"PRIVILEGE1",
				"PRIVILEGE2"
			],
			"unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
		}
		错误的Json返回示例:
		{
			"errcode":40003,"errmsg":"invalid openid"
		}
	 * */
	@Override
	public WeiXinProfile extractUserProfile(String body) throws HttpAction {
		final WeiXinProfile profile = new WeiXinProfile();
        JsonNode json = JsonHelper.getFirstNode(body);
        if (json != null && JsonHelper.getElement(json, "errcode") == null) {
        	// 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
            profile.setId(JsonHelper.getElement(json, OAuth2Constants.UNIONID));
            for (final String attribute : getPrimaryAttributes()) {
				convertAndAdd(profile, attribute, JsonHelper.getElement(json, attribute));
			}
        }
        return profile;
	}
}
