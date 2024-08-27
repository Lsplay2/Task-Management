package com.example.tm.service;

import com.example.tm.entity.User;
import com.example.tm.exception.NotFoundException;
import com.example.tm.exception.ValidationException;
import com.example.tm.repository.UserRepository;
import com.example.tm.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private UserRepository userRepository;
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }
    public User getUserById(Long id) throws NotFoundException {
        if(!userRepository.existsById(id)) {
            throw new NotFoundException("Wrong user id");
        }
        return userRepository.getUserById(id);
    }

    public void saveUser(User user) throws ValidationException {
        validationCreateUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User getByEmail(String email) throws NotFoundException {
        return userRepository.getUserByEmail(email);
    }

    public User getByToken(String token) {
        token = token.replace("Bearer ", "");
        return userRepository.getUserByEmail(jwtUtil.extractUsername(token));
    }

    private void validationCreateUser(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")
                || user.getName() == null || user.getName().isBlank()
                || user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ValidationException("Указаны не все данные");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ValidationException("Почта уже зарегистрированная");
        }
    }


}
