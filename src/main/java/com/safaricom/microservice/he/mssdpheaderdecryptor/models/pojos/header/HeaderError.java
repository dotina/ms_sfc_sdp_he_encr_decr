package com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.header;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class HeaderError implements Serializable {

    @JsonProperty("header")
    private String header;

    @JsonProperty("error")
    private String error;

    /**
     * Instantiates a new Header error.
     */
    public HeaderError() { // Empty constructor
    }

    /**
     * Gets header.
     *
     * @return the header
     */
    public String getHeader() {
        return header;
    }

    /**
     * Sets header.
     *
     * @param header the header
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Gets error.
     *
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets error.
     *
     * @param error the error
     */
    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return String.format("{" +
                "\"header\": \"%s\"" +
                "\"error\": \"%s\"" +
                "}", header, error);
    }

}
