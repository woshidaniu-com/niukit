package com.woshidaniu.niuca.tp.cas.ldap;

public class LdapConfigRead {
    public LdapConfigRead(String dbConfigUrl) {
        new TestProperties();
        String ldapRoot = TestProperties.readValue(dbConfigUrl, "ldap.searchBase");
        String ladpURL = TestProperties.readValue(dbConfigUrl, "ldap.address");
        String ldapAdminDN = TestProperties.readValue(dbConfigUrl, "ldap.admindn");
        String ldapAdminPwd = TestProperties.readValue(dbConfigUrl, "ldap.adminpwd");
        LdapConfig.ldapRoot = ldapRoot.trim();
        LdapConfig.ladpURL = ladpURL.trim();
        LdapConfig.ldapAdminDN = ldapAdminDN.trim();
        LdapConfig.ldapAdminPwd = ldapAdminPwd.trim();
    }
}
