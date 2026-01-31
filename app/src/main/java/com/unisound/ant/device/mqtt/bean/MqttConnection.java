package com.unisound.ant.device.mqtt.bean;

/* loaded from: classes.dex */
public class MqttConnection {
    private String ip;
    private int keepAlive;
    private String password;
    private int port;
    private String protocol;
    private String ssl;
    private String sslPassword;
    private String username;

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getKeepAlive() {
        return this.keepAlive;
    }

    public void setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSsl() {
        return this.ssl;
    }

    public void setSsl(String ssl) {
        this.ssl = ssl;
    }

    public String getSslPassword() {
        return this.sslPassword;
    }

    public void setSslPassword(String sslPassword) {
        this.sslPassword = sslPassword;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return "MqttConnection{ip='" + this.ip + "', keepAlive=" + this.keepAlive + ", password='" + this.password + "', port=" + this.port + ", protocol='" + this.protocol + "', ssl='" + this.ssl + "', sslPassword='" + this.sslPassword + "', username='" + this.username + "'}";
    }
}
