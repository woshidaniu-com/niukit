package com.woshidaniu.safety.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;


public class AntiSamyEscapeUtils {

	public static String escapeHtml(String str) {
		return escapeHtmlString(str, true, true);
	}

	public static void escapeHtml(Writer out, String str) throws IOException {
		escapeHtmlString(out, str, true, true);
	}

    private static String escapeHtmlString(String str, boolean escapeSingleQuotes, boolean escapeForwardSlash) {
        if (str == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter(str.length() * 2);
            escapeHtmlString(writer, str, escapeSingleQuotes, escapeForwardSlash);
            return writer.toString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            throw new RuntimeException(ioe);
        }
    }

    private static void escapeHtmlString(Writer out, String str, boolean escapeSingleQuote, boolean escapeForwardSlash) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null) {
            return;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            String entityName = Entities.HTML40.entityName(ch);
            if (entityName == null) {
	             if (ch < 32) {
	                switch (ch) {
	                    case '\b' :
	                        out.write('\\');
	                        out.write('b');
	                        break;
	                    case '\n' :
	                        out.write('\\');
	                        out.write('n');
	                        break;
	                    case '\t' :
	                        out.write('\\');
	                        out.write('t');
	                        break;
	                    case '\f' :
	                        out.write('\\');
	                        out.write('f');
	                        break;
	                    case '\r' :
	                        out.write('\\');
	                        out.write('r');
	                        break;
	                    default :
	                    	out.write(ch);
	                        break;
	                }
	            } else {
	                switch (ch) {
	                    case '\'' :
	                        if (escapeSingleQuote) {
	                            out.write('\\');
	                        }
	                        out.write('\'');
	                        break;
	                    case '"' :
	                        out.write('\\');
	                        out.write('"');
	                        break;
	                    case '\\' :
	                        out.write('\\');
	                        out.write('\\');
	                        break;
	                    case '/' :
	                        if (escapeForwardSlash) {
	                            out.write('\\');
	                        }
	                        out.write('/');
	                        break;
	                    case ' ' :
	                        out.write("&nbsp;");
	                        break;
	                    default :
	                        out.write(ch);
	                        break;
	                }
	            }
	             
            } else {
            	out.write('&');
                out.write(entityName);
                out.write(';');
            }
        }
    }
    
    public static String unescapeHtml(String str) {
        if (str == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter(str.length());
            unescapeHtml(writer, str);
            return writer.toString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            throw new RuntimeException(ioe);
        }
    }

    public static void unescapeHtml(Writer out, String str) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null) {
            return;
        }
        
        int sz = str.length();
        boolean hadSlash = false;
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            if (ch == '&') {
                int nextIdx = i + 1;
                int semiColonIdx = str.indexOf(';', nextIdx);
                if (semiColonIdx == -1) {
                	out.write(ch);
                    continue;
                }
                int amphersandIdx = str.indexOf('&', i + 1);
                if (amphersandIdx != -1 && amphersandIdx < semiColonIdx) {
                    // Then the text looks like &...&...;
                	out.write(ch);
                    continue;
                }
                String entityContent = str.substring(nextIdx, semiColonIdx);
                int entityValue = -1;
                int entityContentLen = entityContent.length();
                if (entityContentLen > 0) {
                	// escaped value content is an entity name
                    entityValue = Entities.HTML40.entityValue(entityContent);
                }
                if (entityValue == -1) {
                	out.write('&');
                    out.write(entityContent);
                    out.write(';');
                } else {
                	out.write(entityValue);
                }
                i = semiColonIdx; // move index up to the semi-colon
            } else {
            	if (hadSlash) {
                    // handle an escaped value
                    hadSlash = false;
                    switch (ch) {
                        case '\\':
                            out.write('\\');
                            break;
                        case '\'':
                            out.write('\'');
                            break;
                        case '\"':
                            out.write('"');
                            break;
                        case 'r':
                            out.write('\r');
                            break;
                        case 'f':
                            out.write('\f');
                            break;
                        case 't':
                            out.write('\t');
                            break;
                        case 'n':
                            out.write('\n');
                            break;
                        case 'b':
                            out.write('\b');
                            break;
                        default :
                            out.write(ch);
                            break;
                    }
                    continue;
                } else if (ch == '\\') {
                    hadSlash = true;
                    continue;
                }
                out.write(ch);
            }
        }
        if (hadSlash) {
            // then we're in the weird case of a \ at the end of the
            // string, let's output it anyway.
            out.write('\\');
        }
    }

}
