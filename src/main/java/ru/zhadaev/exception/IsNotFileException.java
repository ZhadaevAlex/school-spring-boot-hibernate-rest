package ru.zhadaev.exception;

import java.nio.file.FileSystemException;

public class IsNotFileException extends FileSystemException {
    public IsNotFileException(String file) {
        super(file);
    }
}
