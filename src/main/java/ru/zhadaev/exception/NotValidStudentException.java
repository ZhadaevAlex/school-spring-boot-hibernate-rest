package ru.zhadaev.exception;

public class NotValidStudentException extends RuntimeException {
    public NotValidStudentException(String s) {
        super(s);
    }
}
