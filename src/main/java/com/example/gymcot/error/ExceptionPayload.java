package com.example.gymcot.error;

import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
public class ExceptionPayload {
    private String code;
    private String message;
    private ErrorDetail detail;

    public ExceptionPayload(Exception e, ExceptionCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
        if (e.getClass() == MethodArgumentNotValidException.class) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            this.detail = new ErrorDetail(bindingResult);
        } else {
            this.detail = new ErrorDetail(e.getMessage());
        }
    }

    @Getter
    public static class ErrorDetail {
        private String field;
        private String reason;

        public ErrorDetail(String reason) {
            this.reason = reason;
        }

        public ErrorDetail(final BindingResult bindingResult) {
            this.field = bindingResult.getFieldError().getField();

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
            this.reason = builder.toString();
        }
    }

}
