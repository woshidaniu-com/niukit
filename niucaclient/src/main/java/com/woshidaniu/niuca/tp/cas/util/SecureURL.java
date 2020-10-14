package com.woshidaniu.niuca.tp.cas.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SecureURL {
    public SecureURL() {
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
        System.out.println(retrieve(args[0]));
    }

    public static String retrieve(String url) throws IOException {
        BufferedReader r = null;

        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            uc.setRequestProperty("Connection", "close");
            r = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            StringBuffer buf = new StringBuffer();

            String line;
            while((line = r.readLine()) != null) {
                buf.append(line + "\n");
            }

            String var7 = buf.toString();
            return var7;
        } finally {
            try {
                if (r != null) {
                    r.close();
                }
            } catch (IOException var12) {
            }

        }
    }
}
