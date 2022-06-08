package com.example.gymcot.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResult processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            if (!fieldError.getField().equals("password")) {
                builder.append(" 입력된 값: [");
                builder.append(fieldError.getRejectedValue());
                builder.append("]");
            }
        }
        return new ExceptionResult("100",builder.toString());


    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResult processValidationError(IllegalArgumentException exception) {
        return new ExceptionResult("200", exception.getMessage());
    }


}
