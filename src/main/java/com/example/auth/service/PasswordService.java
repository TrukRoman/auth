package com.example.auth.service;

import com.example.auth.dto.ChangePasswordRequest;
import com.example.auth.dto.NewPasswordRequest;
import com.example.auth.entity.User;
import com.example.auth.exception.ServiceException;
import com.example.auth.repository.UserRepository;
import com.example.auth.util.JwtUtil;
import com.example.auth.util.SecurityUtils;
import com.example.auth.validator.password.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.auth.exception.ExceptionType.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class PasswordService {
    
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordValidator passwordValidator;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public void recoveryPasswordSendRequest(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
        String token = jwtUtil.generatePasswordRecoveryRequestToken(user);
        emailService.sendPasswordRecoveryEmail(user, token);
    }

    @Transactional
    public void setNewPassword(NewPasswordRequest request) {
        jwtUtil.validateJwtToken(request.token());
        String userEmail = jwtUtil.extractUserEmail(request.token());
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(request.newPassword()));
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = SecurityUtils.getUser();
        passwordValidator.isCorrectPreviousPassword(request.previousPassword(), user);
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setAccessToken(null);
        user.setRefreshToken(null);
        userRepository.save(user);
    }
    
}
