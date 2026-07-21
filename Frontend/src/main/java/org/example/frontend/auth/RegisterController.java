package org.example.frontend.auth;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Platform;
import org.example.frontend.shared.NavigationService;

public class RegisterController {

    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();


        if (fullName.isEmpty() || username.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            showError("لطفاً تمامی فیلدهای اجباری را پر کنید.");
            return;
        }

        if (password.length() < 6) {
            showError("رمز عبور باید حداقل ۶ کاراکتر باشد.");
            return;
        }

        if (!password.matches("\\S+")) {
            showError("رمز عبور نباید شامل فاصله باشد.");
            return;
        }

        if (!phone.matches("^09\\d{9}$")) {
            showError("شماره تلفن معتبر نیست. فرمت صحیح: 09123456789");
            return;
        }

        errorLabel.setVisible(false);

        // Send to backend in difference Thread
        new Thread(() -> {
            String result = authService.register(fullName, username, password, phone, email);

            Platform.runLater(() -> {
                if ("SUCCESS".equals(result)) {
                    showSuccess("ثبت‌نام با موفقیت انجام شد! در حال انتقال...");

                    NavigationService.switchScene("/fxml/dashboard/dashboard-view.fxml", "داشبورد اصلی");
                } else {
                    showError(translateErrorMessage(result));
                }
            });

        }).start();
    }

    @FXML
    private void navigateToLogin() {
        NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری");
    }


    private String translateErrorMessage(String rawError) {
        if (rawError == null) return "خطای نامشخصی رخ داده است.";

        String cleanError = rawError.trim().toLowerCase();

        if (cleanError.contains("username already exists") || cleanError.contains("username is taken")) {
            return "این نام کاربری قبلاً ثبت شده است.";
        }
        if (cleanError.contains("email already exists") || cleanError.contains("email is taken")) {
            return "این آدرس ایمیل قبلاً ثبت شده است.";
        }
        if (cleanError.contains("phone already exists") || cleanError.contains("phone is taken")) {
            return "این شماره تلفن قبلاً ثبت شده است.";
        }
        if (cleanError.contains("valid iranian mobile number") || cleanError.contains("phone number must be")) {
            return "شماره تلفن معتبر نیست. فرمت صحیح: 09123456789";
        }
        if (cleanError.contains("connection refused") || cleanError.contains("network") || cleanError.contains("timeout")) {
            return "برقراری ارتباط با سرور برقرار نشد. اتصال اینترنت خود را بررسی کنید.";
        }

        return rawError;
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.getStyleClass().removeAll("success-text");
        errorLabel.getStyleClass().add("error-text");
        errorLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.getStyleClass().removeAll("error-text");
        errorLabel.getStyleClass().add("success-text");
        errorLabel.setVisible(true);
    }
}