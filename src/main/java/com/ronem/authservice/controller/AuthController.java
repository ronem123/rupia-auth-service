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

    @GetMapping(value = "/greet")
    ResponseEntity<HashMap<String, String>> greet() {
        HashMap<String, String> body = new HashMap<>();
        body.put("Status", "success");
        body.put("Message", "Welcome to microservice");
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    //End points for admin access
    @PostMapping("/admin/login")
    ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Auth Controller UserRequest body : {}", request);
        LoginResponse response = authService.adminLogin(request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "success", response));
    }


    // Create admin. Only super admin has access to this
    @PostMapping("/admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    ResponseEntity<CreateUserResponse> createAdmin(@Validated(AdminValidation.class) @RequestBody CreateUserRequest request) {
        log.info("Admin Controller UserRequest body : {}", request);
        CreateUserResponse createUserResponse = authService.createNewUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserResponse);
    }

    @PutMapping("/admin/activate/{userId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    ResponseEntity<ApiResponse<Boolean>> activateUser(@PathVariable Long userId) {
        Boolean activated = authService.activateUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(activated, activated ? "User activated" : "Error during activation", activated));
    }

    //Get list of admins
    @GetMapping("/admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    ResponseEntity<List<UserDTO>> getAdmins() {
        List<UserDTO> users = authService.getUserLists(UserRole.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

//    @PutMapping("/admin/{userId}/activate")
//    ResponseEntity<CreateUserResponse> approveAdmin(@PathVariable Long userId) {
//        CreateUserResponse createUserResponse = adminService.approveAdmin(userId);
//        return ResponseEntity.status(HttpStatus.OK).body(createUserResponse);
//    }

//    @PutMapping("/admin/{userId}/block")
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
