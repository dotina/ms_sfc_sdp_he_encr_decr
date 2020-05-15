package com.safaricom.microservice.he.mssdpheaderdecryptor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.safaricom.libs.MSISDNCryptoLib;
import com.safaricom.microservice.he.mssdpheaderdecryptor.components.CallBackRestUtil;
import com.safaricom.microservice.he.mssdpheaderdecryptor.components.RestTemplateUtil;
import com.safaricom.microservice.he.mssdpheaderdecryptor.config.ConfigProperties;
import com.safaricom.microservice.he.mssdpheaderdecryptor.config.ErrorMapperBean;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.DAO.Entity.CallBacks;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.DAO.Repository.CallBackRepository;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.payloads.api.ApiResponse;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.payloads.api.ResponseBody;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.payloads.sdp.activation.ActivationRequest;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.payloads.sdp.activation.Datum;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.payloads.sdp.activation.RequestParam;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.api.*;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.callback.CallBackResponse;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.callback.CallResponse;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.header.HeaderErrorMessage;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.sdp.activation.ActivationResponse;
import com.safaricom.microservice.he.mssdpheaderdecryptor.utils.LogsManager;
import com.safaricom.microservice.he.mssdpheaderdecryptor.utils.Validations;
import com.safaricom.util.errors.model.ErrorDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    private final CallBackRestUtil callBackRestUtil;
    private final ConfigProperties configProperties;
    private final CallBackRepository callBackRepository;
    private String msisdn;
    private String operation;
    private String url;
    private ObjectMapper objectMapper;
    private ResponseBody responseBody = new ResponseBody();



    @Autowired
    public ActivationService(ErrorMapperBean errorMapperBean, Validations validations, RestTemplateUtil restTemplateUtil, CallBackRestUtil callBackRestUtil, ConfigProperties configProperties, CallBackRepository callBackRepository) {
        this.errorMapperBean = errorMapperBean;
        this.validations = validations;
        this.restTemplateUtil = restTemplateUtil;
        this.callBackRestUtil = callBackRestUtil;
        this.configProperties = configProperties;
        this.callBackRepository = callBackRepository;
    }

    public ResponseEntity<ApiResponse> processRequest(HttpHeaders httpHeaders, Object object, String operation)
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
        if ((object instanceof ApiRequest) && !this.validations.urlValidator(((ApiRequest) object).getCallBackUrl()) ) {
                return new ResponseEntity<>(ApiResponse.responseFormatter(RESPONSE_CODE_400, requestReferenceID,
                        RESPONSE_INVALID_REQUEST, RESPONSE_WRONG_URL_FORMAT, "Wrong  CallBackURL Format"), HttpStatus.BAD_REQUEST);

        }

        url = object instanceof ApiRequest ? ((ApiRequest) object).getCallBackUrl() : "https://posthere.io/6ebb-4eda-8465";

        // Check if CpiD is empty
        if (object instanceof ApiRequest && isEmpty(((ApiRequest) object).getCpId())) {
            return new ResponseEntity<>(ApiResponse.responseFormatter(RESPONSE_CODE_400, requestReferenceID,
                    RESPONSE_INVALID_REQUEST, RESPONSE_EMPTY_FIELD, "The CpId field can't be empty"), HttpStatus.OK);
        }

        // Check if msisdn empty
        if (object instanceof ApiRequest && isEmpty(((ApiRequest) object).getMsisdn()) ) {
            return new ResponseEntity<>(ApiResponse.responseFormatter(RESPONSE_CODE_400, requestReferenceID,
                    RESPONSE_INVALID_REQUEST, RESPONSE_INVALID_MSISDN, "The MSISDN field can't be empty!!!"), HttpStatus.BAD_REQUEST);
        }

        // Check if msisdn is not encrypted
        if (object instanceof ApiRequest && ((((ApiRequest) object).getMsisdn().substring(0,4).equals("2547")) || (((ApiRequest) object).getMsisdn().substring(0,4).equals("2541"))) ) {
            return new ResponseEntity<>(ApiResponse.responseFormatter(RESPONSE_CODE_400, requestReferenceID,
                    RESPONSE_INVALID_REQUEST, RESPONSE_INVALID_MSISDN, "Invalid MSISDN | Only encrypted MSISDN allowed"), HttpStatus.BAD_REQUEST);

        }

        // Check if offer id is empty
        if (object instanceof ApiRequest && isEmpty(((ApiRequest) object).getOfferCode())) {
            return new ResponseEntity<>(ApiResponse.responseFormatter(RESPONSE_CODE_401, requestReferenceID,
                    RESPONSE_INVALID_REQUEST, RESPONSE_EMPTY_FIELD, "The Offer Code field can't be empty"), HttpStatus.OK);
        }

        // Default
        ApiResponse apiResponse = null;
        // Log service input

            LogsManager.info(requestReferenceID, TRANSACTION_TYPE, "processRequest", "",
                    ((ApiRequest) object).getMsisdn(), sourceSystem, TARGET_SYSTEM, "", RESPONSE_CODE_0, "",
                    "", "HEADERS: = ".concat(httpHeaders.toString()), "");
            apiResponse = this.activateCust(requestReferenceID, sourceSystem, apiToken, object, operation);



        // return correct status
