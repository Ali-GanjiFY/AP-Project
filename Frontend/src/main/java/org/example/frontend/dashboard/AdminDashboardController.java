package org.example.frontend.dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

/**
 * Represents admin dashboard controller.
 */
public class AdminDashboardController {

    @FXML
    private Label usersCountLabel;

    @FXML
    private Label advertisementsCountLabel;

    @FXML
    private Label adminReviewsCountLabel;

    @FXML
    private Label pendingAdsCountLabel;

    @FXML
    private Label statusLabel;

    private final AdminDashboardService adminDashboardService = new AdminDashboardService();

    /**
     * Initialize.
     */
    @FXML
    public void initialize() {
        loadStats();
    }

    /**
     * Loads admin dashboard statistics.
     */
    private void loadStats() {
        setLoadingState();

        new Thread(() -> {
            try {
                AdminDashboardStats stats = adminDashboardService.getStats();

                Platform.runLater(() -> {
                    usersCountLabel.setText(String.valueOf(stats.getUsersCount()));
                    advertisementsCountLabel.setText(String.valueOf(stats.getAdvertisementsCount()));
                    adminReviewsCountLabel.setText(String.valueOf(stats.getAdminReviewsCount()));
                    pendingAdsCountLabel.setText(String.valueOf(stats.getPendingAdvertisementsCount()));
                    statusLabel.setText("آمار داشبورد با موفقیت بروزرسانی شد.");
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    resetStats();
                    statusLabel.setText("خطا در دریافت آمار داشبورد.");
                    showError("خطا", "امکان دریافت آمار داشبورد وجود ندارد:\n" + e.getMessage());
                });
            }
        }).start();
    }

    /**
     * Sets loading state.
     */
    private void setLoadingState() {
        usersCountLabel.setText("...");
        advertisementsCountLabel.setText("...");
        adminReviewsCountLabel.setText("...");
        pendingAdsCountLabel.setText("...");
        statusLabel.setText("در حال بارگذاری آمار داشبورد...");
    }

    /**
     * Resets stats.
     */
    private void resetStats() {
        usersCountLabel.setText("-");
        advertisementsCountLabel.setText("-");
        adminReviewsCountLabel.setText("-");
        pendingAdsCountLabel.setText("-");
    }

    /**
     * Shows error.
     * @param title the title
     * @param content the content
     */
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Handles refresh.
     */
    @FXML
    private void handleRefresh() {
        loadStats();
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

    /**
     * Handles logout.
     */
    @FXML
    private void handleLogout() {
        UserSession.getInstance().cleanSession();
        NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری");
    }
}

