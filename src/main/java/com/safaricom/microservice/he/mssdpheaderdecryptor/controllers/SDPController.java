package com.safaricom.microservice.he.mssdpheaderdecryptor.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.payloads.api.ApiResponse;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.api.ApiRequest;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.api.PaymentRequest;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.api.RefundRequest;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.api.SendSMS;
import com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.callback.CallBackResponse;
import com.safaricom.microservice.he.mssdpheaderdecryptor.service.ActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author DOTINA
 */
@RestController
@RequestMapping("/api/v1")
public class SDPController {
    private final ActivationService activationService;

    @Autowired
    public SDPController(ActivationService activationService) {
        this.activationService = activationService;
    }

    @PostMapping("/activate")
    public ResponseEntity<ApiResponse> serviceEndPointHandlerActivate(
            @RequestHeader HttpHeaders httpHeaders,
            @Valid @RequestBody ApiRequest apiRequest) throws JsonProcessingException {
        return activationService.processRequest(httpHeaders, apiRequest,"ACTIVATE");
    }

    @PostMapping("/deactivate")
    public ResponseEntity<ApiResponse> serviceEndPointHandlerDeactivate(
            @RequestHeader HttpHeaders httpHeaders,
            @Valid @RequestBody ApiRequest apiRequest) throws JsonProcessingException {
        return activationService.processRequest(httpHeaders, apiRequest,"DEACTIVATE");
    }

    @PostMapping("/charge")
    public ResponseEntity<ApiResponse> serviceEndPointHandlerCharge(
            @RequestHeader HttpHeaders httpHeaders,
            @Valid @RequestBody PaymentRequest apiRequest) throws JsonProcessingException {
        return activationService.processRequest(httpHeaders,  apiRequest,"Payment");
    }

    @PostMapping("/refund")
    public ResponseEntity<ApiResponse> serviceEndPointHandlerRefund(
            @RequestHeader HttpHeaders httpHeaders,
            @Valid @RequestBody RefundRequest apiRequest) throws JsonProcessingException {
        return activationService.processRequest(httpHeaders, apiRequest, "Refund");
    }

    @PostMapping("/sendSms")
    public ResponseEntity<ApiResponse> serviceEndPointHandlerSendSMS(
            @RequestHeader HttpHeaders httpHeaders,
            @Valid @RequestBody SendSMS apiRequest) throws JsonProcessingException {
        return activationService.processRequest(httpHeaders, apiRequest, "SendSMS");
    }

    @PostMapping("/callback")
    public ResponseEntity<ApiResponse> serviceCallBackHandler(
            @RequestHeader HttpHeaders httpHeaders,
            @Valid @RequestBody CallBackResponse callBackResponse) throws JsonProcessingException {
        return activationService.processCallBack(httpHeaders, callBackResponse);
    }
}
