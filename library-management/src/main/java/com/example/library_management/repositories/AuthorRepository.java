package com.example.library_management.repositories;

import com.example.library_management.domain.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
    public Optional<Author> findByName (String name);
}
