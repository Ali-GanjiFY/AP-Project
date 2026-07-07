package org.example.frontend.dashboard;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private void handleLogout() {
        UserSession.getInstance().cleanSession();
        NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری");
    }
}
