package com.example.library_management.controllers;

import com.example.library_management.domain.Author;
import com.example.library_management.domain.Book;
import com.example.library_management.services.BookService;
import com.example.library_management.utils.AuthorMocks;
import com.example.library_management.utils.BookMocks;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private Book book;
    private List<Book> books;
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        Author author = AuthorMocks.mockAuthor();
        List<Author> authors = AuthorMocks.mockAuthorList();
        book = BookMocks.mockBook(author);
        books = BookMocks.mockBookList(authors);
        objectMapper = new ObjectMapper();
    }

    @Test
    void whenGetAllBooks_thenReturnListOfBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(books)));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void whenAddBook_thenReturnResponseCreated() throws Exception {
        doNothing().when(bookService).addBook(any(Book.class));

        mockMvc.perform(post("/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());

        verify(bookService, times(1)).addBook(any(Book.class));
    }

    @Test
    void whenUpdateBook_thenReturnResponseOk() throws Exception {
        doNothing().when(bookService).updateBook(any(Book.class));

        mockMvc.perform(post("/books/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk());

        verify(bookService, times(1)).updateBook(any(Book.class));
    }

    @Test
    void whenDeleteBook_givenExistentId_thenReturnResponseOk() throws Exception {
        doNothing().when(bookService).removeBook(book.getId());

        mockMvc.perform(delete("/books/remove/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Book removed successfully"));

        verify(bookService, times(1)).removeBook(book.getId());
    }

    @Test
    void whenDeleteBook_givenNonExistentId_thenReturnNotFound() throws Exception {
        doThrow(new NoSuchElementException()).when(bookService).removeBook(book.getId());

        mockMvc.perform(delete("/books/remove/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book not found with id: " + book.getId()));

        verify(bookService, times(1)).removeBook(book.getId());
    }

    @Test
    void whenGetBookById_givenExistentId_thenReturnResponseOk() throws Exception {
        when(bookService.getBookById(book.getId())).thenReturn(book);

        mockMvc.perform(get("/books/getInfo/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(book)));

        verify(bookService, times(1)).getBookById(book.getId());
    }

    @Test
    void whenGetBook_givenNonExistentId_thenReturnNotFound() throws Exception {
        long nonExistentId = 999L;
        doThrow(new NoSuchElementException("Book not found with id: " + nonExistentId))
                .when(bookService).getBookById(nonExistentId);

        mockMvc.perform(get("/books/getInfo/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).getBookById(nonExistentId);
    }
}
