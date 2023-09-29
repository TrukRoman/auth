package com.example.auth.repository;

import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByRole(Role role, Pageable pageable);

    boolean existsUserByEmail(String email);

    Optional<User> findUserByEmail(String email);

    Page<User> searchUserByFirstNameAndLastNameAndRole(String firstname, String lastname, Role role, Pageable pageable);

    @Query(value = "select u from User u where u.role='ADMIN'")
    Optional<User> findAdmin();
}
