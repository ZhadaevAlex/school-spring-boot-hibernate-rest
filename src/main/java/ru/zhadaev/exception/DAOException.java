package ru.zhadaev.exception;

import java.sql.SQLException;

public class DAOException extends RuntimeException {
    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Exception e) {
        super(message, e);
    }
}
