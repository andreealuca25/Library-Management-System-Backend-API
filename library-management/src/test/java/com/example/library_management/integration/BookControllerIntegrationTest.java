package com.example.library_management.integration;

import com.example.library_management.domain.Author;
import com.example.library_management.domain.Book;
import com.example.library_management.services.AuthorService;
import com.example.library_management.services.BookService;
import com.example.library_management.utils.AuthorMocks;
import com.example.library_management.utils.BookMocks;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;

    @BeforeEach
    void setUp() {
        bookService.deleteAllBooks();
        authorService.deleteAllAuthors();

        Author author = AuthorMocks.mockAuthor();
        authorService.addAuthor(author);
        author = authorService.retrieveAllAuthors().get(0);

        book = BookMocks.mockBook(author);
        bookService.addBook(book);
        book = bookService.getAllBooks().get(0);
    }

    @Test
    void whenGetAllBooks_thenReturnListOfBooks() throws Exception {
        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(book.getTitle())));
    }

    @Test
    void whenAddBook_thenReturnResponseCreated() throws Exception {
        Author newAuthor = AuthorMocks.mockAuthor2();
        authorService.addAuthor(newAuthor);
        newAuthor = authorService.retrieveAllAuthors().get(1);

        Book newBook = BookMocks.mockBook2(newAuthor);

        mockMvc.perform(post("/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Book added successfully"));
    }

    @Test
    void whenUpdateBook_givenExistentId_thenReturnResponseOk() throws Exception {
        book.setDescription("new description");

        mockMvc.perform(post("/books/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book updated successfully"));
    }

    @Test
    void whenUpdateBook_givenNonExistentId_thenReturnResponseNotFound() throws Exception {
        Book nonExistingBook = Book.builder()
                .id(2L)
                .title("Not Found Title")
                .description("description2")
                .isbn("9780747532733")
                .author(AuthorMocks.mockAuthor2())
                .build();
        mockMvc.perform(post("/books/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistingBook)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book not found"));
    }


    @Test
    void whenDeleteBook_givenExistentId_thenReturnResponseOk() throws Exception {
        mockMvc.perform(delete("/books/remove/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Book removed successfully"));
    }

    @Test
    void whenDeleteBook_givenNonExistentId_thenReturnNotFound() throws Exception {
        mockMvc.perform(delete("/books/remove/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book not found with id: 999"));
    }

    @Test
    void whenGetBookById_givenExistentId_thenReturnResponseOk() throws Exception {
        mockMvc.perform(get("/books/getInfo/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(book.getTitle())));
    }

    @Test
    void whenGetBookById_givenNonExistentId_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/books/getInfo/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
