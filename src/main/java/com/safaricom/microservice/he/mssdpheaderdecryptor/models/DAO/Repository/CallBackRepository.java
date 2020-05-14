package com.safaricom.microservice.he.mssdpheaderdecryptor.models.DAO.Repository;

import com.safaricom.microservice.he.mssdpheaderdecryptor.models.DAO.Entity.CallBacks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallBackRepository extends JpaRepository<CallBacks, Long> {
    CallBacks findByRequestId(String requestId);
    CallBacks findByUrl(String url);
}
