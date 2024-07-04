package com.example.library_management.services.impl;

import com.example.library_management.domain.Book;
import com.example.library_management.repositories.BookRepository;
import com.example.library_management.services.BookService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    @Override
    public List<Book> getAllBooks() {
        Iterable<Book> iterable = bookRepository.findAll();
        List<Book> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    @Override
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void updateBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public Book getBookById(long id) {
        return bookRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Book not found with id: " + id));

    }

    @Override
    public void removeBook(long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Book not found with id: " + id));
        bookRepository.delete(book);
    }

    @Override
    public void deleteAllBooks() {
        bookRepository.deleteAll();
    }
}
