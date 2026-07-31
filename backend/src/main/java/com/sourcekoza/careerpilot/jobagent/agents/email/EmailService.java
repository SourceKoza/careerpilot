package com.sourcekoza.careerpilot.jobagent.agents.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Service for sending emails via SMTP.
 *
 * <p>When email is disabled (default for dev), it logs the email
 * content instead of actually sending it.</p>
 *
 * @since Sprint-15
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String fromAddress;
    private final boolean emailEnabled;

    public EmailService(JavaMailSender mailSender,
                        @Value("${app.email.from:careerpilot@sourcekoza.com}") String fromAddress,
                        @Value("${app.email.enabled:false}") boolean emailEnabled) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
        this.emailEnabled = emailEnabled;
        log.info("EmailService initialized: enabled={}, from='{}'", emailEnabled, fromAddress);
    }

    /**
     * Sends an email. If email is disabled, logs it instead.
     *
     * @return true if sent/logged successfully
     */
    public boolean sendEmail(String to, String subject, String body) {
        if (!emailEnabled) {
            log.info("📧 [DRY RUN] Email NOT sent (email.enabled=false)");
            log.info("  To: {}", to);
            log.info("  Subject: {}", subject);
            log.info("  Body:\n{}", body);
            return true; // Treat as success for status tracking
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email sent successfully: to='{}', subject='{}'", to, subject);
            return true;
        } catch (Exception e) {
            log.error("Failed to send email: to='{}', error='{}'", to, e.getMessage());
            return false;
        }
    }

    /**
     * Sends an email with a file attachment. If email is disabled, logs it instead.
     *
     * @param to recipient email
     * @param subject email subject
     * @param body email body text
     * @param attachmentPath path to the file to attach
     * @param attachmentName name for the attachment in the email
     * @return true if sent/logged successfully
     * @since Sprint-16
     */
    public boolean sendEmailWithAttachment(String to, String subject, String body,
                                            String attachmentPath, String attachmentName) {
        if (!emailEnabled) {
            log.info("📧 [DRY RUN] Email with attachment NOT sent (email.enabled=false)");
            log.info("  To: {}", to);
            log.info("  Subject: {}", subject);
            log.info("  Attachment: {} ({})", attachmentName, attachmentPath);
            log.info("  Body:\n{}", body);
            return true;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            FileSystemResource file = new FileSystemResource(new File(attachmentPath));
            helper.addAttachment(attachmentName, file);

            mailSender.send(mimeMessage);
            log.info("Email with attachment sent: to='{}', subject='{}', attachment='{}'",
                    to, subject, attachmentName);
            return true;
        } catch (MessagingException e) {
            log.error("Failed to send email with attachment: to='{}', error='{}'", to, e.getMessage());
            return false;
        }
    }
}
