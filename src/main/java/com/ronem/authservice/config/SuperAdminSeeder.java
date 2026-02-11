/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:09/02/2026
 * Time:16:52
 */


package com.ronem.authservice.config;

import com.ronem.authservice.model.entity.User;
import com.ronem.authservice.model.enums.UserRole;
import com.ronem.authservice.model.enums.UserStatus;
import com.ronem.authservice.repository.AuthRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuperAdminSeeder {
    private final AuthRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Value("${bootstrap.super-admin.email}")
    private String email;

    @Value("${bootstrap.super-admin.password}")
    private String password;

    @Value("${server.address:NOT_SET}")
    private String serverAddress;

    @Value("${server.port}")
    private int serverPort;

    @PostConstruct
    void logServerConfig() {
        System.out.println("Server address = " + serverAddress);
        System.out.println("Server port = " + serverPort);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void seedSuperAdmin() {
        boolean exists = repository.existsByEmail(email);
        log.info("SuperAdminSeeder exists: {}", exists);
        if (!exists) {
            //create fresh new super-admin
            User superAdmin = new User();
            superAdmin.setStatus(UserStatus.ACTIVE);
            superAdmin.setEmail(email);
            superAdmin.setPassword(passwordEncoder.encode(password));
            superAdmin.setMobileNumber("9808065961");
            superAdmin.setActivatedAt(LocalDateTime.now());
            superAdmin.setCreatedAt(LocalDateTime.now());
            superAdmin.setLastLoginAt(LocalDateTime.now());
            superAdmin.setUserRole(UserRole.SUPER_ADMIN);
            repository.save(superAdmin);
            System.out.println("Super Admin created");
        }
    }
}