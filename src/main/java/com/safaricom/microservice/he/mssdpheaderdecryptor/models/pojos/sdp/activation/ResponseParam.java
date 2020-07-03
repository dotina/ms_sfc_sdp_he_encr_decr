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
        "status",
        "statusCode",
        "description"
})
public class ResponseParam {
    @JsonProperty("status")
    private String status;
    @JsonProperty("statusCode")
    private String statusCode;
    @JsonProperty("description")
    private String description;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
