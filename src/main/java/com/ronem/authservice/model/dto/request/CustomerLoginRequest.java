/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:09/03/2026
 * Time:13:16
 */


package com.ronem.authservice.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CustomerLoginRequest {
    private String mobileNumber;
    private String loginOtp;
}