package com.woshidaniu.authz.pac4j.oauth.profile.sina;

import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.converter.Converters;
import org.pac4j.oauth.config.OAuth20Configuration;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.definition.OAuth20ProfileDefinition;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.model.OAuth2AccessToken;

/**
 * http://open.weibo.com/wiki/2/statuses/user_timeline
 */
public class SinaWeiboProfileDefinition extends OAuth20ProfileDefinition<SinaWeiboProfile> {

	private final static String SINA_PROFILE_URL = "https://api.weibo.com/2/users/show.json?access_token=%s";

	// 字符串型的用户UID
	public static final String IDSTR = "idstr";
	// 用户昵称
	public static final String SCREEN_NAME = "screen_name";
	// 友好显示名称
	public static final String NAME = "name";
	// 用户所在省级ID
	public static final String PROVINCE = "province";
	// 用户所在城市ID
	public static final String CITY = "city";
	// 用户所在地
	public static final String LOCATION = "location";
	// 用户个人描述
	public static final String DESCRIPTION = "description";
	// 用户博客地址
	public static final String BLOG_URL = "url";
	// 用户头像地址（中图），50×50像素
	public static final String PROFILE_IMAGE_URL = "profile_image_url";
	// 用户的微博统一URL地址
	public static final String PROFILE_URL = "profile_url";
	// 用户的个性化域名
	public static final String DOMAIN = "domain";
	// 用户的微号
	public static final String WEIHAO = "weihao";
	// 性别，m：男、f：女、n：未知
	public static final String GENDER = "gender";
	// 粉丝数
	public static final String FOLLOWERS_COUNT = "followers_count";
	// 关注数
	public static final String FRIENDS_COUNT = "friends_count";
	// 微博数
	public static final String STATUSES_COUNT = "statuses_count";
	// 收藏数
	public static final String FAVOURITES_COUNT = "favourites_count";
	// 用户创建（注册）时间
	public static final String CREATED_AT = "created_at";
	// 是否允许所有人给我发私信，true：是，false：否
	public static final String ALLOW_ALL_ACT_MSG = "allow_all_act_msg";
	// 是否允许标识用户的地理位置，true：是，false：否
	public static final String GEO_ENABLED = "geo_enabled";
	// 是否是微博认证用户，即加V用户，true：是，false：否
	public static final String VERIFIED = "verified";
	// 暂未支持
	public static final String VERIFIED_TYPE = "verified_type";
	// 认证原因
	public static final String VERIFIED_REASON = "verified_reason";
	// 用户备注信息，只有在查询用户关系时才返回此字段
	public static final String REMARK = "remark";
	// 是否允许所有人对我的微博进行评论，true：是，false：否
	public static final String ALLOW_ALL_COMMENT = "allow_all_comment";
	// 用户头像地址（大图），180×180像素
	public static final String AVATAR_LARGE_URL = "avatar_large";
	// 用户头像地址（高清），高清头像原图
	public static final String AVATAR_HD_URL = "avatar_hd";
	// 该用户是否关注当前登录用户，true：是，false：否
	public static final String FOLLOW_ME = "follow_me";
	// 用户的在线状态，0：不在线、1：在线
	public static final String ONLINE_STATUS = "online_status";
	// 用户的互粉数
	public static final String BI_FOLLOWERS_COUNT = "bi_followers_count";
	// 用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语
	public static final String LANG = "lang";
	/* 用户的最近一条微博信息字段 详细
	 * <pre>
	 * {
	    "created_at": "Tue May 24 18:04:53 +0800 2011",
	    "id": 11142488790,
	    "text": "我的相机到了。",
	    "source": "<a href="http://weibo.com" rel="nofollow">新浪微博</a>",
	    "favorited": false,
	    "truncated": false,
	    "in_reply_to_status_id": "",
	    "in_reply_to_user_id": "",
	    "in_reply_to_screen_name": "",
	    "geo": null,
	    "mid": "5610221544300749636",
	    "annotations": [],
	    "reposts_count": 5,
	    "comments_count": 8
	  }
	  </pre>
	*/
	public static final String STATUS = "status";
	

	public SinaWeiboProfileDefinition() {
		
		String[] names = new String[] { IDSTR, SCREEN_NAME, NAME, PROVINCE, CITY, LOCATION, DESCRIPTION, 
				DOMAIN, WEIHAO, VERIFIED_TYPE, VERIFIED_REASON, REMARK, STATUS };
		for (String name : names) {
			primary(name, Converters.STRING);
		}
		
		names = new String[] { BLOG_URL, PROFILE_IMAGE_URL, PROFILE_URL, AVATAR_LARGE_URL, AVATAR_HD_URL };
		for (String name : names) {
			primary(name, Converters.URL);
		}
		
		names = new String[] { FOLLOWERS_COUNT, FRIENDS_COUNT, STATUSES_COUNT, FAVOURITES_COUNT, BI_FOLLOWERS_COUNT };
		for (String name : names) {
			primary(name, Converters.INTEGER);
		}
		names = new String[] { ALLOW_ALL_ACT_MSG, GEO_ENABLED, VERIFIED, ALLOW_ALL_COMMENT, FOLLOW_ME, ONLINE_STATUS };
		for (String name : names) {
			primary(name, Converters.BOOLEAN);
		}
		primary(CREATED_AT, Converters.DATE_TZ_RFC822);
		primary(GENDER, Converters.GENDER);
		primary(LANG, Converters.LOCALE);

	}

	@Override
	public String getProfileUrl(final OAuth2AccessToken accessToken, final OAuth20Configuration configuration) {
		return String.format(SINA_PROFILE_URL, accessToken.getAccessToken());
	}

	@Override
	public SinaWeiboProfile extractUserProfile(final String body) throws HttpAction {
		final SinaWeiboProfile profile = new SinaWeiboProfile();
        JsonNode json = JsonHelper.getFirstNode(body);
        if (json != null) {
        	//用户UID
            profile.setId(JsonHelper.getElement(json, "id"));
            for (final String attribute : getPrimaryAttributes()) {
				convertAndAdd(profile, attribute, JsonHelper.getElement(json, attribute));
			}
        }
        return profile;
	}

}
