package com.woshidaniu.authz.pac4j.oauth.profile.qq;

import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.converter.Converters;
import org.pac4j.oauth.config.OAuth20Configuration;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.definition.OAuth20ProfileDefinition;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.model.OAuth2AccessToken;

/**
 * http://wiki.connect.qq.com/get_user_info
 */
public class QQProfileDefinition extends OAuth20ProfileDefinition<QQProfile>  {

	private static final String PROFILE_URL = "https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=%s";
	 
	/** 用户在QQ空间的昵称。 */
    public static final String NICK_NAME = "nickname";
	/** 大小为30×30像素的QQ空间头像URL。 */
    public static final String FIGURE_URL = "figureurl";
	/** 大小为50×50像素的QQ空间头像URL。 */
    public static final String FIGURE_URL_1 = "figureurl_1";
	/** 大小为100×100像素的QQ空间头像URL。 */
    public static final String FIGURE_URL_2 = "figureurl_2";
	/** 大小为40×40像素的QQ头像URL。 */
    public static final String FIGURE_URL_QQ_1 = "figureurl_qq_1";
	/** 大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100x100的头像，但40x40像素则是一定会有。 */
	public static final String FIGURE_URL_QQ_2 = "figureurl_qq_2";
	/** 标识用户是否为VIP用户（0：不是；1：是） */
	public static final String IS_VIP = "vip";
	/** 标识用户是否为黄钻用户（0：不是；1：是）。 */
	public static final String IS_YELLOW_VIP = "is_yellow_vip";
	/** 黄钻等级 */
	public static final String YELLOW_VIP_LEVEL = "yellow_vip_level";
	/** QQ等级 */
	public static final String LEVEL = "level";
	/** 标识是否为年费黄钻用户（0：不是； 1：是） */
    public static final String IS_YELLOW_YEAR_VIP = "is_yellow_year_vip";
    

    public static final String RET = "ret";
    public static final String MSG = "msg";
    
    public QQProfileDefinition() {
    	
    	//  { "ret":1002, "msg":"请先登录" }
    	primary(RET, Converters.STRING);
    	primary(MSG, Converters.STRING);
        
        primary(NICK_NAME, Converters.STRING);
        primary(FIGURE_URL, Converters.URL);
        primary(FIGURE_URL_1, Converters.URL);
        primary(FIGURE_URL_2, Converters.URL);
        primary(FIGURE_URL_QQ_1, Converters.URL);
        primary(FIGURE_URL_QQ_2, Converters.URL);
        primary(IS_VIP, Converters.BOOLEAN);
        primary(IS_YELLOW_VIP, Converters.BOOLEAN);
        primary(YELLOW_VIP_LEVEL, Converters.INTEGER);
        primary(LEVEL, Converters.INTEGER);
        primary(IS_YELLOW_YEAR_VIP, Converters.BOOLEAN);
    }

	@Override
	public String getProfileUrl(OAuth2AccessToken accessToken, OAuth20Configuration configuration) {
		return String.format(PROFILE_URL, accessToken.getAccessToken(), configuration.getKey());
	}

	@Override
	public QQProfile extractUserProfile(String body) throws HttpAction {
		final QQProfile profile = new QQProfile();
        JsonNode json = JsonHelper.getFirstNode(body);
        if (json != null ) {
            profile.setId(JsonHelper.getElement(json, "id"));
            for (final String attribute : getPrimaryAttributes()) {
				convertAndAdd(profile, attribute, JsonHelper.getElement(json, attribute));
			}
        }
        return profile;
	}
	
}