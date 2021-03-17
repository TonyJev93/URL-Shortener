package com.tonyjev.urlshortener.presentation.api;

import com.tonyjev.urlshortener.application.GetOriginalUrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RedirectController.class)
@DisplayName("MVC 테스트 : URL Redirect Controller")
class RedirectControllerTest {

    @MockBean
    private GetOriginalUrlService getOriginalUrlService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("Shorten URL Redirect 컨트롤러 테스트")
    public void redirectUrlTest() throws Exception {

        /* Given */
        String shortenUrl = "1";
        String originalUrl = "https://en.wikipedia.org/wiki/URL_shortening";

        given(getOriginalUrlService.getOriginalUrl(shortenUrl)).willReturn(originalUrl);

        /* When */
        ResultActions resultActions = this.mockMvc
                .perform(get("/shorten-url/redirect/{shortenUrl}", "1"));

        /* Then */
        resultActions.andExpect(status().isPermanentRedirect())
                .andExpect(redirectedUrl(originalUrl));
    }

}
