package org.example.backend.dto.request;

/**
 * Represents update profile request.
 */
public class UpdateProfileRequest {

    private String fullName;
    private String email;
    private String phone;

    /**
     * Constructs a new UpdateProfileRequest.
     */
    public UpdateProfileRequest() {}

    /**
     * Gets full name.
     * @return the result
     */
    public String getFullName() { return fullName; }
    /**
     * Sets full name.
     * @param fullName the full name
     */
    public void setFullName(String fullName) { this.fullName = fullName; }

    /**
     * Gets email.
     * @return the result
     */
    public String getEmail() { return email; }
    /**
     * Sets email.
     * @param email the email
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Gets phone.
     * @return the result
     */
    public String getPhone() { return phone; }
    /**
     * Sets phone.
     * @param phone the phone
     */
    public void setPhone(String phone) { this.phone = phone; }
}