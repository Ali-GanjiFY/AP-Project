
package org.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequest {

    @NotBlank(message = "رمز عبور فعلی الزامی است")
    private String oldPassword;

    @NotBlank(message = "رمز عبور جدید الزامی است")
    @Size(min = 6, message = "رمز عبور جدید باید حداقل ۶ کاراکتر باشد")
    private String newPassword;

    // Constructor
    public ChangePasswordRequest() {}

    // Getters and Setters
    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}