package com.example.library_management.utils;

import com.example.library_management.domain.User;


public class UserMocks {

    public static User mockUser() {
        return User.builder()
                .id(1L)
                .email("john.doe@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .build();
    }

}
