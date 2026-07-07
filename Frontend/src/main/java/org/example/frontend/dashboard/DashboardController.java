package org.example.frontend.dashboard;

import javafx.fxml.FXML;
import org.example.frontend.shared.NavigationService;

public class DashboardController {

    @FXML
    private void handleLogout() {
        // اینجا می‌توانی متد پاک کردن سشن (UserSession) را هم صدا بزنی اگر ساختی
        NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری");
    }
}

