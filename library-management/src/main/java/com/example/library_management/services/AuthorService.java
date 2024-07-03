package com.example.library_management.services;

import com.example.library_management.domain.Author;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthorService {
    Author getAuthorById(long id);

    void updateAuthor(Author author);

    void addAuthor(Author author);

    void deleteAuthor(long id);

    List<Author> retrieveAllAuthors();
}
