package com.example.auth.service;

import com.example.auth.dto.RegistrationUserRequest;
import com.example.auth.dto.UpdateUserRequest;
import com.example.auth.dto.UpdateUserResponse;
import com.example.auth.dto.UserDetailsDto;
import com.example.auth.dto.UserInfoDto;
import com.example.auth.dto.UserInfoEditRequest;
import com.example.auth.entity.PhoneNumber;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.exception.ExceptionType;
import com.example.auth.exception.ServiceException;
import com.example.auth.mapper.UserMapper;
import com.example.auth.repository.UserRepository;
import com.example.auth.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.auth.exception.ExceptionType.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneNumberService phoneNumberService;

    @Transactional
    public void register(RegistrationUserRequest request) {
        if (userRepository.existsUserByEmail(request.email())) {
            throw new ServiceException(ExceptionType.EXISTS_USER_WITH_SUCH_EMAIL);
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);

        List<PhoneNumber> phoneNumbers = createPhoneNumbers(request.phoneNumbers(), user);
        user.setPhoneNumbers(phoneNumbers);

        User savedUser = userRepository.save(user);
        emailService.sendSuccessfulRegistrationEmail(savedUser);
    }

    public Page<UserInfoDto> getAllUsersWithUserRole(Pageable pageable) {
        Page<User> users = userRepository.findAllByRole(Role.USER, pageable);
        return userMapper.mapToUserDto(users);
    }

    public UserDetailsDto getUserInfo(String email) {
        return userRepository.findUserByEmail(email)
                .map(userMapper::mapToUserDetails)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
    }

    public UserDetailsDto getUserDetails(String email) {
        return userRepository.findUserByEmail(email)
                .map(userMapper::mapToUserDetails)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
    }

    public Page<UserDetailsDto> searchUserByFirstNameAndLastName(String firstname,
                                                                 String lastname,
                                                                 Pageable pageable) {
        Page<User> usersPage = userRepository.searchUserByFirstNameAndLastNameAndRole(firstname,
                lastname, Role.USER, pageable);
        return userMapper.mapToUserDetails(usersPage);
    }

    @Transactional
    public void removeUsers(List<Long> ids) {
        List<User> usersByIds = userRepository.findAllById(ids);
        if (usersByIds.stream().anyMatch(user -> Role.ADMIN == user.getRole())) {
            throw new ServiceException(ExceptionType.INVALID_REQUEST, "Admin cannot be deleted");
        }
        userRepository.deleteAllById(ids);
        emailService.sendUserRemovedEmail(usersByIds);
    }

    @Transactional
    public UserInfoDto updateCurrentUserInfo(UserInfoEditRequest request) {
        User user = userMapper.mapUserInfoEditRequestToUser(request, SecurityUtils.getUser());

        if (request.phoneNumbers() != null) {
            user.setPhoneNumbers(createPhoneNumbers(request.phoneNumbers(), user));
        }

        User updatedUser = userRepository.save(user);
        phoneNumberService.deletePhoneNumbersWhichNotBelongToUser(updatedUser);
        return userMapper.mapToUserDto(updatedUser);
    }

    @Transactional
    public UpdateUserResponse updateUserInfoById(UpdateUserRequest request) {
        User dbUser = userRepository.findById(request.id())
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND, "User not found by id: " + request.id()));

        if (request.phoneNumbers() != null) {
            dbUser.setPhoneNumbers(createPhoneNumbers(request.phoneNumbers(), dbUser));
        }

        User updatedUser = userRepository.save(dbUser);
        phoneNumberService.deletePhoneNumbersWhichNotBelongToUser(updatedUser);
        return userMapper.mapToUpdateUserResponse(updatedUser);
    }

    public UserDetailsDto getAdminInfo() {
        User adminUser = userRepository.findAdmin().orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
        return userMapper.mapToUserDetails(adminUser);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
    }

    private List<PhoneNumber> createPhoneNumbers(List<String> phoneNumbers, User user) {
        return phoneNumbers.stream().map(phoneNumber -> {
            PhoneNumber phone = new PhoneNumber();
            phone.setPhoneNumber(phoneNumber);
            phone.setUser(user);
            return phone;
        }).collect(Collectors.toList());
    }
}
