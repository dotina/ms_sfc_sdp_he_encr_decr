package com.safaricom.microservice.he.mssdpheaderdecryptor.models.payloads.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * The type Model api response.
 * @author DOTINA
 */
public class ModelApiResponse {

    @JsonProperty("header")
    private ApiHeaderResponse apiHeaderResponse;

    @JsonProperty("body")
    private Object responseBodyObject;

    /**
     * Instantiates a new Model api response.
     */
    public ModelApiResponse() {
        this.apiHeaderResponse = new ApiHeaderResponse();
    }

    /**
     * Get responseBodyObject
     *
     * @return responseBodyObject defines how the Body will look like
     */
    @ApiModelProperty(value = "[]")
    public Object getResponseBodyObject() {
        return responseBodyObject;
    }

    /**
     * Sets response body object.
     *
     * @param responseBodyObject the response body object
     */
    public void setResponseBodyObject(Object responseBodyObject) {
        this.responseBodyObject = responseBodyObject;
    }

    /**
     * Get apiHeaderResponse
     *
     * @return apiHeaderResponse api header response
     */
    @ApiModelProperty()
    public ApiHeaderResponse getApiHeaderResponse() {
        return apiHeaderResponse;
    }

    /**
     * Sets api header response.
     *
     * @param apiHeaderResponse the api header response
     */
    public void setApiHeaderResponse(ApiHeaderResponse apiHeaderResponse) {
        this.apiHeaderResponse = apiHeaderResponse;
    }

    /**
     * Defines the header of the response
     */
    public class ApiHeaderResponse {

        @JsonProperty("requestRefId")
        private String requestRefId;

        @JsonProperty("responseCode")
        private int responseCode;

        @JsonProperty("responseMessage")
        private String responseMessage;

        @JsonProperty("customerMessage")
        private String customerMessage;

        @JsonProperty("timestamp")
        private String timestamp;

        /**
         * Instantiates a new Api header response.
         */
        ApiHeaderResponse() {
        }

        /**
         * Sets headers.
         *
         * @param responseCode    the response code
         * @param responseMessage the response message
         * @param timestamp       the timestamp
         * @param customerMessage the customer message
         * @param requestRefId    the request ref id
         */
        public void setHeaders(int responseCode, String responseMessage, String timestamp, String customerMessage,
                               String requestRefId) {
            this.responseCode = responseCode;
            this.responseMessage = responseMessage;
            this.timestamp = timestamp;
            this.requestRefId = requestRefId;
            this.customerMessage = customerMessage;
        }

        /**
         * Get responseCode
         *
         * @return responseCode response code
         */
        @ApiModelProperty(example = "200")
        public Integer getResponseCode() {
            return responseCode;
        }

        /**
         * Sets response code.
         *
         * @param responseCode the response code
         */
        public void setResponseCode(Integer responseCode) {
            this.responseCode = responseCode;
        }

        /**
         * Get responseMessage
         *
         * @return responseMessage response message
         */
        @ApiModelProperty(example = "Success")
        public String getResponseMessage() {
            return responseMessage;
        }

        /**
         * Sets response message.
         *
         * @param responseMessage the response message
         */
        public void setResponseMessage(String responseMessage) {
            this.responseMessage = responseMessage;
        }

        /**
         * Get responseDescription
         *
         * @return responseDescription customer message
         */
        @ApiModelProperty(example = "Successfully generated OTP")
        public String getCustomerMessage() {
            return customerMessage;
        }

        /**
         * Sets customer message.
         *
         * @param customerMessage the customer message
         */
        public void setCustomerMessage(String customerMessage) {
            this.customerMessage = customerMessage;
        }

        /**
         * Get requestRefId
         *
         * @return requestRefId request ref id
         */
        @ApiModelProperty(example = "436b9080-db16-46be-93e7-afc0bc41afb4")
        public String getRequestRefId() {
            return requestRefId;
        }

        /**
         * Sets request ref id.
         *
         * @param requestRefId the request ref id
         */
        public void setRequestRefId(String requestRefId) {
            this.requestRefId = requestRefId;
        }

        /**
         * Get timestamp
         *
         * @return timestamp timestamp
         */
        @ApiModelProperty(example = "2019-07-30T09:21:58.217+0000")
        public String getTimestamp() {
            return timestamp;
        }

        /**
         * Sets timestamp.
         *
         * @param timestamp the timestamp
         */
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
