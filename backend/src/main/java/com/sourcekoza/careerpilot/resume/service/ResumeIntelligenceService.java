package com.sourcekoza.careerpilot.resume.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcekoza.careerpilot.ai.llm.LlmService;
import com.sourcekoza.careerpilot.exception.ResourceNotFoundException;
import com.sourcekoza.careerpilot.resume.domain.Education;
import com.sourcekoza.careerpilot.resume.domain.Experience;
import com.sourcekoza.careerpilot.resume.domain.Resume;
import com.sourcekoza.careerpilot.resume.domain.Skill;
import com.sourcekoza.careerpilot.resume.dto.ResumeAnalysisResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeAnalysisResponse.ATSScore;
import com.sourcekoza.careerpilot.resume.dto.ResumeAnalysisResponse.SkillGap;
import com.sourcekoza.careerpilot.resume.dto.ResumeAnalysisResponse.Suggestion;
import com.sourcekoza.careerpilot.resume.dto.ResumeFileUploadResponse;
import com.sourcekoza.careerpilot.resume.repository.ResumeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for resume file upload and AI-powered analysis using local LLM (Ollama).
 *
 * @since Sprint-15
 */
@Service
@Transactional(readOnly = true)
public class ResumeIntelligenceService {

    private static final Logger log = LoggerFactory.getLogger(ResumeIntelligenceService.class);
    private static final String UPLOAD_DIR = "uploads/resumes";

    private final ResumeRepository resumeRepository;
    private final LlmService llmService;
    private final ObjectMapper objectMapper;

