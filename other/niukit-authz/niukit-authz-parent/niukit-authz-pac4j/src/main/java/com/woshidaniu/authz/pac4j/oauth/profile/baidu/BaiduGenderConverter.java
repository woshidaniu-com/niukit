package com.woshidaniu.authz.pac4j.oauth.profile.baidu;

import org.pac4j.core.profile.Gender;
import org.pac4j.core.profile.converter.AbstractAttributeConverter;

public class BaiduGenderConverter extends AbstractAttributeConverter<Gender> {

    public BaiduGenderConverter() {
        super(Gender.class);
    }

    @Override
    protected Gender internalConvert(final Object attribute) {
    	// for baidu: 用户性别, 性别。"1"表示男，"0"表示女 
        if (attribute instanceof String) {
            final String s = ((String) attribute).toLowerCase();
            if ("1".equals(s) ||"m".equals(s) || "male".equals(s)) {
                return Gender.MALE;
            } else if ("0".equals(s) || "f".equals(s) || "female".equals(s)) {
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
