package com.safaricom.microservice.he.mssdpheaderdecryptor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.safaricom.libs.MSISDNCryptoLib;
import com.safaricom.microservice.he.mssdpheaderdecryptor.components.RestTemplateUtil;
import com.safaricom.microservice.he.mssdpheaderdecryptor.config.ConfigProperties;
import com.safaricom.microservice.he.mssdpheaderdecryptor.config.ErrorMapperBean;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.payloads.api.ApiResponse;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.payloads.api.ResponseBody;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.payloads.sdp.activation.ActivationRequest;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.payloads.sdp.activation.Datum;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.payloads.sdp.activation.RequestParam;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.api.ApiRequest;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.api.ErrorMapping;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.callback.CallBackResponse;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.header.HeaderErrorMessage;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.sdp.activation.ActivationResponse;
import com.safaricom.microservice.he.mssdpheaderdecryptor.utils.LogsManager;
import com.safaricom.microservice.he.mssdpheaderdecryptor.utils.Validations;
import com.safaricom.util.errors.model.ErrorDetails;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.safaricom.microservice.he.mssdpheaderdecryptor.utils.GlobalVariables.*;
import static com.safaricom.microservice.he.mssdpheaderdecryptor.utils.Utilities.*;
import static java.lang.Integer.parseInt;
import static org.apache.logging.log4j.util.Strings.isEmpty;

/**
 * @author DOTINA
 */
@Service
public class ActivationService {
    private final ErrorMapperBean errorMapperBean;
    private final Validations validations;
    private final RestTemplateUtil restTemplateUtil;
    private final ConfigProperties configProperties;
    private String msisdn;
    private String operation;
    private String url;
    private ObjectMapper objectMapper;


    @Autowired
    public ActivationService(ErrorMapperBean errorMapperBean, Validations validations, RestTemplateUtil restTemplateUtil, ConfigProperties configProperties) {
        this.errorMapperBean = errorMapperBean;
        this.validations = validations;
        this.restTemplateUtil = restTemplateUtil;
        this.configProperties = configProperties;
    }

