package ru.zhadaev.exception;

public class NotValidCourseException extends RuntimeException {
    public NotValidCourseException(String s) {
        super(s);
    }
}
