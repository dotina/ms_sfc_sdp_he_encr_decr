package com.safaricom.microservice.he.mssdpheaderdecryptor.models.payloads.api;

import java.sql.Timestamp;

/**
 * created by
 * @author DOTINA | 23.03.20
 */

public class ApiResponse extends ModelApiResponse {

    public ApiResponse() { // Empty Constructor
    }

    /**
     * Format the response Object according to the standard. That is {@code} ModelApiResponse
     *
     * @param responseCode   Contains the response code
     * @param referenceId    the reference id
     * @param message        Holds the message of the header
     * @param description    Holds the description of the header
     * @param responseObject Holds the response Object if available.
     * @return responseFormatter of type ResponseBalances
     */
    public static ApiResponse responseFormatter(
            int responseCode, String referenceId, String message, String description, Object responseObject
    ) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.getApiHeaderResponse().setResponseCode(responseCode);
        apiResponse.getApiHeaderResponse().setRequestRefId(referenceId);
        apiResponse.getApiHeaderResponse().setCustomerMessage(description);
        apiResponse.getApiHeaderResponse().setResponseMessage(message);
        apiResponse.getApiHeaderResponse().setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        if (responseObject != null) {
            apiResponse.setResponseBodyObject(responseObject);
        }
        return apiResponse;
    }

}
