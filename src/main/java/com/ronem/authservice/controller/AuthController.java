package com.ronem.authservice.controller;

import com.ronem.authservice.model.request.CreateUserRequest;
import com.ronem.authservice.model.response.ApiResponse;
import com.ronem.authservice.model.response.CreateUserResponse;
import com.ronem.authservice.service.AuthServiceImpl;
import jakarta.ws.rs.PUT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @GetMapping(value = "/greet")
    ResponseEntity<HashMap<String, String>> greet() {
        HashMap<String, String> body = new HashMap<>();
        body.put("Status", "success");
        body.put("Message", "Welcome to microservice");
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping("/internal/user")
    ResponseEntity<ApiResponse<CreateUserResponse>> createNewUser(@RequestBody CreateUserRequest request) {
        log.info("Auth Controller UserRequest body : {}", request);
        CreateUserResponse response = authService.createNewUser(request);
        return new ResponseEntity<>(new ApiResponse<>(true, "User created", response), HttpStatus.OK);
    }

    @PutMapping("/internal/activate/{userId}")
    ResponseEntity<ApiResponse<Boolean>> activateUser(@PathVariable Long userId) {
        Boolean activated = authService.activateUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(activated, activated ? "User activated" : "Error during activation", activated));
    }

}
