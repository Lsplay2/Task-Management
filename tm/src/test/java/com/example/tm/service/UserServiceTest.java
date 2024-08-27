package com.example.tm.service;

import com.example.tm.entity.User;
import com.example.tm.exception.NotFoundException;
import com.example.tm.exception.ValidationException;
import com.example.tm.repository.UserRepository;
import com.example.tm.security.JwtUtil;
import com.example.tm.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User getUserForTest() {
        User user = new User();
        user.setId(1L);
        user.setName("Alex");
        user.setEmail("lsplay@yandex.ru");
        user.setPassword("qweqwe");
        return user;
    }

    @Test
    public void getUserBiIdTest() throws NotFoundException {
        Long id = 1L;
        User user = getUserForTest();
        when(userRepository.getUserById(id)).thenReturn(user);
        when(userRepository.existsById(id)).thenReturn(true);
        Assertions.assertEquals(user, userService.getUserById(id));
    }

    @Test
    public void getUserWithWrongIdTest() {
        Long id = 1L;
        User user = getUserForTest();
        when(userRepository.existsById(id)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {userService.getUserById(id);});
    }

    @Test
    public void saveUserEmailException() throws ValidationException {
        User user = getUserForTest();
        user.setEmail("");
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.saveUser(user);
        });
    }

    @Test
    public void saveUserEmailCopyException() throws ValidationException {
        when(userRepository.existsByEmail("lsplay@yandex.ru")).thenReturn(true);
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.saveUser(getUserForTest());
        });
    }

    @Test
    public void saveUserNameException() throws ValidationException {
        User user = getUserForTest();
        user.setName("");
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.saveUser(user);
        });
    }

    @Test
    public void saveUserPasswordException() throws ValidationException {
        User user = getUserForTest();
        user.setPassword("");
        Assertions.assertThrows(ValidationException.class, () -> {
            userService.saveUser(user);
        });
    }

    @Test
    public void getByTokenTest() {
        String token = "Bearer qweasfqwrhnqogn";
        when(userRepository.getUserByEmail("lsplay@yandex.ru")).thenReturn(getUserForTest());
        when(jwtUtil.extractUsername("qweasfqwrhnqogn")).thenReturn("lsplay@yandex.ru");

        Assertions.assertEquals(getUserForTest(), userService.getByToken(token));
    }
}
