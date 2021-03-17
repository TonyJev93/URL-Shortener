package com.tonyjev.urlshortener.infrastructure.repository;

import com.tonyjev.urlshortener.domain.repository.ShortenUrlKeyIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ShortenUrlKeyIdRepositoryRedis implements ShortenUrlKeyIdRepository {
    final private StringRedisTemplate stringRedisTemplate;
    final private String SEQ_KEY_ID = "shortenUrlKeyIdSequence";

    @Override
    public Long getNextKeyId() {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.increment(SEQ_KEY_ID);
    }
}
