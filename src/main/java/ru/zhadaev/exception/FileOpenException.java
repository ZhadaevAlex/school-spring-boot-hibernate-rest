package ru.zhadaev.exception;

public class FileOpenException extends RuntimeException {
    public FileOpenException(String s) {
        super(s);
    }
}