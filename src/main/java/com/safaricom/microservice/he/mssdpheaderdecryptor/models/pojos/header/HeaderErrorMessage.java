package com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.header;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HeaderErrorMessage implements Serializable {

    @JsonProperty("missingHeaders")
    private boolean missingHeaders;

    @JsonProperty("invalidHeaders")
    private List<HeaderError> invalidHeaderErrors;

    /**
     * Instantiates a new Header error message.
     */
    public HeaderErrorMessage() {
        invalidHeaderErrors = new ArrayList<>();
    }

    /**
     * Is missing headers boolean.
     *
     * @return the boolean
     */
    public boolean isMissingHeaders() {
        return missingHeaders;
    }

    /**
     * Sets missing headers.
     *
     * @param missingHeaders the missing headers
     */
    public void setMissingHeaders(boolean missingHeaders) {
        this.missingHeaders = missingHeaders;
    }

    /**
     * Gets invalid header errors.
     *
     * @return the invalid header errors
     */
    public List<HeaderError> getInvalidHeaderErrors() {
        return invalidHeaderErrors;
    }

    /**
     * Sets invalid header errors.
     *
     * @param invalidHeaderErrors the invalid header errors
     */
    public void setInvalidHeaderErrors(List<HeaderError> invalidHeaderErrors) {
        this.invalidHeaderErrors = invalidHeaderErrors;
    }

    @Override
    public String toString() {
        return String.format("{" +
                        "\"invalidHeaderErrors\": \"%s\", " +
                        "\"invalid\": [%s] " +
                        "}",
                missingHeaders, invalidHeaderErrors
        );
    }
}
