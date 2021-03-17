package com.tonyjev.urlshortener.application;

import com.tonyjev.urlshortener.domain.ShortenUrl;

public interface EnrollShortenUrlService {
    ShortenUrl enrollShortenUrl(String originalUrl);
}
