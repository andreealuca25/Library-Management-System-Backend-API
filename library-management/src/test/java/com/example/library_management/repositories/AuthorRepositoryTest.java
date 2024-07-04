package com.example.library_management.repositories;


import com.example.library_management.domain.Author;
import com.example.library_management.utils.AuthorMocks;
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
public class AuthorRepositoryTest {

    @Mock
    private AuthorRepository authorRepository;

    private Author author;

    List<Author> authors;

    @BeforeEach
    public void setUp() {
        author = AuthorMocks.mockAuthor();
        authors = AuthorMocks.mockAuthorList();
    }

    @Test
    public void whenFindById_thenReturnAuthor() {
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

        Optional<Author> foundAuthor = authorRepository.findById(author.getId());
        assertThat(foundAuthor).isPresent();
        assertThat(foundAuthor.get().getBio()).isEqualTo(author.getBio());
        assertThat(foundAuthor.get().getId()).isEqualTo(author.getId());
        assertThat(foundAuthor.get().getName()).isEqualTo(author.getName());

        verify(authorRepository, times(1)).findById(author.getId());
    }

    @Test
    void whenFindAll_thenReturnAuthors() {
        when(authorRepository.findAll()).thenReturn(authors);

        Iterable<Author> foundAuthors = authorRepository.findAll();

        assertThat(foundAuthors).isNotNull();
        assertThat(((List<Author>) foundAuthors).size()).isEqualTo(authors.size());

        verify(authorRepository, times(1)).findAll();
    }

    @Test
    public void whenSave_thenReturnAuthor() {
        when(authorRepository.save(author)).thenReturn(author);

        Author savedAuthor = authorRepository.save(author);
        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.getName()).isEqualTo(author.getName());
        assertThat(savedAuthor.getBio()).isEqualTo(author.getBio());
        assertThat(savedAuthor.getId()).isEqualTo(author.getId());

        verify(authorRepository, times(1)).save(author);
    }

    @Test
    public void whenDelete_thenAuthorShouldBeDeleted() {
        doNothing().when(authorRepository).deleteById(author.getId());

        authorRepository.deleteById(author.getId());
        verify(authorRepository, times(1)).deleteById(author.getId());
    }
}
