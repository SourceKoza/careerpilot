package com.sourcekoza.careerpilot.admin.entity;

import com.sourcekoza.careerpilot.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * Security configuration settings (rate limits, session rules, etc.).
 *
 * @since Sprint-17
 */
@Entity
@Table(name = "security_configs", indexes = {
        @Index(name = "idx_security_config_key", columnList = "config_key", unique = true)
})
public class SecurityConfig extends BaseEntity {

    @Column(name = "config_key", nullable = false, unique = true, length = 100)
    private String configKey;

    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;

    @Column(nullable = false)
    private boolean enabled = true;

    public SecurityConfig() {
    }

    public SecurityConfig(String configKey, String configValue, boolean enabled) {
        this.configKey = configKey;
        this.configValue = configValue;
        this.enabled = enabled;
    }

    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }
    public String getConfigValue() { return configValue; }
    public void setConfigValue(String configValue) { this.configValue = configValue; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
