package com.example.library_management.services;

import com.example.library_management.domain.Author;
import com.example.library_management.repositories.AuthorRepository;
import com.example.library_management.services.impl.AuthorServiceImpl;
import com.example.library_management.utils.AuthorMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;
    List<Author> authors;

    @BeforeEach
    void setUp() {
        author = AuthorMocks.mockAuthor();
        authors = AuthorMocks.mockAuthorList();
    }

    @Test
    void whenGetAuthorById_givenExistingId_shouldReturnAuthor() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Author foundAuthor = authorService.getAuthorById(1L);

        assertNotNull(foundAuthor);
        assertEquals(author.getId(), foundAuthor.getId());
        assertEquals(author.getName(), foundAuthor.getName());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void whenGetAuthorById_givenNonExistingId_shouldThrowException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            authorService.getAuthorById(1L);
        });
        assertEquals("Author not found with id: 1", exception.getMessage());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void whenUpdateAuthor_givenExistingAuthor_shouldUpdateAuthor() {
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.of(author));

        authorService.updateAuthor(author);

        verify(authorRepository, times(1)).findByName(author.getName());
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void whenUpdateAuthor_givenNonExistingAuthor_shouldThrowException() {
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            authorService.updateAuthor(author);
        });

        assertEquals("Author not found", exception.getMessage());
        verify(authorRepository, times(1)).findByName(author.getName());
        verify(authorRepository, times(0)).save(author);
    }


    @Test
    void whenAddAuthor_shouldAddAuthor() {
        when(authorRepository.save(author)).thenReturn(author);

        authorService.addAuthor(author);

        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void whenDeleteAuthor_givenExistingId_shouldDeleteAuthor() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        authorService.deleteAuthor(1L);

        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).delete(author);
    }

    @Test
    void whenDeleteAuthor_givenNonExistingId_shouldThrowException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            authorService.deleteAuthor(1L);
        });
        assertEquals("Author not found with id: 1", exception.getMessage());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(0)).delete(any());
    }

    @Test
    void whenRetrieveAllAuthors_shouldReturnAuthorList() {
        when(authorRepository.findAll()).thenReturn(authors);

        List<Author> retrievedAuthors = authorService.retrieveAllAuthors();

        assertNotNull(retrievedAuthors);
        assertEquals(authors.size(), retrievedAuthors.size());
        verify(authorRepository, times(1)).findAll();
    }
}
