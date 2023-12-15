package com.example.skillnest.models.dtos;

import jakarta.validation.constraints.NotNull;

public class ChangePasswordDto {
    @NotNull(message = "Password can't be empty")
    private String oldPassword;

    @NotNull(message = "Password can't be empty")
    private String newPassword;

    @NotNull(message = "Password can't be empty")
    private String confirmPassword;

    public ChangePasswordDto() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
