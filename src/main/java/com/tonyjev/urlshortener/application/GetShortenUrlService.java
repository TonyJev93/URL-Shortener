package com.tonyjev.urlshortener.application;

import com.tonyjev.urlshortener.domain.ShortenUrl;

public interface GetShortenUrlService {
    ShortenUrl getShortenUrl(String originalUrl);
    ShortenUrl incrementRequestCount(ShortenUrl shortenUrl);
}
