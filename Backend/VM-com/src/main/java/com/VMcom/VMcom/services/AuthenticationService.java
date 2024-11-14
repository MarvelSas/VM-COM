package com.VMcom.VMcom.services;

import com.VMcom.VMcom.enums.AppUserRole;
import com.VMcom.VMcom.enums.TokenType;
import com.VMcom.VMcom.model.*;
import com.VMcom.VMcom.repository.AppUserRepository;
import com.VMcom.VMcom.repository.ShopCartRepository;
import com.VMcom.VMcom.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final ShopCartRepository shopCartRepository;

    public AuthenciationResponse register(RegisterRequest request) {
        if(appUserRepository.findByUsername(request.getEmail()).isPresent()){
            throw new InvalidParameterException("User with email "+request.getEmail()+" already exist");
        }

        var user = new AppUser(
                request.getFirstname(),
                request.getLastname(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                AppUserRole.ROLE_USER,
                false,
                true);
        AppUser appUser = appUserRepository.save(user);
        createShopCart(appUser);
        HashMap<String,Object> claims = new HashMap<>();
        claims.put("roles",user.getAppUserRole());
        revokeAllAccessAndRefreshAppUserTokens(appUser);
        var jwtToken = jwtService.generateToken(claims,user);
        saveUserToken(appUser, jwtToken,TokenType.ACCESS);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(appUser,jwtRefreshToken,TokenType.REFRESH);
        return AuthenciationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();

    }

    public void createShopCart(AppUser appUser){
        ShopCart shopCart = new ShopCart();
        List<ShopCartLine> shopCartLines = new ArrayList<>();
        shopCart.setShopCardLines(shopCartLines);
        shopCart.setAppUser(appUser);
        shopCartRepository.save(shopCart);
    }

    public void revokeAllAccessAndRefreshAppUserTokens(AppUser appUser){
        revokeAllUsersTokens(appUser,tokenRepository.findAllValidTokensByUser(appUser.getId()));
    }

    public void revokeAllUsersTokens(AppUser appUser, List<Token> validAppUserTokens){

        if (validAppUserTokens.isEmpty()){
            return;
        }

        validAppUserTokens.forEach(t ->{
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepository.saveAll(validAppUserTokens);

    }

    public void revokeAllAccessAppUserToken(AppUser appUser){
        revokeAllUsersTokens(appUser,tokenRepository.findAllValidAccessTokensByUser(appUser.getId()));
    }

    private void saveUserToken(AppUser appUser, String jwtToken,TokenType tokenType) {
        var token = Token.builder()
                .appUser(appUser)
                .token(jwtToken)
                .tokenType(tokenType)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    public AuthenciationResponse authenticate(AuthenciationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = appUserRepository.findByUsername(request.getUsername())
                .orElseThrow(); //todo Add good exception

        HashMap<String,Object> claims = new HashMap<>();
        claims.put("roles",user.getAppUserRole());
        revokeAllAccessAndRefreshAppUserTokens(user);
        var jwtToken = jwtService.generateToken(claims,user);
        saveUserToken(user, jwtToken,TokenType.ACCESS);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user,jwtRefreshToken,TokenType.REFRESH);
        return AuthenciationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();

    }

    public AuthenciationResponse refresthToken(HttpServletRequest request) {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        AuthenciationResponse authenciationResponse = new AuthenciationResponse();
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return authenciationResponse;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractEmail(refreshToken);
        if(username != null){
            var user = this.appUserRepository.findByUsername(username).orElseThrow();
            boolean isTokenValid = tokenRepository.findByToken(refreshToken)
            .map(t -> !t.isExpired() && !t.isRevoked())
            .orElse(false);
            if(jwtService.isTokenValid(refreshToken,user) && isTokenValid){
                revokeAllAccessAndRefreshAppUserTokens(user);
                HashMap<String,Object> claims = new HashMap<>();
                claims.put("roles",user.getAppUserRole());
                String accessToken = jwtService.generateToken(claims,user);
                saveUserToken(user, accessToken,TokenType.ACCESS);
                String newRefreshToken = jwtService.generateRefreshToken(user);
                saveUserToken(user,newRefreshToken,TokenType.REFRESH);
                authenciationResponse = AuthenciationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(newRefreshToken)
                        .build();

            }
            return authenciationResponse;
        }

        return authenciationResponse;
    }
}
