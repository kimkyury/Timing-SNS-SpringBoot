package com.kkukku.timing.response.codes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(400, "Bad Request"),
    MISSING_BODY(400, "Required request body is missing"),
    INVALID_TYPE_VALUE(400, "Invalid Type Value"),
    MISSING_PARAMETER(400, "Missing Servlet Request Parameter"),
    IO_EXCEPTION(400, "I/O Exception"),
    JSON_PARSE_EXCEPTION(400, "JSON Parse Exception"),
    JACKSON_EXCEPTION(400, "Jackson Core Exception"),
    VALIDATION_ERROR(400, "Validation Exception"),
    MISSING_HEADER(400, "Header data missing"),

    UNAUTHORIZED(401, "Unauthorized"),
    TOKEN_EXPIRED(401, "Token is expired"),

    FORBIDDEN(403, "Forbidden"),

    NOT_FOUND(404, "Not Found"),
    NULL_POINTER(404, "Null Pointer Exception"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    FAIL_SAVE_FILE_S3(500, "Server Error: File save file to s3");

    private final int status;
    private final String message;
}
