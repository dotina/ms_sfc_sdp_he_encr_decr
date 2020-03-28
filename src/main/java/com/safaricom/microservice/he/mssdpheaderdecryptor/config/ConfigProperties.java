package com.safaricom.microservice.he.mssdpheaderdecryptor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class ConfigProperties {

    @Value("${spring.profiles.active}")
    private String appProfile;

    private String appVersion;
    private String staticIv;
    private String apigeeUserCredentials;
    private String devUserCredentials;
    private String prodUserCredentials;
    private String testUserCredentials;
    private String dbUserCredentials;
    private String serviceName;
    private String redisPassword;
    private String redisIp;
    private int redisPort;
    private String sdpUrl;
    private String dbUrl;
    private String smsUrl;
    private String routeId;
    private String identityToken;
    private String acceptedLanguages;
    private String sourceOperators;
    private String sourceCountryCodes;
    private String acceptedApps;
    private int connectionTimeout;
    private int readTimeout;
    private int dbMaxPool;
    private int dbMinIdle;
    private int dbMaxLife;
    private int dbConnTimeout;
    private String dbConnTestQuery;
    private int dbIdleTimeout;
    private boolean dbAutoCommit;

    @Autowired
    ConfigProperties(BuildProperties buildProperties){
        this.appVersion = buildProperties.getVersion();
    }

    public String getAppProfile() {
        return appProfile;
    }

    public void setAppProfile(String appProfile) {
        this.appProfile = appProfile;
    }

    public String getStaticIv() {
        return staticIv;
    }

    public void setStaticIv(String staticIv) {
        this.staticIv = staticIv;
    }

    public String getApigeeUserCredentials() {
        return apigeeUserCredentials;
    }

    public void setApigeeUserCredentials(String apigeeUserCredentials) {
        this.apigeeUserCredentials = apigeeUserCredentials;
    }

    public String getDevUserCredentials() {
        return devUserCredentials;
    }

    public void setDevUserCredentials(String devUserCredentials) {
        this.devUserCredentials = devUserCredentials;
    }

    public String getProdUserCredentials() {
        return prodUserCredentials;
    }

    public void setProdUserCredentials(String prodUserCredentials) {
        this.prodUserCredentials = prodUserCredentials;
    }

    public String getTestUserCredentials() {
        return testUserCredentials;
    }

    public void setTestUserCredentials(String testUserCredentials) {
        this.testUserCredentials = testUserCredentials;
    }

    public String getDbUserCredentials() {
        return dbUserCredentials;
    }

    public void setDbUserCredentials(String dbUserCredentials) {
        this.dbUserCredentials = dbUserCredentials;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public String getRedisIp() {
        return redisIp;
    }

    public void setRedisIp(String redisIp) {
        this.redisIp = redisIp;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getSdpUrl() {
        return sdpUrl;
    }

    public void setSdpUrl(String sdpUrl) {
        this.sdpUrl = sdpUrl;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getSmsUrl() { return smsUrl; }

    public void setSmsUrl(String smsUrl) { this.smsUrl = smsUrl; }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getIdentityToken() {
        return identityToken;
    }

    public void setIdentityToken(String identityToken) {
        this.identityToken = identityToken;
    }

    public String getAcceptedLanguages() {
        return acceptedLanguages;
    }

    public void setAcceptedLanguages(String acceptedLanguages) {
        this.acceptedLanguages = acceptedLanguages;
    }

    public String getSourceOperators() {
        return sourceOperators;
    }

    public void setSourceOperators(String sourceOperators) {
        this.sourceOperators = sourceOperators;
    }

    public String getSourceCountryCodes() {
        return sourceCountryCodes;
    }

    public void setSourceCountryCodes(String sourceCountryCodes) {
        this.sourceCountryCodes = sourceCountryCodes;
    }

    public String getAcceptedApps() {
        return acceptedApps;
    }

    public void setAcceptedApps(String acceptedApps) {
        this.acceptedApps = acceptedApps;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getDbMaxPool() {
        return dbMaxPool;
    }

    public void setDbMaxPool(int dbMaxPool) {
        this.dbMaxPool = dbMaxPool;
    }

    public int getDbMinIdle() {
        return dbMinIdle;
    }

    public void setDbMinIdle(int dbMinIdle) {
        this.dbMinIdle = dbMinIdle;
    }

    public int getDbMaxLife() {
        return dbMaxLife;
    }

    public void setDbMaxLife(int dbMaxLife) {
        this.dbMaxLife = dbMaxLife;
    }

    public int getDbConnTimeout() {
        return dbConnTimeout;
    }

    public void setDbConnTimeout(int dbConnTimeout) {
        this.dbConnTimeout = dbConnTimeout;
    }

    public String getDbConnTestQuery() {
        return dbConnTestQuery;
    }

    public void setDbConnTestQuery(String dbConnTestQuery) {
        this.dbConnTestQuery = dbConnTestQuery;
    }

    public int getDbIdleTimeout() {
        return dbIdleTimeout;
    }

    public void setDbIdleTimeout(int dbIdleTimeout) {
        this.dbIdleTimeout = dbIdleTimeout;
    }

    public boolean isDbAutoCommit() {
        return dbAutoCommit;
    }

    public void setDbAutoCommit(boolean dbAutoCommit) {
        this.dbAutoCommit = dbAutoCommit;
    }
}