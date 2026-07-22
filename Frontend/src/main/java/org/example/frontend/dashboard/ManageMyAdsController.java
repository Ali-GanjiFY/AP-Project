package org.example.frontend.dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.example.frontend.advertisement.Advertisement;
import org.example.frontend.advertisement.AdvertisementService;
import org.example.frontend.advertisement.RatingService;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Represents manage my ads controller.
 */
public class ManageMyAdsController implements javafx.fxml.Initializable {

    private static final double CARD_WIDTH = 280;

    @FXML
    private StackPane contentArea;

    @FXML
    private FlowPane adsListContainer;

    @FXML
    private Label statusLabel;

    @FXML
    private Label ratingsAverageLabel;

    @FXML
    private Label ratingsCountLabel;

    @FXML
    private Button toggleRatingsButton;

    @FXML
    private VBox ratingsCommentsContainer;

    @FXML
    private Label ratingsCommentsStatusLabel;

    @FXML
    private VBox ratingsCommentsList;

    private final AdvertisementService advertisementService = new AdvertisementService();
    private final RatingService ratingService = new RatingService();
    private boolean ratingsCommentsLoaded = false;

    /**
     * Initialize.
     * @param location the location
     * @param resources the resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMyAdvertisements();
        loadRatingsSummary();
    }

    /**
     * Loads ratings summary.
     */
    private void loadRatingsSummary() {
        String token = UserSession.getInstance().getToken();
        new Thread(() -> {
            RatingService.RatingSummaryDto summary = ratingService.getMyRatingSummary(token);
            Platform.runLater(() -> {
                if (summary == null) {
                    ratingsAverageLabel.setText("میانگین امتیاز: -");
                    ratingsCountLabel.setText("تعداد نظرات: -");
                    return;
                }
                ratingsAverageLabel.setText(String.format("میانگین امتیاز: %.1f از 5", summary.getAverageScore()));
                ratingsCountLabel.setText("تعداد نظرات: " + summary.getTotalCount());
            });
        }).start();
    }

    /**
     * Handles toggle ratings comments.
     */
    @FXML
    private void handleToggleRatingsComments() {
        boolean showing = ratingsCommentsContainer.isVisible();
        if (showing) {
            ratingsCommentsContainer.setVisible(false);
            ratingsCommentsContainer.setManaged(false);
            toggleRatingsButton.setText("نمایش نظرات");
            return;
        }

        ratingsCommentsContainer.setVisible(true);
        ratingsCommentsContainer.setManaged(true);
        toggleRatingsButton.setText("پنهان کردن نظرات");

        if (!ratingsCommentsLoaded) {
            loadRatingsComments();
        }
    }

    /**
     * Loads ratings comments.
     */
    private void loadRatingsComments() {
        ratingsCommentsStatusLabel.setText("در حال بارگذاری نظرات...");
        String token = UserSession.getInstance().getToken();

        new Thread(() -> {
            List<RatingService.RatingDto> ratings = ratingService.getMyReceivedRatings(token);
            Platform.runLater(() -> {
                ratingsCommentsLoaded = true;
                ratingsCommentsList.getChildren().clear();

                if (ratings.isEmpty()) {
                    ratingsCommentsStatusLabel.setText("هنوز هیچ نظری برای شما ثبت نشده است.");
                    return;
                }

                ratingsCommentsStatusLabel.setText("تعداد " + ratings.size() + " نظر ثبت‌شده:");
                for (RatingService.RatingDto rating : ratings) {
                    ratingsCommentsList.getChildren().add(buildRatingCard(rating));
                }
            });
        }).start();
    }

    /**
     * Builds rating card.
     * @param rating the rating
     * @return the result
     */
    private VBox buildRatingCard(RatingService.RatingDto rating) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #0f172a; -fx-background-radius: 8; -fx-border-color: #334155; -fx-border-radius: 8;");

        HBox headerRow = new HBox(10);
        headerRow.setAlignment(Pos.CENTER_RIGHT);

        Label scoreLabel = new Label("امتیاز: " + rating.getScore() + " / 5");
        scoreLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #fbbf24;");

        Label buyerLabel = new Label("از طرف: " + safeText(rating.getBuyerUsername(), "کاربر"));
        buyerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");

        headerRow.getChildren().addAll(scoreLabel, buyerLabel);

        card.getChildren().add(headerRow);

        String comment = rating.getComment();
        if (comment != null && !comment.isBlank()) {
            Label commentLabel = new Label(comment);
            commentLabel.setWrapText(true);
            commentLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #e2e8f0;");
            card.getChildren().add(commentLabel);
        }

