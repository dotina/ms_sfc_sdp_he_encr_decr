package com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.callback;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author DOTINA | 26.03.20
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "requestId",
        "requestDesc"
})
public class CallResponse {
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("requestDesc")
    private String requestDesc;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestDesc() {
        return requestDesc;
    }

    public void setRequestDesc(String requestDesc) {
        this.requestDesc = requestDesc;
    }
}
