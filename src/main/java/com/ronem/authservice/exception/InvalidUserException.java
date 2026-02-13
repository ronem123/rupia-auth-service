/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:13/02/2026
 * Time:23:24
 */


package com.ronem.authservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidUserException extends RuntimeException {
    final HttpStatus status;

    public InvalidUserException(HttpStatus status, String errorMessage) {
        super(errorMessage);
        this.status = status;
    }
}