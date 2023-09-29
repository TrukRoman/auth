package com.example.auth.repository;

import com.example.auth.entity.PhoneNumber;
import com.example.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhoneRepository extends JpaRepository<PhoneNumber, Long> {

    void deleteByUserAndIdNotIn(User user, List<Long> ids);
}
