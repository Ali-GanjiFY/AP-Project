package org.example.frontend.auth;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        System.out.println("دکمه ورود زده شد! نام کاربری: " + usernameField.getText());
        // اینجا بعداً به سرویس وصل می‌شویم
    }

    @FXML
    private void navigateToRegister() {
        System.out.println("هدایت به صفحه ثبت‌نام...");
    }
}

