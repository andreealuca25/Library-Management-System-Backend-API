package com.example.library_management.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.library_management.domain.User;
import com.example.library_management.repositories.UserRepository;
import com.example.library_management.services.impl.UserServiceImpl;
import com.example.library_management.utils.UserMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = UserMocks.mockUser();
        BCryptPasswordEncoder realPasswordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = realPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
    }

    @Test
    void whenLogin_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);

        Optional<User> result = userService.login(user.getEmail(), "password123");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(passwordEncoder, times(1)).matches("password123", user.getPassword());
    }

    @Test
    void whenLogin_Failure() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        Optional<User> result = userService.login(user.getEmail(), "wrongPassword");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(passwordEncoder, times(1)).matches("wrongPassword", user.getPassword());
    }

    @Test
    void whenLogin_UserNotFound() {
        when(userRepository.findByEmail("aaaaaaaaa@example.com")).thenReturn(null);

        Optional<User> result = userService.login("aaaaaaaaa@example.com", "password");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail("aaaaaaaaa@example.com");
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
    }
}
