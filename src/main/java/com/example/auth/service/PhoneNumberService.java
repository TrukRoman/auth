package com.example.auth.service;

import com.example.auth.entity.PhoneNumber;
import com.example.auth.entity.User;
import com.example.auth.repository.PhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PhoneNumberService {

    private final PhoneRepository phoneRepository;

    @Transactional
    public void deletePhoneNumbersWhichNotBelongToUser(User user) {
        List<Long> ids = user.getPhoneNumbers().stream().map(PhoneNumber::getId).toList();
        phoneRepository.deleteByUserAndIdNotIn(user, ids);
    }
}
