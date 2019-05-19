package com.klangfang.core.common;

import com.klangfang.core.exception.CompositionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CompositionNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(CompositionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String compositionNotFoundHandler(CompositionNotFoundException ex) {
        return ex.getMessage();
    }
}
