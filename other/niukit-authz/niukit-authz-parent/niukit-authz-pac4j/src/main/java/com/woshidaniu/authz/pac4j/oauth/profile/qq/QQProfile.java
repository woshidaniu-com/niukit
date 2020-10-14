package com.woshidaniu.authz.pac4j.oauth.profile.qq;

import java.net.URI;

import org.pac4j.oauth.profile.OAuth20Profile;

/**
 * http://wiki.connect.qq.com/get_user_info
 */
public class QQProfile extends OAuth20Profile {
	
	@Override
    public String getDisplayName() {
        return (String) getAttribute(QQProfileDefinition.NICK_NAME);
    }
    
    @Override
    public String getUsername() {
        return (String) getAttribute(QQProfileDefinition.NICK_NAME);
    }
    
    @Override
    public String getFirstName() {
    	return (String) getAttribute(QQProfileDefinition.NICK_NAME);
    }
    
    @Override
    public URI getPictureUrl() {
        return (URI) getAttribute(QQProfileDefinition.FIGURE_URL_1);
    }
  
	public URI getFigureurl() {
		return (URI) getAttribute(QQProfileDefinition.FIGURE_URL);
	}

	public URI getFigureurl_1() {
		return (URI) getAttribute(QQProfileDefinition.FIGURE_URL_1);
	}

	public URI getFigureurl_2() {
		return (URI) getAttribute(QQProfileDefinition.FIGURE_URL_2);
	}

	public URI getFigureurl_qq_1() {
		return (URI) getAttribute(QQProfileDefinition.FIGURE_URL_QQ_1);
	}

	public URI getFigureurl_qq_2() {
		return (URI) getAttribute(QQProfileDefinition.FIGURE_URL_QQ_2);
	}

	public boolean isVip() {
		return (Boolean) getAttribute(QQProfileDefinition.IS_VIP);
	}
	
	public boolean isYellowVip() {
		return (Boolean) getAttribute(QQProfileDefinition.IS_YELLOW_VIP);
	}

	public int getYellowVipLevel() {
		return (Integer) getAttribute(QQProfileDefinition.YELLOW_VIP_LEVEL);
	}

	public int getLevel() {
		return (Integer) getAttribute(QQProfileDefinition.LEVEL);
	}

	public boolean isYellowYearVip() {
		return (Boolean) getAttribute(QQProfileDefinition.IS_YELLOW_YEAR_VIP);
	}
    
}
