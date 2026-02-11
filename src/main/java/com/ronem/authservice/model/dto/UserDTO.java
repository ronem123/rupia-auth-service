/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:08/02/2026
 * Time:21:47
 */


package com.ronem.authservice.model.dto;

import java.time.LocalDateTime;

public record UserDTO(
        String id,
        String email,
        String mobileNumber,
        String userRole,
        String status,
        LocalDateTime createdAt,
        LocalDateTime lastLoginAt,
        LocalDateTime activatedAt
) {
}