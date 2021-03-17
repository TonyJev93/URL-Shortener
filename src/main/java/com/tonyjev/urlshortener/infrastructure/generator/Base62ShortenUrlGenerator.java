package com.tonyjev.urlshortener.infrastructure.generator;

import com.tonyjev.urlshortener.application.infra.ShortenUrlGenerator;
import com.tonyjev.urlshortener.global.util.Base62Util;
import com.tonyjev.urlshortener.global.util.HttpHelper;
import com.tonyjev.urlshortener.infrastructure.generator.exception.OutOfKeyIdForEncodingUrlException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource(value = {"classpath:/application.properties"})
@Component
public class Base62ShortenUrlGenerator implements ShortenUrlGenerator {

    @Value("${shorten.url.redirect.server.dns}")
    private String redirectServerDns;

    @Value("${shorten.url.redirect.service.path}")
    private String redirectServicePath;

    @Value("${shorten.url.max.length}")
    private int shortenUrlMaxLength;

    @Override
    public String generateShortenUrl(long keyId) {
        // transfer long type ID to Base62 encoded String ID
        if (keyId >= Math.pow(62, shortenUrlMaxLength)) {
            throw new OutOfKeyIdForEncodingUrlException();
        }

        String encodedId = Base62Util.encoding(keyId);

        String shortenUrl = HttpHelper.AddPath(redirectServerDns, redirectServicePath, encodedId);

        return shortenUrl;
    }
}
