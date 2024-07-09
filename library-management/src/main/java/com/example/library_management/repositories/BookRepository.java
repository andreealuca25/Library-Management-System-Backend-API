package com.example.library_management.repositories;

import com.example.library_management.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    public Optional<Book> findByTitle(String title);
}
