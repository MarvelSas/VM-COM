package com.VMcom.VMcom.services;

import com.VMcom.VMcom.enums.AppUserRole;
import com.VMcom.VMcom.enums.TokenType;
import com.VMcom.VMcom.model.*;
import com.VMcom.VMcom.repository.AppUserRepository;
import com.VMcom.VMcom.repository.ShopCartRepository;
import com.VMcom.VMcom.repository.TokenRepository;
import com.VMcom.VMcom.repository.TokenSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final ShopCartRepository shopCartRepository;
    private final TokenSessionRepository tokenSessionRepository;

    public AppUser getAppUserFromContextHolder(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new IllegalStateException("No authentication object found in security context");
        return findUserByUsername(authentication.getName());
    }

    private AppUser findUserByUsername(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    public AuthenciationResponse register(RegisterRequest request) {
        if(appUserRepository.findByUsername(request.getEmail()).isPresent()){
            throw new InvalidParameterException("User with email "+request.getEmail()+" already exist");
        }

        checkPassword(request.getPassword());

        var user = new AppUser(
                request.getFirstname(),
                request.getLastname(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                AppUserRole.ROLE_USER,
                false,
                true,
                request.getPhoneNumber());
        AppUser appUser = appUserRepository.save(user);
        createShopCart(appUser);
        HashMap<String,Object> claims = new HashMap<>();
        claims.put("roles",user.getAppUserRole());
        revokeAllAccessAndRefreshAppUserTokens(appUser);
        var jwtToken = jwtService.generateToken(claims,user);
        Token savedJwtToken = saveUserToken(appUser, jwtToken,TokenType.ACCESS);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        Token savedJwtRefreshToken = saveUserToken(appUser,jwtRefreshToken,TokenType.REFRESH);
        generateTokenSession(savedJwtToken,savedJwtRefreshToken);
        return AuthenciationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();

    }


    private void checkPassword(String password){
        if(password.isEmpty()){
            throw new InvalidParameterException("Password can't be empty");
        }

        if(password.length() < 8){
            throw new InvalidParameterException("Password must be at least 8 characters long");
        }

        if(password.length() > 20){
            throw new InvalidParameterException("Password must be at most 20 characters long");
        }

    }



    private void generateTokenSession(Token accessToken, Token refreshToken){
       tokenSessionRepository.save(TokenSession.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());


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

    private void revokeAccessAndRefreshAppUserTokenByAccessToken(String accessTokenString){
      Token accessToken = tokenRepository.findByToken(accessTokenString).orElseThrow(() -> new InvalidParameterException("access token not found"));
      TokenSession tokenSession = tokenSessionRepository.findByAccessToken(accessToken).orElseThrow(() -> new InvalidParameterException("token session not found"));
      revokeAccessAndRefreshAppUserTokenByTokenSession(tokenSession);
    }

    private void revokeAccessAndRefreshAppUserTokenByRefreshToken(String refreshTokenString){
      Token refreshToken = tokenRepository.findByToken(refreshTokenString).orElseThrow(() -> new InvalidParameterException("refresh token not found"));
      TokenSession tokenSession = tokenSessionRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new InvalidParameterException("token session not found"));
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

    private Token saveUserToken(AppUser appUser, String jwtToken,TokenType tokenType) {
        var token = Token.builder()
                .appUser(appUser)
                .token(jwtToken)
                .tokenType(tokenType)
                .expired(false)
                .revoked(false)
                .build();
       return tokenRepository.save(token);
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
        var jwtToken = jwtService.generateToken(claims,user);
        Token savedJwtToken = saveUserToken(user, jwtToken,TokenType.ACCESS);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        Token savedJwtRefreshToken = saveUserToken(user,jwtRefreshToken,TokenType.REFRESH);
        generateTokenSession(savedJwtToken,savedJwtRefreshToken);
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
                revokeAccessAndRefreshAppUserTokenByRefreshToken(refreshToken);
                HashMap<String,Object> claims = new HashMap<>();
                claims.put("roles",user.getAppUserRole());
                String accessToken = jwtService.generateToken(claims,user);
                Token savedJwtToken = saveUserToken(user, accessToken,TokenType.ACCESS);
                String newRefreshToken = jwtService.generateRefreshToken(user);
                Token savedNewJwtRefreshToken = saveUserToken(user,newRefreshToken,TokenType.REFRESH);
                generateTokenSession(savedJwtToken,savedNewJwtRefreshToken);
                authenciationResponse = AuthenciationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(newRefreshToken)
                        .build();

            }
            return authenciationResponse;
        }

        return authenciationResponse;
    }

    public Boolean changePassword(Map<String, String> request) {

        AppUser appUser = getAppUserFromContextHolder();

        if(passwordEncoder.matches(request.get("oldPassword"),appUser.getPassword()) && validateOldAndNewPassword(request)){
            appUser.setPassword(passwordEncoder.encode(request.get("newPassword")));
            appUserRepository.save(appUser);
            return true;
        }else {
            throw new InvalidParameterException("Old password is incorrect");
        }


    }

    private boolean validateOldAndNewPassword(Map<String, String> request){

        checkPassword(request.get("newPassword"));

        if(request.get("newPassword").isEmpty()){
            throw new InvalidParameterException("New password can't be empty");
        }

        if(request.get("oldPassword").isEmpty()){
            throw new InvalidParameterException("Old password can't be empty");
        }

        if(request.get("newPassword").equals(request.get("oldPassword"))){
            throw new InvalidParameterException("New password can't be the same as old password");
        }

        return true;
    }

}
