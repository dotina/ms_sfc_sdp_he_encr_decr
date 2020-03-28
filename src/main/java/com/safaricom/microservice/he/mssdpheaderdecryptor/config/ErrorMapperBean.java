package com.safaricom.microservice.he.mssdpheaderdecryptor.config;

import com.safaricom.microservices.libs.encryption.AESUtility;
import com.safaricom.util.errors.Mapper;
import com.safaricom.util.errors.model.ErrorDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Configuration
public class ErrorMapperBean extends AESUtility {

    private final ConfigProperties configProperties;
    private final BuildProperties buildProperties;

    /**
     * Instantiates a new Error mapper bean.
     *
     * @param configProperties the config properties
     * @param buildProperties  the build properties
     */
    @Autowired
    public ErrorMapperBean(ConfigProperties configProperties, BuildProperties buildProperties) {
        this.configProperties = configProperties;
        this.buildProperties = buildProperties;
    }

    /**
     * Gets redis password.
     *
     * @return the redis password
     */
    private String getRedisPassword() {
        return decryptPayload(configProperties.getAppProfile(), buildProperties.getVersion(),
                configProperties.getStaticIv(), configProperties.getRedisPassword());
    }

    /**
     * Get service responses map.
     *
     * @return the map
     */
    @Bean
    @RefreshScope
    public Map<String, ErrorDetails> getServiceResponses() {
        String appName = configProperties.getServiceName();
        Mapper mapper = new Mapper(configProperties.getRedisIp(), configProperties.getRedisPort(), getRedisPassword());
        List<ErrorDetails> errorDetailsList = mapper.getAll(appName, UUID.randomUUID().toString());
        Map<String, ErrorDetails> mappedErrors = new HashMap<>();
        errorDetailsList.forEach(errorDetail -> mappedErrors.put(errorDetail.getErrorCode(), errorDetail));
        return mappedErrors;
    }

}
