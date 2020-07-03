package com.safaricom.microservice.he.mssdpheaderdecryptor.models.DAO.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Dickens
 */
@Entity
@Table(name = "callback")
public class CallBacks extends BaseModel implements Serializable {
    @Column(length=200, nullable = false, name = "request_id")
    private String requestId;

    @Column(length=200, nullable = false)
    private String url;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
