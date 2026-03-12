package com.sprint.mission.findex.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "요청 JSON이 올바르지 않습니다."),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "요청 파라미터 타입이 올바르지 않습니다."),

    //404
    INDEX_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "지수 정보를 찾을 수 없습니다."),
    INDEX_DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "지수 데이터를 찾을 수 없습니다."),
    AUTO_SYNC_CONFIG_NOT_FOUND(HttpStatus.NOT_FOUND, "자동 연동 설정을 찾을 수 없습니다."),

    //409
    DUPLICATE_INDEX_INFO(HttpStatus.CONFLICT, "이미 존재하는 지수 정보입니다."),
    DUPLICATE_INDEX_DATA(HttpStatus.CONFLICT, "이미 존재하는 지수 데이터입니다."),

    //500
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}