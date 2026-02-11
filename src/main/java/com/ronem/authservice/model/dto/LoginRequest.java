/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:08/02/2026
 * Time:21:54
 */


package com.ronem.authservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is Required")
    String email;
    @NotBlank(message = "Password is Required")
    String password;
}