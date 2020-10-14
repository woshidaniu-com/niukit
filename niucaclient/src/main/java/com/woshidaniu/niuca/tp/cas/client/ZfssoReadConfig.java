package com.woshidaniu.niuca.tp.cas.client;


import com.woshidaniu.niuca.tp.cas.util.TestProperties;
import com.woshidaniu.niuca.tp.cas.util.encrypt.DBEncrypt;

public class ZfssoReadConfig {
    public ZfssoReadConfig(String dbConfigUrl) {
        String casurl = TestProperties.readValue(dbConfigUrl, "zfsso.casurl");
        String usezfca = TestProperties.readValue(dbConfigUrl, "zfsso.usezfca");
        String ywxtservername = TestProperties.readValue(dbConfigUrl, "zfsso.ywxtservername");
        String viewname = TestProperties.readValue(dbConfigUrl, "zfsso.viewname");
        String dbadminuser = TestProperties.readValue(dbConfigUrl, "zfsso.dbadminuser");
        String dbadminpwd = TestProperties.readValue(dbConfigUrl, "zfsso.dbadminpwd");
        String ipaddress = TestProperties.readValue(dbConfigUrl, "zfsso.ipaddress");
        String overtime = TestProperties.readValue(dbConfigUrl, "zfsso.overtime");
        DBEncrypt dbEncrypt = new DBEncrypt();
        ZfssoConfig.casurl = casurl.trim();
        ZfssoConfig.usezfca = usezfca.trim();
        ZfssoConfig.ywxtservername = ywxtservername.trim();
        ZfssoConfig.viewname = viewname.trim();
        if (dbadminuser != null) {
            ZfssoConfig.dbadminuser = dbEncrypt.dCode(dbadminuser.trim().getBytes());
        }

        if (dbadminpwd != null) {
            ZfssoConfig.dbadminpwd = dbEncrypt.dCode(dbadminpwd.trim().getBytes());
        }

        ZfssoConfig.ipaddress = ipaddress.trim();
        ZfssoConfig.overtime = overtime.trim();
    }
}
