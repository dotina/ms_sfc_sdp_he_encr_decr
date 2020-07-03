package com.safaricom.microservice.he.mssdpheaderdecryptor.utils;

public class GlobalVariables {

    /*================================================
     * REQUEST HEADERS
     * ==============================================*/
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String X_APP = "X-App";
    public static final String X_CORRELATION_CONVERSATION_ID = "X-Correlation-Conversation-ID";
    public static final String X_DEVICE_ID = "X-DeviceId";
    public static final String X_DEVICE_TOKEN = "X-DeviceToken";
    public static final String X_MESSAGE_ID = "X-MessageID";
    public static final String X_MSISDN = "X-Msisdn";
    public static final String X_ROUTE_ID = "X-Route-Id";
    public static final String X_SOURCE_DIVISION = "X-Source-Division";
    public static final String X_SOURCE_COUNTRY_CODE = "X-Source-CountryCode";
    public static final String X_SOURCE_IDENTITY_TOKEN = "X-Source-Identity-Token";
    public static final String X_SOURCE_OPERATOR = "X-Source-Operator";
    public static final String X_SOURCE_SYSTEM = "X-Source-System";
    public static final String X_SOURCE_TIMESTAMP = "X-Source-Timestamp";
    public static final String X_VERSION = "X-Version";
    public static final String X_API_TOKEN = "X-api-auth-token";
    public static final String X_XSS_PROTECTION = "X-XSS-Protection";
    public static final String X_FRAME_OPTIONS = "X-Frame-Options";
    public static final String X_CONTENT_TYPE_OPTION = "X-Content-Type-Options";

    /*================================================
     * APPLICATION PROFILES
     * ==============================================*/
    public static final String PROFILE_TESTING = "testing";
    public static final String PROFILE_PRODUCTION = "production";
    public static final String PROFILE_DEVELOPMENT = "development";

    /*================================================
     * CODING FORMATS
     * ==============================================*/
    public static final String NUMBER_PREFIX_KE = "254";
    public static final String RESPONSE_DATE_TIME_FORMAT = "yyyy-MM-dd'T'hh:mm:ssZ";
    public static final String REQUEST_DATE_TIME_FORMAT = "yyyyMMddHHmmss";
    public static final String LOGGER_FORMAT = "TransactionID={} | TransactionType={} | Process={} | ProcessDuration={} | Msisdn={} | SourceSystem={} | TargetSystem={}  | Response={} | ResponseCode={}  | ResponseMsg={} | ErrorDescription={} | RequestPayload={} | ResponsePayload={}";

    /*================================================
     * SYSTEM INTERACTIONS
     * ==============================================*/
    public static final String TARGET_SYSTEM = "API_CALL_SDP";
    public static final String TRANSACTION_TYPE = "SDP_HE_MIDDLEWARE";

    /*================================================
     * RESPONSE CODES
     * ==============================================*/
    public static final int RESPONSE_CODE_0 = 0;
    public static final int RESPONSE_CODE_200 = 200;
    public static final int RESPONSE_CODE_400 = 400;
    public static final int RESPONSE_CODE_401 = 401;
    public static final int TIMEOUT_ERROR_CODE = 408;
    public static final int RESPONSE_CODE_409 = 409;

    /*================================================
     * RESPONSE MESSAGES
     * ==============================================*/
    public static final String RESPONSE_INVALID_REQUEST = "Invalid request";
    public static final String RESPONSE_SERVICE_UNREACHABLE = "Service is unreachable. Please try later.";
    public static final String RESPONSE_TIBCO_CALL_FAILED = "Call to SDP Failed";
    public static final String RESPONSE_SUCCESS = "SUCCESS";
    public static final String RESPONSE_INVALID_HEADERS = "Invalid headers";
    public static final String RESPONSE_INVALID_MSISDN = "Invalid Body MSISDN.";
    public static final String RESPONSE_EMPTY_FIELD = "Enter value to the empty field";
    public static final String RESPONSE_REJECTED_TERMS = "Kindly Accept Terms and Conditions";
    public static final String RESPONSE_WRONG_URL_FORMAT = "Entered URL is wrongly formed";
    public static final String RESPONSE_SEND_SMS = "Dear Customer, you have successfully opted into Device Financing. Dial *544# to request for your phone.";

    /*================================================
     * SERVICE TEST ENDPOINTS
     * ==============================================*/
    public static final String TEST_URL_ENDPOINT = "/v1/activate";

    private GlobalVariables() {
    }

}
