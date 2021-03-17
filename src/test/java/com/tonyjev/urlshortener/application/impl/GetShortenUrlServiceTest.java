package com.tonyjev.urlshortener.application.impl;

import com.tonyjev.urlshortener.application.EnrollShortenUrlService;
import com.tonyjev.urlshortener.application.GetShortenUrlService;
import com.tonyjev.urlshortener.domain.ShortenUrl;
import com.tonyjev.urlshortener.domain.repository.ShortenUrlRepository;
import com.tonyjev.urlshortener.infrastructure.generator.Base62ShortenUrlGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("GET Shorten URL 테스트")
class GetShortenUrlServiceTest {

    @Mock
    EnrollShortenUrlService enrollShortenUrlService;

    @Mock
    ShortenUrlRepository shortenUrlRepository;

    @Mock
    private Base62ShortenUrlGenerator base62ShortenUrlGenerator;

    @Test
    @DisplayName("신규 URL 단축 요청 테스트")
    void getShortenUrlByNotEnrolledUrlTest() {
        /* Given */
        GetShortenUrlService getShortenUrlService = new GetShortenUrlServiceImpl(enrollShortenUrlService, shortenUrlRepository);

        // URL Generator mock
        given(base62ShortenUrlGenerator.generateShortenUrl(1L)).willReturn("http://localhost:8080/shorten-url/redirect/1");

        // Shorten URL mock
        long keyId = 1L;
        String originalUrl = "http://shorten-url-test.com";
        String shortenUrl = base62ShortenUrlGenerator.generateShortenUrl(1L);

        ShortenUrl shortenUrlByUtil = ShortenUrl.of(keyId, originalUrl, shortenUrl);

        // Request Count Incremented URL mock
        given(shortenUrlRepository.findByOriginalUrl(originalUrl)).willReturn(Optional.empty()); // URL 조회 요청 시 null 반환
        given(enrollShortenUrlService.enrollShortenUrl(originalUrl)).willReturn(shortenUrlByUtil);

        /* When */
        ShortenUrl shortenUrlByService = getShortenUrlService.getShortenUrl(originalUrl);

        /* Then */
        // 서비스 호출 확인
        then(shortenUrlRepository).should(times(1)).findByOriginalUrl(originalUrl); // 기존 URL 조회 서비스 호출 확인
        then(enrollShortenUrlService).should(times(1)).enrollShortenUrl(originalUrl); // 등록된 URL이 없었을 경우, 신규 Shorten URL 등록 서비스 호출 확인

        // Return 객체 확인
        assertNotEquals(shortenUrlByService.getRequestCnt(), shortenUrlByUtil.getRequestCnt() + 1); // 등록된 URL이 없었을 경우, 요청 횟수를 증가 시키는 서비스를 호출하지 않았음 확인
        assertEquals(shortenUrlByService, shortenUrlByUtil); // Base62 Util을 통해 Encoding 한 객체와 Service를 통해 받은 객체 일치 확인
    }

    @Test
    @DisplayName("기등록된 URL 단축 요청 테스트")
    void getShortenUrlByEnrolledUrlTest() {
        /* Given */
        GetShortenUrlService getShortenUrlService = new GetShortenUrlServiceImpl(enrollShortenUrlService, shortenUrlRepository);

        // URL Generator mock
        given(base62ShortenUrlGenerator.generateShortenUrl(1L)).willReturn("http://localhost:8080/shorten-url/redirect/1");

        // Shorten URL mock
        long keyId = 1L;
        String originalUrl = "http://shorten-url-test.com";
        String shortenUrl = base62ShortenUrlGenerator.generateShortenUrl(1L);

        ShortenUrl shortenUrlByUtil = ShortenUrl.of(keyId, originalUrl, shortenUrl);

        // Request Count Incremented URL mock
        ShortenUrl incrementedShortenUrl = ShortenUrl.of(keyId, originalUrl, shortenUrl);
        incrementedShortenUrl.increteRequestCnt();

        // Repository Mock
        given(shortenUrlRepository.findByOriginalUrl(originalUrl)).willReturn(Optional.of(shortenUrlByUtil));
        given(shortenUrlRepository.save(any())).willReturn(incrementedShortenUrl);

        /* When */
        ShortenUrl shortenUrlByService = getShortenUrlService.getShortenUrl(originalUrl);

        /* Then */
        // 서비스 호출 확인
        then(shortenUrlRepository).should(times(1)).findByOriginalUrl(originalUrl); // 기존 URL 조회 서비스 호출 확인
        then(enrollShortenUrlService).should(never()).enrollShortenUrl(originalUrl); // 이미 등록된 URL이 있는 경우, 신규 Shorten URL을 등록하는 서비스를 호출하지 않았음 확인

        // Return 객체 확인
        assertEquals(shortenUrlByService.getRequestCnt(), 2); // 이미 등록된 URL이 있는 경우, 요청 횟수를 증가 시키는 서비스 호출 하였음을 확인
        assertTrue(shortenUrlByService.equals(shortenUrlByUtil)); // 이미 등록된 URL이 있는 경우, 조회를 통해 얻은 객체와 return된 객체가 같음을 확인
    }

}
