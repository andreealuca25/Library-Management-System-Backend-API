package com.example.library_management.repositories;

import com.example.library_management.domain.User;
import com.example.library_management.utils.UserMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = UserMocks.mockUser();
    }

    @Test
    void whenFindByEmail_thenReturnUser() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        User foundUser = userRepository.findByEmail(user.getEmail());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());

        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void whenFindById_thenReturnUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepository.findById(user.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());

        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void whenSave_thenReturnUser() {
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void whenDelete_thenUserShouldBeDeleted() {
        doNothing().when(userRepository).deleteById(user.getId());

        userRepository.deleteById(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());
    }
}
