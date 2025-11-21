package com.jwt.security.config;

import com.jwt.security.domain.User;
import com.jwt.security.dto.user.UserToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access_expiration_time}") long accessTokenExpiration,
            @Value("${jwt.refresh_expiration_time}") long refreshTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }



    private String generateAccessToken(UserToken userToken) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(userToken.getEmail())
                .claim("userId", userToken.getId())
                .claim("role",userToken.getRole().name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    private String generateRefreshToken(UserToken userToken) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .subject(userToken.getEmail())
                .claim("userId",userToken.getId())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public String getEmailFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    public String getRoleFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        }catch (ExpiredJwtException e){
            log.error("JWT 토큰이 만료되었습니다: {}", e.getMessage());
        }catch (UnsupportedJwtException e){
            log.error("지원되지 않는 JWT 토큰입니다: {}", e.getMessage());
        }catch (MalformedJwtException e){
            log.error("잘못된 형식의 JWT 토큰입니다: {}", e.getMessage());
        }catch (IllegalArgumentException e){
            log.error("JWT 토큰이 비어있습니다: {}", e.getMessage());
        } catch (Exception e) {
            log.error("JWT 토큰 검증 실패: {}", e.getMessage());
        }
        return false;
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
