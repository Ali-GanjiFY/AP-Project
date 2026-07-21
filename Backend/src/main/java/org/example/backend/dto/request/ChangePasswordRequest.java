package org.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents change password request.
 */
public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "New password must be at least 6 characters")
    private String newPassword;

    /**
     * Constructs a new ChangePasswordRequest.
     */
    public ChangePasswordRequest() {}

    /**
     * Gets old password.
     * @return the result
     */
    public String getOldPassword() { return oldPassword; }
    /**
     * Sets old password.
     * @param oldPassword the old password
     */
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    /**
     * Gets new password.
     * @return the result
     */
    public String getNewPassword() { return newPassword; }
    /**
     * Sets new password.
     * @param newPassword the new password
     */
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}