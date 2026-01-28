/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:10:20
 */


package com.ronem.authservice.model.request;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    String email;
    String mobileNumber;
    String userRole;
}