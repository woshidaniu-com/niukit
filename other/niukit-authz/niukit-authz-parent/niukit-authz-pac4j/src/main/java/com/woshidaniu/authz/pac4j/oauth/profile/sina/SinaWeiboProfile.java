package com.woshidaniu.authz.pac4j.oauth.profile.sina;

import java.net.URI;
import java.util.Date;
import java.util.Locale;

import org.pac4j.oauth.profile.OAuth20Profile;

public class SinaWeiboProfile extends OAuth20Profile {

	@Override
	public String getFamilyName() {
		return null;
	}

	/**
	 * 用户昵称
	 */
	@Override
	public String getFirstName() {
		return (String) getAttribute(SinaWeiboProfileDefinition.SCREEN_NAME);
	}

	/**
	 * 友好显示名称
	 */
	@Override
	public String getDisplayName() {
		return (String) getAttribute(SinaWeiboProfileDefinition.NAME);
	}

	@Override
	public String getUsername() {
		return (String) getAttribute(SinaWeiboProfileDefinition.SCREEN_NAME);
	}

	/**
	 * 用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语
	 */
	@Override
	public Locale getLocale() {
		return (Locale) getAttribute(SinaWeiboProfileDefinition.LANG);
	}

	/**
	 * 用户的微博统一URL地址
	 */
	@Override
	public URI getProfileUrl() {
		return (URI) getAttribute(SinaWeiboProfileDefinition.PROFILE_URL);
	}

	/**
	 * 用户头像地址（中图），50×50像素
	 */
	@Override
	public URI getPictureUrl() {
		return (URI) getAttribute(SinaWeiboProfileDefinition.PROFILE_IMAGE_URL);
	}

	/**
	 * 用户头像地址（大图），180×180像素
	 */
	public URI getLargeAvatarUrl() {
		return (URI) getAttribute(SinaWeiboProfileDefinition.AVATAR_LARGE_URL);
	}

	/**
	 * 用户头像地址（高清），高清头像原图
	 */
	public URI getHDAvatarUrl() {
		return (URI) getAttribute(SinaWeiboProfileDefinition.AVATAR_HD_URL);
	}

	/**
	 * 用户所在省级ID
	 */
	public String getProvince() {
		return (String) getAttribute(SinaWeiboProfileDefinition.PROVINCE);
	}

	/**
	 * 用户所在城市ID
	 */
	public String getCity() {
		return (String) getAttribute(SinaWeiboProfileDefinition.CITY);
	}

	/**
	 * 用户所在地
	 */
	@Override
	public String getLocation() {
		return (String) getAttribute(SinaWeiboProfileDefinition.LOCATION);
	}

	/**
	 * 字符串型的用户UID
	 */
	public String getIdStr() {
		return (String) getAttribute(SinaWeiboProfileDefinition.IDSTR);
	}

	/**
	 * 用户个人描述
	 */
	public String getDescription() {
		return (String) getAttribute(SinaWeiboProfileDefinition.DESCRIPTION);
	}

	/**
	 * 用户的微号
	 */
	public String getWeihao() {
		return (String) getAttribute(SinaWeiboProfileDefinition.WEIHAO);
	}

	/**
	 * 用户的个性化域名
	 */
	public String getDomain() {
		return (String) getAttribute(SinaWeiboProfileDefinition.DOMAIN);
	}

	/**
	 * 用户备注信息，只有在查询用户关系时才返回此字段
	 */
	public String getRemark() {
		return (String) getAttribute(SinaWeiboProfileDefinition.REMARK);
	}

	/**
	 * 粉丝数
	 */
	public Integer getFollowers() {
		return (Integer) getAttribute(SinaWeiboProfileDefinition.FOLLOWERS_COUNT);
	}

	/**
	 * 关注数
	 */
	public Integer getOwnedPrivateRepos() {
		return (Integer) getAttribute(SinaWeiboProfileDefinition.FRIENDS_COUNT);
	}

	/**
	 * 微博数
	 */
	public Integer getTotalPrivateRepos() {
		return (Integer) getAttribute(SinaWeiboProfileDefinition.STATUSES_COUNT);
	}

	/**
	 * 收藏数
	 */
	public Integer getPrivateGists() {
		return (Integer) getAttribute(SinaWeiboProfileDefinition.FAVOURITES_COUNT);
	}

	/**
	 * 用户的互粉数
	 */
	public Integer getBiFollowers() {
		return (Integer) getAttribute(SinaWeiboProfileDefinition.BI_FOLLOWERS_COUNT);
	}

	/**
	 * 是否允许所有人给我发私信，true：是，false：否
	 */
	public Boolean isAllowAllActMsg() {
		return (Boolean) getAttribute(SinaWeiboProfileDefinition.ALLOW_ALL_ACT_MSG);
	}

	/**
	 * 是否允许标识用户的地理位置，true：是，false：否
	 */
	public Boolean isGeoEnabled() {
		return (Boolean) getAttribute(SinaWeiboProfileDefinition.GEO_ENABLED);
	}

	/**
	 * 是否是微博认证用户，即加V用户，true：是，false：否
	 */
	public Boolean isVerified() {
		return (Boolean) getAttribute(SinaWeiboProfileDefinition.VERIFIED);
	}

	/**
	 * 暂未支持
	 */
	public String getVerified() {
		return (String) getAttribute(SinaWeiboProfileDefinition.VERIFIED_TYPE);
	}

	/**
	 * 认证原因
	 */
	public String getVerifiedReason() {
		return (String) getAttribute(SinaWeiboProfileDefinition.VERIFIED_REASON);
	}

	/**
	 * 是否允许所有人对我的微博进行评论，true：是，false：否
	 */
	public Boolean isAllowAllComment() {
		return (Boolean) getAttribute(SinaWeiboProfileDefinition.ALLOW_ALL_COMMENT);
	}

	/**
	 * 该用户是否关注当前登录用户，true：是，false：否
	 */
	public Boolean isFollowMe() {
		return (Boolean) getAttribute(SinaWeiboProfileDefinition.FOLLOW_ME);
	}

	/**
	 * 用户的在线状态，false：不在线、true：在线
	 */
	public Boolean isOnline() {
		return (Boolean) getAttribute(SinaWeiboProfileDefinition.ONLINE_STATUS);
	}

	/**
	 * 用户创建（注册）时间
	 */
	public Date getCreatedAt() {
		return (Date) getAttribute(SinaWeiboProfileDefinition.CREATED_AT);
	}

	/**
	 * 用户博客地址
	 */
	public String getBlogUrl() {
		return (String) getAttribute(SinaWeiboProfileDefinition.BLOG_URL);
	}

	/**
	 * 用户的最近一条微博信息字段 详细 :
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
	public String getBlog() {
		return (String) getAttribute(SinaWeiboProfileDefinition.STATUS);
	}

}
