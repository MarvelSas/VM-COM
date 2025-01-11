package com.VMcom.VMcom.services;

import com.VMcom.VMcom.model.Token;
import com.VMcom.VMcom.model.TokenSession;
import com.VMcom.VMcom.repository.AppUserRepository;
import com.VMcom.VMcom.repository.TokenRepository;
import com.VMcom.VMcom.repository.TokenSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final JWTService jwtService;
    private final AppUserRepository appUserRepository;
    private final TokenSessionRepository tokenSessionRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);

        revokeAccessAndRefreshAppUserTokenByAccessToken(jwt);

    }

    private void revokeAccessAndRefreshAppUserTokenByAccessToken(String accessTokenString){
        System.out.println(accessTokenString);
        Token accessToken = tokenRepository.findByToken(accessTokenString).orElseThrow(() -> new InvalidParameterException("access token not found"));
        TokenSession tokenSession = tokenSessionRepository.findByAccessToken(accessToken).orElseThrow(() -> new InvalidParameterException("token session not found"));
        revokeAccessAndRefreshAppUserTokenByTokenSession(tokenSession);
    }

    private void  revokeAccessAndRefreshAppUserTokenByTokenSession(TokenSession tokenSession){
        tokenSession.getAccessToken().setRevoked(true);
        tokenSession.getAccessToken().setExpired(true);
        tokenSession.getRefreshToken().setRevoked(true);
        tokenSession.getRefreshToken().setExpired(true);
        tokenRepository.save(tokenSession.getAccessToken());
        tokenRepository.save(tokenSession.getRefreshToken());
    }
}
