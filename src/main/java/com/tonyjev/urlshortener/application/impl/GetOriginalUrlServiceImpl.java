package com.tonyjev.urlshortener.application.impl;

import com.tonyjev.urlshortener.application.GetOriginalUrlService;
import com.tonyjev.urlshortener.application.exception.NotFoundMappingUrlException;
import com.tonyjev.urlshortener.domain.ShortenUrl;
import com.tonyjev.urlshortener.domain.repository.ShortenUrlRepository;
import com.tonyjev.urlshortener.global.util.Base62Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetOriginalUrlServiceImpl implements GetOriginalUrlService {

    private final ShortenUrlRepository shortenUrlRepository;

    @Override
    public String getOriginalUrl(String shortenUrl) {
        long keyId = Base62Util.decoding(shortenUrl);
        ShortenUrl shortenUrlObj = shortenUrlRepository.findById(keyId)
                .orElseThrow(() -> new NotFoundMappingUrlException());

        log.info("URL Redirect : {} -> {}", shortenUrl, shortenUrlObj.getOriginalUrl());

        return shortenUrlObj.getOriginalUrl();
    }
}
