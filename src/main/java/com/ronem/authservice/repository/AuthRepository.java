/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:10:23
 */


package com.ronem.authservice.repository;

import com.ronem.authservice.model.entity.User;
import com.ronem.authservice.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailOrMobileNumber(String email, String mobileNumber);

    Optional<List<User>> findByUserRole(UserRole userRole);
}