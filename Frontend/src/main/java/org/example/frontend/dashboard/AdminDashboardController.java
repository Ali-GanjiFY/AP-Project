package org.example.frontend.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

/**
 * Represents admin dashboard controller.
 */
public class AdminDashboardController {

    @FXML
    private StackPane contentArea;

    /**
     * Handles logout.
     */
    @FXML
    private void handleLogout() {
        UserSession.getInstance().cleanSession();
        NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری");
    }

    /**
     * Handles manage users.
     */
    @FXML
    private void handleManageUsers() {
        NavigationService.switchScene("/fxml/dashboard/user-management-view.fxml", "مدیریت کاربران");
    }

    /**
     * Handles review ads.
     */
    @FXML
    private void handleReviewAds() {
        NavigationService.switchScene("/fxml/dashboard/review-ads-view.fxml", "بررسی آگهی‌ها");
    }

    /**
     * Handles manage sections.
     */
    @FXML
    private void handleManageSections() {
        NavigationService.switchScene("/fxml/dashboard/manage-sections-view.fxml", "مدیریت بخش‌ها");
    }
}
