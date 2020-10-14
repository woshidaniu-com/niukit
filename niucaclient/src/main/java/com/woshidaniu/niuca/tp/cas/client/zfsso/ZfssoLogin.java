package com.woshidaniu.niuca.tp.cas.client.zfsso;


import com.woshidaniu.niuca.tp.cas.client.ZfssoBean;
import com.woshidaniu.niuca.tp.cas.client.ZfssoConfig;
import com.woshidaniu.niuca.tp.cas.client.ZfssoReadConfig;
import com.woshidaniu.niuca.tp.cas.util.Tool;
import com.woshidaniu.niuca.tp.cas.util.encrypt.MD5;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ZfssoLogin extends HttpServlet {
    private static final long serialVersionUID = -3373124479065506173L;
    private String casurl;
    private String casLogin;
    private String dbadminuser;
    private String dbadminpwd;
    private String ipaddress;
    private String setsessionclass;
    private String overtime;
    private String viewname;
    private Class<?> classType = null;
    private Object objectCopy = null;
    private Method setMethod = null;
    private Object result = null;

    public ZfssoLogin() {
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        String dbpagh = servletContext.getRealPath("/WEB-INF/zfssoconfig.properties");
        new ZfssoReadConfig(dbpagh);
        this.casurl = ZfssoConfig.casurl;
        this.setsessionclass = config.getInitParameter("setsessionclass").trim();
        this.dbadminuser = ZfssoConfig.dbadminuser;
        this.dbadminpwd = ZfssoConfig.dbadminpwd;
        this.ipaddress = ZfssoConfig.ipaddress;
        this.overtime = ZfssoConfig.overtime;
        this.casLogin = this.casurl + "/zfssoererr.jsp";
        this.viewname = ZfssoConfig.viewname;

        try {
            this.classType = Class.forName(this.setsessionclass);
            this.objectCopy = this.classType.newInstance();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setHeader("P3P", "CP=CAO PSA OUR");
        String userName = request.getParameter("userName");
        String strSysDatetime = request.getParameter("strSysDatetime");
        String verify = request.getParameter("verify");
        String userType = request.getParameter("jsName");
        String openType = request.getParameter("openType");
        String url = request.getParameter("url");
        openType = Tool.isNull(openType) ? "2" : openType;
        ZfssoBean zfssobean = new ZfssoBean();
        zfssobean.setRequest(request);
        zfssobean.setResponse(response);
        zfssobean.setSession(session);
        zfssobean.setYhm(userName);
        zfssobean.setYhlx(userType);
        if (!Tool.isNull(userName) && !Tool.isNull(strSysDatetime) && !Tool.isNull(userType) && !Tool.isNull(url)) {
            try {
                this.setMethod = this.classType.getMethod("chkUserSession", ZfssoBean.class);
                this.result = this.setMethod.invoke(this.objectCopy, zfssobean);
            } catch (Exception var18) {
                var18.printStackTrace();
            }

            Boolean ischk = (Boolean)this.result;
            if (!ischk) {
                MD5 md5 = new MD5();
                String sql = "select zfssokey,round(to_number(sysdate - to_date(?,'YYYY-MM-DDHH24:MI:SS'))*1440*60) SYSDATESTR from " + this.viewname + " where rownum<2";
                String[] tmps = this.getString(sql, strSysDatetime);
                if (tmps == null) {
                    response.sendRedirect(this.casLogin + "?errordm=21");
                    return;
                }

                if (Tool.isNull(tmps[0])) {
                    response.sendRedirect(this.casLogin + "?errordm=22");
                    return;
                }

                if (Integer.parseInt(tmps[1]) > Integer.parseInt(this.overtime)) {
                    response.sendRedirect(this.casLogin + "?errordm=23");
                    return;
                }

                String res = md5.getMD5ofStr(userName + tmps[0] + strSysDatetime + userType);
                if (!res.equalsIgnoreCase(verify)) {
                    response.sendRedirect(this.casLogin + "?errordm=24");
                    return;
                }

                try {
                    this.setMethod = this.classType.getMethod("setUserSession", ZfssoBean.class);
                    this.result = this.setMethod.invoke(this.objectCopy, zfssobean);
                } catch (Exception var17) {
                    var17.printStackTrace();
                }

                ischk = (Boolean)this.result;
                if (!ischk) {
                    if (session.getAttribute("errMsg") != null) {
                        String errMsg = (String)session.getAttribute("errMsg");
                        if (errMsg != null && !"".equals(errMsg)) {
                            errMsg = URLEncoder.encode(errMsg, "UTF-8");
                        }

                        response.sendRedirect(this.casLogin + "?errMsg=" + errMsg);
                    } else {
                        response.sendRedirect(this.casLogin + "?errordm=3");
                    }

                    return;
                }
            }

            url = URLDecoder.decode(url, "utf-8");
            System.out.println("业务系统跳转地址：" + url);
            response.sendRedirect(url);
        } else {
            response.sendRedirect(this.casLogin + "?errordm=1");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    private String[] getString(String sql, String time) {
        String[] tmp = (String[])null;
        Connection con = null;
        PreparedStatement pt = null;
        ResultSet rs = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            try {
                con = DriverManager.getConnection("jdbc:oracle:thin:@" + this.ipaddress, this.dbadminuser, this.dbadminpwd);
                pt = con.prepareStatement(sql);
                pt.setString(1, time);
                rs = pt.executeQuery();
                if (rs.next()) {
                    tmp = new String[]{rs.getString("zfssokey"), rs.getString("SYSDATESTR")};
                }

                rs.close();
                pt.close();
                con.close();
            } catch (SQLException var20) {
                var20.printStackTrace();
            }
        } catch (ClassNotFoundException var21) {
            var21.printStackTrace();

            try {
                if (rs != null) {
                    rs.close();
                }

                if (pt != null) {
                    pt.close();
                }

                if (con != null) {
                    con.close();
                }

                rs = null;
                pt = null;
                con = null;
            } catch (SQLException var19) {
                var19.printStackTrace();
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pt != null) {
                    pt.close();
                }

                if (con != null) {
                    con.close();
                }

                rs = null;
                pt = null;
                con = null;
            } catch (SQLException var18) {
                var18.printStackTrace();
            }

        }

        return tmp;
    }

    public void destroy() {
    }
}
