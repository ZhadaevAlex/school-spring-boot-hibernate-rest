package ru.zhadaev.dao.repository;

import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public interface CrudRepository<T, ID> {
    T save(T entity);

    T update(T entity);

    Optional<T> findById(ID id);

    List<T> findLike(T entity);

    List<T> findAll();

    boolean existsById(ID id);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAll();

    default int extractId(KeyHolder keyHolder) {
        return ofNullable(keyHolder.getKey()).map(Number::intValue)
                .orElseThrow(() -> new RuntimeException("ID-generation failed"));
    }
}

