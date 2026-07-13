package org.example.frontend.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
        try {
            System.out.println("[AdminDashboard] Loading User Management View...");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/fxml/dashboard/user-management-view.fxml")
            );
            javafx.scene.Parent view = loader.load();

            // پاکسازی محتوای قبلی و نمایش بخش جدید
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);

            System.out.println("[AdminDashboard] User Management View Loaded Successfully!");
        } catch (Exception e) {
            System.err.println("!!! خطا در لود صفحه مدیریت کاربران !!!");
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطا");
            alert.setHeaderText("بارگذاری صفحه با خطا مواجه شد.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
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
