package com.example.gymcot.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionPayload processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return new ExceptionPayload(exception, ExceptionCode.INVALID_INPUT_VALUE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionPayload processValidationError(IllegalArgumentException exception) {
        return new ExceptionPayload(exception, ExceptionCode.INVALID_INPUT_VALUE);
    }

    @ExceptionHandler(RuntimeException.class)
    public ExceptionPayload notAllowedUserException(RuntimeException exception) {
        return new ExceptionPayload(exception, ExceptionCode.NOT_ALLOW_ACCESS);
    }

    @ExceptionHandler(NotAllowedUserException.class)
    public ExceptionPayload notAllowedUserException(NotAllowedUserException exception) {
        return new ExceptionPayload(exception, ExceptionCode.NOT_ALLOW_ACCESS);
    }

}
