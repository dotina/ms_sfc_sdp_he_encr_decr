package com.safaricom.microservice.he.mssdpheaderdecryptor.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogsManager {

    private static final Logger logger = LoggerFactory.getLogger(LogsManager.class);

    private LogsManager() { // Empty private constructor
    }

    /**
     * Warn.
     *
     * @param requestId        the request id
     * @param transactionType  the transaction type
     * @param process          the process
     * @param processDuration  the process duration
     * @param msisdn           the msisdn
     * @param sourceSystem     the source system
     * @param targetSystem     the target system
     * @param response         the response
     * @param responseCode     the response code
     * @param responseMsg      the response msg
     * @param errorDescription the error description
     * @param requestPayload   the request payload
     * @param responsePayload  the response payload
     */

    public static void warn(String requestId, String transactionType, String process, String processDuration,
                            String msisdn, String sourceSystem, String targetSystem, String response, int responseCode,
                            String responseMsg, String errorDescription, String requestPayload, String responsePayload) {

        logger.warn(GlobalVariables.LOGGER_FORMAT, requestId, transactionType, process, processDuration, msisdn,
                sourceSystem, targetSystem, response, responseCode, responseMsg, errorDescription, requestPayload,
                responsePayload);
    }

    /**
     * Info.
     *
     * @param requestId        the request id
     * @param transactionType  the transaction type
     * @param process          the process
     * @param processDuration  the process duration
     * @param msisdn           the msisdn
     * @param sourceSystem     the source system
     * @param targetSystem     the target system
     * @param response         the response
     * @param responseCode     the response code
     * @param responseMsg      the response msg
     * @param errorDescription the error description
     * @param requestPayload   the request payload
     * @param responsePayload  the response payload
     */
    public static void info(String requestId, String transactionType, String process, String processDuration,
                            String msisdn, String sourceSystem, String targetSystem, String response, int responseCode,
                            String responseMsg, String errorDescription, String requestPayload,
                            String responsePayload) {

        logger.info(GlobalVariables.LOGGER_FORMAT, requestId, transactionType, process, processDuration, msisdn,
                sourceSystem, targetSystem, response, responseCode, responseMsg, errorDescription, requestPayload,
                responsePayload);
    }

    /**
     * Info.
     *
     * @param requestId        the request id
     * @param transactionType  the transaction type
     * @param process          the process
     * @param processDuration  the process duration
     * @param msisdn           the msisdn
     * @param sourceSystem     the source system
     * @param targetSystem     the target system
     * @param response         the response
     * @param responseCode     the response code
     * @param responseMsg      the response msg
     * @param errorDescription the error description
     * @param requestPayload   the request payload
     * @param responsePayload  the response payload
     * @param headers          the headers
     */
    public static void info(String requestId, String transactionType, String process, String processDuration,
                            String msisdn, String sourceSystem, String targetSystem, String response, int responseCode,
                            String responseMsg, String errorDescription, String requestPayload, String responsePayload,
                            String headers) {

        String loggerFormat = GlobalVariables.LOGGER_FORMAT + " | RequestHeaders={}";

        logger.info(loggerFormat, requestId, transactionType, process, processDuration, msisdn, sourceSystem,
                targetSystem, response, responseCode, responseMsg, errorDescription, requestPayload, responsePayload,
                headers);
    }

    /**
     * Error.
     *
     * @param requestId        the request id
     * @param transactionType  the transaction type
     * @param process          the process
     * @param processDuration  the process duration
     * @param msisdn           the msisdn
     * @param sourceSystem     the source system
     * @param targetSystem     the target system
     * @param response         the response
     * @param responseCode     the response code
     * @param responseMsg      the response msg
     * @param errorDescription the error description
     * @param requestPayload   the request payload
     * @param responsePayload  the response payload
     */
    public static void error(String requestId, String transactionType, String process, String processDuration,
                             String msisdn, String sourceSystem, String targetSystem, String response, int responseCode,
                             String responseMsg, String errorDescription, String requestPayload, String responsePayload) {

        logger.error(GlobalVariables.LOGGER_FORMAT, requestId, transactionType, process, processDuration, msisdn,
                sourceSystem, targetSystem, response, responseCode, responseMsg, errorDescription, requestPayload,
                responsePayload);
    }

}
