package ru.zhadaev.api.errors;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String s) {
        super(s);
    }
}
