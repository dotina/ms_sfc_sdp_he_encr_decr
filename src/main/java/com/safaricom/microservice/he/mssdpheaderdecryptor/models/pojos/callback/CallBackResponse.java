package com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.callback;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * @author DOTINA | 26.03.20
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "requestId",
        "requestTimeStamp",
        "requestParam",
        "operation"
})
public class CallBackResponse {
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("requestTimeStamp")
    private String requestTimeStamp;
    @JsonProperty("requestParam")
    private RequestParam requestParam;
    @JsonProperty("operation")
    private String operation;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("requestId")
    public String getRequestId() {
        return requestId;
    }

    @JsonProperty("requestId")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @JsonProperty("requestTimeStamp")
    public String getRequestTimeStamp() {
        return requestTimeStamp;
    }

    @JsonProperty("requestTimeStamp")
    public void setRequestTimeStamp(String requestTimeStamp) {
        this.requestTimeStamp = requestTimeStamp;
    }

    @JsonProperty("requestParam")
    public RequestParam getRequestParam() {
        return requestParam;
    }

    @JsonProperty("requestParam")
    public void setRequestParam(RequestParam requestParam) {
        this.requestParam = requestParam;
    }

    @JsonProperty("operation")
    public String getOperation() {
        return operation;
    }

    @JsonProperty("operation")
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
