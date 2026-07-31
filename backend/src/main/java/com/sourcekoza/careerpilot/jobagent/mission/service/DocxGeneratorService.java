package com.sourcekoza.careerpilot.jobagent.mission.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcekoza.careerpilot.jobagent.agents.tailoring.TailoredResumeContent;
import com.sourcekoza.careerpilot.jobagent.mission.entity.TailoredResume;
import com.sourcekoza.careerpilot.jobagent.mission.repository.TailoredResumeRepository;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * Generates professional DOCX resume files from tailored resume content.
 *
 * <p>Uses Apache POI to create ATS-friendly documents with clean formatting.
 * Files are stored on disk at uploads/tailored/{userId}/{jobId}.docx</p>
 *
 * @since Sprint-16
 */
@Service
public class DocxGeneratorService {

    private static final Logger log = LoggerFactory.getLogger(DocxGeneratorService.class);

    private final ObjectMapper objectMapper;
    private final TailoredResumeRepository tailoredResumeRepository;
    private final String uploadDir;

    public DocxGeneratorService(ObjectMapper objectMapper,
                                 TailoredResumeRepository tailoredResumeRepository,
                                 @Value("${app.uploads.dir:uploads}") String uploadDir) {
        this.objectMapper = objectMapper;
        this.tailoredResumeRepository = tailoredResumeRepository;
        this.uploadDir = uploadDir;
        log.info("DocxGeneratorService initialized: uploadDir='{}'", uploadDir);
    }

    /**
     * Generates a DOCX file for a tailored resume and stores it on disk.
     *
     * @param tailoredResume the tailored resume entity with content
     * @param resumeTitle the candidate's name/title for the header
     * @return the file path of the generated DOCX
     */
    public String generateDocx(TailoredResume tailoredResume, String resumeTitle) {
        UUID userId = tailoredResume.getUserId();
        UUID jobId = tailoredResume.getJob().getId();

        Path dir = Paths.get(uploadDir, "tailored", userId.toString());
        Path filePath = dir.resolve(jobId + ".docx");

        try {
            Files.createDirectories(dir);

            try (XWPFDocument document = new XWPFDocument();
                 FileOutputStream out = new FileOutputStream(filePath.toFile())) {

                // Header: Name
                addHeading(document, resumeTitle, 18, true, ParagraphAlignment.CENTER);
                addEmptyLine(document);

                // Professional Summary
                addSectionHeading(document, "PROFESSIONAL SUMMARY");
                if (tailoredResume.getSummary() != null) {
                    addParagraph(document, tailoredResume.getSummary());
                }
                addEmptyLine(document);

                // Skills
                addSectionHeading(document, "SKILLS");
                List<String> skills = parseSkills(tailoredResume.getSkillsJson());
                if (!skills.isEmpty()) {
                    addParagraph(document, String.join(" • ", skills));
                }
                addEmptyLine(document);

                // Experience
                addSectionHeading(document, "EXPERIENCE");
                List<TailoredResumeContent.ExperienceEntry> experiences = parseExperiences(tailoredResume.getExperienceJson());
                for (TailoredResumeContent.ExperienceEntry exp : experiences) {
                    addExperienceEntry(document, exp);
                }
                addEmptyLine(document);

                // Education
                addSectionHeading(document, "EDUCATION");
                List<TailoredResumeContent.EducationEntry> educations = parseEducation(tailoredResume.getEducationJson());
                for (TailoredResumeContent.EducationEntry edu : educations) {
                    addEducationEntry(document, edu);
                }

                document.write(out);
            }

            // Update the entity with file path
            tailoredResume.setFilePath(filePath.toString());
            tailoredResumeRepository.save(tailoredResume);

            log.info("DOCX generated: {}", filePath);
            return filePath.toString();
        } catch (IOException e) {
            log.error("Failed to generate DOCX: {}", e.getMessage());
            throw new RuntimeException("DOCX generation failed: " + e.getMessage(), e);
        }
    }

