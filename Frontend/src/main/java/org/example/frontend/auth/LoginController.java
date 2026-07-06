package org.example.frontend.auth;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Platform;

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
                    showSuccess("ورود موفقیت‌آمیز بود! توکن دریافت شد.");
                    // TODO: در مرحله بعد جابجایی به صفحه اصلی (Dashboard) را اینجا می‌نویسیم
                } else {
                    showError(result);
                }
            });
        }).start();
    }

    @FXML
    private void navigateToRegister() {
        System.out.println("هدایت به صفحه ثبت‌نام...");
        // بعداً صفحه ثبت نام را اینجا باز می‌کنیم
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