    public ResponseEntity<ApiResponse> processRequest(HttpHeaders httpHeaders, ApiRequest apiRequest, String operation)
            throws JsonProcessingException {

        // Validate Headers
        HeaderErrorMessage headerErrorMessage = validations.validateHeaders(httpHeaders);

        if (!headerErrorMessage.getInvalidHeaderErrors().isEmpty() || headerErrorMessage.isMissingHeaders()) {
            return new ResponseEntity<>(ApiResponse.responseFormatter(HttpStatus.BAD_REQUEST.value(),
                    generateTrackingID(), RESPONSE_INVALID_REQUEST, RESPONSE_INVALID_HEADERS, headerErrorMessage),
                    HttpStatus.BAD_REQUEST);
        }

        // Remove Authorization Headers
        this.validations.removeAuthorizationHeaders(httpHeaders);

        // Pick Header Values
        String requestReferenceID = httpHeaders.get(X_CORRELATION_CONVERSATION_ID).get(0);
        String sourceSystem = httpHeaders.get(X_SOURCE_SYSTEM).get(0);
        String apiToken = httpHeaders.get(X_API_TOKEN).get(0);

        // Validate the CallBack url entered
        if (!urlValidator(apiRequest.getCallBackUrl()) || apiRequest.getCallBackUrl().isEmpty()) {
            return new ResponseEntity<>(ApiResponse.responseFormatter(RESPONSE_CODE_400, requestReferenceID,
                    RESPONSE_INVALID_REQUEST, RESPONSE_WRONG_URL_FORMAT, "Wrong URL Format"), HttpStatus.BAD_REQUEST);
        }
        url = apiRequest.getCallBackUrl();

        // Check if CpiD is empty
        if (isEmpty(apiRequest.getCpId())) {
            return new ResponseEntity<>(ApiResponse.responseFormatter(RESPONSE_CODE_400, requestReferenceID,
                    RESPONSE_INVALID_REQUEST, RESPONSE_EMPTY_FIELD, headerErrorMessage), HttpStatus.OK);
        }

        // Check if msisdn is not encrypted
        if (apiRequest.getMsisdn().isEmpty()) {
            return new ResponseEntity<>(ApiResponse.responseFormatter(RESPONSE_CODE_400, requestReferenceID,
                    RESPONSE_INVALID_REQUEST, RESPONSE_INVALID_MSISDN, "This field can't be empty!!!"), HttpStatus.BAD_REQUEST);
        }

        // Check if msisdn is not encrypted
        if ((apiRequest.getMsisdn().substring(0,4).equals("2547")) || (apiRequest.getMsisdn().substring(0,4).equals("2541"))) {
            return new ResponseEntity<>(ApiResponse.responseFormatter(RESPONSE_CODE_400, requestReferenceID,
                    RESPONSE_INVALID_REQUEST, RESPONSE_INVALID_MSISDN, "Invalid MSISDN | Only encrypted MSISDN allowed"), HttpStatus.BAD_REQUEST);

        }

        // Check if offer id is empty
        if (isEmpty(apiRequest.getOfferCode())) {
            return new ResponseEntity<>(ApiResponse.responseFormatter(RESPONSE_CODE_401, requestReferenceID,
                    RESPONSE_INVALID_REQUEST, RESPONSE_EMPTY_FIELD, headerErrorMessage), HttpStatus.OK);
        }


        // Log service input
        LogsManager.info(requestReferenceID, TRANSACTION_TYPE, "processRequest", "",
                apiRequest.getMsisdn(), sourceSystem, TARGET_SYSTEM, "", RESPONSE_CODE_0, "",
                "", "HEADERS: = ".concat(httpHeaders.toString()), "");
        ApiResponse apiResponse = this.activateCust(requestReferenceID, sourceSystem, apiToken, apiRequest, operation);

        // return correct status
//        if (!apiResponse.getApiHeaderResponse().getResponseCode().equals("200")) {
//            return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.valueOf(apiResponse.getApiHeaderResponse().getResponseCode()));
//        }
        // Log service output
        LogsManager.info(requestReferenceID, TRANSACTION_TYPE, "processRequest", "",
                apiRequest.getMsisdn(), sourceSystem, TARGET_SYSTEM, "", RESPONSE_CODE_200, "",
                "", parseToJsonString(apiRequest), parseToJsonString(apiResponse));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    private ApiResponse activateCust(String requestReferenceID, String sourceSystem, String apiToken, ApiRequest apiRequest, String operation)
            throws JsonProcessingException {

       String  decMsisdn = (MSISDNCryptoLib.SAFFPEdecryptor(apiRequest.getMsisdn()));

        ActivationRequest activationRequest = prepRequest(apiRequest, requestReferenceID, operation, decMsisdn);
        objectMapper = new ObjectMapper();

        String restTemplateResponse = this.restTemplateUtil.makeAPICall(sourceSystem,apiToken ,X_CORRELATION_CONVERSATION_ID,
                requestReferenceID,configProperties.getSdpUrl(),operation, msisdn, activationRequest);
        ResponseBody responseBody = new ResponseBody();
        String responseCode ="";
        String message = "";
        switch (restTemplateResponse){
            case "401":
                responseCode = "401";
                responseBody.setStatus(responseCode);
                responseBody.setDescription(this.restTemplateUtil.getErrorMessage());
                message = this.restTemplateUtil.getErrorStatusDesc();
                break;
            case "403" :
                responseCode = "403";
                responseBody.setStatus(responseCode);
                responseBody.setDescription(this.restTemplateUtil.getErrorMessage());
                message = this.restTemplateUtil.getErrorStatusDesc();
                break;
            default:
                ActivationResponse activationResponse = objectMapper.readValue(restTemplateResponse, ActivationResponse.class);
                responseCode = activationResponse.getResponseParam().getStatusCode();
                responseBody.setStatus(activationResponse.getResponseParam().getStatus());
                responseBody.setDescription(activationResponse.getResponseParam().getDescription());
                message = activationRequest.getOperation();
                break;
        }




            LogsManager.error(requestReferenceID, TRANSACTION_TYPE, operation+" Customer", "",
                    msisdn, sourceSystem, TARGET_SYSTEM, "", parseInt(responseCode), RESPONSE_TIBCO_CALL_FAILED,
                    "", msisdn, parseToJsonString(responseBody));



       // ErrorMapping errorMapping = this.getResponseMessage(responseCode);
        return ApiResponse.responseFormatter(
                parseInt(responseCode), requestReferenceID, message,
                responseBody.getDescription(), responseBody);
    }



    private ActivationRequest prepRequest(ApiRequest apiRequest, String requestId, String operationName, String msisdn){

        String[] dataValue = {apiRequest.getOfferCode(), msisdn, apiRequest.getCpId()};
        String[] dataName = {"OfferCode", "Msisdn", "CpId"};

        Set<Datum> dataSet = new HashSet<>();

        IntStream.range(0, dataName.length).forEachOrdered(i -> {
            Datum data = new Datum();
            data.setName(dataName[i]);
            data.setValue(dataValue[i]);
            dataSet.add(data);
        });

        RequestParam requestParam = new RequestParam();
        requestParam.setData(dataSet);

        //TODO: make this calls come from headers later for now they will be static
        ActivationRequest activationRequest = new ActivationRequest();
        activationRequest.setChannel("APIGW");
        activationRequest.setRequestId(requestId);
        activationRequest.setOperation(operationName);
        activationRequest.setRequestParam(requestParam);

        return activationRequest;
    }

    public ResponseEntity<ApiResponse> processCallBack(HttpHeaders httpHeaders, CallBackResponse callBackResponse)
            throws JsonProcessingException {

        int callBackCode = Integer.parseInt(callBackResponse.getRequestId());
        String requestReferenceID = httpHeaders.get(X_CORRELATION_CONVERSATION_ID).get(0);

        String callBackMsg = callBackResponse.getOperation();

        LogsManager.info(requestReferenceID, TRANSACTION_TYPE, "Process Call Back", "",
                "", "", TARGET_SYSTEM, "", callBackCode, callBackMsg,
                "", "", parseToJsonString(callBackResponse));

        String[] dataValue = {"callBackResponse", msisdn, "callBackResponse"};
        String[] dataName = {"OfferCode", "TransactionId","ClientTransactionId",
                "Language", "SubscriberLifeCycle","SubscriptionStatus",
                "Channel","NextBillingDate","Type","OfferName","ShortCode","Msisdn"};

        Set<Datum> dataSet = new HashSet<>();

        IntStream.range(0, dataName.length).forEachOrdered(i -> {
            Datum data = new Datum();
            data.setName(dataName[i]);
            data.setValue(dataValue[i]);
            dataSet.add(data);
        });



        return new ResponseEntity<>(new ApiResponse(), HttpStatus.OK);
    }




    private ErrorMapping getResponseMessage(final String responseCode) {
        ErrorMapping errorMapping = new ErrorMapping();
        ErrorDetails errorDetails = this.errorMapperBean.getServiceResponses().get(responseCode);
        errorMapping.setCustomerMessage(errorDetails.getCustomerMessage());
        errorMapping.setErrorCode(errorDetails.getErrorCode());
        errorMapping.setErrorSource(errorDetails.getErrorSource());
        errorMapping.setErrorType(errorDetails.getErrorType());
        errorMapping.setServiceName(errorDetails.getServiceName());
        errorMapping.setResponseCode(errorDetails.getResponseCode());
        errorMapping.setTechnicalMessage(errorDetails.getTechnicalMessage());
        return errorMapping;
    }

    private String currentIp(){
        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);
            return ip.getHostAddress();
        } catch (UnknownHostException e) {

            e.printStackTrace();
            LogsManager.info("001", TRANSACTION_TYPE, "Ip Error", "",
                    "", "", TARGET_SYSTEM, "", 1, e.getMessage(),
                    "", "", parseToJsonString(e.getStackTrace()));
            return null;
        }

    }

    public Boolean urlValidator(String url){
        // Get a url to validate using default schemas
        UrlValidator urlValidator = UrlValidator.getInstance();
        return urlValidator.isValid(url);

    }
}
