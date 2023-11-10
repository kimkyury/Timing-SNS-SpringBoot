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
    NOT_EXIST_MEMBER_EMAIL(400, "No user exists with the specified email"),
    NOT_EXIST_CHALLENGE(400, "Not exist Challenge"),
    NOT_EXIST_HASHTAG_OPTION(400, "Not exist HashTagOption"),
    NOT_EXIST_FEED(400, "Not exist feed"),
    THIS_CHALLENGE_IS_NOT_YOURS(400, "This challenge is not your Challenge"),
    NOT_EXIST_MULTIPART_FILE(400, "Not Exist MultiPart File"),
    NOT_COMPLETED_CHALLENGE(400, "The number of snapshots is insufficient."),
    DELETED_FEED(400, "This feed is already delete"),
    PRIVATE_FEED(400, "Private feed can access only writer"),
    NOT_HIGH_SIMILARITY_SNAPSHOT(400, "This snapshot is not High similarity from pythonServer"),
    NOT_PROPER_COORDINATE(400, "This COORDINATE is not Proper coordinate from pythonServer"),
    NOT_FOUNT_OBJECT_IN_IMAGE(400, "This Image is Not found Object from pythonServer"),
    EXIST_OBJECT_IN_CHALLENGE(400, "This Challenge already has ObjectUrl"),

    UNAUTHORIZED(401, "Unauthorized"),
    TOKEN_EXPIRED(401, "Token is expired"),
    LOGGED_OUT_MEMBER_TOKEN(401, "Logged out token"),
    INVALID_TOKEN_SUBJECT(401, "Invalid token: No subject(email) claim"),
    INVALID_TOKEN(401, "Invalid token"),
    TOKEN_UNAUTHORIZED(401, "JWT token unauthorized"),
    MISSING_LOGIN_INFO(401, "Missing login info"),

    FORBIDDEN(403, "Forbidden"),
    TOKEN_ACCESS_DENIED(403, "JWT token access denied"),

    NOT_FOUND(404, "Not Found"),

    NULL_POINTER(404, "Null Pointer Exception"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    FAIL_SAVE_FILE_S3(500, "Server Error: Fail save file to s3"),
    FAIL_DELETE_FILE_S3(500, "Server Error: Fail delete file to S3");

    private final int status;
    private final String message;
}
