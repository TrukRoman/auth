package com.example.auth.validator.password;

import com.example.auth.entity.User;
import com.example.auth.exception.ExceptionType;
import com.example.auth.exception.ServiceException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private final PasswordEncoder passwordEncoder;

    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~`!@#$%^&*()_\\-+={[}]|:\"'<,>.?/]).{8,100}$";

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        List<String> errorMessages = new ArrayList<>();

        if (StringUtils.isBlank(password)) {
            errorMessages.add("Password cannot be blank.");
        } else {
            if (!Pattern.matches(PASSWORD_PATTERN, password)) {
                if (!password.matches(".*[a-z].*")) {
                    errorMessages.add("At least one lowercase letter required.");
                }
                if (!password.matches(".*[A-Z].*")) {
                    errorMessages.add("At least one uppercase letter required.");
                }
                if (!password.matches(".*\\d.*")) {
                    errorMessages.add("At least one digit (0-9) required.");
                }
                if (!password.matches(".*[~`!@#$%^&*()_\\-+={[}]|:\"'<,>.?/].*")) {
                    errorMessages.add("At least one special character required.");
                }
                if (password.length() < 8) {
                    errorMessages.add("Minimum length of 8 characters required.");
                }
                if (password.length() > 100) {
                    errorMessages.add("Maximum length of 100 characters exceeded.");
                }
            }
        }

        if (!errorMessages.isEmpty()) {
            context.disableDefaultConstraintViolation();
            for (String errorMessage : errorMessages) {
                context.buildConstraintViolationWithTemplate(errorMessage)
                        .addConstraintViolation();
            }
        }

        return errorMessages.isEmpty();
    }

    public void isCorrectPreviousPassword(String previousPassword, User user) {
        if (!passwordEncoder.matches(previousPassword, user.getPassword())) {
            throw new ServiceException(ExceptionType.PREVIOUS_PASSWORD_NOT_MATCH);
        }
    }
}

