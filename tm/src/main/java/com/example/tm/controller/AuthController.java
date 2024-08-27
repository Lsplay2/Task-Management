package com.example.tm.controller;

import com.example.tm.entity.User;
import com.example.tm.exception.NotFoundException;
import com.example.tm.exception.ValidationException;
import com.example.tm.security.AuthenticationRequest;
import com.example.tm.security.AuthenticationResponse;
import com.example.tm.security.JwtService;
import com.example.tm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        final String jwt = jwtService.createJwtToken(authenticationRequest);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws NotFoundException, ValidationException {
        if (userService.getByEmail(user.getEmail()) != null){
            return ResponseEntity.badRequest().body("Username is already taken.");
        }
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully.");
    }
}
