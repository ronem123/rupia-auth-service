package com.ronem.authservice.controller;

import com.ronem.authservice.model.dto.LoginRequest;
import com.ronem.authservice.model.dto.UserDTO;
import com.ronem.authservice.model.enums.UserRole;
import com.ronem.authservice.model.request.CreateUserRequest;
import com.ronem.authservice.model.response.ApiResponse;
import com.ronem.authservice.model.response.CreateUserResponse;
import com.ronem.authservice.model.response.LoginResponse;
import com.ronem.authservice.service.AuthServiceImpl;
import com.ronem.authservice.validation.AdminValidation;
import jakarta.validation.Valid;
import jakarta.ws.rs.PUT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;


    //End points for admin access
    @PostMapping("/admin/login")
    ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Auth Controller UserRequest body : {}", request);
        LoginResponse response = authService.adminLogin(request.getEmail(), request.getPassword());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "success", response));
    }


    // Create admin. Only super admin has access to this
    @PostMapping("/admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    ResponseEntity<ApiResponse<CreateUserResponse>> createAdmin(@Validated(AdminValidation.class) @RequestBody CreateUserRequest request) {
        log.info("Admin Controller UserRequest body : {}", request);
        CreateUserResponse createUserResponse = authService.createNewUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "user created", createUserResponse));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    ResponseEntity<ApiResponse<List<UserDTO>>> getAdmins() {
        List<UserDTO> users = authService.getUserLists(UserRole.ADMIN);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "success", users));
    }

    @PutMapping("/admin/activate/{userId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    ResponseEntity<ApiResponse<Boolean>> activateUser(@PathVariable Long userId) {
        Boolean activated = authService.activateUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(activated, activated ? "User activated" : "Error during activation", activated));
    }

    @PutMapping("/admin/block/{userId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    ResponseEntity<ApiResponse<Boolean>> blockUser(@PathVariable Long userId) {
        Boolean blocked = authService.blockUser(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(blocked, blocked ? "User blocked" : "Error during user block", blocked));
    }


    //End points for Customers
    //internal endpoints
    @PostMapping("/internal/users")
    ResponseEntity<ApiResponse<CreateUserResponse>> createCustomerUser(@RequestBody CreateUserRequest request) {
        CreateUserResponse response = authService.createNewUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User created", response));
    }


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
