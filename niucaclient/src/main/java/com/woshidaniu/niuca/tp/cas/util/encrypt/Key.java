package com.woshidaniu.niuca.tp.cas.util.encrypt;


import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import sun.misc.BASE64Decoder;

public class Key {
    private static String encryptKey = "7EV/Zzutjzg=";

    public Key() {
    }

    public static SecretKey loadKey() throws Exception {
        BASE64Decoder d = new BASE64Decoder();
        byte[] b = d.decodeBuffer(encryptKey);
        DESKeySpec dks = new DESKeySpec(b);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(dks);
    }
}

