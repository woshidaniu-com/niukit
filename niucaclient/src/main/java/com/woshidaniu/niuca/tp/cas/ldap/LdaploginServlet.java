package com.woshidaniu.niuca.tp.cas.ldap;


import com.woshidaniu.niuca.tp.cas.client.ZfssoBean;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LdaploginServlet extends HttpServlet {
    private static final long serialVersionUID = 9100400085590710137L;
    private String setsessionclass;
    private String loginpage;
    private Class classType = null;
    private Object objectCopy = null;
    private Method setMethod = null;
    private Object result = null;

    public LdaploginServlet() {
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        String dbpagh = servletContext.getRealPath("/WEB-INF/LdapSso.properties");
        new LdapConfigRead(dbpagh);
        this.setsessionclass = config.getInitParameter("setsessionclass").trim();
        this.loginpage = config.getInitParameter("loginpage").trim();

        try {
            this.classType = Class.forName(this.setsessionclass);
            this.objectCopy = this.classType.newInstance();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String yhlx = request.getParameter("yhlx");
        String service = request.getParameter("service");
        ZfssoBean zfssobean = new ZfssoBean();
        zfssobean.setRequest(request);
        zfssobean.setResponse(response);
        zfssobean.setSession(session);
        zfssobean.setYhm(username);
        zfssobean.setYhlx(yhlx);

        try {
            this.setMethod = this.classType.getMethod("chkUserSession", ZfssoBean.class);
            this.result = this.setMethod.invoke(this.objectCopy, zfssobean);
        } catch (Exception var12) {
            var12.printStackTrace();
        }

        if (Boolean.valueOf(this.result.toString())) {
            response.sendRedirect(service);
        } else {
            UserDao userdao = new UserDao();
            if (userdao.CheckUser(username, password)) {
                try {
                    this.setMethod = this.classType.getMethod("setUserSession", ZfssoBean.class);
                    this.result = this.setMethod.invoke(this.objectCopy, zfssobean);
                } catch (Exception var11) {
                    var11.printStackTrace();
                }

                if (Boolean.valueOf(this.result.toString())) {
                    response.sendRedirect(service);
                    return;
                }

                request.setAttribute("error", "业务系统用户不存在！");
            } else {
                request.setAttribute("error", "用户名或密码错误！");
            }

            request.getRequestDispatcher(this.loginpage).forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    public void destroy() {
    }
}
