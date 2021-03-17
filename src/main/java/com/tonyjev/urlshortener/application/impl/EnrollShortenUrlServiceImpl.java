package com.tonyjev.urlshortener.application.impl;

import com.tonyjev.urlshortener.application.EnrollShortenUrlService;
import com.tonyjev.urlshortener.application.infra.ShortenUrlGenerator;
import com.tonyjev.urlshortener.domain.ShortenUrl;
import com.tonyjev.urlshortener.domain.repository.ShortenUrlKeyIdRepository;
import com.tonyjev.urlshortener.domain.repository.ShortenUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollShortenUrlServiceImpl implements EnrollShortenUrlService {

    private final ShortenUrlRepository shortenUrlRepository;
    private final ShortenUrlGenerator shortenUrlGenerator;
    private final ShortenUrlKeyIdRepository shortenUrlKeyIdRepository;

    @Override
    public ShortenUrl enrollShortenUrl(String originalUrl) {
        // get Key ID
        Long keyId = shortenUrlKeyIdRepository.getNextKeyId();

        // get Shorten URL By keyID
        String shortenUrl = shortenUrlGenerator.generateShortenUrl(keyId);

        ShortenUrl shortenUrlObj = ShortenUrl.of(keyId, originalUrl, shortenUrl);

        ShortenUrl newShortenUrl = shortenUrlRepository.save(shortenUrlObj);

        return newShortenUrl;
    }

}
