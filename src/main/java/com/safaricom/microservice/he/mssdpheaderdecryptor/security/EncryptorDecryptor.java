package com.safaricom.microservice.he.mssdpheaderdecryptor.security;


import com.safaricom.microservices.libs.encryption.AESUtility;

public class EncryptorDecryptor extends AESUtility {

    private EncryptorDecryptor() {
        // Create a constructor
    }

    public static String decryptor(String appProfile, String appVersion,
                                   String staticIv, String encryptedText) {

        EncryptorDecryptor encryptorDecryptor = new EncryptorDecryptor();

        return encryptorDecryptor.decryptPayload(appProfile, appVersion,
                staticIv, encryptedText);
    }

    public static String encryptor(String appProfile, String appVersion,
                                   String staticIv, String encryptedText) {

        EncryptorDecryptor encryptorDecryptor = new EncryptorDecryptor();

        return encryptorDecryptor.encryptPayload(appProfile, appVersion, staticIv, encryptedText);
    }
}
