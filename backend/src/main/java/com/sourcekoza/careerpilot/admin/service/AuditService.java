package com.sourcekoza.careerpilot.admin.service;

import com.sourcekoza.careerpilot.admin.dto.AuditLogResponse;
import com.sourcekoza.careerpilot.admin.entity.AuditLog;
import com.sourcekoza.careerpilot.admin.event.AdminActionEvent;
import com.sourcekoza.careerpilot.admin.repository.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Listens to AdminActionEvents and persists audit logs.
 *
 * @since Sprint-17
 */
@Service
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);
    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Async
    @EventListener
    public void handleAdminAction(AdminActionEvent event) {
        AuditLog entry = new AuditLog();
        entry.setAdminId(event.adminId());
        entry.setAction(event.action());
        entry.setTargetType(event.targetType());
        entry.setTargetId(event.targetId());
        entry.setDetails(event.details());
        entry.setIpAddress(event.ipAddress());
        repository.save(entry);
        log.info("Audit: {} {} {} by admin {}", event.action(), event.targetType(), event.targetId(), event.adminId());
    }

    public Page<AuditLogResponse> getAuditLogs(Pageable pageable) {
        return repository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::toResponse);
    }

    private AuditLogResponse toResponse(AuditLog e) {
        return new AuditLogResponse(e.getId(), e.getAdminId(), e.getAction(),
                e.getTargetType(), e.getTargetId(), e.getDetails(),
                e.getIpAddress(), e.getCreatedAt());
    }
}
