package com.ronem.authservice;

import com.ronem.authservice.model.enums.UserRole;
import com.ronem.authservice.model.request.CreateUserRequest;
import com.ronem.authservice.service.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RupiaAuthServiceApplicationTests {

    @Autowired
    private AuthServiceImpl authService;

    @Test
    void createUser() {
        CreateUserRequest request = CreateUserRequest.builder()
                .mobileNumber("9808065961")
                .email("ram.cp18113@gmail.com")
                .userRole(UserRole.CUSTOMER.name())
                .build();
        authService.createNewUser(request);
    }

}
