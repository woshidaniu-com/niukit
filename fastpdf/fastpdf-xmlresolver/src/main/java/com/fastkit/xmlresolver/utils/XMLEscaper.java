package com.fastkit.xmlresolver.utils;


public class XMLEscaper  {

    public static String convert(Object value) {
	    return null==value ? "" : value.toString().
	            replace("&", "&amp;").
	            replace("<", "&lt;").
	            replace(">","&gt;").
	            replace("'","&apos;").
	            replace("\"", "&quot;");
    }

    public static String revert(Object value) {
        return null==value ? "" : value.toString().
            replace("&lt;", "<").
            replace("&gt;", ">").
            replace("&apos;", "'").
            replace("&quot;", "\"").
            replace("&amp;", "&");
    }
    
}
