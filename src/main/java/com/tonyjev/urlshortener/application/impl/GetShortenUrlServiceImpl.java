package com.tonyjev.urlshortener.application.impl;

import com.tonyjev.urlshortener.application.EnrollShortenUrlService;
import com.tonyjev.urlshortener.application.GetShortenUrlService;
import com.tonyjev.urlshortener.domain.ShortenUrl;
import com.tonyjev.urlshortener.domain.repository.ShortenUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetShortenUrlServiceImpl implements GetShortenUrlService {

    private final EnrollShortenUrlService enrollShortenUrlService;
    private final ShortenUrlRepository shortenUrlRepository;

    @Override
    public ShortenUrl getShortenUrl(String originalUrl) {
        log.info("URL 단축 요청 서비스 시작 : {}", originalUrl);

        // 기존 등록된 Shorten URL 조회
        ShortenUrl alreadyExistShortenUrl = shortenUrlRepository.findByOriginalUrl(originalUrl).orElse(null);

        // 기존 등록된 Shorten URL이 존재 하면,
        if (alreadyExistShortenUrl != null) {
            // URL 요청 횟수 증가
            return this.incrementRequestCount(alreadyExistShortenUrl);
        } else {
            // 등록된 Shorten URL 없는 경우 신규 등록
            ShortenUrl newShortenUrl = enrollShortenUrlService.enrollShortenUrl(originalUrl);

            return newShortenUrl;
        }
    }

    @Override
    public ShortenUrl incrementRequestCount(ShortenUrl shortenUrl) {
        // 요청 횟수 증가 처리
        shortenUrl.increteRequestCnt();

        log.info("URL({}) 요청 증가 : {}", shortenUrl.getOriginalUrl(), shortenUrl.getRequestCnt());

        // 변경 내용 반영
        return shortenUrlRepository.save(shortenUrl);
    }

}
