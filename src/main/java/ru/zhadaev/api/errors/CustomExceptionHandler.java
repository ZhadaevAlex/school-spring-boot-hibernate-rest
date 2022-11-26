package ru.zhadaev.api.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomError> onNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage(), ex);
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity
                .status(status)
                .body(new CustomError(
                        new Timestamp(System.currentTimeMillis()),
                        status.getReasonPhrase(),
                        ex.getMessage()));
    }
}
