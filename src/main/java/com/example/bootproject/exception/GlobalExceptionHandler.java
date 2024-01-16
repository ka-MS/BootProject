package com.example.bootproject.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorMsg> setErrorResponse(Exception e, HttpStatus status){
        ErrorMsg msg = new ErrorMsg(e, status);
        return ResponseEntity.status(status).body(msg);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMsg> illegalArgumentExceptionHandle(IllegalArgumentException e) {
        log.error("IllegalArgumentException", e);
        return setErrorResponse(e,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMsg> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        return setErrorResponse(e,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMsg> badRequestException(BadRequestException e) {
        log.error("BadRequestException", e);
        return setErrorResponse(e,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMsg> notFoundException(NotFoundException e) {
        log.error("NotFoundException", e);
        return setErrorResponse(e,HttpStatus.NOT_FOUND);
    }
}
