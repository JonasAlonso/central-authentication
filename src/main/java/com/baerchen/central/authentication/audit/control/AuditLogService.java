package com.baerchen.central.authentication.audit.control;


import com.baerchen.central.authentication.audit.entity.AuditLog;

import com.baerchen.central.authentication.audit.infrastructure.persistence.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuditLogService {

    private final AuditLogRepository repository;

    public AuditLogService(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AuditLog save(AuditLog log) {
        if (log.getTimestamp() == null) {
            log.setTimestamp(LocalDateTime.now());
        }
        return repository.save(log);
    }

    public Optional<AuditLog> findById(Long id) {
        return repository.findById(id);
    }

    public List<AuditLog> findByClientId(String clientId) {
        return repository.findByClientId(clientId);
    }

    public List<AuditLog> findByClientIdAndTimeRange(String clientId, LocalDateTime start, LocalDateTime end) {
        return repository.findByClientIdAndTimestampBetween(clientId, start, end);
    }

    public List<AuditLog> findByClientIdAndStatusCode(String clientId, Integer statusCode) {
        return repository.findByClientIdAndStatusCode(clientId, statusCode);
    }

    public List<AuditLog> findByClientIdAndUserId(String clientId, String userId) {
        return repository.findByClientIdAndUserId(clientId, userId);
    }
}
