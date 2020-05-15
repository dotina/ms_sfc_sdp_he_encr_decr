package com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

/**
 * @author Dickens | 15.may.20
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "BillingId",
        "DeactivateOperation"
})
public class RefundRequest extends PaymentRequest implements Serializable {
    @JsonProperty("BillingId")
    private String BillingId;

    @JsonProperty("DeactivateOperation")
    private String DeactivateOperation;

    public String getBillingId() {
        return BillingId;
    }

    public void setBillingId(String billingId) {
        BillingId = billingId;
    }

    public String getDeactivateOperation() {
        return DeactivateOperation;
    }

    public void setDeactivateOperation(String deactivateOperation) {
        DeactivateOperation = deactivateOperation;
    }
}
