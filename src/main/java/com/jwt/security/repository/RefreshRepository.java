package com.jwt.security.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshRepository {

    public final RedisTemplate<String,String> redisTemplate;
    public static final String REFRESH_TOKEN_PREFIX = "R_T:";

    public void save(String email, String refreshToken, long expiration) {
        String key = REFRESH_TOKEN_PREFIX + email;

        redisTemplate.opsForValue().set(
                key,
                refreshToken,
                expiration,
                TimeUnit.MILLISECONDS
        );
    }
}
