package com.sourcekoza.careerpilot.resume.repository;

import com.sourcekoza.careerpilot.resume.domain.Experience;
import com.sourcekoza.careerpilot.resume.domain.Resume;
import com.sourcekoza.careerpilot.resume.domain.Skill;
import com.sourcekoza.careerpilot.resume.domain.SkillProficiency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@EnableJpaAuditing
@ActiveProfiles("local")
class ResumeRepositoryTest {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("findByIdAndUserIdAndDeletedAtIsNull - returns resume for correct user")
    void findByIdAndUserIdAndDeletedAtIsNull_correctUser() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setTitle("Test Resume");

        Resume saved = entityManager.persistAndFlush(resume);

        // Act
        Optional<Resume> result = resumeRepository.findByIdAndUserIdAndDeletedAtIsNull(saved.getId(), userId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test Resume");
        assertThat(result.get().getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("findByIdAndUserIdAndDeletedAtIsNull - returns empty for wrong user")
    void findByIdAndUserIdAndDeletedAtIsNull_wrongUser() {
        // Arrange
        UUID userA = UUID.randomUUID();
        UUID userB = UUID.randomUUID();
        Resume resume = new Resume();
        resume.setUserId(userA);
        resume.setTitle("User A Resume");

        Resume saved = entityManager.persistAndFlush(resume);

        // Act
        Optional<Resume> result = resumeRepository.findByIdAndUserIdAndDeletedAtIsNull(saved.getId(), userB);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByIdAndUserIdAndDeletedAtIsNull - returns empty for soft-deleted resume")
    void findByIdAndUserIdAndDeletedAtIsNull_softDeleted() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setTitle("Deleted Resume");
        resume.setDeletedAt(Instant.now());

        Resume saved = entityManager.persistAndFlush(resume);

        // Act
        Optional<Resume> result = resumeRepository.findByIdAndUserIdAndDeletedAtIsNull(saved.getId(), userId);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByUserIdAndDeletedAtIsNull - returns paginated results")
    void findByUserIdAndDeletedAtIsNull_pagination() {
        // Arrange
        UUID userId = UUID.randomUUID();
        for (int i = 1; i <= 3; i++) {
            Resume resume = new Resume();
            resume.setUserId(userId);
            resume.setTitle("Resume " + i);
            entityManager.persist(resume);
        }
        entityManager.flush();

        // Act
        Page<Resume> result = resumeRepository.findByUserIdAndDeletedAtIsNull(userId, PageRequest.of(0, 2));

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("cascade persist - saving Resume with Experience child persists Experience")
    void cascadePersist_experience() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setTitle("Resume with Experience");

        Experience experience = new Experience();
        experience.setResume(resume);
        experience.setCompanyName("Acme Corp");
        experience.setPosition("Developer");
        experience.setStartDate(LocalDate.of(2020, 1, 1));
        experience.setCurrentlyWorking(true);
        resume.getExperiences().add(experience);

        // Act
        Resume saved = entityManager.persistAndFlush(resume);
        entityManager.clear();

        // Assert
        Resume found = entityManager.find(Resume.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getExperiences()).hasSize(1);
        assertThat(found.getExperiences().get(0).getCompanyName()).isEqualTo("Acme Corp");
    }

    @Test
    @DisplayName("orphan removal - removing Skill from collection deletes it")
    void orphanRemoval_skill() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setTitle("Resume with Skill");

        Skill skill = new Skill();
        skill.setResume(resume);
        skill.setName("Java");
        skill.setProficiency(SkillProficiency.ADVANCED);
        resume.getSkills().add(skill);

        Resume saved = entityManager.persistAndFlush(resume);
        entityManager.clear();

        // Act
        Resume found = entityManager.find(Resume.class, saved.getId());
        found.getSkills().clear();
        entityManager.persistAndFlush(found);
        entityManager.clear();

        // Assert
        Resume reloaded = entityManager.find(Resume.class, saved.getId());
        assertThat(reloaded.getSkills()).isEmpty();
    }
}
