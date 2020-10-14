package com.woshidaniu.niuca.tp.cas.test;


import com.woshidaniu.niuca.tp.cas.client.ZfssoConfig;
import com.woshidaniu.niuca.tp.cas.util.Tool;
import com.woshidaniu.niuca.tp.cas.util.encrypt.MD5;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {
    private static final long serialVersionUID = 7730805226272510737L;
    private String dbadminuser;
    private String dbadminpwd;
    private String ipaddress;
    private String viewname;

    public TestServlet() {
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.dbadminuser = ZfssoConfig.dbadminuser;
        this.dbadminpwd = ZfssoConfig.dbadminpwd;
        this.ipaddress = ZfssoConfig.ipaddress;
        this.viewname = ZfssoConfig.viewname;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ywxtdz = request.getParameter("ywxtdz");
        String ywxtdlm = request.getParameter("ywxtdlm");
        String jsName = request.getParameter("jsName");
        String ywxturl = request.getParameter("ywxturl");
        MD5 md5 = new MD5();
        String sql = "select zfssokey,to_char(sysdate,'YYYY-MM-DDHH24:MI:SS') SYSDATESTR from " + this.viewname + " where rownum<2";
        String[] tmps = this.getString(sql);
        if (tmps == null) {
            request.setAttribute("error", "数据查询失败！");
            request.getRequestDispatcher("ywxtlogintest.jsp").forward(request, response);
        } else if (Tool.isNull(tmps[0])) {
            request.setAttribute("error", "key不能为空！");
            request.getRequestDispatcher("ywxtlogintest.jsp").forward(request, response);
        }

        String jmcs = URLEncoder.encode(ywxtdlm, "utf-8") + tmps[0] + tmps[1] + jsName;
        String strMd5 = md5.getMD5ofStr(jmcs);
        String url = ywxtdz + "?verify=" + strMd5 + "&userName=" + this.toUtf8String(ywxtdlm) + "&strSysDatetime=" + tmps[1] + "&jsName=" + jsName;
        if (!Tool.isNull(ywxturl)) {
            url = url + "&url=" + URLEncoder.encode(ywxturl, "utf-8");
        }

        System.out.println(url);
        response.sendRedirect(url);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    public String toUtf8String(String src) {
        byte[] b = src.getBytes();
        char[] c = new char[b.length];

        for(int i = 0; i < b.length; ++i) {
            c[i] = (char)(b[i] & 255);
        }

        return new String(c);
    }

    private String[] getString(String sql) {
        String[] tmp = null;
        Connection con = null;
        PreparedStatement pt = null;
        ResultSet rs = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            try {
                con = DriverManager.getConnection("jdbc:oracle:thin:@" + this.ipaddress, this.dbadminuser, this.dbadminpwd);
                pt = con.prepareStatement(sql);
                rs = pt.executeQuery();
                if (rs.next()) {
                    tmp = new String[]{rs.getString("zfssokey"), rs.getString("SYSDATESTR")};
                }

                rs.close();
                pt.close();
                con.close();
            } catch (SQLException var16) {
                var16.printStackTrace();
            }
        } catch (ClassNotFoundException var17) {
            var17.printStackTrace();
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
            } catch (SQLException var15) {
                var15.printStackTrace();
            }

        }

        return tmp;
    }
}
