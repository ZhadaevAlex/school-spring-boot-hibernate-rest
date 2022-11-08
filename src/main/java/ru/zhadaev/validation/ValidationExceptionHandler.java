package ru.zhadaev.validation;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ValidationErrorResponse onConstraintViolationException(ConstraintViolationException ex) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        ex.getConstraintViolations().forEach(violation ->
                response.getViolations().add(new Violation(
                        violation.getMessage(),
                        violation.getPropertyPath().toString(),
                        violation.getInvalidValue(),
                        ex.getClass().toString())));
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                response.getViolations().add(new Violation(
                        fieldError.getDefaultMessage(),
                        fieldError.getObjectName(),
                        fieldError.getRejectedValue(),
                        ex.getClass().toString())));
        return response;
    }
}
