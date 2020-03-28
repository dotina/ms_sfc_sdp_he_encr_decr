package com.safaricom.microservice.he.mssdpheaderdecryptor.components;

import com.safaricom.microservice.he.mssdpheaderdecryptor.config.ConfigProperties;
import com.safaricom.microservice.he.mssdpheaderdecryptor.utils.LogsManager;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

import static com.safaricom.microservice.he.mssdpheaderdecryptor.utils.GlobalVariables.*;
import static com.safaricom.microservice.he.mssdpheaderdecryptor.utils.Utilities.parseToJsonString;

/**
 * @author Dickens
 */
@Component
public class CallBackRestUtil {
    private final ConfigProperties configProperties;
    private final RestTemplate restTemplate;

    @Autowired
    public CallBackRestUtil(ConfigProperties configProperties, RestTemplate restTemplate) {
        this.configProperties = configProperties;
        this.restTemplate = restTemplate;
    }

    /**
     * Make api call SDP response payload.
     *
     * @param apiCallUrl   the sdp api url
     * @param msisdn       the msisdn
     * @param requestPayload the request payload
     * @return the String response payload
     */
    public String makeAPICall(final String sourceSystem, String apiToken,String convId, final String referenceId, final String apiCallUrl, final String endPoint
            ,final String msisdn, Object requestPayload) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
        headers.add(HttpHeaders.ACCEPT_ENCODING, MediaType.APPLICATION_JSON_VALUE);
        headers.add(X_APP, sourceSystem);
        headers.add(convId, referenceId);
        headers.add(X_MSISDN, msisdn);
        headers.add(X_ROUTE_ID, configProperties.getRouteId());
        headers.add(X_SOURCE_IDENTITY_TOKEN, configProperties.getIdentityToken());
        headers.add(X_SOURCE_SYSTEM, sourceSystem);
        headers.add("X-Authorization", apiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("dev_user", "dev_password");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiCallUrl+endPoint.toLowerCase());

        String requestCallToJson = parseToJsonString(requestPayload);

        try {
            HttpEntity<?> entity = new HttpEntity<>(requestPayload, headers);

            HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
                    String.class);

            return response.getBody();


        } catch (RestClientResponseException exception) {
            LogsManager.error(referenceId, TRANSACTION_TYPE, "makeAPICall()", "", msisdn,
                    sourceSystem, TARGET_SYSTEM, RESPONSE_TIBCO_CALL_FAILED, TIMEOUT_ERROR_CODE, RESPONSE_SERVICE_UNREACHABLE,
                    ExceptionUtils.getStackTrace(exception), requestCallToJson, exception.getResponseBodyAsString());

//            errorMessage = exception.getMessage();
//            errorStatusDesc = exception.getStatusText();
            return String.valueOf(exception.getRawStatusCode());

        } catch (ResourceAccessException exception) {
            LogsManager.error(referenceId, TRANSACTION_TYPE, "makeAPICall()", "", msisdn,
                    sourceSystem, TARGET_SYSTEM, RESPONSE_TIBCO_CALL_FAILED, TIMEOUT_ERROR_CODE, RESPONSE_SERVICE_UNREACHABLE,
                    ExceptionUtils.getStackTrace(exception), requestCallToJson, exception.getLocalizedMessage());
            return null;
        }
    }
}
