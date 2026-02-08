/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:08/02/2026
 * Time:21:54
 */


package com.ronem.authservice.model.dto;

import lombok.Data;

@Data
public class LoginRequest {
    String email;
    String mobileNumber;
    String password;
}