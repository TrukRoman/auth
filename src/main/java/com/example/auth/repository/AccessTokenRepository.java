package com.example.auth.repository;

import com.example.auth.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    AccessToken findByToken(String token);

    @Query("SELECT a FROM AccessToken a WHERE a.isRevoked = false AND a.isExpired = false AND a.user.id = :userId")
    List<AccessToken> findActiveTokens(@Param("userId") Long userId);

    @Query("SELECT at FROM AccessToken at WHERE at.refreshToken.token = :refreshToken AND at.isRevoked = false AND at.isExpired = false")
    List<AccessToken> findActiveAccessTokensByRefreshToken(@Param("refreshToken") String refreshToken);


}
