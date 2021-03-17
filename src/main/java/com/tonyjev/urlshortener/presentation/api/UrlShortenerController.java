package com.tonyjev.urlshortener.presentation.api;

import com.tonyjev.urlshortener.application.GetShortenUrlService;
import com.tonyjev.urlshortener.domain.ShortenUrl;
import com.tonyjev.urlshortener.presentation.api.dto.UrlShortenerDto;
import com.tonyjev.urlshortener.presentation.api.validator.URLValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class UrlShortenerController {

    private final GetShortenUrlService getShortenUrlService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/shorten-url")
    @ResponseStatus(HttpStatus.OK)
    public String getShortenUrl(UrlShortenerDto.ShortenUrlReq shortenUrlReq, Model model) {
        String originalUrl = shortenUrlReq.getUrl();

        // URL 유효성 검증
        URLValidator.validateURL(originalUrl);

        // URL 단축 요청
        ShortenUrl shortenUrl = getShortenUrlService.getShortenUrl(originalUrl);

        // 결과 Model 반환
        model.addAttribute("originalUrl", originalUrl);
        model.addAttribute("shortenUrl", shortenUrl.getShortenUrl());
        model.addAttribute("requestCnt", shortenUrl.getRequestCnt());

        return "index";
    }

}