//        if (!apiResponse.getApiHeaderResponse().getResponseCode().equals("200")) {
//            return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.valueOf(apiResponse.getApiHeaderResponse().getResponseCode()));
//        }
        // Log service output
        LogsManager.info(requestReferenceID, TRANSACTION_TYPE, "processRequest", "",
                ((ApiRequest) object).getMsisdn(), sourceSystem, TARGET_SYSTEM, "", RESPONSE_CODE_200, "",
                "", parseToJsonString(object), parseToJsonString(apiResponse));

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    private ApiResponse activateCust(String requestReferenceID, String sourceSystem, String apiToken, Object obj, String operation)
            throws JsonProcessingException {

        ActivationRequest makeRequest = null;

        objectMapper = new ObjectMapper();
        String restTemplateResponse = "";
        if ((obj instanceof PaymentRequest) && (operation.equalsIgnoreCase("payment"))) {
            String  decMsisdn = (MSISDNCryptoLib.SAFFPEdecryptor(((PaymentRequest) obj).getMsisdn()));
            makeRequest = prepRequest(obj, requestReferenceID, operation, decMsisdn);
            restTemplateResponse = this.restTemplateUtil.makeAPICall(sourceSystem, apiToken, X_CORRELATION_CONVERSATION_ID,
                    requestReferenceID, configProperties.getSdpUrl(), "paymentRequest", msisdn, makeRequest);
        }
        if ((obj instanceof ApiRequest) && (operation.equalsIgnoreCase("deACTIVATE") || operation.equalsIgnoreCase("activate"))) {
            String  decMsisdn = (MSISDNCryptoLib.SAFFPEdecryptor(((ApiRequest) obj).getMsisdn()));
            makeRequest = prepRequest(obj,requestReferenceID, operation, decMsisdn);
            restTemplateResponse = this.restTemplateUtil.makeAPICall(sourceSystem, apiToken, X_CORRELATION_CONVERSATION_ID,
                    requestReferenceID, configProperties.getSdpUrl(), operation, msisdn, makeRequest);
        }

        if ((obj instanceof RefundRequest) && (operation.equalsIgnoreCase("refund"))) {
            String  decMsisdn = (MSISDNCryptoLib.SAFFPEdecryptor(((ApiRequest) obj).getMsisdn()));
            makeRequest = prepRequest(obj,requestReferenceID, operation, decMsisdn);
            restTemplateResponse = this.restTemplateUtil.makeAPICall(sourceSystem, apiToken, X_CORRELATION_CONVERSATION_ID,
                    requestReferenceID, configProperties.getSdpUrl(), "refundRequest", msisdn, makeRequest);
        }

        if ((obj instanceof SendSMS) && (operation.equalsIgnoreCase("sendsms"))) {
            String  decMsisdn = (MSISDNCryptoLib.SAFFPEdecryptor(((SendSMS) obj).getMsisdn()));
            makeRequest = prepRequest(obj,requestReferenceID, operation, decMsisdn);
            restTemplateResponse = this.restTemplateUtil.makeAPICall(sourceSystem, apiToken, X_CORRELATION_CONVERSATION_ID,
                    requestReferenceID, configProperties.getSdpUrl(), "sendSMSRequest", msisdn, makeRequest);
        }

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
            case "404":
                responseCode = "404";
                responseBody.setStatus(responseCode);
                responseBody.setDescription(this.restTemplateUtil.getErrorMessage());
                message = this.restTemplateUtil.getErrorStatusDesc();
                break;
            case "408":
                responseCode = "408";
                responseBody.setStatus(responseCode);
                responseBody.setDescription(this.restTemplateUtil.getErrorMessage());
                message = this.restTemplateUtil.getErrorStatusDesc();
                break;
            case "500" :
                responseCode = "500";
                responseBody.setStatus(responseCode);
                responseBody.setDescription(this.restTemplateUtil.getErrorMessage().substring(18,187));
                message = this.restTemplateUtil.getErrorStatusDesc();
                break;
            default:
                ActivationResponse activationResponse = objectMapper.readValue(restTemplateResponse, ActivationResponse.class);
                responseCode = activationResponse.getResponseParam().getStatusCode();
                responseBody.setStatus(activationResponse.getResponseParam().getStatus());
                responseBody.setDescription(activationResponse.getResponseParam().getDescription());
                message = activationResponse.getOperation();
                break;
        }


            LogsManager.error(requestReferenceID, TRANSACTION_TYPE, operation+" Customer", "",
                    msisdn, sourceSystem, TARGET_SYSTEM, "", parseInt(responseCode), RESPONSE_TIBCO_CALL_FAILED,
                    "", msisdn, parseToJsonString(responseBody));

            CallBacks callBacks = new CallBacks();
        if ((callBackRepository.findByRequestId(requestReferenceID) == null ) && callBackRepository.findByUrl(url) == null){
            callBacks.setRequestId(requestReferenceID);
            callBacks.setUrl(url);
            callBacks.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            callBacks.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

            callBackRepository.save(callBacks);
        }else if ((callBackRepository.findByUrl(url) != null)){
            callBacks.setId(callBackRepository.findByUrl(url).getId());
            callBacks.setRequestId(requestReferenceID);
            callBacks.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            callBacks.setUrl(url);
            callBackRepository.save(callBacks);
        }

       // ErrorMapping errorMapping = this.getResponseMessage(responseCode);
        return ApiResponse.responseFormatter(
                parseInt(responseCode), requestReferenceID, message,
                responseBody.getDescription(), responseBody);
    }



    private ActivationRequest prepRequest(Object obj,String requestId, String operationName, String msisdn){
        String[] dataValue ={} ;
        String[] dataName = {};
        if((obj instanceof PaymentRequest) && (operationName.equalsIgnoreCase("payment"))) {
             dataValue = new String[]{((PaymentRequest) obj).getOfferCode(), msisdn, ((PaymentRequest) obj).getChargeAmount(), ((PaymentRequest) obj).getCpId()};
             dataName = new String[]{"OfferCode", "Msisdn", "ChargeAmount", "CpId"};
        }
        if((obj instanceof ApiRequest) && (operationName.equalsIgnoreCase("deACTIVATE") || operationName.equalsIgnoreCase("activate"))) {
            dataValue = new String[]{((ApiRequest) obj).getOfferCode(), msisdn, ((ApiRequest) obj).getCpId()};
            dataName = new String[]{"OfferCode", "Msisdn", "CpId"};
        }

        if((obj instanceof RefundRequest) && (operationName.equalsIgnoreCase("refund"))) {
            dataValue = new String[]{((RefundRequest) obj).getOfferCode(), msisdn, ((RefundRequest) obj).getCpId(), ((RefundRequest) obj).getChargeAmount(), ((RefundRequest) obj).getBillingId(), ((RefundRequest) obj).getDeactivateOperation()};
            dataName = new String[]{"OfferCode", "Msisdn", "CpId", "ChargeAmount", "BillingId", "DeactivateOperation"};
        }
        if((obj instanceof SendSMS) && (operationName.equalsIgnoreCase("sendsms"))) {
            dataValue = new String[]{((SendSMS) obj).getOfferCode(), msisdn, ((SendSMS) obj).getCpId(), ((SendSMS) obj).getLinkId(), ((SendSMS) obj).getContent()};
            dataName = new String[]{"OfferCode", "Msisdn", "CpId", "LinkId", "Content"};
        }

        Set<Datum> dataSet = new HashSet<>();

        String[] finalDataName = dataName;
        String[] finalDataValue = dataValue;
        IntStream.range(0, dataName.length).forEachOrdered(i -> {
            Datum data = new Datum();
            data.setName(finalDataName[i]);
            data.setValue(finalDataValue[i]);
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
        //String apiToken = httpHeaders.get(X_API_TOKEN).get(0);
        String callBackCode = callBackResponse.getRequestId();

        operation = callBackResponse.getOperation();



        // Log service input
            LogsManager.info(callBackCode, TRANSACTION_TYPE, "processCallBackRequest", "",
                    msisdn, sourceSystem, TARGET_SYSTEM, "", RESPONSE_CODE_0, "",
                    "", "HEADERS: = ".concat(httpHeaders.toString()), "");
            ApiResponse apiResponse = this.callBackResp(callBackCode, sourceSystem, callBackResponse, operation);


        LogsManager.info(requestReferenceID, TRANSACTION_TYPE, "Process Call Back", "",
                msisdn, "callback", TARGET_SYSTEM, "", (int) Double.parseDouble(callBackCode), operation,
                "", parseToJsonString(callBackResponse), parseToJsonString(apiResponse));

        

        return new ResponseEntity<>(new ApiResponse(), HttpStatus.OK);
    }
    private ApiResponse callBackResp(String requestReferenceID, String sourceSystem, CallBackResponse callBackResponse, String operate)
            throws JsonProcessingException {
        // this map gets me the specific item in the set i need the msisdn
        Map<String, String> callBackMsisdn =
                callBackResponse.getRequestParam().getData().stream()
                        .collect(Collectors.toMap(com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.callback.Datum::getName,
                                com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.callback.Datum::getValue));
        msisdn = callBackMsisdn.get("Msisdn");
        String encryptMsisdn = MSISDNCryptoLib.SAFFPEEncryptor(callBackMsisdn.get("Msisdn")); // this gets me the msisdn and encrypts it

        CallBackResponse callBackRequest = prepRequest(callBackResponse, requestReferenceID, operate, callBackMsisdn,encryptMsisdn);
           CallBacks url = callBackRepository.findByRequestId(callBackResponse.getRequestId());
        String restTemplateResponse = this.callBackRestUtil.makeAPICall(sourceSystem, "", X_CORRELATION_CONVERSATION_ID,
                requestReferenceID, url.getUrl(), "", encryptMsisdn, callBackRequest);


        String responseCode ="";
        String message = "";
        switch (restTemplateResponse){
            case "401":
                responseCode = "401";
                responseBody.setStatus(responseCode);
                responseBody.setDescription(this.callBackRestUtil.getErrorMessage());
                message = this.callBackRestUtil.getErrorStatusDesc();

                LogsManager.error(requestReferenceID, TRANSACTION_TYPE, operation, "",
                        encryptMsisdn, sourceSystem, TARGET_SYSTEM, "", parseInt(responseCode), RESPONSE_TIBCO_CALL_FAILED,
                        "", msisdn, parseToJsonString(responseBody));
                break;
            case "403" :
                responseCode = "403";
                responseBody.setStatus(responseCode);
                responseBody.setDescription(this.callBackRestUtil.getErrorMessage());
                message = this.callBackRestUtil.getErrorStatusDesc();
                LogsManager.error(requestReferenceID, TRANSACTION_TYPE, operation, "",
                        encryptMsisdn, sourceSystem, TARGET_SYSTEM, "", parseInt(responseCode), RESPONSE_TIBCO_CALL_FAILED,
                        "", msisdn, parseToJsonString(responseBody));
                break;
            case "500" :
                responseCode = "500";
                responseBody.setStatus(responseCode);
                responseBody.setDescription(this.callBackRestUtil.getErrorMessage().substring(18,187));
                message = this.callBackRestUtil.getErrorStatusDesc();

                LogsManager.error(requestReferenceID, TRANSACTION_TYPE, operation, "",
                        encryptMsisdn, sourceSystem, TARGET_SYSTEM, "", parseInt(responseCode), RESPONSE_TIBCO_CALL_FAILED,
                        "", msisdn, parseToJsonString(responseBody));
                break;
            default:
                CallResponse callBackResponse1 = objectMapper.readValue(restTemplateResponse, CallResponse.class);
                responseCode = "200";
                responseBody.setStatus("200");
                responseBody.setDescription(callBackResponse1.getRequestDesc());
                message = operate;
                break;
        }



//         ErrorMapping errorMapping = this.getResponseMessage(responseCode);
        return ApiResponse.responseFormatter(
                parseInt(responseCode), requestReferenceID, message,
                responseBody.getDescription(), responseBody);

    }

    private CallBackResponse prepRequest(CallBackResponse apiRequest,String requestId, String operationName,Map<String, String> callBackMsisdn, String encryptMsisdn){

        String[] dataValue = {callBackMsisdn.get("OfferCode"), callBackMsisdn.get("TransactionId"),callBackMsisdn.get("ClientTransactionId"),
                callBackMsisdn.get("Language"),callBackMsisdn.get("SubscriberLifeCycle"), callBackMsisdn.get("SubscriptionStatus"),callBackMsisdn.get("Channel"),
                callBackMsisdn.get("NextBillingDate"),callBackMsisdn.get("Type"),callBackMsisdn.get("OfferName"),callBackMsisdn.get("ShortCode"),encryptMsisdn};
        String[] dataName = {"OfferCode", "TransactionId","ClientTransactionId",
                "Language", "SubscriberLifeCycle","SubscriptionStatus",
                "Channel","NextBillingDate","Type","OfferName","ShortCode","Msisdn"};

        Set<com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.callback.Datum> dataSet = new HashSet<>();

        IntStream.range(0, dataName.length).forEachOrdered(i -> {
            com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.callback.Datum data = new com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.callback.Datum();
            data.setName(dataName[i]);
            data.setValue(dataValue[i]);
            dataSet.add(data);
        });

        com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.callback.RequestParam requestParam = new com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.callback.RequestParam();
        requestParam.setData(dataSet);

        //TODO: make this calls come from headers later for now they will be static
        CallBackResponse callBackResponse = new CallBackResponse();
//        callBackResponse.setChannel("APIGW");
        callBackResponse.setRequestId(apiRequest.getRequestId());
        callBackResponse.setRequestTimeStamp(apiRequest.getRequestTimeStamp());
        callBackResponse.setOperation(operationName);
        callBackResponse.setRequestParam(requestParam);

        return callBackResponse;
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


}
