/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:12/02/2026
 * Time:22:32
 */


package com.ronem.authservice.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {
    private final HttpStatus status;

    public UserNotFoundException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}