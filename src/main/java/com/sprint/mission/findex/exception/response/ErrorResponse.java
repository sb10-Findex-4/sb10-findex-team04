package com.sprint.mission.findex.exception.response;

import com.sprint.mission.findex.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class ErrorResponse {

    private String timestamp;
    private int status;
    private String message;
    private String details;

    public static ErrorResponse from(ErrorCode code, String details) {
        return ErrorResponse.builder()
                .timestamp(OffsetDateTime.now().toString())
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .details(details)
                .build();
    }
}
