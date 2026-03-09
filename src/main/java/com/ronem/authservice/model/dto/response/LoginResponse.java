/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:10/02/2026
 * Time:22:02
 */


package com.ronem.authservice.model.dto.response;

public record LoginResponse(
        Long userId,
        String accessToken,
        String refreshToken
) {
}