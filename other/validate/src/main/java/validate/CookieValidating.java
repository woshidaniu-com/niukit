//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package validate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieValidating implements CookieValidate {
    public CookieValidating() {
    }

    private String setValidateData(HttpServletRequest request, HttpServletResponse response, String token) {
        String validateDate = request.getRemoteAddr();
        validateDate = validateDate + request.getHeader("User-Agent");
        if (request.getHeader("UA-OS") != null) {
            validateDate = validateDate + request.getHeader("UA-OS");
        }

        if (request.getHeader("UA-CPU") != null) {
            validateDate = validateDate + request.getHeader("UA-CPU");
        }

        String status = "0";
        if (validateDate != null) {
            HttpURLConnection httpConn = null;

            try {
                URL localURL = new URL("http://zuinfo.zju.edu.cn:8080/AMWebService/Validate");
                URLConnection conn = localURL.openConnection();
                httpConn = (HttpURLConnection)conn;
                httpConn.setRequestMethod("POST");
                httpConn.addRequestProperty("validateDate", validateDate);
                httpConn.addRequestProperty("iPlanetDirectoryPro", token);
                httpConn.setDoOutput(true);
                httpConn.connect();
                status = httpConn.getHeaderField("status");
            } catch (MalformedURLException var13) {
                var13.printStackTrace();
            } catch (IOException var14) {
                var14.printStackTrace();
            } finally {
                if (httpConn != null) {
                    httpConn.disconnect();
                }

            }
        }

        return status;
    }

    public String validate(HttpServletRequest request, HttpServletResponse response, String token) {
        String status = "0";
        String validateDate = request.getRemoteAddr();
        validateDate = validateDate + request.getHeader("User-Agent");
        if (request.getHeader("UA-OS") != null) {
            validateDate = validateDate + request.getHeader("UA-OS");
        }

        if (request.getHeader("UA-CPU") != null) {
            validateDate = validateDate + request.getHeader("UA-CPU");
        }

        if (validateDate != null) {
            HttpURLConnection httpConn = null;

            try {
                URL url = new URL("http://zuinfo.zju.edu.cn:8080/AMWebService/Validate");
                URLConnection conn = url.openConnection();
                httpConn = (HttpURLConnection)conn;
                httpConn.setRequestMethod("GET");
                httpConn.addRequestProperty("iPlanetDirectoryPro", token);
                httpConn.setDoOutput(true);
                httpConn.connect();
                String validatedata = httpConn.getHeaderField("validatedata");
                validatedata = validatedata.replaceAll("null", "");
                if (validatedata != null && validatedata.length() > 0) {
                    if (validatedata.equalsIgnoreCase(validateDate)) {
                        status = "1";
                    }
                } else {
                    status = this.setValidateData(request, response, token);
                }
            } catch (MalformedURLException var14) {
                var14.printStackTrace();
            } catch (IOException var15) {
                var15.printStackTrace();
            } finally {
                if (httpConn != null) {
                    httpConn.disconnect();
                }

            }
        }

        return status;
    }

    public void deleteValidateData(String token) {
        HttpURLConnection httpConn = null;

        try {
            URL url = new URL("http://zuinfo.zju.edu.cn:8080/AMWebService/Validate");
            URLConnection conn = url.openConnection();
            httpConn = (HttpURLConnection)conn;
            httpConn.setRequestMethod("DELETE");
            httpConn.addRequestProperty("iPlanetDirectoryPro", token);
            httpConn.setDoOutput(true);
            httpConn.connect();
        } catch (MalformedURLException var9) {
            var9.printStackTrace();
        } catch (IOException var10) {
            var10.printStackTrace();
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }

        }

    }
}
