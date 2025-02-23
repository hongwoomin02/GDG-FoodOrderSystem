package com.example.foodordersystem.Exception;

import org.springframework.http.HttpStatus;

public enum UserError implements ErrorCode {
    EMAIL_EXISTS(HttpStatus.CONFLICT, "US1", "이미 존재하는 이메일입니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "US2", "존재하지 않는 이메일입니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "US3", "비밀번호가 일치하지 않습니다.");

    private final HttpStatus httpStatusCode;
    private final String developCode;
    private final String message;

    UserError(HttpStatus httpStatusCode, String developCode, String message) {
        this.httpStatusCode = httpStatusCode;
        this.developCode = developCode;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatusCode;
    }

    @Override
    public String getDevelopCode() {
        return developCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
