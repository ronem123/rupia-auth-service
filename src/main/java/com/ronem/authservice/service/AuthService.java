package com.ronem.authservice.service;

import com.ronem.authservice.model.request.CreateUserRequest;
import com.ronem.authservice.model.response.CreateUserResponse;

/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:10:08
 */

public interface AuthService {
    CreateUserResponse createNewUser(CreateUserRequest request);
    Boolean activateUser(Long userId);
}
