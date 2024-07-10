package com.example.library_management.integration;

import com.example.library_management.domain.Author;
import com.example.library_management.services.AuthorService;
import com.example.library_management.utils.AuthorMocks;
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
public class AuthorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    private Author author;

    @BeforeEach
    void setUp() {
        authorService.deleteAllAuthors();
        author = AuthorMocks.mockAuthor();
        authorService.addAuthor(author);
        author = authorService.retrieveAllAuthors().get(0);
    }

    @Test
    void whenGetAllAuthors_thenReturnListOfAuthors() throws Exception {
        mockMvc.perform(get("/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(author.getName())));
    }

    @Test
    void whenAddAuthor_thenReturnResponseCreated() throws Exception {
        Author newAuthor = AuthorMocks.mockAuthor2();

        mockMvc.perform(post("/authors/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAuthor)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Author added successfully"));
    }

    @Test
    void whenUpdateAuthor_givenExistentAuthor_thenReturnResponseOk() throws Exception {
        author.setBio("Updated Bio");

        mockMvc.perform(put("/authors/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isOk())
                .andExpect(content().string("Author updated successfully"));
    }

    @Test
    void whenUpdateAuthor_givenNonExistentAuthor_thenReturnResponseNotFound() throws Exception {
        Author nonExistingAuthor = Author.builder()
                .id(60L)
                .name("Non Existing Author")
                .bio("bio1")
                .build();

        mockMvc.perform(put("/authors/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistingAuthor)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Author not found"));
    }


    @Test
    void whenDeleteAuthor_givenExistentId_thenReturnResponseOk() throws Exception {
        mockMvc.perform(delete("/authors/remove/{id}", author.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Author removed successfully"));
    }

    @Test
    void whenDeleteAuthor_givenNonExistentId_thenReturnNotFound() throws Exception {
        mockMvc.perform(delete("/authors/remove/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Author not found with id: 999"));
    }

    @Test
    void whenGetAuthorById_givenExistentId_thenReturnResponseOk() throws Exception {
        mockMvc.perform(get("/authors/getInfo/{id}", author.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(author.getName())));
    }

    @Test
    void whenGetAuthorById_givenNonExistentId_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/authors/getInfo/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
