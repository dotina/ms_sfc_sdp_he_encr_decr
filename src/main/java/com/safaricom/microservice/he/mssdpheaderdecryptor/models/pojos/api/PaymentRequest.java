package com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ChargeAmount"
})
public class PaymentRequest extends ApiRequest implements Serializable {
    @JsonProperty("ChargeAmount")
    private String ChargeAmount;

    public String getChargeAmount() {
        return ChargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        ChargeAmount = chargeAmount;
    }
}
