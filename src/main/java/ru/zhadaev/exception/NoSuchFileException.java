package ru.zhadaev.exception;

public class NoSuchFileException extends RuntimeException{
    public NoSuchFileException(String s) {
        super(s);
    }
}
