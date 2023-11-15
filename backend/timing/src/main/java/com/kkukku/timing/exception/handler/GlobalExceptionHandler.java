package com.kkukku.timing.exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kkukku.timing.exception.CustomException;
import com.kkukku.timing.response.ApiResponseUtil;
import com.kkukku.timing.response.ErrorResponse;
import com.kkukku.timing.response.codes.ErrorCode;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException", ex);
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField())
                         .append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }

        return ApiResponseUtil.error(ErrorCode.VALIDATION_ERROR, stringBuilder.toString());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(
        MissingRequestHeaderException ex) {
        log.error("MissingRequestHeaderException", ex);
        return ApiResponseUtil.error(ErrorCode.MISSING_BODY, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException", ex);
        return ApiResponseUtil.error(ErrorCode.MISSING_BODY, ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderExceptionException(
        MissingServletRequestParameterException ex) {
        log.error("handleMissingServletRequestParameterException", ex);
        return ApiResponseUtil.error(ErrorCode.MISSING_PARAMETER, ex.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<ErrorResponse> handleBadRequestException(HttpClientErrorException ex) {
        log.error("HttpClientErrorException.BadRequest", ex);
        return ApiResponseUtil.error(ErrorCode.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoHandlerFoundExceptionException(
        NoHandlerFoundException ex) {
        log.error("handleNoHandlerFoundExceptionException", ex);
        return ApiResponseUtil.error(ErrorCode.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        log.error("handleNullPointerException", ex);
        return ApiResponseUtil.error(ErrorCode.NULL_POINTER, ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        log.error("handleIOException", ex);
        return ApiResponseUtil.error(ErrorCode.IO_EXCEPTION, ex.getMessage());
    }

    @ExceptionHandler(JsonParseException.class)
    protected ResponseEntity<ErrorResponse> handleJsonParseExceptionException(
        JsonParseException ex) {
        log.error("handleJsonParseExceptionException", ex);
        return ApiResponseUtil.error(ErrorCode.JSON_PARSE_EXCEPTION, ex.getMessage());
    }

    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<ErrorResponse> handleJsonProcessingException(
        JsonProcessingException ex) {
        log.error("handleJsonProcessingException", ex);
        return ApiResponseUtil.error(ErrorCode.MISSING_BODY, ex.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    protected final ResponseEntity<ErrorResponse> handleCustomExceptions(CustomException ex) {
        log.error("CustomException", ex);

        return ApiResponseUtil.error(ex.getErrorCode(), ex.getMessage());
    }
}
