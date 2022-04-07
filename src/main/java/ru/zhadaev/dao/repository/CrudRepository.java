package ru.zhadaev.dao.repository;

import ru.zhadaev.exception.DAOException;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
        T save(T entity) throws DAOException;

        Optional<T> findById(ID id) throws DAOException;

        boolean existsById(ID id) throws DAOException;

        List<T> findAll() throws DAOException;

        long count() throws DAOException;

        void deleteById(ID id) throws DAOException;

        void delete(T entity) throws DAOException;

        void deleteAll() throws DAOException;
}

