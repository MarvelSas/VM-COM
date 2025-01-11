package com.VMcom.VMcom.repository;

import com.VMcom.VMcom.model.Token;
import com.VMcom.VMcom.model.TokenSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenSessionRepository extends JpaRepository<TokenSession, Integer> {

    Optional<TokenSession> findByAccessToken(Token accessToken);
    Optional<TokenSession> findByRefreshToken(Token refreshToken);
}
