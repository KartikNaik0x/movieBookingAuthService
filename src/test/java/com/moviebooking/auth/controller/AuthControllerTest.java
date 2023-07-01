package com.moviebooking.auth.controller;

 

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviebooking.auth.exception.InvalidInputException;
import com.moviebooking.auth.exception.JwtGenerationException;
import com.moviebooking.auth.model.User;
import com.moviebooking.auth.payload.JwtResponse;
import com.moviebooking.auth.payload.LoginRequest;
import com.moviebooking.auth.payload.SignupRequest;
import com.moviebooking.auth.security.JwtUtils;
import com.moviebooking.auth.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

 


import java.util.Optional;

 

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 

class AuthControllerTest {

 

    @Mock
    private UserService userService;

 

    @Mock
    private JwtUtils jwtUtils;

 

    @InjectMocks
    private AuthController authController;

 

    private ObjectMapper objectMapper;

 

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

 

    @Test
    void testRegisterUser() {
        SignupRequest signupRequest = new SignupRequest();
        // Set up signupRequest object with required data
        signupRequest.setEmail("naika34@gmail.com");
        signupRequest.setUsername("kartik");
        signupRequest.setRole("admin");
        signupRequest.setPassword("hfioehoei");
        signupRequest.setSecurityQuestion("colour");
        signupRequest.setSecurityAnswer("noen");

 

        doReturn(ResponseEntity.ok("User registered successfully"))
        .when(userService)
        .addUser(any(SignupRequest.class));

 


        ResponseEntity<?> response = authController.registerUser(signupRequest);

 

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());

 

        verify(userService, times(1)).addUser(signupRequest);
    }

 

    @Test
    void testLoginUserWithValidCredentials() throws JwtGenerationException, InvalidInputException {
        LoginRequest loginRequest = new LoginRequest();
        // Set up loginRequest object with valid credentials
        loginRequest.setUsername("kartik");
        loginRequest.setPassword("hfioehoei");

 

        String jwtToken = "sample.jwt.token";
        User user = new User();
        // Set up user object with user details

 

        when(jwtUtils.generatToken(loginRequest)).thenReturn(jwtToken);
        when(userService.getUserByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));

 

        ResponseEntity<?> response = authController.loginUser(loginRequest);

 

        assertEquals(HttpStatus.OK, response.getStatusCode());

 

        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertNotNull(jwtResponse);
        System.out.println(jwtResponse.getAccessToken());
        assertEquals(jwtToken, jwtResponse.getAccessToken());
        // Verify other fields of JwtResponse as per your implementation

 

        verify(jwtUtils, times(1)).generatToken(loginRequest);
        verify(userService, times(1)).getUserByUsername(loginRequest.getUsername());
    }

 

    @Test
    void testLoginUserWithInvalidCredentials() throws JwtGenerationException, InvalidInputException {
        LoginRequest loginRequest = new LoginRequest();
        // Set up loginRequest object with invalid credentials

 

        when(jwtUtils.generatToken(loginRequest)).thenThrow(new InvalidInputException("Invalid Credentials"));

 

        ResponseEntity<?> response = authController.loginUser(loginRequest);

 

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid Credentials", response.getBody());

 

        verify(jwtUtils, times(1)).generatToken(loginRequest);
    }
}