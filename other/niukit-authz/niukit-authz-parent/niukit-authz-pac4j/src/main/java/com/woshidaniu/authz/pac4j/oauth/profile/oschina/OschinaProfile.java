package com.woshidaniu.authz.pac4j.oauth.profile.oschina;

import java.net.URI;

import org.pac4j.oauth.profile.OAuth20Profile;

/**
 * 用于添加返回用户信息
 */
public class OschinaProfile extends OAuth20Profile {

	@Override
    public String getDisplayName() {
        return (String) getAttribute(OschinaProfileDefinition.NAME);
    }
    
    @Override
    public String getUsername() {
        return (String) getAttribute(OschinaProfileDefinition.NAME);
    }
    
    @Override
    public String getFirstName() {
    	return (String) getAttribute(OschinaProfileDefinition.NAME);
    }
    
    @Override
    public URI getPictureUrl() {
        return (URI) getAttribute(OschinaProfileDefinition.AVATAR_URL);
    }
    
    @Override
    public URI getProfileUrl() {
        return (URI) getAttribute(OschinaProfileDefinition.URL);
    }

}
