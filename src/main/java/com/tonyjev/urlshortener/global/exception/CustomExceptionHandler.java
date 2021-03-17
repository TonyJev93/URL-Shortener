package com.tonyjev.urlshortener.global.exception;

import com.tonyjev.urlshortener.application.exception.NotFoundMappingUrlException;
import com.tonyjev.urlshortener.infrastructure.generator.exception.OutOfKeyIdForEncodingUrlException;
import com.tonyjev.urlshortener.presentation.api.exception.NotValidUrlFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {
    private void printCausedByException(String prefix, Exception e) {
        if (e.getCause() == null) {
            log.error(prefix + e.getMessage());
            return;
        }

        do {
            e = (Exception) e.getCause();
            log.error(prefix + e.getMessage());
        } while (e.getCause() != null);
    }

    @ExceptionHandler(NotFoundMappingUrlException.class)
    protected String handleIssuerException(NotFoundMappingUrlException e, Model model) {
        printCausedByException("handleBusinessException :: ", e);
        e.printStackTrace();

        model.addAttribute("errorMsg", ErrorMsg.NotFoundMappingUrlException.getErrMsg());

        return "index";
    }

    @ExceptionHandler(NotValidUrlFormatException.class)
    protected String handleIssuerException(HttpServletRequest request, NotValidUrlFormatException e, Model model) {
        printCausedByException("handleBusinessException :: ", e);
        e.printStackTrace();

        model.addAttribute("originalUrl", request.getParameter("url"));
        model.addAttribute("errorMsg", ErrorMsg.NotValidUrlFormatException.getErrMsg());

        return "index";
    }

    @ExceptionHandler(OutOfKeyIdForEncodingUrlException.class)
    protected String handleIssuerException(HttpServletRequest request, OutOfKeyIdForEncodingUrlException e, Model model) {
        printCausedByException("handleBusinessException :: ", e);
        e.printStackTrace();

        model.addAttribute("errorMsg", ErrorMsg.NotValidUrlFormatException.getErrMsg());

        return "index";
    }
}
