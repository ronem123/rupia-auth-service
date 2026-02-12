package com.ronem.authservice.service;

import com.ronem.authservice.model.enums.UserRole;
import com.ronem.authservice.model.request.CreateUserRequest;
import com.ronem.authservice.model.response.CreateUserResponse;
import com.ronem.authservice.model.dto.UserDTO;
import com.ronem.authservice.model.response.LoginResponse;

import java.util.List;

/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:10:08
 */

public interface AuthService {
    //method to create new customer
    CreateUserResponse createNewUser(CreateUserRequest request);

    //delete user
    boolean deleteUser(Long userId);

    //approve customer/admin: update status: ACTIVE
    Boolean activateUser(Long userId);

    //block customer/admin : update status : INACTIVE
    Boolean blockUser(Long userId);

    //method to login superadmin and admin user
    LoginResponse adminLogin(String email, String password);

    //method to return list of Users
    List<UserDTO> getUserLists(UserRole userRole);
}
