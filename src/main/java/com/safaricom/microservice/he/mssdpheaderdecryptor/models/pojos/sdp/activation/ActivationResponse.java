package com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.sdp.activation;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;

/**
 * @author DOTINA
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "requestId",
        "responseId",
        "responseTimeStamp",
        "channel",
        "operation",
        "requestParam",
        "responseParam"
})
public class ActivationResponse {
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("responseId")
    private String responseId;
    @JsonProperty("responseTimeStamp")
    private String responseTimeStamp;
    @JsonProperty("channel")
    private String channel;
    @JsonProperty("operation")
    private String operation;
    @JsonProperty("requestParam")
    private RequestParam requestParam;
    @JsonProperty("responseParam")
    private ResponseParam responseParam;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getResponseTimeStamp() {
        return responseTimeStamp;
    }

    public void setResponseTimeStamp(String responseTimeStamp) {
        this.responseTimeStamp = responseTimeStamp;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public RequestParam getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(RequestParam requestParam) {
        this.requestParam = requestParam;
    }

    public ResponseParam getResponseParam() {
        return responseParam;
    }

    public void setResponseParam(ResponseParam responseParam) {
        this.responseParam = responseParam;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
