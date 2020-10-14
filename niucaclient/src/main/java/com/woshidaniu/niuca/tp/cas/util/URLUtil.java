package com.woshidaniu.niuca.tp.cas.util;


import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class URLUtil {
    public URLUtil() {
    }

    public static void main(String[] args) {
        try {
            Map kvMap = splitQuery("http://10.71.32.36:81/portal.do?caUserName=201&ticket=ST-376-3lTKkyirLDnkGfhwx6Tw-niuca");
            System.out.println(kvMap.get("caUserName"));
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
        } catch (MalformedURLException var3) {
            var3.printStackTrace();
        }

    }

    public static Map splitQuery(URL url) throws UnsupportedEncodingException {
        Map query_pairs = new LinkedHashMap();
        String query = url.getQuery();
        String[] pairs = query.split("&");

        for(int i = 0; i < pairs.length; ++i) {
            String pair = pairs[i];
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }

        return query_pairs;
    }

    public static Map splitQuery(String url0) throws UnsupportedEncodingException, MalformedURLException {
        Map query_pairs = new LinkedHashMap();
        if (url0 != null && !"".equals(url0)) {
            URL url = new URL(url0);
            String query = url.getQuery();
            if (query != null && !"".equals(query)) {
                String[] pairs = query.split("&");
                if (pairs != null && pairs.length > 0) {
                    for(int i = 0; i < pairs.length; ++i) {
                        String pair = pairs[i];
                        if (pair != null && !"".equals(pair)) {
                            int idx = pair.indexOf("=");
                            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
                        }
                    }
                }
            }
        }

        return query_pairs;
    }

    public static Map splitQueryString(String query) {
        Map query_pairs = new LinkedHashMap();
        String[] pairs = query.split("&");

        for(int i = 0; i < pairs.length; ++i) {
            String pair = pairs[i];
            int idx = pair.indexOf("=");
            query_pairs.put(pair.substring(0, idx), pair.substring(idx + 1));
        }

        return query_pairs;
    }
}
