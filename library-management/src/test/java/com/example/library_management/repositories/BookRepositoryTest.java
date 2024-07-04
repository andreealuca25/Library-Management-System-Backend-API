package com.example.library_management.repositories;

import com.example.library_management.domain.Author;
import com.example.library_management.domain.Book;
import com.example.library_management.utils.AuthorMocks;
import com.example.library_management.utils.BookMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookRepositoryTest {

    @Mock
    private BookRepository bookRepository;

    private Book book;
    private List<Book> books;

    @BeforeEach
    public void setUp() {
        Author author = AuthorMocks.mockAuthor();
        List<Author> authors = AuthorMocks.mockAuthorList();
        book = BookMocks.mockBook(author);
        books = BookMocks.mockBookList(authors);
    }

    @Test
    void whenFindById_thenReturnBook() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookRepository.findById(book.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getId()).isEqualTo(book.getId());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
        assertThat(foundBook.get().getDescription()).isEqualTo(book.getDescription());
        verify(bookRepository, times(1)).findById(book.getId());
    }

    @Test
    void whenFindAll_thenReturnBooks() {
        when(bookRepository.findAll()).thenReturn(books);

        Iterable<Book> foundBooks = bookRepository.findAll();

        assertThat(foundBooks).isNotNull();
        assertThat(((List<Book>) foundBooks).size()).isEqualTo(books.size());

        verify(bookRepository, times(1)).findAll();
    }


    @Test
    void whenSave_thenReturnBook() {
        when(bookRepository.save(book)).thenReturn(book);
        Book savedBook = bookRepository.save(book);
        assertThat(savedBook.getId()).isEqualTo(book.getId());
        assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(savedBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(savedBook.getIsbn()).isEqualTo(book.getIsbn());
        assertThat(savedBook.getDescription()).isEqualTo(book.getDescription());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void whenDelete_thenBookShouldBeDeleted() {

        doNothing().when(bookRepository).deleteById(book.getId());
        bookRepository.deleteById(book.getId());
        verify(bookRepository, times(1)).deleteById(book.getId());
    }
}
