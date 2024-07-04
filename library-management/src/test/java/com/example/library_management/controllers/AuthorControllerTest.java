package com.example.library_management.controllers;

import com.example.library_management.domain.Author;
import com.example.library_management.domain.User;
import com.example.library_management.services.AuthorService;
import com.example.library_management.utils.AuthorMocks;
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
public class AuthorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private Author author;
    private List<Author> authors;
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
        author = AuthorMocks.mockAuthor();
        authors = AuthorMocks.mockAuthorList();
        objectMapper = new ObjectMapper();
    }

    @Test
    void whenGetAllAuthors_thenReturnListOfAuthors() throws Exception {
        when(authorService.retrieveAllAuthors()).thenReturn(authors);

        mockMvc.perform(get("/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authors)));

        verify(authorService, times(1)).retrieveAllAuthors();
    }

    @Test
    void whenAddAuthor_thenReturnResponseOk() throws Exception {
        doNothing().when(authorService).addAuthor(any(Author.class));

        mockMvc.perform(post("/authors/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isCreated());

        verify(authorService, times(1)).addAuthor(any(Author.class));
    }

    @Test
    void whenUpdateAuthor_thenReturnResponseOk() throws Exception {
        doNothing().when(authorService).updateAuthor(any(Author.class));

        mockMvc.perform(post("/authors/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isOk());

        verify(authorService, times(1)).updateAuthor(any(Author.class));
    }

    @Test
    void whenDeleteAuthor_givenExistentId_thenReturnResponseOk() throws Exception {
        doNothing().when(authorService).deleteAuthor(author.getId());

        mockMvc.perform(delete("/authors/remove/{id}", author.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Author removed successfully"));

        verify(authorService, times(1)).deleteAuthor(author.getId());
    }

    @Test
    void whenDeleteAuthor_givenNonExistentId_thenReturnNotFound() throws Exception {
        doThrow(new NoSuchElementException()).when(authorService).deleteAuthor(author.getId());

        mockMvc.perform(delete("/authors/remove/{id}", author.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Author not found with id: " + author.getId()));

        verify(authorService, times(1)).deleteAuthor(author.getId());
    }

    @Test
    void whenGetAuthorById_givenExistentId_thenReturnResponseOk() throws Exception {
        when(authorService.getAuthorById(author.getId())).thenReturn(author);

        mockMvc.perform(get("/authors/getInfo/{id}", author.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(author)));

        verify(authorService, times(1)).getAuthorById(author.getId());
    }

    @Test
    void whenGetAuthor_givenNonExistentId_thenReturnNotFound() throws Exception {
        long nonExistentId = 999L;
        when(authorService.getAuthorById(nonExistentId)).thenReturn(null);

        mockMvc.perform(get("/authors/getInfo/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(authorService, times(1)).getAuthorById(nonExistentId);
    }
}
