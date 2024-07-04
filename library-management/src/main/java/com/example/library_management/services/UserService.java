package com.example.library_management.services;

import com.example.library_management.domain.User;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public interface UserService {
    void signup(User user);

    Optional<User> login(String email, String password);

    void removeAllUsers();
}
