package com.example.foodordersystem.Exception;

import org.springframework.http.HttpStatus;

import org.springframework.http.HttpStatus;

public enum OrderError implements ErrorCode {
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "OR1", "주문을 찾을 수 없습니다."),
    WRONG_ORDER_QUANTITY(HttpStatus.BAD_REQUEST, "OR2", "잘못된 주문 수량입니다."),
    DUPLICATE_ORDER(HttpStatus.BAD_REQUEST, "OR3", "중복된 주문입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "OR4", "사용자를 찾을 수 없습니다."),
    FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, "OR5", "음식을 찾을 수 없습니다.");

    private final HttpStatus httpStatusCode;
    private final String developCode;
    private final String message;

    OrderError(HttpStatus httpStatusCode, String developCode, String message) {
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
