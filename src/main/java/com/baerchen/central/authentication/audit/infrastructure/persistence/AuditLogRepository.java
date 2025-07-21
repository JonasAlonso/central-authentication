package com.baerchen.central.authentication.audit.infrastructure.persistence;

import com.baerchen.central.authentication.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByClientId(String clientId);

    List<AuditLog> findByClientIdAndTimestampBetween(String clientId, LocalDateTime start, LocalDateTime end);

    List<AuditLog> findByClientIdAndStatusCode(String clientId, Integer statusCode);

    List<AuditLog> findByClientIdAndUserId(String clientId, String userId);

}
