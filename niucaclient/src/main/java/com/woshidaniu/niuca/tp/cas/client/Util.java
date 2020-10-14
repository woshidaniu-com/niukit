package com.woshidaniu.niuca.tp.cas.client;


import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class Util {
    public Util() {
    }

    public static String getService(HttpServletRequest request, String server) throws ServletException {
        if (server == null) {
            throw new IllegalArgumentException("name of server is required");
        } else {
            StringBuffer sb = new StringBuffer();
            if (request.isSecure()) {
                sb.append("https://");
            } else {
                sb.append("http://");
            }

            sb.append(server);
            sb.append(request.getRequestURI());
            if (request.getQueryString() != null && !"".equals(request.getQueryString())) {
                int ticketLoc = request.getQueryString().indexOf("ticket=");
                if (ticketLoc == -1) {
                    sb.append("?" + request.getQueryString());
                } else if (ticketLoc > 0) {
                    ticketLoc = request.getQueryString().indexOf("&ticket=");
                    if (ticketLoc == -1) {
                        sb.append("?" + request.getQueryString());
                    } else if (ticketLoc > 0) {
                        sb.append("?" + request.getQueryString().substring(0, ticketLoc));
                    }
                }
            }

            return URLEncoder.encode(sb.toString());
        }
    }
}
