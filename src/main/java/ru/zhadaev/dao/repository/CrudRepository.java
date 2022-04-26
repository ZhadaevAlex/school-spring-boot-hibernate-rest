package ru.zhadaev.dao.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
        T save(T entity) ;

        Optional<T> findById(ID id) ;

        List<T> find(T entity) ;

        List<T> findAll();

        boolean existsById(ID id);

        long count();

        void deleteById(ID id);

        void delete(T entity);

        void deleteAll();
}

