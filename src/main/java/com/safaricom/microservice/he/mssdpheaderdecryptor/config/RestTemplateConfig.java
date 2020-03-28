package com.safaricom.microservice.he.mssdpheaderdecryptor.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Configuration
public class RestTemplateConfig {
    private final ConfigProperties configProperties;

    @Autowired
    public RestTemplateConfig(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    /**
     * @param builder Provides convenience methods to register converters, error handlers and UriTemplateHandlers
     * @return RestTemplate instance for the synchronous calls by the client to access a REST web-service.
     * @throws KeyStoreException        handle ssl Exceptions
     * @throws NoSuchAlgorithmException handle ssl Exceptions
     * @throws KeyManagementException   handle ssl Exceptions
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) throws KeyStoreException, NoSuchAlgorithmException,
            KeyManagementException {

        // Do any additional configuration here
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(this.configProperties.getConnectionTimeout());
        requestFactory.setReadTimeout(this.configProperties.getReadTimeout());
        requestFactory.setHttpClient(httpClient);

        return new RestTemplate(requestFactory);
    }
}
