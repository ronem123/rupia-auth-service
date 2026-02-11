/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:11/02/2026
 * Time:11:59
 */


package com.ronem.authservice.validation;

/**
 * Admin validation: this is group validation
 * as we are using same CreateUserRequest class for
 * Customer creation and Admin creation
 * Since Customer needs only Mobile Number and Email and not password
 * Admin needs only email and password and optionally mobileNumber
 * So, we need to do group validation with Jakarta
 */
public interface AdminValidation {
}