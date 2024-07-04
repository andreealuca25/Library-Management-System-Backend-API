package com.example.library_management.controllers;

import com.example.library_management.domain.User;
import com.example.library_management.services.UserService;
import com.example.library_management.utils.UserMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        user = UserMocks.mockUser();
    }

    @Test
    void whenSignup_thenReturnCreatedStatus() throws Exception {
        doNothing().when(userService).signup(any(User.class));

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"johndoe@example.com\", \"password\":\"password123\", \"firstName\":\"John\", \"lastName\":\"Doe\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("User signed up successfully"));

        verify(userService, times(1)).signup(any(User.class));
    }

    @Test
    void whenLoginWithValidCredentials_thenReturnOkStatus() throws Exception {
        when(userService.login(anyString(), anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"johndoe@example.com\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User logged in successfully"));

        verify(userService, times(1)).login(anyString(), anyString());
    }

    @Test
    void whenLoginWithInvalidCredentials_thenReturnUnauthorizedStatus() throws Exception {
        when(userService.login(anyString(), anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"johndoe@example.com\", \"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));

        verify(userService, times(1)).login(anyString(), anyString());
    }
}
