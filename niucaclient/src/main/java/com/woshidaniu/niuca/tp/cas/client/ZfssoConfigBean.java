package com.woshidaniu.niuca.tp.cas.client;


public class ZfssoConfigBean {
    private String casurl;
    private String usezfca;
    private String ywxtservername;
    private String viewname;
    private String dbadminuser;
    private String dbadminpwd;
    private String ipaddress;
    private String overtime;

    public ZfssoConfigBean() {
    }

    public String getCasurl() {
        return this.casurl;
    }

    public void setCasurl(String casurl) {
        this.casurl = casurl;
    }

    public String getUsezfca() {
        return this.usezfca;
    }

    public void setUsezfca(String usezfca) {
        this.usezfca = usezfca;
    }

    public String getYwxtservername() {
        return this.ywxtservername;
    }

    public void setYwxtservername(String ywxtservername) {
        this.ywxtservername = ywxtservername;
    }

    public String getViewname() {
        return this.viewname;
    }

    public void setViewname(String viewname) {
        this.viewname = viewname;
    }

    public String getDbadminuser() {
        return this.dbadminuser;
    }

    public void setDbadminuser(String dbadminuser) {
        this.dbadminuser = dbadminuser;
    }

    public String getDbadminpwd() {
        return this.dbadminpwd;
    }

    public void setDbadminpwd(String dbadminpwd) {
        this.dbadminpwd = dbadminpwd;
    }

    public String getIpaddress() {
        return this.ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getOvertime() {
        return this.overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }
}
