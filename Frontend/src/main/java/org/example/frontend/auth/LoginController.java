package org.example.frontend.auth;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Platform;
import org.example.frontend.shared.NavigationService;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // بررسی اعتبارسنجی اولیه در سمت فرانت
        if (username.isEmpty() || password.isEmpty()) {
            showError("لطفاً نام کاربری و رمز عبور را وارد کنید.");
            return;
        }

        // غیرفعال کردن موقت دکمه ورود برای جلوگیری از کلیک‌های مکرر (در صورت تمایل)
        errorLabel.setVisible(false);

        // ارسال درخواست در یک Thread جداگانه تا UI فریز نشود
        new Thread(() -> {
            String result = authService.login(username, password);

            // اعمال تغییرات روی UI باید در Thread اصلی JavaFX انجام شود
            Platform.runLater(() -> {
                if ("SUCCESS".equals(result)) {
                    showSuccess("ورود موفقیت‌آمیز بود! در حال انتقال...");
                    // TODO: در مرحله بعد جابجایی به صفحه اصلی (Dashboard) را اینجا می‌نویسیم
                } else {
                    // ترجمه خطاها به فارسی
                    showError(translateErrorMessage(result));
                }
            });
        }).start();
    }

    @FXML
    private void navigateToRegister() {
        NavigationService.switchScene("/fxml/auth/register-view.fxml", "ثبت‌نام در سامانه");
    }

    /**
     * متد کمکی برای ترجمه خطاهای دریافتی از AuthService به فارسی
     */
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

        // بازگرداندن خطای اصلی در صورتی که ترجمه‌ای پیدا نشد
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
