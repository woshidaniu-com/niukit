package com.woshidaniu.javamail;

import java.io.Serializable;

public class MailConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    private String            hostName;              // mail.smtp.host
    private String            userName;
    private String            password;
    private String            sendFrom;
    private String            needAuth;
    private String            onOff;
    private String               smtpPort;
    private String               factoryPort;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNeedAuth() {
        return needAuth;
    }

    public void setNeedAuth(String needAuth) {
        this.needAuth = needAuth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public String getOnOff() {
        return onOff;
    }

    public void setOnOff(String onOff) {
        this.onOff = onOff;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getFactoryPort() {
        return factoryPort;
    }

    public void setFactoryPort(String factoryPort) {
        this.factoryPort = factoryPort;
    }

}
