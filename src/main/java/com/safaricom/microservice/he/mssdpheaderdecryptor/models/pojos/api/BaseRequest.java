package com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Dickens | 28.04.20
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "msisdn",
        "offerCode",
        "CpId"
})
public abstract class BaseRequest {
    @JsonProperty("msisdn")
    private String msisdn;

    @JsonProperty("offerCode")
    private String offerCode;

    @JsonProperty("CpId")
    private String CpId;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getCpId() {
        return CpId;
    }

    public void setCpId(String cpId) {
        CpId = cpId;
    }
}
