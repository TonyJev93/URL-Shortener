package com.tonyjev.urlshortener.presentation.api;

import com.tonyjev.urlshortener.application.GetOriginalUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shorten-url/redirect")
public class RedirectController {

    private final GetOriginalUrlService getOriginalUrlService;

    @GetMapping("/{shortenUrl}")
    @ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
    public RedirectView redirectToUrl(@PathVariable String shortenUrl) {
        String originalUrl = getOriginalUrlService.getOriginalUrl(shortenUrl);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(originalUrl);

        return redirectView;
    }
}
