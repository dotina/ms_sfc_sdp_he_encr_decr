package com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.api;

public class ErrorMapping {

    private String errorCode;
    private int responseCode;
    private String serviceName;
    private String errorType;
    private String technicalMessage;
    private String customerMessage;
    private String errorSource;

    /**
     * Gets error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return this.errorCode;
    }

    /**
     * Sets error code.
     *
     * @param errorCode the error code
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Gets service name.
     *
     * @return the service name
     */
    public String getServiceName() {
        return this.serviceName;
    }

    /**
     * Sets service name.
     *
     * @param serviceName the service name
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * Gets response code.
     *
     * @return the response code
     */
    public int getResponseCode() {
        return this.responseCode;
    }

    /**
     * Sets response code.
     *
     * @param responseCode the response code
     */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * Gets error type.
     *
     * @return the error type
     */
    public String getErrorType() {
        return this.errorType;
    }

    /**
     * Sets error type.
     *
     * @param errorType the error type
     */
    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    /**
     * Gets technical message.
     *
     * @return the technical message
     */
    public String getTechnicalMessage() {
        return this.technicalMessage;
    }

    /**
     * Sets technical message.
     *
     * @param technicalMessage the technical message
     */
    public void setTechnicalMessage(String technicalMessage) {
        this.technicalMessage = technicalMessage;
    }

    /**
     * Gets error source.
     *
     * @return the error source
     */
    public String getErrorSource() {
        return this.errorSource;
    }

    /**
     * Sets error source.
     *
     * @param errorSource the error source
     */
    public void setErrorSource(String errorSource) {
        this.errorSource = errorSource;
    }

    /**
     * Gets customer message.
     *
     * @return the customer message
     */
    public String getCustomerMessage() {
        return this.customerMessage;
    }

    /**
     * Sets customer message.
     *
     * @param customerMessage the customer message
     */
    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }

    @Override
    public String toString() {
        return String.format("{\"errorCode\" : \"%s\", \"responseCode\" : \"%d\", \"serviceName\" : \"%s\", " +
                        "\"errorType\" : \"%s\", \"technicalMessage\" : \"%s\", \"customerMessage\" : \"%s\", " +
                        "\"errorSource\" : \"%s\"}", errorCode, responseCode, serviceName, errorType, technicalMessage,
                customerMessage, errorSource);
    }
}
