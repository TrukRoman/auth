package com.example.auth.service;

import com.example.auth.entity.User;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    private static final String SUCCESSFUL_REGISTRATION = "templates/successful_registration";
    private static final String USER_REMOVED_TEMPLATE = "templates/user_removed";
    private static final String PASSWORD_RECOVERY_TEMPLATE = "templates/password_recovery";
    private static final String PASSWORD_RECOVERY_URL = "https://app.com/reset-password?token=";

    private final JavaMailSender javaMailSender;

    public void sendSuccessfulRegistrationEmail(User user) {
        Map<String, Object> templateValues = Map.of(
                FIRST_NAME, user.getFirstName(),
                LAST_NAME, user.getLastName()
        );

        String htmlContent = parseThymeleafTemplate(templateValues, SUCCESSFUL_REGISTRATION);
        sendEmail(htmlContent, user.getEmail());
    }

    public void sendPasswordRecoveryEmail(User user,
                                          String token) {
        log.info("Password recovery token {} was sent for user {}", token, user.getEmail());
        Map<String, Object> templateValues = Map.of(
                FIRST_NAME, user.getFirstName(),
                LAST_NAME, user.getLastName(),
                "passwordRecoveryLink", generatePasswordRecoveryLink(token)
        );

        String htmlContent = parseThymeleafTemplate(templateValues, PASSWORD_RECOVERY_TEMPLATE);
        sendEmail(htmlContent, user.getEmail());
    }

    public void sendUserRemovedEmail(List<User> deletedUsers) {
        deletedUsers.forEach(user -> {
            Map<String, Object> templateValues = Map.of(
                    FIRST_NAME, user.getFirstName(),
                    LAST_NAME, user.getLastName()
            );

            String htmlContent = parseThymeleafTemplate(templateValues, USER_REMOVED_TEMPLATE);
            sendEmail(htmlContent, user.getEmail());
        });
    }

    private String parseThymeleafTemplate(Map<String, Object> templateValues,
                                          String templatePath) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        for (Map.Entry<String, Object> entry : templateValues.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return templateEngine.process(templatePath, context);
    }

    private void sendEmail(String htmlContent,
                           String email) {
        log.info("Sending email to {}", email);
//        MimeMessage message = javaMailSender.createMimeMessage();
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setTo(email);
//            helper.setText(htmlContent, true);
//
//            javaMailSender.send(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private String generatePasswordRecoveryLink(String token) {
        return PASSWORD_RECOVERY_URL + token;
    }
}
