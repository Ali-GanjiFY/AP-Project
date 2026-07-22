package org.example.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;



/**
 * Represents register request.
 */
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Pattern(regexp = "\\S+", message = "Password must not contain whitespace")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^09\\d{9}$", message = "Phone number must be a valid Iranian mobile number (e.g. 09123456789)")
    private String phone;

    @Email(message = "Email format is invalid")
    private String email;

    /**
     * Constructs a new RegisterRequest.
     */
    public RegisterRequest() {
    }

    /**
     * Constructs a new RegisterRequest.
     * @param fullName the full name
     * @param username the username
     * @param password the password
     * @param phone the phone
     * @param email the email
     */
    public RegisterRequest(String fullName, String username, String password, String phone, String email) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }


    /**
     * Gets full name.
     * @return the result
     */
    public String getFullName() {
        return fullName;
    }
    /**
     * Sets full name.
     * @param fullName the full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets username.
     * @return the result
     */
    public String getUsername() {
        return username;
    }
    /**
     * Sets username.
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     * @return the result
     */
    public String getPassword() {
        return password;
    }
    /**
     * Sets password.
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets phone.
     * @return the result
     */
    public String getPhone() {
        return phone;
    }
    /**
     * Sets phone.
     * @param phone the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets email.
     * @return the result
     */
    public String getEmail() {
        return email;
    }
    /**
     * Sets email.
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}