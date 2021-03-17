package com.tonyjev.urlshortener.application.infra;

public interface ShortenUrlGenerator {
    String generateShortenUrl(long keyId);
}
