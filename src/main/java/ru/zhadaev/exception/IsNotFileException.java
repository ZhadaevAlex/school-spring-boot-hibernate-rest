package ru.zhadaev.exception;

public class IsNotFileException extends RuntimeException {
    public IsNotFileException(String file) {
        super(file);
    }
}
