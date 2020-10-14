package com.woshidaniu.niuca.tp.cas.util.encrypt;


import java.io.ByteArrayOutputStream;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DBEncrypt {
    private Properties properties;

    public DBEncrypt() {
    }

    public Object getObject() throws Exception {
        return this.getProperties();
    }

    public Class getObjectType() {
        return Properties.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties inProperties) {
        this.properties = inProperties;
        String originalUsername = this.properties.getProperty("user");
        String originalPassword = this.properties.getProperty("password");
        String newPassword;
        if (originalUsername != null) {
            newPassword = this.deEncryptUsername(originalUsername);
            this.properties.put("user", newPassword);
        }

        if (originalPassword != null) {
            newPassword = this.deEncryptPassword(originalPassword);
            this.properties.put("password", newPassword);
        }

    }

    private String deEncryptUsername(String originalUsername) {
        return this.dCode(originalUsername.getBytes());
    }

    private String deEncryptPassword(String originalPassword) {
        return this.dCode(originalPassword.getBytes());
    }

    public String eCode(String needEncrypt) {
        byte[] result = (byte[])null;

        try {
            Cipher enCipher = Cipher.getInstance("DES");
            SecretKey key = Key.loadKey();
            enCipher.init(1, key);
            result = enCipher.doFinal(needEncrypt.getBytes());
            BASE64Encoder b = new BASE64Encoder();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            b.encode(result, bos);
            result = bos.toByteArray();
        } catch (Exception var7) {
            throw new IllegalStateException("System doesn't support DES algorithm.");
        }

        return new String(result);
    }

    public String dCode(byte[] result) {
        String s = "woshidaniu";

        try {
            Cipher deCipher = Cipher.getInstance("DES");
            deCipher.init(2, Key.loadKey());
            BASE64Decoder d = new BASE64Decoder();
            result = d.decodeBuffer(new String(result));
            byte[] strByte = deCipher.doFinal(result);
            s = new String(strByte);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return s;
    }

    public static void main(String[] args) {
        String s = "www==";
        DBEncrypt p = new DBEncrypt();
        String afterE = p.eCode(s);
        System.out.println(afterE);
        System.out.println(p.dCode("www==".getBytes()));
        System.out.println(p.eCode("dnsmp"));
        System.out.println(p.dCode("zs5qq/kS1Oelw=".getBytes()));
    }
}
