package com.tonyjev.urlshortener.application.impl;

import com.tonyjev.urlshortener.application.GetOriginalUrlService;
import com.tonyjev.urlshortener.application.exception.NotFoundMappingUrlException;
import com.tonyjev.urlshortener.domain.ShortenUrl;
import com.tonyjev.urlshortener.domain.repository.ShortenUrlRepository;
import com.tonyjev.urlshortener.global.util.Base62Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetOriginalUrlServiceImpl implements GetOriginalUrlService {

    private final ShortenUrlRepository shortenUrlRepository;

    @Override
    public String getOriginalUrl(String shortenUrl) {
        long keyId = Base62Util.decoding(shortenUrl);
        ShortenUrl shortenUrlObj = shortenUrlRepository.findById(keyId)
                .orElseThrow(() -> new NotFoundMappingUrlException());

        return shortenUrlObj.getOriginalUrl();
    }
}
