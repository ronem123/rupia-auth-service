/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:10:23
 */


package com.ronem.authservice.repository;

import com.ronem.authservice.model.entity.User;
import com.ronem.authservice.model.response.CreateUserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}