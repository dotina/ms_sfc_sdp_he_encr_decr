package com.safaricom.microservice.he.mssdpheaderdecryptor.utils;


import com.safaricom.microservice.he.mssdpheaderdecryptor.config.ConfigProperties;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.header.HeaderError;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.header.HeaderErrorMessage;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.safaricom.microservice.he.mssdpheaderdecryptor.utils.GlobalVariables.*;


@Service
public class Validations {

    private final ConfigProperties configProperties;
    private HeaderErrorMessage headerErrorMessage;

    @Autowired
    public Validations(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public HeaderErrorMessage validateHeaders(HttpHeaders httpHeaders) {
        this.headerErrorMessage = new HeaderErrorMessage();

        if (this.checkMissingHeaders(httpHeaders, X_SOURCE_SYSTEM)) {
            return headerErrorMessage;
        } else {
            if (httpHeaders.get(X_SOURCE_SYSTEM).get(0).equalsIgnoreCase("zuri")) {
                this.validateInternalCalls(httpHeaders);
                return headerErrorMessage;
            }
        }

        this.validateExternalCalls(httpHeaders);
        return headerErrorMessage;
    }

    private void validateExternalCalls(HttpHeaders headers) {
        String commonPattern = "^[A-Za-z0-9].*$";

        checkValidHeaderInput(headers, ACCEPT_LANGUAGE, configProperties.getAcceptedLanguages(), "Invalid Accept-Language");
        checkValidHeaderInput(headers, CONTENT_TYPE, "application/json", "Invalid Content Type");
//        checkValidHeaderPattern(headers, X_DEVICE_ID, commonPattern, "Invalid Device ID");
//        checkValidHeaderPattern(headers, X_DEVICE_TOKEN, commonPattern, "Invalid Device Token");
        checkValidHeaderPattern(headers, X_MESSAGE_ID, commonPattern, "Invalid Message ID");
//        checkValidHeaderPattern(headers, X_MSISDN, "^(254|0)?[71]\\d{8}$", "Invalid Header Msisdn");
        checkValidHeaderInput(headers, X_SOURCE_COUNTRY_CODE, configProperties.getSourceCountryCodes(), "Invalid Country Code");
        checkValidHeaderInput(headers, X_SOURCE_DIVISION, "DIT", "Invalid Source Division");
        checkValidHeaderInput(headers, X_SOURCE_OPERATOR, configProperties.getSourceOperators(), "Invalid Timestamp");
        checkValidHeaderPattern(headers, X_SOURCE_TIMESTAMP, commonPattern, "Invalid Timestamp");
        checkValidHeaderPattern(headers, X_VERSION, commonPattern, "Invalid X_Version");
//        checkValidHeaderInput(headers, X_API_TOKEN, commonPattern,"Invalid token");

        this.validateInternalCalls(headers);
    }

    private void validateInternalCalls(HttpHeaders headers) {
        String commonPattern = "^[A-Za-z0-9].*$";

        checkValidHeaderInput(headers, ACCEPT_ENCODING, "application/json", "Invalid Accept-Encoding");
        checkValidHeaderPattern(headers, X_APP, commonPattern, "Invalid X_App");
        checkValidHeaderPattern(headers, X_CORRELATION_CONVERSATION_ID, commonPattern, "Invalid Correlation ID");
        checkValidHeaderInput(headers, X_SOURCE_SYSTEM, configProperties.getAcceptedApps(), "Invalid Source System");
    }

    private boolean checkMissingHeaders(HttpHeaders httpHeaders, String header) {
        if (httpHeaders.containsKey(header)) {
            return false;
        }
        this.headerErrorMessage.setMissingHeaders(true);
        return true;
    }

    private void checkValidHeaderInput(HttpHeaders httpHeaders, String header, String allowedInput, String message) {
        if (!this.checkMissingHeaders(httpHeaders, header) &&
                !checkInputMatch(Objects.requireNonNull(httpHeaders.get(header)).get(0), Arrays.asList(allowedInput.split(",")))) {
            HeaderError headerError = new HeaderError();
            headerError.setHeader(header);
            headerError.setError(message);
            this.headerErrorMessage.getInvalidHeaderErrors().add(headerError);
        }
    }

    private void checkValidHeaderPattern(HttpHeaders httpHeaders, String header, String allowedPattern, String message) {
        if (!this.checkMissingHeaders(httpHeaders, header) &&
                !checkPatternMatch(Objects.requireNonNull(httpHeaders.get(header)).get(0), allowedPattern)) {
            HeaderError headerError = new HeaderError();
            headerError.setHeader(header);
            headerError.setError(message);
            headerErrorMessage.getInvalidHeaderErrors().add(headerError);
        }
    }

    private boolean checkInputMatch(String headerValue, List<String> allowedInput) {
        return allowedInput.stream().anyMatch(i -> i.equals(headerValue));
    }

    public boolean checkPatternMatch(String header, String pattern) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(header);
        return matcher.matches();
    }

    public void removeAuthorizationHeaders(HttpHeaders headers) {
        headers.remove(AUTHORIZATION);
        headers.remove("postman-token");
        headers.remove(X_MESSAGE_ID);
        headers.remove("X-DeviceInfo");
//        headers.remove(X_API_TOKEN);
//        headers.remove(X_DEVICE_ID);
//        headers.remove(X_DEVICE_TOKEN);
        headers.remove("password");
        headers.remove("accept-charset");
    }

    public String getMsisdnSubString(String msisdn) {
        return msisdn.substring(msisdn.length() - 9);
    }

    /**
     *
     * @param url the url to be validated
     * @return returns a boolean true or false
     */
    public Boolean urlValidator(String url){
        // Get a url to validate using default schemas
        UrlValidator urlValidator = UrlValidator.getInstance();
        return urlValidator.isValid(url);

    }
}
