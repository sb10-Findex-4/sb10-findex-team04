package com.sprint.mission.findex.exception.response;

import com.sprint.mission.findex.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
@Schema(description = "에러 응답 DTO")
public record ErrorResponse(
        @Schema(description = "에러 발생 시간", example = "2025-03-06T05:39:06.152068Z")
        OffsetDateTime timestamp,

        @Schema(description = "HTTP 상태 코드", example = "400")
        int status,

        @Schema(description = "에러 메시지", example = "잘못된 요청입니다.")
        String message,

        @Schema(description = "에러 상세 내용", example = "부서 코드는 필수입니다.")
        String details
){
    public static ErrorResponse from(ErrorCode code, String details) {
        return ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .details(details)
                .build();
    }
}
