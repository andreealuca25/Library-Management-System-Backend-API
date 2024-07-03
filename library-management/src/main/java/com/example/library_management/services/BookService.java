package com.example.library_management.services;

import com.example.library_management.domain.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {
    List<Book> getAllBooks();

    void addBook(Book book);

    void updateBook(Book book);

    Book getBookById(long id);

    void removeBook(long id);
}
