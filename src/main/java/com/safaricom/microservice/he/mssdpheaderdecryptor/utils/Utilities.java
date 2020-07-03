package com.safaricom.microservice.he.mssdpheaderdecryptor.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

import static com.safaricom.microservice.he.mssdpheaderdecryptor.utils.GlobalVariables.REQUEST_DATE_TIME_FORMAT;


public class Utilities {

    private Utilities() { // Empty Constructor
    }

    /**
     * Gets formatted timestamp.
     *
     * @return the formatted timestamp
     */
    public static String getFormattedTimestamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(REQUEST_DATE_TIME_FORMAT);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return simpleDateFormat.format(timestamp);
    }

    /**
     * Generate tracking id string.
     *
     * @return the string
     */
    public static String generateTrackingID() {
        return UUID.randomUUID().toString();
    }

    public static String parseToJsonString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public static String nullSafe(Object obj) {
        return obj == null ? "" : String.valueOf(obj);
    }

}