    /**
     * Returns the path to the generated DOCX file.
     */
    public Path getDocxPath(TailoredResume tailoredResume) {
        if (tailoredResume.getFilePath() == null) {
            return null;
        }
        return Paths.get(tailoredResume.getFilePath());
    }

    private void addHeading(XWPFDocument doc, String text, int fontSize, boolean bold, ParagraphAlignment alignment) {
        XWPFParagraph para = doc.createParagraph();
        para.setAlignment(alignment);
        XWPFRun run = para.createRun();
        run.setText(text);
        run.setFontSize(fontSize);
        run.setBold(bold);
        run.setFontFamily("Calibri");
    }

    private void addSectionHeading(XWPFDocument doc, String text) {
        XWPFParagraph para = doc.createParagraph();
        para.setSpacingBefore(200);
        XWPFRun run = para.createRun();
        run.setText(text);
        run.setFontSize(12);
        run.setBold(true);
        run.setFontFamily("Calibri");
        // Add underline effect via border
        para.setBorderBottom(org.apache.poi.xwpf.usermodel.Borders.SINGLE);
    }

    private void addParagraph(XWPFDocument doc, String text) {
        XWPFParagraph para = doc.createParagraph();
        XWPFRun run = para.createRun();
        run.setText(text);
        run.setFontSize(10);
        run.setFontFamily("Calibri");
    }

    private void addExperienceEntry(XWPFDocument doc, TailoredResumeContent.ExperienceEntry exp) {
        // Position at Company
        XWPFParagraph titlePara = doc.createParagraph();
        titlePara.setSpacingBefore(100);
        XWPFRun titleRun = titlePara.createRun();
        titleRun.setText(exp.position() + " at " + exp.company());
        titleRun.setFontSize(10);
        titleRun.setBold(true);
        titleRun.setFontFamily("Calibri");

        // Location | Date range
        XWPFParagraph datePara = doc.createParagraph();
        XWPFRun dateRun = datePara.createRun();
        String dateText = (exp.location() != null ? exp.location() + " | " : "")
                + exp.startDate() + " - " + (exp.endDate() != null ? exp.endDate() : "Present");
        dateRun.setText(dateText);
        dateRun.setFontSize(9);
        dateRun.setItalic(true);
        dateRun.setFontFamily("Calibri");

        // Description
        if (exp.description() != null && !exp.description().isBlank()) {
            XWPFParagraph descPara = doc.createParagraph();
            XWPFRun descRun = descPara.createRun();
            descRun.setText(exp.description());
            descRun.setFontSize(10);
            descRun.setFontFamily("Calibri");
        }
    }

    private void addEducationEntry(XWPFDocument doc, TailoredResumeContent.EducationEntry edu) {
        XWPFParagraph para = doc.createParagraph();
        para.setSpacingBefore(100);
        XWPFRun run = para.createRun();
        String text = edu.degree();
        if (edu.fieldOfStudy() != null) text += " in " + edu.fieldOfStudy();
        text += " — " + edu.institution();
        run.setText(text);
        run.setFontSize(10);
        run.setBold(true);
        run.setFontFamily("Calibri");

        XWPFParagraph datePara = doc.createParagraph();
        XWPFRun dateRun = datePara.createRun();
        dateRun.setText(edu.startDate() + " - " + (edu.endDate() != null ? edu.endDate() : "Present"));
        dateRun.setFontSize(9);
        dateRun.setItalic(true);
        dateRun.setFontFamily("Calibri");
    }

    private void addEmptyLine(XWPFDocument doc) {
        doc.createParagraph();
    }

    private List<String> parseSkills(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            log.debug("Failed to parse skills JSON: {}", e.getMessage());
            return List.of();
        }
    }

    private List<TailoredResumeContent.ExperienceEntry> parseExperiences(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            log.debug("Failed to parse experience JSON: {}", e.getMessage());
            return List.of();
        }
    }

    private List<TailoredResumeContent.EducationEntry> parseEducation(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            log.debug("Failed to parse education JSON: {}", e.getMessage());
            return List.of();
        }
    }
}
