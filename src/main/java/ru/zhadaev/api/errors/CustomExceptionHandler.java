package ru.zhadaev.api.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.zhadaev.service.StudentService;

import java.sql.Timestamp;

@RestControllerAdvice
public class CustomExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomError> onNotFoundException(NotFoundException ex) {
        logger.error(ex.getMessage());
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity
                .status(status)
                .body(new CustomError(
                        new Timestamp(System.currentTimeMillis()),
                        status.getReasonPhrase(),
                        ex.getMessage()));
    }
}
