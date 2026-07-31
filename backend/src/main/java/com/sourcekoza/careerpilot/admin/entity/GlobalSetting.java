package com.sourcekoza.careerpilot.admin.entity;

import com.sourcekoza.careerpilot.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * Key-value store for global platform configuration.
 *
 * @since Sprint-17
 */
@Entity
@Table(name = "global_settings", indexes = {
        @Index(name = "idx_global_setting_key", columnList = "setting_key", unique = true),
        @Index(name = "idx_global_setting_category", columnList = "category")
})
public class GlobalSetting extends BaseEntity {

    @Column(name = "setting_key", nullable = false, unique = true, length = 100)
    private String settingKey;

    @Column(name = "setting_value", columnDefinition = "TEXT")
    private String settingValue;

    @Column(length = 50)
    private String category;

    @Column(length = 500)
    private String description;

    public GlobalSetting() {
    }

    public GlobalSetting(String settingKey, String settingValue, String category, String description) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.category = category;
        this.description = description;
    }

    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String settingKey) { this.settingKey = settingKey; }
    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { this.settingValue = settingValue; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
