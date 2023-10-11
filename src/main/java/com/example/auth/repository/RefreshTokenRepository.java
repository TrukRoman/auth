package com.example.auth.repository;

import com.example.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByToken(String token);

    @Query("SELECT a FROM RefreshToken a WHERE a.isRevoked = false AND a.isExpired = false AND a.user.id = :userId")
    List<RefreshToken> findActiveTokens(@Param("userId") Long userId);
}
