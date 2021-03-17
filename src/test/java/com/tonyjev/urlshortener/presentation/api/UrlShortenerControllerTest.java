package com.tonyjev.urlshortener.presentation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tonyjev.urlshortener.application.GetShortenUrlService;
import com.tonyjev.urlshortener.domain.ShortenUrl;
import com.tonyjev.urlshortener.infrastructure.generator.Base62ShortenUrlGenerator;
import com.tonyjev.urlshortener.presentation.api.dto.UrlShortenerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Paths;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlShortenerController.class)
@DisplayName("MVC 테스트 : URL Shorten Controller")
class UrlShortenerControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GetShortenUrlService getShortenUrlService;

    @MockBean
    private Base62ShortenUrlGenerator base62ShortenUrlGenerator;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("Shorten URL 요청 컨트롤러 테스트")
    public void getShortenUrlTest() throws Exception {

        /* Given */
        UrlShortenerDto.ShortenUrlReq shortenUrlReq =
                objectMapper.readValue(Paths.get(ClassLoader.getSystemResource("request/ShortenUrlReq.json").toURI()).toFile(), UrlShortenerDto.ShortenUrlReq.class);

        given(base62ShortenUrlGenerator.generateShortenUrl(1L)).willReturn("http://localhost:8080/shorten-url/redirect/1");

        long keyId = 1L;
        String originalUrl = shortenUrlReq.getUrl();
        String shortenUrl = base62ShortenUrlGenerator.generateShortenUrl(1L);

        ShortenUrl shortenUrlObj = ShortenUrl.of(keyId, originalUrl, shortenUrl);

        given(getShortenUrlService.getShortenUrl(originalUrl)).willReturn(shortenUrlObj);


        /* When & Then */
        this.mockMvc
                .perform(post("/shorten-url")
                        .param("url", shortenUrlReq.getUrl())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("shortenUrl")) // Return Shorten Url
                .andExpect(model().attribute("shortenUrl", "http://localhost:8080/shorten-url/redirect/1")); // Return Shorten Url
    }

}
