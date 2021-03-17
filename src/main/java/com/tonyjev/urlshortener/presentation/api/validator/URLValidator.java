package com.tonyjev.urlshortener.presentation.api.validator;

import com.tonyjev.urlshortener.presentation.api.exception.NotValidUrlFormatException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class URLValidator {

    private static final String URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";

    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    public static void validateURL(String url) {

        // Not Empty Check
        if(url == null || "".equals(url)) {
            throw new NotValidUrlFormatException();
        }

        // Pattern Check
        Matcher m = URL_PATTERN.matcher(url);
        if (!m.matches()) {
            throw new NotValidUrlFormatException();
        }

        // Transfer to Url Possible Check
        try {
            URI uri = new URI(url);
            uri.toURL();
        } catch (Exception e) {
            throw new NotValidUrlFormatException();
        }

    }
}
