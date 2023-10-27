package com.kkukku.timing.redis.services;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public String getValue(String key) {
        return redisTemplate.opsForValue()
                            .get(key);
    }

    public void saveData(String key, String value) {
        redisTemplate.opsForValue()
                     .set(key, value);
    }

    public void saveDataWithTimeout(String key, String value, Long timeout) {
        redisTemplate.opsForValue()
                     .set(key, value, timeout, TimeUnit.SECONDS);
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}

