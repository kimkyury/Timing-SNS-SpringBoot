package com.kkukku.timing.response;

import com.kkukku.timing.response.codes.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public class ApiResponseUtil {

    public static ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode code) {
        return new ResponseEntity<>(ErrorResponse.of(code),
            HttpStatus.valueOf(code.getStatus()));
    }

    public static <T> ResponseEntity<ErrorResponse> createErrorResponse(
        ErrorCode code, BindingResult bindingResult) {
        return new ResponseEntity<>(
            ErrorResponse.of(code, bindingResult),
            HttpStatus.valueOf(code.getStatus()));
    }

    public static <T> ResponseEntity<T> createSuccessResponse(T data) {
        return ResponseEntity.ok()
                             .body(data);
    }
}
