package ru.zhadaev.exception;

public class FileReadException extends RuntimeException{
    public FileReadException(String s) {
        super(s);
    }
}
