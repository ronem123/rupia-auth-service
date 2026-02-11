/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:10:20
 */


package com.ronem.authservice.model.request;

import com.ronem.authservice.validation.AdminValidation;
import com.ronem.authservice.validation.CustomerValidation;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    @NotBlank(message = "Email is required", groups = {AdminValidation.class, CustomerValidation.class})
    String email;

    @NotBlank(message = "Mobile Number is required", groups = CustomerValidation.class)
    String mobileNumber;

    @NotBlank(message = "Role is required", groups = {AdminValidation.class, CustomerValidation.class})
    String userRole;

    //for admin only
    @NotBlank(message = "Password is required", groups = AdminValidation.class)
    String password;
}