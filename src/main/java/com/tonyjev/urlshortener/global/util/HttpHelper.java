package com.tonyjev.urlshortener.global.util;

import org.springframework.web.util.UriComponentsBuilder;

public class HttpHelper {

    public static String AddPath(String url, String... segments) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        builder.pathSegment(segments);

        return builder.build(false).toUriString();
    }

}
