/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:10:16
 */


package com.ronem.authservice.service;

import com.ronem.authservice.exception.BadRequestException;
import com.ronem.authservice.mapper.UserMapper;
import com.ronem.authservice.model.entity.User;
import com.ronem.authservice.model.enums.UserRole;
import com.ronem.authservice.model.enums.UserStatus;
import com.ronem.authservice.model.request.CreateUserRequest;
import com.ronem.authservice.model.response.CreateUserResponse;
import com.ronem.authservice.model.dto.UserDTO;
import com.ronem.authservice.model.response.LoginResponse;
import com.ronem.authservice.repository.AuthRepository;
import com.ronem.authservice.service.jwt.JwtAuthService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final JwtAuthService jwtAuthService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public CreateUserResponse createNewUser(CreateUserRequest request) {
        log.info("Auth Service UserRequest body : {}", request);
        User newUser = userMapper.toEntity(request);
        newUser.setStatus(UserStatus.INACTIVE);
        newUser.setCreatedAt(LocalDateTime.now());
        if (request.getUserRole().equals(UserRole.ADMIN.name())) {
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return userMapper.toResponse(authRepository.save(newUser));
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
    public LoginResponse adminLogin(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            throw new BadRequestException("Invalid email or password");
        }
        User user = authRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));

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