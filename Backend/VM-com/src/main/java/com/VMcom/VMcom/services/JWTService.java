package com.VMcom.VMcom.services;

import com.VMcom.VMcom.enums.TokenType;
import com.VMcom.VMcom.model.AppUser;
import com.VMcom.VMcom.model.Token;
import com.VMcom.VMcom.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class JWTService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private String jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private String refreshExpiration;

    private final TokenRepository tokenRepository;


    private Long parseJwtExpiration(){
        return Long.parseLong(jwtExpiration);
    }

    private Long parseRefreshExpiration(){
        return Long.parseLong(refreshExpiration);
    }

    public String extractEmail(String token) {
        return extractClaim(token,Claims::getSubject);
    }


    public Token generateToken(Map<String, Object> extraClaims, AppUser appUser){

        return buildToken(extraClaims,appUser,parseJwtExpiration(),TokenType.ACCESS);
    }

    public Token generateRefreshToken( AppUser appUser){
        return buildToken(new HashMap<>(),appUser,parseRefreshExpiration(),TokenType.REFRESH);
    }


    private Token buildToken(Map<String, Object> extraClaims, AppUser appUser, long expiration, TokenType tokenType){
        // Add a unique identifier to the extra claims
        extraClaims.put("jti", UUID.randomUUID().toString());

        String token;
            do {
                token = Jwts
                        .builder()
                        .setClaims(extraClaims)
                        .setSubject(appUser.getUsername())
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis()+expiration))
                        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                        .compact();
                System.out.println(token);
            }while (tokenRepository.existsByToken(token));

           return saveToken(token,tokenType,appUser);



    }

    private Token saveToken(String tokenString, TokenType tokenType, AppUser appUser){
        Token token = Token.builder()
                .token(tokenString)
                .tokenType(tokenType)
                .appUser(appUser)
                .expired(false)
                .revoked(false)
                .build();
      return tokenRepository.save(token);

    }



    public boolean isTokenValid(String token,UserDetails userDetails){
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
