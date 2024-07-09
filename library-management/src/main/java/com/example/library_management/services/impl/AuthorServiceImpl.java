package com.example.library_management.services.impl;

import com.example.library_management.domain.Author;
import com.example.library_management.repositories.AuthorRepository;
import com.example.library_management.services.AuthorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author getAuthorById(long id) {
        return authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Author not found with id: " + id));
    }

    @Override
    public void updateAuthor(Author author) {
        Optional<Author> optionalAuthor = authorRepository.findByName(author.getName());
        if (optionalAuthor.isPresent()) {
            Author authorToUpdate = optionalAuthor.get();
            authorToUpdate.setBio(author.getBio());
            authorRepository.save(authorToUpdate);
        }
        else {
            throw new NoSuchElementException("Author not found");
        }
    }

    @Override
    public void addAuthor(Author author) {
        authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Author not found with id: " + id));
        authorRepository.delete(author);
    }

    public List<Author> retrieveAllAuthors() {
        Iterable<Author> iterable = authorRepository.findAll();
        List<Author> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    @Override
    public void deleteAllAuthors() {
        authorRepository.deleteAll();
    }

}
