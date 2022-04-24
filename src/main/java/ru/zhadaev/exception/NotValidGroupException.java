package ru.zhadaev.exception;

public class NotValidGroupException extends RuntimeException {
    public NotValidGroupException(String s) {
        super(s);
    }
}