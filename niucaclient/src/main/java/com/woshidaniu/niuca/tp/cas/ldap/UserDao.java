package com.woshidaniu.niuca.tp.cas.ldap;

import java.security.MessageDigest;
import java.util.Hashtable;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import sun.misc.BASE64Encoder;


public class UserDao {
    public UserDao() {
    }

    public Boolean CheckUser(String yhm, String kl) {
        Boolean res = new Boolean(false);
        DirContext dc = null;
        Hashtable env = new Hashtable();
        env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("java.naming.provider.url", LdapConfig.ladpURL + LdapConfig.ldapRoot);
        env.put("java.naming.security.authentication", "simple");
        env.put("java.naming.security.principal", LdapConfig.ldapAdminDN);
        env.put("java.naming.security.credentials", LdapConfig.ldapAdminPwd);

        try {
            dc = new InitialDirContext(env);
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(2);
            String filter = "(&(uid=" + yhm + ")(userPassword=" + encryptkey(kl) + "))";
            NamingEnumeration en = dc.search("", filter, constraints);

            label126:
            while(true) {
                Attributes attrs;
                do {
                    Object obj;
                    do {
                        if (en == null || !en.hasMoreElements()) {
                            if (res) {
                                System.out.println("认证成功");
                            }

                            return res;
                        }

                        obj = en.nextElement();
                    } while(!(obj instanceof SearchResult));

                    SearchResult si = (SearchResult)obj;
                    String[] ys = si.getName().split(",");
                    String[] lx = ys[1].split("=");
                    attrs = si.getAttributes();
                } while(attrs == null);

                NamingEnumeration ae = attrs.getAll();

                while(true) {
                    Attribute attr;
                    String attrId;
                    do {
                        if (!ae.hasMoreElements()) {
                            continue label126;
                        }

                        attr = (Attribute)ae.next();
                        attrId = attr.getID();
                    } while(!"userPassword".equals(attrId));

                    NamingEnumeration vals = attr.getAll();

                    while(vals.hasMoreElements()) {
                        Object o = vals.nextElement();
                        if ((new String((byte[])o)).equalsIgnoreCase(encryptkey(kl))) {
                            res = new Boolean(true);
                        }
                    }
                }
            }
        } catch (Exception var22) {
            var22.printStackTrace();
            System.out.println("认证失败");
        } finally {
            this.close(dc);
        }

        return res;
    }

    public void close(DirContext dc) {
        if (dc != null) {
            try {
                dc.close();
                System.out.println("关闭认证链接");
            } catch (NamingException var3) {
            }
        }

    }

    public static String encryptkey(String data) {
        return "{MD5}" + testHA2(data, "md5");
    }

    public static String testHA2(String data, String ha) {
        byte[] buffer = (byte[])null;
        MessageDigest messageDigest = null;
        String s = "";

        try {
            buffer = data.getBytes("UTF-8");
            messageDigest = MessageDigest.getInstance(ha);
            messageDigest.update(buffer);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        buffer = messageDigest.digest();
        s = (new BASE64Encoder()).encode(buffer);
        return s;
    }
}
