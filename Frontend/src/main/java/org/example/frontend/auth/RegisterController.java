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
        String password = passwordField.getText(); // پسورد بدون trim

        // اعتبارسنجی‌های پایه سمت کلاینت
        if (fullName.isEmpty() || username.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            showError("لطفاً تمامی فیلدهای اجباری را پر کنید.");
            return;
        }

        if (password.length() < 6) {
            showError("رمز عبور باید حداقل ۶ کاراکتر باشد.");
            return;
        }

        errorLabel.setVisible(false);

        // ارسال به بک‌اند در Thread جداگانه
        new Thread(() -> {
            String result = authService.register(fullName, username, password, phone, email);

            Platform.runLater(() -> {
                if ("SUCCESS".equals(result)) {
                    showSuccess("ثبت‌نام با موفقیت انجام شد! در حال انتقال...");
                    // TODO: در بخش بعد، انتقال مستقیم به داشبورد یا صفحه لاگین را می‌نویسیم
                } else {
                    showError(result);
                }
            });
        }).start();
    }

    @FXML
    private void navigateToLogin() {
        NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری");
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

