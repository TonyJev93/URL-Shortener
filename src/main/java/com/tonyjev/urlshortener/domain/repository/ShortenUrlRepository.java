package com.tonyjev.urlshortener.domain.repository;

import com.tonyjev.urlshortener.domain.ShortenUrl;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ShortenUrlRepository extends CrudRepository<ShortenUrl, Long> {
    Optional<ShortenUrl> findByOriginalUrl(String originalUrl);

    Optional<ShortenUrl> findById(Long id);
}
