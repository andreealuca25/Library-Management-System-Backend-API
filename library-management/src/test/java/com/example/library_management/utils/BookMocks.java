package com.example.library_management.utils;

import com.example.library_management.domain.Author;
import com.example.library_management.domain.Book;

import java.util.ArrayList;
import java.util.List;

public class BookMocks {

    public static Book mockBook(Author author) {
        return Book.builder()
                .id(1L)
                .title("Harry Potter and the Philosopher's Stone")
                .description("description1")
                .isbn("9780747532743")
                .author(author)
                .build();
    }

    public static Book mockBook2(Author author) {
        return Book.builder()
                .id(2L)
                .title("Game of Thrones")
                .description("description2")
                .isbn("9780747532733")
                .author(author)
                .build();
    }

    public static List<Book> mockBookList(List<Author> authors) {
        List<Book> books = new ArrayList<>();

        books.add(Book.builder().id(1L).title("Harry Potter and the Philosopher's Stone").description("description1").isbn("9780747532743").author(authors.get(0)).build());
        books.add(Book.builder().id(2L).title("Game of Thrones").description("description2").isbn("9780553103540").author(authors.get(1)).build());
        books.add(Book.builder().id(3L).title("The Hobbit").description("description3").isbn("9780261103344").author(authors.get(2)).build());

        return books;
    }
}