    public ResumeIntelligenceService(ResumeRepository resumeRepository,
                                      LlmService llmService,
                                      ObjectMapper objectMapper) {
        this.resumeRepository = resumeRepository;
        this.llmService = llmService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public ResumeFileUploadResponse uploadResume(UUID userId, MultipartFile file) throws IOException {
        log.info("Uploading resume for user: {}, file: {}", userId, file.getOriginalFilename());

        String fileName = file.getOriginalFilename();
        String fileType = extractFileType(fileName);

        Path uploadPath = Path.of(UPLOAD_DIR, userId.toString());
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(UUID.randomUUID() + "." + fileType);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Resume resume = resumeRepository.findByUserIdAndDeletedAtIsNull(userId,
                        org.springframework.data.domain.PageRequest.of(0, 1))
                .stream().findFirst().orElse(null);

        if (resume == null) {
            resume = Resume.builder()
                    .userId(userId)
                    .title(stripExtension(fileName))
                    .summary("Uploaded resume: " + fileName)
                    .targetRole("Software Engineer")
                    .build();
            resume.setUserId(userId);
            resume = resumeRepository.save(resume);
        } else {
            resume.setTitle(stripExtension(fileName));
            resume = resumeRepository.save(resume);
        }

        return new ResumeFileUploadResponse(
                resume.getId(), resume.getTitle(), fileName, fileType,
                file.getSize(), resume.getCreatedAt(), Instant.now(), true);
    }

    public ResumeFileUploadResponse getResumeFile(UUID userId) {
        Resume resume = findUserResume(userId);
        // Estimate file size from content length
        String content = buildResumeText(resume);
        long estimatedSize = content.getBytes().length;
        return new ResumeFileUploadResponse(
                resume.getId(), resume.getTitle(), resume.getTitle() + ".pdf",
                "pdf", estimatedSize, resume.getCreatedAt(), resume.getUpdatedAt(), true);
    }

    /**
     * Performs AI-powered analysis of the resume using local Ollama LLM.
     *
     * <p>Sends the structured resume content to the LLM and asks it to:
     * - Score the resume (ATS compatibility)
     * - Identify strengths
     * - Find missing skills for the target role
     * - Provide actionable improvement suggestions</p>
     */
    public ResumeAnalysisResponse analyzeResume(UUID userId) {
        log.info("AI-powered resume analysis starting for user: {}", userId);

        Resume resume = findUserResume(userId);
        String resumeContent = buildResumeText(resume);

        String systemPrompt = """
                You are an expert resume analyst and career coach. Analyze the resume provided and return a JSON response with exactly this structure:
                {
                  "atsScore": {"overall": 0-100, "formatting": 0-100, "keywords": 0-100, "experience": 0-100},
                  "strengths": ["skill1", "skill2", ...up to 8 items],
                  "missingSkills": [{"skill": "name", "importance": "high|medium|low"}, ...up to 5 items],
                  "suggestions": [{"id": "s1", "text": "suggestion text", "category": "content|formatting|keywords"}, ...up to 5 items],
                  "keywordMatch": 0-100
                }
                
                Scoring guidelines:
                - overall: General ATS compatibility (consider formatting, keywords, quantified achievements)
                - formatting: Structure, readability, section organization
                - keywords: Relevant technical keywords for the target role
                - experience: Relevance and depth of work experience
                - keywordMatch: How well skills match current market demand for the target role
                
                For missingSkills, identify skills that are in high demand for the target role but missing from the resume.
                For suggestions, provide specific, actionable improvements.
                
                IMPORTANT: Return ONLY valid JSON. No markdown, no explanation, no code blocks. Just the raw JSON object.
                """;

        String userPrompt = "Analyze this resume:\n\n" + resumeContent;

        try {
            String llmResponse = llmService.chat(systemPrompt, userPrompt);
            log.info("LLM analysis received: {} chars", llmResponse.length());
            return parseLlmResponse(llmResponse);
        } catch (Exception e) {
            log.warn("LLM analysis failed, falling back to rule-based: {}", e.getMessage());
            return fallbackAnalysis(resume);
        }
    }

    private ResumeAnalysisResponse parseLlmResponse(String response) {
        try {
            // Strip any markdown code fences if present
            String json = response.trim();
            if (json.startsWith("```")) {
                json = json.replaceAll("```json\\s*", "").replaceAll("```\\s*", "");
            }

            JsonNode root = objectMapper.readTree(json);

            JsonNode atsNode = root.get("atsScore");
            ATSScore atsScore = new ATSScore(
                    atsNode.get("overall").asInt(),
                    atsNode.get("formatting").asInt(),
                    atsNode.get("keywords").asInt(),
                    atsNode.get("experience").asInt()
            );

            List<String> strengths = new ArrayList<>();
            if (root.has("strengths")) {
                root.get("strengths").forEach(s -> strengths.add(s.asText()));
            }

            List<SkillGap> missingSkills = new ArrayList<>();
            if (root.has("missingSkills")) {
                root.get("missingSkills").forEach(s -> missingSkills.add(
                        new SkillGap(s.get("skill").asText(), s.get("importance").asText())));
            }

            List<Suggestion> suggestions = new ArrayList<>();
            if (root.has("suggestions")) {
                root.get("suggestions").forEach(s -> suggestions.add(
                        new Suggestion(s.get("id").asText(), s.get("text").asText(), s.get("category").asText())));
            }

            int keywordMatch = root.has("keywordMatch") ? root.get("keywordMatch").asInt() : 70;

            return new ResumeAnalysisResponse(atsScore, strengths, missingSkills, suggestions, keywordMatch);
        } catch (Exception e) {
            log.error("Failed to parse LLM response: {}", e.getMessage());
            throw new RuntimeException("Failed to parse LLM analysis response", e);
        }
    }

    private String buildResumeText(Resume resume) {
        StringBuilder sb = new StringBuilder();

        sb.append("Title: ").append(resume.getTitle()).append("\n");
        if (resume.getTargetRole() != null) {
            sb.append("Target Role: ").append(resume.getTargetRole()).append("\n");
        }
        if (resume.getSummary() != null) {
            sb.append("Summary: ").append(resume.getSummary()).append("\n");
        }

        if (!resume.getSkills().isEmpty()) {
            sb.append("\nSkills:\n");
            for (Skill skill : resume.getSkills()) {
                sb.append("- ").append(skill.getName());
                if (skill.getProficiency() != null) {
                    sb.append(" (").append(skill.getProficiency()).append(")");
                }
                sb.append("\n");
            }
        }

        if (!resume.getExperiences().isEmpty()) {
            sb.append("\nExperience:\n");
            for (Experience exp : resume.getExperiences()) {
                sb.append("- ").append(exp.getPosition()).append(" at ").append(exp.getCompanyName());
                if (exp.getStartDate() != null) {
                    sb.append(" (").append(exp.getStartDate()).append(" - ");
                    sb.append(exp.isCurrentlyWorking() ? "Present" : exp.getEndDate()).append(")");
                }
                sb.append("\n");
                if (exp.getDescription() != null) {
                    sb.append("  ").append(exp.getDescription()).append("\n");
                }
            }
        }

        if (!resume.getEducations().isEmpty()) {
            sb.append("\nEducation:\n");
            for (Education edu : resume.getEducations()) {
                sb.append("- ").append(edu.getDegree()).append(" in ").append(edu.getFieldOfStudy());
                sb.append(" from ").append(edu.getInstitution()).append("\n");
            }
        }

        if (!resume.getProjects().isEmpty()) {
            sb.append("\nProjects:\n");
            resume.getProjects().forEach(p ->
                    sb.append("- ").append(p.getName()).append(": ").append(p.getDescription()).append("\n"));
        }

        return sb.toString();
    }

    /**
     * Fallback rule-based analysis when LLM is unavailable.
     */
    private ResumeAnalysisResponse fallbackAnalysis(Resume resume) {
        int formatting = 50 + (resume.getSummary() != null ? 15 : 0)
                + (!resume.getExperiences().isEmpty() ? 15 : 0)
                + (!resume.getEducations().isEmpty() ? 10 : 0)
                + (!resume.getSkills().isEmpty() ? 10 : 0);
        formatting = Math.min(100, formatting);

        int keywords = 40 + Math.min(60, resume.getSkills().size() * 8);
        keywords = Math.min(100, keywords);

        int experience = 40 + Math.min(40, resume.getExperiences().size() * 15)
                + (resume.getTargetRole() != null ? 10 : 0)
                + (!resume.getProjects().isEmpty() ? 10 : 0);
        experience = Math.min(100, experience);

        int overall = (formatting + keywords + experience) / 3;
        ATSScore atsScore = new ATSScore(overall, formatting, keywords, experience);

        List<String> strengths = resume.getSkills().stream()
                .map(Skill::getName).limit(8).collect(Collectors.toList());

        List<SkillGap> gaps = List.of(
                new SkillGap("Kubernetes", "high"),
                new SkillGap("Terraform", "high"),
                new SkillGap("AWS Lambda", "high"),
                new SkillGap("GraphQL", "medium"),
                new SkillGap("Go", "medium"));

        List<Suggestion> suggestions = List.of(
                new Suggestion("s1", "Add quantified achievements (e.g., 'reduced latency by 40%')", "content"),
                new Suggestion("s2", "Include target role keywords in your summary", "keywords"),
                new Suggestion("s3", "Add links to GitHub or portfolio projects", "content"),
                new Suggestion("s4", "Use consistent formatting with clear section headers", "formatting"));

        return new ResumeAnalysisResponse(atsScore, strengths, gaps, suggestions, Math.min(95, keywords + 10));
    }

    private Resume findUserResume(UUID userId) {
        return resumeRepository.findByUserIdAndDeletedAtIsNull(userId,
                        org.springframework.data.domain.PageRequest.of(0, 1))
                .stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Resume", "userId", userId));
    }

    private String extractFileType(String fileName) {
        if (fileName == null) return "pdf";
        int dot = fileName.lastIndexOf('.');
        return dot < 0 ? "pdf" : fileName.substring(dot + 1).toLowerCase();
    }

    private String stripExtension(String fileName) {
        if (fileName == null) return "Resume";
        int dot = fileName.lastIndexOf('.');
        return dot < 0 ? fileName : fileName.substring(0, dot);
    }
}
