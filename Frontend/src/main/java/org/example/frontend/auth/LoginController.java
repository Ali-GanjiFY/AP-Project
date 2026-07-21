package org.example.frontend.auth;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Platform;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("لطفاً نام کاربری و رمز عبور را وارد کنید.");
            return;
        }

        errorLabel.setVisible(false);

        new Thread(() -> {
            String result = authService.login(username, password);

            Platform.runLater(() -> {
                if ("SUCCESS".equals(result)) {
                    showSuccess("ورود موفقیت‌آمیز بود! در حال انتقال...");

                    Platform.runLater(() -> {
                        try {
                            String role = UserSession.getInstance().getRole();

                            if (role != null && role.equalsIgnoreCase("ADMIN")) {
                                NavigationService.switchScene("/fxml/dashboard/admin-dashboard-view.fxml", "پنل مدیریت");
                            } else {
                                NavigationService.switchScene("/fxml/dashboard/dashboard-view.fxml", "داشبورد اصلی");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showError("خطا در بارگذاری صفحه داشبورد: " + e.getMessage());
                        }
                    });
                } else {
                    showError(translateErrorMessage(result));
                }
            });

        }).start();
    }

    @FXML
    private void navigateToRegister() {
        NavigationService.switchScene("/fxml/auth/register-view.fxml", "ثبت‌نام در سامانه");
    }

    private String translateErrorMessage(String rawError) {
        if (rawError == null) return "خطای نامشخصی رخ داده است.";

        String cleanError = rawError.trim().toLowerCase();

        if (cleanError.contains("invalid username or password") || cleanError.contains("bad credentials") || cleanError.contains("unauthorized")) {
            return "نام کاربری یا رمز عبور اشتباه است.";
        }
        if (cleanError.contains("user not found")) {
            return "کاربری با این مشخصات یافت نشد.";
        }
        if (cleanError.contains("connection refused") || cleanError.contains("network") || cleanError.contains("timeout")) {
            return "برقراری ارتباط با سرور برقرار نشد. لطفاً اتصال اینترنت خود را بررسی کنید.";
        }
        if (cleanError.toLowerCase().contains("blocked")) {
            return "حساب کاربری شما مسدود شده است.";
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