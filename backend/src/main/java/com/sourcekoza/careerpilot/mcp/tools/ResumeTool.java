package com.sourcekoza.careerpilot.mcp.tools;

import com.sourcekoza.careerpilot.resume.dto.CreateResumeRequest;
import com.sourcekoza.careerpilot.resume.dto.ResumeResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeSummaryResponse;
import com.sourcekoza.careerpilot.resume.dto.ResumeVersionResponse;
import com.sourcekoza.careerpilot.resume.service.ResumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * MCP Resume Tool.
 *
 * <p>Exposes resume operations as MCP-discoverable tools for AI agents.
 * Delegates all business logic to {@link ResumeService}.</p>
 */
@Service
public class ResumeTool {

    private static final Logger log = LoggerFactory.getLogger(ResumeTool.class);

    private final ResumeService resumeService;

    public ResumeTool(ResumeService resumeService) {
        this.resumeService = resumeService;
        log.info("MCP ResumeTool initialized");
    }

    @Tool(description = "Creates a new resume for a user. Returns the full resume response with all sections.")
    public ResumeResponse createResume(
            @ToolParam(description = "The user ID (UUID) who owns the resume") String userId,
            @ToolParam(description = "The resume title") String title,
            @ToolParam(description = "A brief professional summary (optional)") String summary,
            @ToolParam(description = "The target job role (optional)") String targetRole) {
        log.debug("MCP ResumeTool.createResume invoked: userId={}, title={}", userId, title);

        CreateResumeRequest request = new CreateResumeRequest(
                title, summary, targetRole,
                List.of(), List.of(), java.util.Set.of(),
                java.util.Set.of(), List.of(), java.util.Set.of());

        ResumeResponse response = resumeService.createResume(UUID.fromString(userId), request);
        log.debug("MCP ResumeTool.createResume completed: resumeId={}", response.id());
        return response;
    }

    @Tool(description = "Retrieves a resume by ID for a specific user. Returns the full resume with all sections.")
    public ResumeResponse getResume(
            @ToolParam(description = "The user ID (UUID) who owns the resume") String userId,
            @ToolParam(description = "The resume ID (UUID) to retrieve") String resumeId) {
        log.debug("MCP ResumeTool.getResume invoked: userId={}, resumeId={}", userId, resumeId);

        ResumeResponse response = resumeService.getResume(
                UUID.fromString(userId), UUID.fromString(resumeId));
        log.debug("MCP ResumeTool.getResume completed: title={}", response.title());
        return response;
    }

    @Tool(description = "Lists all resumes for a user with pagination. Returns lightweight summaries.")
    public Page<ResumeSummaryResponse> listResumes(
            @ToolParam(description = "The user ID (UUID) who owns the resumes") String userId,
            @ToolParam(description = "Page number (0-based)") int page,
            @ToolParam(description = "Page size (number of items per page)") int size) {
        log.debug("MCP ResumeTool.listResumes invoked: userId={}, page={}, size={}", userId, page, size);

        Page<ResumeSummaryResponse> result = resumeService.listResumes(
                UUID.fromString(userId), PageRequest.of(page, size));
        log.debug("MCP ResumeTool.listResumes completed: totalElements={}", result.getTotalElements());
        return result;
    }

    @Tool(description = "Retrieves the version history for a resume. Returns versions ordered by version number descending.")
    public List<ResumeVersionResponse> listResumeVersions(
            @ToolParam(description = "The user ID (UUID) who owns the resume") String userId,
            @ToolParam(description = "The resume ID (UUID) to get versions for") String resumeId) {
        log.debug("MCP ResumeTool.listResumeVersions invoked: userId={}, resumeId={}", userId, resumeId);

        List<ResumeVersionResponse> versions = resumeService.getVersions(
                UUID.fromString(userId), UUID.fromString(resumeId));
        log.debug("MCP ResumeTool.listResumeVersions completed: count={}", versions.size());
        return versions;
    }
}
