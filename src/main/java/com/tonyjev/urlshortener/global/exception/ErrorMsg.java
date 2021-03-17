package com.tonyjev.urlshortener.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMsg {

    NotFoundMappingUrlException("해당 URL에 맵핑된 URL이 존재하지 않습니다."),
    NotValidUrlFormatException("입력 URL 포맷이 올바르지 않습니다. (http:// or https:// 필수)"),
    OutOfKeyIdForEncodingUrlException("더 이상 등록 가능한 URL 공간이 없습니다."),
    ;

    private String errMsg;

}
