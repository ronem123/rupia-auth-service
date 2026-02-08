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

    //End points for admin access
//    @PostMapping("/login")
//    ResponseEntity<ApiResponse<CreateUserResponse>> login(@RequestBody CreateUserRequest request) {
//        log.info("Auth Controller UserRequest body : {}", request);
//        CreateUserResponse response = authService.login(request);
//        return new ResponseEntity<>(new ApiResponse<>(true, "User created", response), HttpStatus.OK);
//    }


    // Create admin. Only super admin has access to this
    @PostMapping("/admins")
    ResponseEntity<CreateUserResponse> createAdmin(@RequestBody CreateUserRequest request) {
        log.info("Admin Controller UserRequest body : {}", request);
        CreateUserResponse createUserResponse = authService.createNewUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserResponse);
    }

    @PutMapping("/admins/activate/{userId}")
    ResponseEntity<ApiResponse<Boolean>> activateUser(@PathVariable Long userId) {
        Boolean activated = authService.activateUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(activated, activated ? "User activated" : "Error during activation", activated));
    }

    //Get list of admins
//    @GetMapping("/admins")
//    ResponseEntity<CreateUserResponse> createAdmin(@RequestBody CreateUserRequest request) {
//        log.info("Admin Controller UserRequest body : {}", request);
//        CreateUserResponse createUserResponse = adminService.createAdmin(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createUserResponse);
//    }

//    @PutMapping("/admins/{userId}/activate")
//    ResponseEntity<CreateUserResponse> approveAdmin(@PathVariable Long userId) {
//        CreateUserResponse createUserResponse = adminService.approveAdmin(userId);
//        return ResponseEntity.status(HttpStatus.OK).body(createUserResponse);
//    }

//    @PutMapping("/admins/{userId}/block")
//    ResponseEntity<CreateUserResponse> approveAdmin(@PathVariable Long userId) {
//        CreateUserResponse createUserResponse = adminService.approveAdmin(userId);
//        return ResponseEntity.status(HttpStatus.OK).body(createUserResponse);
//    }


    //End points for Customers
    //internal endpoints
//    @PostMapping("/internal/users")
//    ResponseEntity<ApiResponse<CreateUserResponse>> createNewUser(@RequestBody CreateUserRequest request) {
//        log.info("Auth Controller UserRequest body : {}", request);
//        CreateUserResponse response = authService.createNewUser(request);
//        return new ResponseEntity<>(new ApiResponse<>(true, "User created", response), HttpStatus.OK);
//    }

//    @GetMapping("/internal/users/{userId}")
//    ResponseEntity<CreateUserResponse> createAdmin(@RequestBody CreateUserRequest request) {
//        log.info("Admin Controller UserRequest body : {}", request);
//        CreateUserResponse createUserResponse = adminService.createAdmin(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createUserResponse);
//    }


//    @PostMapping("/otp/send")
//    ResponseEntity<ApiResponse<CreateUserResponse>> login(@RequestBody CreateUserRequest request) {
//        log.info("Auth Controller UserRequest body : {}", request);
//        CreateUserResponse response = authService.login(request);
//        return new ResponseEntity<>(new ApiResponse<>(true, "User created", response), HttpStatus.OK);
//    }
//
//    @PostMapping("/otp/verify")
//    ResponseEntity<ApiResponse<CreateUserResponse>> login(@RequestBody CreateUserRequest request) {
//        log.info("Auth Controller UserRequest body : {}", request);
//        CreateUserResponse response = authService.login(request);
//        return new ResponseEntity<>(new ApiResponse<>(true, "User created", response), HttpStatus.OK);
//    }


    //End points for Both Customers and Admins
//    @PostMapping("/token/refresh")
//    ResponseEntity<ApiResponse<CreateUserResponse>> createNewUser(@RequestBody CreateUserRequest request) {
//        log.info("Auth Controller UserRequest body : {}", request);
//        CreateUserResponse response = authService.createNewUser(request);
//        return new ResponseEntity<>(new ApiResponse<>(true, "User created", response), HttpStatus.OK);
//    }
//
//    @PostMapping("/logout")
//    ResponseEntity<ApiResponse<CreateUserResponse>> createNewUser(@RequestBody CreateUserRequest request) {
//        log.info("Auth Controller UserRequest body : {}", request);
//        CreateUserResponse response = authService.createNewUser(request);
//        return new ResponseEntity<>(new ApiResponse<>(true, "User created", response), HttpStatus.OK);
//    }

}
