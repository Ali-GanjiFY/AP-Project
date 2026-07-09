package org.example.frontend.dashboard;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

public class AdminDashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private void handleLogout() {
        UserSession.getInstance().cleanSession();
        NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری");
    }

    @FXML
    private void handleManageUsers() {
        // TODO: بارگذاری صفحه مدیریت کاربران در contentArea
    }

    @FXML
    private void handleReviewAds() {
        NavigationService.switchScene("/fxml/dashboard/review-ads-view.fxml", "بررسی آگهی‌ها");
    }

    @FXML
    private void handleManageSections() {
        NavigationService.switchScene("/fxml/dashboard/manage-sections-view.fxml", "مدیریت بخش‌ها");
    }
}