package com.lowheap.poc.eventualconsistency.customerservice.utils;

import com.lowheap.poc.eventualconsistency.lib.common.exceptions.BusinessException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;




@ControllerAdvice
@Slf4j
public class CustomerServiceGlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleInternalServerError(Exception e, WebRequest request) {
        log.error("Request {} encountered the following error : ", request.getContextPath(), e);
        return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class,
            IllegalArgumentException.class, MissingServletRequestParameterException.class,
            MissingRequestHeaderException.class, MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class})
    public ResponseEntity<String> handleBadRequest(Exception e, WebRequest request) {
        log.error("Request {} encountered the following error : ", request.getContextPath(), e);
        return createResponseEntity(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<String> handleBusinessException(Exception e, WebRequest request) {
        log.error("Request {} encountered the following error : ", request.getContextPath(), e);
        return createResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }


    public static ResponseEntity createResponseEntity(HttpStatus status, String msg) {
        return ResponseEntity
                .status(status)
                .body(msg);
    }

}
