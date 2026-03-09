/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:10:16
 */


package com.ronem.authservice.service;

import com.ronem.authservice.exception.BadRequestException;
import com.ronem.authservice.exception.InvalidUserException;
import com.ronem.authservice.exception.UserAlreadyExistException;
import com.ronem.authservice.exception.UserNotFoundException;
import com.ronem.authservice.mapper.UserMapper;
import com.ronem.authservice.model.entity.User;
import com.ronem.authservice.model.enums.UserRole;
import com.ronem.authservice.model.enums.UserStatus;
import com.ronem.authservice.model.dto.request.AdminLoginRequest;
import com.ronem.authservice.model.dto.request.CreateUserRequest;
import com.ronem.authservice.model.dto.response.CreateUserResponse;
import com.ronem.authservice.model.dto.UserDTO;
import com.ronem.authservice.model.dto.response.LoginResponse;
import com.ronem.authservice.repository.AuthRepository;
import com.ronem.authservice.service.jwt.JwtTokenGeneratorService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final JwtTokenGeneratorService jwtAuthService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public CreateUserResponse createNewUser(CreateUserRequest request) {
        log.info("Auth Service UserRequest body : {}", request);
        boolean existedUser = authRepository.existsByEmailOrMobileNumber(request.getEmail(), request.getMobileNumber());
        if (existedUser) {
            throw new UserAlreadyExistException("User already exists with provided information");
        }
        User newUser = userMapper.toEntity(request);
        newUser.setStatus(UserStatus.INACTIVE);
        newUser.setCreatedAt(LocalDateTime.now());
        if (request.getUserRole().equals(UserRole.ADMIN.name())) {
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return userMapper.toResponse(authRepository.save(newUser));
    }

    @Override
    public boolean deleteUser(Long userId) {
        User user = authRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "User not found to delete"));
        boolean deleted;
        try {
            authRepository.delete(user);
            deleted = true;
        } catch (Exception e) {
            deleted = false;
        }
        return deleted;
    }


    @Transactional
    @Override
    public Boolean activateUser(Long userId) {
        User user = authRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found for " + userId));
        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new BadRequestException("User already in active state");
        } else {
            user.setStatus(UserStatus.ACTIVE);
            user.setActivatedAt(LocalDateTime.now());
        }
        return true;
    }

    @Transactional
    @Override
    public Boolean blockUser(Long userId) {
        User user = authRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found for " + userId));
        if (user.getStatus() == UserStatus.BLOCK) {
            throw new BadRequestException("User already in blocked state");
        } else {
            user.setStatus(UserStatus.BLOCK);
        }
        return true;
    }

    @Transactional
    @Override
    public LoginResponse adminLogin(AdminLoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            throw new BadRequestException("Invalid email or password");
        }
        User user = authRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));

        log.info("User found with email {}: password {}", user.getEmail(), user.getPassword());


        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new InvalidUserException(HttpStatus.BAD_REQUEST, "User not ACTIVE");
        }

        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new InvalidUserException(HttpStatus.BAD_REQUEST, "User not ACTIVE");
        }

        if (user.getStatus() == UserStatus.BLOCK) {
            throw new InvalidUserException(HttpStatus.BAD_REQUEST, "User not ACTIVE");
        }

        log.info("User found with email {}: password {}", user.getEmail(), user.getPassword());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Invalid user, Password didn't match");
        }

        String accessToken = jwtAuthService.createToken(user);
        String refreshToken = jwtAuthService.createRefreshToken(user);

        user.setLastLoginAt(LocalDateTime.now());
        return new LoginResponse(user.getId(), accessToken, refreshToken);
    }

    @Override
    public List<UserDTO> getUserLists(UserRole userRole) {
        List<User> users = authRepository.findByUserRole(userRole).orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toUserDTO(users);
    }
}