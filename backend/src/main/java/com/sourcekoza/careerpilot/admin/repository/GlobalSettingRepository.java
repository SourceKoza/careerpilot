package com.sourcekoza.careerpilot.admin.repository;

import com.sourcekoza.careerpilot.admin.entity.GlobalSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @since Sprint-17
 */
public interface GlobalSettingRepository extends JpaRepository<GlobalSetting, UUID> {
    Optional<GlobalSetting> findBySettingKey(String settingKey);
    List<GlobalSetting> findByCategory(String category);
    boolean existsBySettingKey(String settingKey);
}
