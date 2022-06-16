package com.example.gymcot.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class SecurityFilterChainExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String)request.getAttribute("exception");
        ExceptionCode exceptionCode;

        log.debug("log: exception: {} ", exception);

        /**
         * 토큰 없는 경우
         */
        if(exception == ExceptionCode.NONE_TOKEN.getCode()) {
            exceptionCode = ExceptionCode.NONE_TOKEN;
            setResponse(response, exceptionCode);
            return;
        }

        /**
         * 토큰 만료된 경우
         */
        if(exception.equals(ExceptionCode.EXPIRED_TOKEN.getCode())) {
            exceptionCode = ExceptionCode.EXPIRED_TOKEN;
            setResponse(response, exceptionCode);
            return;
        }

        /**
         * 토큰 시그니처가 다른 경우
         */
        if(exception.equals(ExceptionCode.INVALID_TOKEN.getCode())) {
            exceptionCode = ExceptionCode.INVALID_TOKEN;
            setResponse(response, exceptionCode);
        }
    }

    /**
     * 한글 출력을 위해 getWriter() 사용
     */

    private void setResponse(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"message\" : \"" + exceptionCode.getMessage()
                + "\", \"code\" : \"" +  exceptionCode.getCode()
                + "\", \"status\" : " + exceptionCode.getMessage()
                );
    }

}
