package com.safaricom.microservice.he.mssdpheaderdecryptor.models.pojos.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

/**
 * @author Dickens.
 * Creating a send sms POJO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "LinkId",
        "Content"
})
public class SendSMS extends ApiRequest implements Serializable {
    @JsonProperty("LinkId")
    private String LinkId;

    @JsonProperty("Content")
    private String Content;

    public String getLinkId() {
        return LinkId;
    }

    public void setLinkId(String linkId) {
        LinkId = linkId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
