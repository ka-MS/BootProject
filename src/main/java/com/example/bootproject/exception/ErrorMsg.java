package com.example.bootproject.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;

import java.time.LocalDateTime;

@Getter
public class ErrorMsg {

    private final LocalDateTime timestamp;
    private final String message;
    private final int status;

    public ErrorMsg(Exception e, HttpStatus status) {
        this.timestamp = LocalDateTime.now();
        if (e instanceof BindException) {
            this.message = ((BindException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage();
        } else {
            this.message = e.getMessage();
        }
        this.status = status.value();
    }
}
