package org.example.frontend.dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.example.frontend.admin.AdminReviewService;
import org.example.frontend.advertisement.Advertisement;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReviewAdsController implements javafx.fxml.Initializable {

    private static final double CARD_WIDTH = 240;

    @FXML
    private StackPane contentArea;

    @FXML
    private FlowPane adsListContainer;

    @FXML
    private Label statusLabel;

    private final AdminReviewService adminReviewService = new AdminReviewService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPendingAdvertisements();
    }

    private void loadPendingAdvertisements() {
        statusLabel.setText("در حال بارگذاری آگهی‌ها...");
        String token = UserSession.getInstance().getToken();

        new Thread(() -> {
            List<Advertisement> ads = adminReviewService.getPendingAdvertisements(token);

            Platform.runLater(() -> {
                adsListContainer.getChildren().clear();

                if (ads.isEmpty()) {
                    statusLabel.setText("در حال حاضر آگهی‌ای در انتظار بررسی وجود ندارد.");
                    return;
                }

                statusLabel.setText("تعداد " + ads.size() + " آگهی در انتظار بررسی است.");

                for (Advertisement ad : ads) {
                    VBox card = buildReviewCard(ad);
                    adsListContainer.getChildren().add(card);
                }
            });
        }).start();
    }

    private VBox buildReviewCard(Advertisement ad) {

        String title = "بدون عنوان";
        if (ad.getTitle() != null) {
            title = ad.getTitle();
        }

        String priceText = "-";
        if (ad.getPrice() != null) {
            priceText = String.format("%,.0f تومان", ad.getPrice());
        }

        String category = "-";
        if (ad.getCategoryName() != null) {
            category = ad.getCategoryName();
        }

        String city = "-";
        if (ad.getCityName() != null) {
            city = ad.getCityName();
        }
        String metaText = category + " | " + city;

        Label statusChip = new Label("در انتظار بررسی");
        String chipStyle = "-fx-font-size: 10px; -fx-text-fill: #92400e; -fx-background-color: #fef3c7; "
                + "-fx-background-radius: 20; -fx-padding: 3px 10px;";
        statusChip.setStyle(chipStyle);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(CARD_WIDTH - 24);

        Label priceLabel = new Label(priceText);
        priceLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #16a34a; -fx-font-weight: bold;");

        Label metaLabel = new Label(metaText);
        metaLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");

        // note
        TextField noteField = new TextField();
        noteField.setPromptText("یادداشت (اختیاری)");
        noteField.setStyle("-fx-font-size: 11px;");

        // APPROVE
        Button approveBtn = new Button("تایید");
        String approveStyle = "-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-background-radius: 6; -fx-cursor: hand;";
        approveBtn.setStyle(approveStyle);

        // REJECT
        Button rejectBtn = new Button("رد");
        String rejectStyle = "-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-background-radius: 6; -fx-cursor: hand;";
        rejectBtn.setStyle(rejectStyle);

        approveBtn.setOnAction(e -> submitDecision(ad, "APPROVED", noteField.getText(), approveBtn, rejectBtn));
        rejectBtn.setOnAction(e -> submitDecision(ad, "REJECTED", noteField.getText(), approveBtn, rejectBtn));

        HBox actionsRow = new HBox(8, approveBtn, rejectBtn);
        actionsRow.setAlignment(Pos.CENTER_RIGHT);

        VBox card = new VBox(8);
        card.setPrefWidth(CARD_WIDTH);
        card.setMaxWidth(CARD_WIDTH);
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.TOP_RIGHT);
        String cardStyle = "-fx-background-color: white; -fx-background-radius: 10; "
                + "-fx-border-color: #e2e8f0; -fx-border-radius: 10;";
        card.setStyle(cardStyle);

        card.getChildren().addAll(statusChip, titleLabel, priceLabel, metaLabel, noteField, actionsRow);
        return card;
    }

    private void submitDecision(Advertisement ad, String decision, String note, Button approveBtn, Button rejectBtn) {
        approveBtn.setDisable(true);
        rejectBtn.setDisable(true);
        String token = UserSession.getInstance().getToken();

        new Thread(() -> {
            String result = adminReviewService.reviewAdvertisement(token, ad.getId(), decision, note);

            Platform.runLater(() -> {
                if (result.equals("SUCCESS")) {
                    loadPendingAdvertisements();
                } else {
                    approveBtn.setDisable(false);
                    rejectBtn.setDisable(false);

                    Alert alert = new Alert(Alert.AlertType.ERROR, result, javafx.scene.control.ButtonType.OK);
                    alert.setHeaderText("خطا در ثبت تصمیم");
                    alert.showAndWait();
                }
            });
        }).start();
    }

    @FXML
    private void handleRefresh() {
        loadPendingAdvertisements();
    }

    @FXML
    private void handleBackToDashboard() {
        NavigationService.switchScene("/fxml/dashboard/admin-dashboard-view.fxml", "پنل مدیریت (ادمین)");
    }

    @FXML
    private void handleManageUsers() {
        // TODO: بارگذاری صفحه مدیریت کاربران
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().cleanSession();
        NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری");
    }
}