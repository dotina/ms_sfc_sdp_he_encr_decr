package com.safaricom.microservice.he.mssdpheaderdecryptor.components;


import com.safaricom.microservice.he.mssdpheaderdecryptor.config.ConfigProperties;
import com.safaricom.microservice.he.mssdpheaderdecryptor.security.EncryptorDecryptor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static com.safaricom.microservice.he.mssdpheaderdecryptor.utils.GlobalVariables.*;


@Component
public class CredentialsManager {

    private final BuildProperties buildProperties;
    private final ConfigProperties configProperties;

    @Autowired
    public CredentialsManager(ConfigProperties configProperties, BuildProperties buildProperties) {
        this.configProperties = configProperties;
        this.buildProperties = buildProperties;
    }

    private String getProdUsername() {
        String username = configProperties.getProdUserCredentials().split(",")[0];
        return decryptedText(username);
    }

    private String getProdPassword() {
        String pw = configProperties.getProdUserCredentials().split(",")[1];
        return decryptedText(pw);
    }

    private String getDevUsername() {
        String username = configProperties.getDevUserCredentials().split(",")[0];
        return decryptedText(username);
    }

    private String getDevPassword() {
        String pw = configProperties.getDevUserCredentials().split(",")[1];
        return decryptedText(pw);
    }

    public String getdbUsername() {
        String username = configProperties.getDbUserCredentials().split(",")[0];
        return decryptedText(username);
    }

    public String getdbPassword() {
        String pw = configProperties.getDbUserCredentials().split(",")[1];
        return decryptedText(pw);
    }

    private String getTestUsername() {
        String username = configProperties.getTestUserCredentials().split(",")[0];
        return decryptedText(username);
    }

    private String getTestPassword() {
        String pw = configProperties.getTestUserCredentials().split(",")[1];
        return decryptedText(pw);
    }


    // helper for decrypting the text using the secret key
    private String decryptedText(String text) {
        return EncryptorDecryptor.decryptor(configProperties.getAppProfile(), buildProperties.getVersion(),
                configProperties.getStaticIv(), text);
    }

    public String getCurrentProfileCred(String appProfile, String credType) {
        String username = "username";
        switch (appProfile) {
            case PROFILE_DEVELOPMENT:
                return credType.equalsIgnoreCase(username) ? getDevUsername() : getDevPassword();

            case PROFILE_PRODUCTION:
                return credType.equalsIgnoreCase(username) ? getProdUsername() : getProdPassword();

            default:
                return credType.equalsIgnoreCase(username) ? getTestUsername() : getTestPassword();
        }
    }

    /**
     * Gets current profile credentials used for basic authorization
     *
     * @return credentials used for basic authorization
     */
    public String getCredentials() {
        String plainCred = getCurrentProfileCred(configProperties.getAppProfile(), "username")
                .concat(":")
                .concat(getCurrentProfileCred(configProperties.getAppProfile(), "password"));

        byte[] plainCredsBytes = plainCred.getBytes(StandardCharsets.UTF_8);
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        return new String(base64CredsBytes, StandardCharsets.UTF_8);
    }
}
