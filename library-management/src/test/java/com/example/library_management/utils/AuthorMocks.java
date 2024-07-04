package com.example.library_management.utils;

import com.example.library_management.domain.Author;

import java.util.ArrayList;
import java.util.List;

public class AuthorMocks {

    public static Author mockAuthor() {
        return Author.builder()
                .id(1L)
                .name("J.K. Rowling")
                .bio("bio1")
                .build();
    }

    public static List<Author> mockAuthorList() {
        List<Author> authors = new ArrayList<>();
        authors.add(Author.builder().id(1L).name("J.K. Rowling").bio("bio1").build());
        authors.add(Author.builder().id(2L).name("George R.R. Martin").bio("bio2").build());
        authors.add(Author.builder().id(3L).name("J.R.R. Tolkien").bio("bio3").build());
        return authors;
    }
}
