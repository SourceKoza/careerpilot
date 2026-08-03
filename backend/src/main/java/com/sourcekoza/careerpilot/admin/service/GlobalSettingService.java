package com.sourcekoza.careerpilot.admin.service;

import com.sourcekoza.careerpilot.admin.dto.GlobalSettingCreateRequest;
import com.sourcekoza.careerpilot.admin.dto.GlobalSettingResponse;
import com.sourcekoza.careerpilot.admin.entity.GlobalSetting;
import com.sourcekoza.careerpilot.admin.repository.GlobalSettingRepository;
import com.sourcekoza.careerpilot.exception.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages global platform settings. Seeds defaults on first run.
 *
 * @since Sprint-17
 */
@Service
public class GlobalSettingService {

    private static final Logger log = LoggerFactory.getLogger(GlobalSettingService.class);
    private final GlobalSettingRepository repository;

    public GlobalSettingService(GlobalSettingRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    @Transactional
    public void seedDefaults() {
        Map<String, String[]> defaults = Map.ofEntries(
                Map.entry("ai.model", new String[]{"qwen2.5:7b", "ai", "LLM model name"}),
                Map.entry("ai.temperature", new String[]{"0.3", "ai", "LLM temperature"}),
                Map.entry("ai.provider", new String[]{"ollama", "ai", "LLM provider"}),
                Map.entry("email.enabled", new String[]{"false", "email", "Enable email sending"}),
                Map.entry("email.from", new String[]{"careerpilot@sourcekoza.com", "email", "From address"}),
                Map.entry("email.daily_limit", new String[]{"50", "email", "Max emails per day"}),
                Map.entry("platform.linkedin.enabled", new String[]{"true", "platform", "LinkedIn adapter enabled"}),
                Map.entry("platform.indeed.enabled", new String[]{"true", "platform", "Indeed adapter enabled"}),
                Map.entry("platform.greenhouse.enabled", new String[]{"true", "platform", "Greenhouse adapter enabled"}),
                Map.entry("security.max_login_attempts", new String[]{"5", "security", "Max failed logins before lockout"}),
                Map.entry("security.session_timeout_minutes", new String[]{"1440", "security", "Session timeout in minutes"}),
                Map.entry("mission.max_concurrent", new String[]{"3", "mission", "Max concurrent missions per user"}),
                Map.entry("mission.auto_apply_threshold", new String[]{"80", "mission", "Min score for auto-apply"})
        );

        int seeded = 0;
        for (Map.Entry<String, String[]> entry : defaults.entrySet()) {
            if (!repository.existsBySettingKey(entry.getKey())) {
                String[] vals = entry.getValue();
                repository.save(new GlobalSetting(entry.getKey(), vals[0], vals[1], vals[2]));
                seeded++;
            }
        }
        if (seeded > 0) {
            log.info("Seeded {} default global settings", seeded);
        }
    }

    public Map<String, List<GlobalSettingResponse>> getAllGrouped() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.groupingBy(r -> r.category() != null ? r.category() : "general"));
    }

    public GlobalSettingResponse getByKey(String key) {
        return toResponse(repository.findBySettingKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("GlobalSetting", "settingKey", key)));
    }

    @Transactional
    public GlobalSettingResponse updateByKey(String key, String value) {
        GlobalSetting setting = repository.findBySettingKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("GlobalSetting", "settingKey", key));
        setting.setSettingValue(value);
        return toResponse(repository.save(setting));
    }

    @Transactional
    public GlobalSettingResponse create(GlobalSettingCreateRequest request) {
        if (repository.existsBySettingKey(request.key())) {
            throw new IllegalArgumentException("Setting already exists: " + request.key());
        }
        GlobalSetting setting = new GlobalSetting(request.key(), request.value(), request.category(), request.description());
        return toResponse(repository.save(setting));
    }

    public String getValue(String key, String defaultValue) {
        return repository.findBySettingKey(key)
                .map(GlobalSetting::getSettingValue)
                .orElse(defaultValue);
    }

    private GlobalSettingResponse toResponse(GlobalSetting s) {
        return new GlobalSettingResponse(s.getId(), s.getSettingKey(), s.getSettingValue(),
                s.getCategory(), s.getDescription(), s.getUpdatedAt());
    }
}
