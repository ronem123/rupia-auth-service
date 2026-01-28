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
import com.ronem.authservice.model.enums.UserStatus;
import com.ronem.authservice.model.request.CreateUserRequest;
import com.ronem.authservice.model.response.CreateUserResponse;
import com.ronem.authservice.repository.AuthRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final UserMapper userMapper;

    @Override
    public CreateUserResponse createNewUser(CreateUserRequest request) {
        User newUser = userMapper.toEntity(request);
        newUser.setStatus(UserStatus.INACTIVE);
        newUser.setCreatedAt(LocalDateTime.now());
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
        }
        return true;
    }
}