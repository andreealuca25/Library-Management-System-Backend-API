package com.example.library_management.controllers;

import com.example.library_management.domain.Author;
import com.example.library_management.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorService.retrieveAllAuthors();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAuthor(@RequestBody Author author) {
        authorService.addAuthor(author);
        return new ResponseEntity<>("Author added successfully", HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateAuthor(@RequestBody Author author) {
        try {
            authorService.updateAuthor(author);
            return new ResponseEntity<>("Author updated successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getInfo/{id}")
    public ResponseEntity<Author> getAuthor(@PathVariable long id) {
        try {
            Author author = authorService.getAuthorById(id);
            return new ResponseEntity<>(author, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeAuthor(@PathVariable long id) {
        try {
            authorService.deleteAuthor(id);
            return new ResponseEntity<>("Author removed successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Author not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }
}