        return card;
    }

    /**
     * Safe text.
     * @param value the value
     * @param defaultValue the default value
     * @return the result
     */
    private String safeText(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }

    /**
     * Loads my advertisements.
     */
    private void loadMyAdvertisements() {
        statusLabel.setText("در حال بارگذاری آگهی‌های شما...");
        String token = UserSession.getInstance().getToken();

        new Thread(() -> {
            List<Advertisement> ads = advertisementService.getMyAdvertisements(token);

            Platform.runLater(() -> {
                adsListContainer.getChildren().clear();

                if (ads.isEmpty()) {
                    statusLabel.setText("شما هنوز هیچ آگهی‌ای ثبت نکرده‌اید.");
                    return;
                }

                statusLabel.setText("تعداد " + ads.size() + " آگهی ثبت شده توسط شما.");

                for (Advertisement ad : ads) {
                    adsListContainer.getChildren().add(buildAdCard(ad));
                }
            });
        }).start();
    }

    /**
     * Builds ad card.
     * @param ad the ad
     * @return the result
     */
    private VBox buildAdCard(Advertisement ad) {

        String title = ad.getTitle() != null ? ad.getTitle() : "بدون عنوان";

        String priceText = "-";
        if (ad.getPrice() != null) {
            priceText = String.format("%,.0f تومان", ad.getPrice());
        }

        String category = ad.getCategoryName() != null ? ad.getCategoryName() : "-";
        String city = ad.getCityName() != null ? ad.getCityName() : "-";
        String metaText = category + " | " + city;

        String status = ad.getStatus() != null ? ad.getStatus() : "";

        Label statusChip = new Label(statusText(status));
        statusChip.setStyle(statusChipStyle(status));

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(CARD_WIDTH - 24);

        Label priceLabel = new Label(priceText);
        priceLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #16a34a; -fx-font-weight: bold;");

        Label metaLabel = new Label(metaText);
        metaLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");

        VBox card = new VBox(8);
        card.setPrefWidth(CARD_WIDTH);
        card.setMaxWidth(CARD_WIDTH);
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.TOP_RIGHT);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; "
                + "-fx-border-color: #e2e8f0; -fx-border-radius: 10;");

        card.getChildren().addAll(statusChip, titleLabel, priceLabel, metaLabel);

        // If the ad was rejected, show the admin's reason so the owner knows why.
        if ("REJECTED".equalsIgnoreCase(status) && ad.getRejectionReason() != null
                && !ad.getRejectionReason().isBlank()) {
            Label rejectionReasonLabel = new Label("دلیل رد: " + ad.getRejectionReason());
            rejectionReasonLabel.setWrapText(true);
            rejectionReasonLabel.setMaxWidth(CARD_WIDTH - 24);
            rejectionReasonLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #991b1b; "
                    + "-fx-background-color: #fee2e2; -fx-background-radius: 6; -fx-padding: 6 8;");
            card.getChildren().add(rejectionReasonLabel);
        }

        // Only ACTIVE ads can be marked as sold; only non-DELETED ads can be deleted;
        // SOLD/DELETED ads can no longer be edited.
        boolean canMarkSold = "ACTIVE".equalsIgnoreCase(status);
        boolean canDelete = !"DELETED".equalsIgnoreCase(status);
        boolean canEdit = !"SOLD".equalsIgnoreCase(status) && !"DELETED".equalsIgnoreCase(status);

        if (canMarkSold || canDelete || canEdit) {
            HBox actionsRow = new HBox(6);
            actionsRow.setAlignment(Pos.CENTER_RIGHT);

            // Base style shared by all action buttons: small font + tight padding so all three
            // fit on one row at CARD_WIDTH, and USE_PREF_SIZE min-width so JavaFX never shrinks
            // the button below its text's natural size (which is what was causing "…" truncation).
            String baseButtonStyle = "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11px; "
                    + "-fx-padding: 5 8; -fx-background-radius: 6; -fx-cursor: hand;";

            if (canEdit) {
                Button editBtn = new Button("ویرایش");
                editBtn.setStyle("-fx-background-color: #f59e0b; " + baseButtonStyle);
                editBtn.setMinWidth(Region.USE_PREF_SIZE);
                editBtn.setOnAction(e -> handleEdit(ad));
                actionsRow.getChildren().add(editBtn);
            }

            if (canMarkSold) {
                Button soldBtn = new Button("فروخته شد");
                soldBtn.setStyle("-fx-background-color: #2563eb; " + baseButtonStyle);
                soldBtn.setMinWidth(Region.USE_PREF_SIZE);
                soldBtn.setOnAction(e -> handleMarkAsSold(ad));
                actionsRow.getChildren().add(soldBtn);
            }

            if (canDelete) {
                Button deleteBtn = new Button("حذف");
                deleteBtn.setStyle("-fx-background-color: #ef4444; " + baseButtonStyle);
                deleteBtn.setMinWidth(Region.USE_PREF_SIZE);
                deleteBtn.setOnAction(e -> handleDelete(ad));
                actionsRow.getChildren().add(deleteBtn);
            }

            card.getChildren().add(actionsRow);
        }

        return card;
    }

    /**
     * Status text.
     * @param status the status
     * @return the result
     */
    private String statusText(String status) {
        return switch (status.toUpperCase()) {
            case "ACTIVE" -> "فعال";
            case "PENDING" -> "در انتظار بررسی";
            case "REJECTED" -> "رد شده";
            case "SOLD" -> "فروخته شده";
            case "DELETED" -> "حذف شده";
            default -> status;
        };
    }

    /**
     * Status chip style.
     * @param status the status
     * @return the result
     */
    private String statusChipStyle(String status) {
        String base = "-fx-font-size: 10px; -fx-background-radius: 20; -fx-padding: 3px 10px;";
        return switch (status.toUpperCase()) {
            case "ACTIVE" -> base + "-fx-text-fill: #166534; -fx-background-color: #dcfce7;";
            case "PENDING" -> base + "-fx-text-fill: #92400e; -fx-background-color: #fef3c7;";
            case "REJECTED" -> base + "-fx-text-fill: #991b1b; -fx-background-color: #fee2e2;";
            case "SOLD" -> base + "-fx-text-fill: #1e40af; -fx-background-color: #dbeafe;";
            case "DELETED" -> base + "-fx-text-fill: #475569; -fx-background-color: #e2e8f0;";
            default -> base + "-fx-text-fill: #334155; -fx-background-color: #f1f5f9;";
        };
    }

    /**
     * Handles mark as sold.
     * @param ad the ad
     */
    private void handleMarkAsSold(Advertisement ad) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "آیا از تغییر وضعیت این آگهی به «فروخته شده» مطمئن هستید؟", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("تغییر وضعیت آگهی");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.YES) {
            return;
        }

        String token = UserSession.getInstance().getToken();
        new Thread(() -> {
            String outcome = advertisementService.markAsSold(token, ad.getId());
            Platform.runLater(() -> {
                if ("SUCCESS".equals(outcome)) {
                    loadMyAdvertisements();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, outcome, ButtonType.OK);
                    alert.setHeaderText("خطا در تغییر وضعیت آگهی");
                    alert.showAndWait();
                }
            });
        }).start();
    }

    /**
     * Handles edit.
     * @param ad the ad
     */
    private void handleEdit(Advertisement ad) {
        String token = UserSession.getInstance().getToken();
        statusLabel.setText("در حال بارگذاری اطلاعات آگهی...");

        new Thread(() -> {
            org.example.frontend.advertisement.AdvertisementDetail detail =
                    advertisementService.getAdvertisementDetail(ad.getId(), token);

            Platform.runLater(() -> {
                if (detail == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "دریافت اطلاعات آگهی برای ویرایش ناموفق بود.", ButtonType.OK);
                    alert.setHeaderText("خطا در بارگذاری آگهی");
                    alert.showAndWait();
                    loadMyAdvertisements();
                    return;
                }

                NavigationService.<org.example.frontend.advertisement.CreateAdvertisementController>switchScene(
                        "/fxml/advertisement/create-advertisement-view.fxml",
                        "ویرایش آگهی",
                        controller -> controller.setEditMode(detail)
                );
            });
        }).start();
    }

    /**
     * Handles delete.
     * @param ad the ad
     */
    private void handleDelete(Advertisement ad) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "آیا از حذف این آگهی مطمئن هستید؟", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("حذف آگهی");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.YES) {
            return;
        }

        String token = UserSession.getInstance().getToken();
        new Thread(() -> {
            String outcome = advertisementService.deleteAdvertisement(token, ad.getId());
            Platform.runLater(() -> {
                if ("SUCCESS".equals(outcome)) {
                    loadMyAdvertisements();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, outcome, ButtonType.OK);
                    alert.setHeaderText("خطا در حذف آگهی");
                    alert.showAndWait();
                }
            });
        }).start();
    }

    /**
     * Handles refresh.
     */
    @FXML
    private void handleRefresh() {
        loadMyAdvertisements();
        loadRatingsSummary();
        ratingsCommentsLoaded = false;
        if (ratingsCommentsContainer.isVisible()) {
            loadRatingsComments();
        }
    }

    /**
     * Handles back to dashboard.
     */
    @FXML
    private void handleBackToDashboard() {
        NavigationService.switchScene("/fxml/dashboard/dashboard-view.fxml", "داشبورد کاربر");
    }

    /**
     * Handles create advertisement.
     */
    @FXML
    private void handleCreateAdvertisement() {
        NavigationService.switchScene("/fxml/advertisement/create-advertisement-view.fxml", "ثبت آگهی جدید");
    }

    /**
     * Handles show favorites.
     */
    @FXML
    private void handleShowFavorites() {
        NavigationService.switchScene("/fxml/dashboard/my-favorites-view.fxml", "علاقه‌مندی‌های من");
    }

    /**
     * Handles open chats.
     */
    @FXML
    private void handleOpenChats() {
        NavigationService.switchScene("/fxml/chat/conversation-list-view.fxml", "چت‌ها");
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