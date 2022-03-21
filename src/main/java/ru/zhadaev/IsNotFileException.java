package ru.zhadaev;

import java.nio.file.FileSystemException;

public class IsNotFileException extends FileSystemException {
    public IsNotFileException(String file) {
        super(file);
    }
}
