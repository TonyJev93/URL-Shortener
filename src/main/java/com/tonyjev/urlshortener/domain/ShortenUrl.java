package com.tonyjev.urlshortener.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("shortenUrl")
@Slf4j
public class ShortenUrl implements Serializable {

    // field 추가 or 제거에 따른 역직렬화 불가능 문제해결을 위해 UID 지정.( -> but, field type 변경 시에는 역직렬화 오류 발생 )
    private static final long serialVersionUID = 1L;

    // PK
    @Id
    private Long id;

    // 원래 URL
    @Indexed
    private String originalUrl;

    // 변경된 URL
    @Indexed
    private String shortenUrl;

    // 요청 횟수
    private Integer requestCnt = 1;

    @Builder
    private ShortenUrl(Long id, String originalUrl, String shortenUrl) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortenUrl = shortenUrl;
    }

    public static ShortenUrl of(Long id, String originalUrl, String shortenUrl) {
        return ShortenUrl.builder().id(id).originalUrl(originalUrl).shortenUrl(shortenUrl).build();
    }

    public void increteRequestCnt() {
        this.requestCnt++;
    }
}
