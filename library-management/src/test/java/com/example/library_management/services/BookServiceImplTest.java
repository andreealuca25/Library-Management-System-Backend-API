package com.example.library_management.services;

import com.example.library_management.domain.Author;
import com.example.library_management.domain.Book;
import com.example.library_management.repositories.BookRepository;
import com.example.library_management.services.impl.BookServiceImpl;
import com.example.library_management.utils.AuthorMocks;
import com.example.library_management.utils.BookMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookServiceImpl bookService;

    private Book book;

    private List<Book> books;

    @BeforeEach
    void setUp() {
        Author author = AuthorMocks.mockAuthor();
        List<Author> authors = AuthorMocks.mockAuthorList();
        book = BookMocks.mockBook(author);
        books = BookMocks.mockBookList(authors);
    }

    @Test
    void whenGetAllBooks_thenReturnListOfBooks() {
       when(bookRepository.findAll()).thenReturn(books);

       List<Book> retrievedBooks = bookService.getAllBooks();
       assertNotNull(retrievedBooks);
       assertEquals(retrievedBooks.size(), books.size());
       verify(bookRepository,times(1)).findAll();
    }

    @Test
    void whenGetBookById_givenExistingId_thenReturnBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book retrievedBook = bookService.getBookById(1L);
        assertNotNull(retrievedBook);
        assertEquals(book.getId(), retrievedBook.getId());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void whenGetBookById_givenNonExistingId_thenReturnNull() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            bookService.getBookById(1L);
        });
        assertEquals("Book not found with id: 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void whenAddBook_shouldAddBook() {
        when(bookRepository.save(book)).thenReturn(book);

        bookService.addBook(book);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void whenUpdateBook_shouldUpdateBook() {
        when(bookRepository.save(book)).thenReturn(book);

        bookService.updateBook(book);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void whenDeleteBook_GivenExistingId_shouldDeleteBook() {

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        bookService.removeBook(1L);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void whenDeleteBook_GivenNonExistingId_shouldDeleteBook() {

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            bookService.removeBook(1L);
        });

        assertEquals("Book not found with id: 1", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(0)).delete(any());
    }

}
