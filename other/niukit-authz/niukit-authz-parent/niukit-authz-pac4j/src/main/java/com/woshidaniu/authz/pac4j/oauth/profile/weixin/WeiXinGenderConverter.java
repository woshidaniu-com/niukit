package com.woshidaniu.authz.pac4j.oauth.profile.weixin;

import org.pac4j.core.profile.Gender;
import org.pac4j.core.profile.converter.AbstractAttributeConverter;

public class WeiXinGenderConverter extends AbstractAttributeConverter<Gender> {

    public WeiXinGenderConverter() {
        super(Gender.class);
    }

    @Override
    protected Gender internalConvert(final Object attribute) {
    	// for Weixin: 普通用户性别，1为男性，2为女性
        if (attribute instanceof String) {
            final String s = ((String) attribute).toLowerCase();
            if ("2".equals(s) ||"m".equals(s) || "male".equals(s)) {
                return Gender.MALE;
            } else if ("1".equals(s) || "f".equals(s) || "female".equals(s)) {
                return Gender.FEMALE;
            } else {
                return Gender.UNSPECIFIED;
            }
            // for Vk:
        } else if (attribute instanceof Integer) {
            Integer value = (Integer) attribute;
            if (value == 2) {
                return Gender.MALE;
            } else if (value == 1) {
                return Gender.FEMALE;
            } else {
                return Gender.UNSPECIFIED;
            }
        }
        return null;
    }
}
