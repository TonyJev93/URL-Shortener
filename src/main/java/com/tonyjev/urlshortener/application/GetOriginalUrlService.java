package com.tonyjev.urlshortener.application;

import com.tonyjev.urlshortener.domain.ShortenUrl;

public interface GetOriginalUrlService {
    String getOriginalUrl(String shortenUrl);
}
