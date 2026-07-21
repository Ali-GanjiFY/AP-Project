package org.example.frontend.dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.example.frontend.admin.AdminReviewService;
import org.example.frontend.advertisement.Advertisement;
import org.example.frontend.advertisement.AdvertisementDetail;
import org.example.frontend.advertisement.AdvertisementService;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Represents review ads controller.
 */
public class ReviewAdsController implements javafx.fxml.Initializable {

    private static final double CARD_WIDTH = 240;
    private static final String SERVER_BASE_URL = "http://localhost:8080";
    private static final double THUMB_SIZE = 60;

    @FXML
    private StackPane contentArea;

    @FXML
    private FlowPane adsListContainer;

    @FXML
    private Label statusLabel;

    private final AdminReviewService adminReviewService = new AdminReviewService();
    private final AdvertisementService advertisementService = new AdvertisementService();

    /**
     * Initialize.
     * @param location the location
     * @param resources the resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPendingAdvertisements();
    }

    /**
     * Loads pending advertisements.
     */
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

    /**
     * Builds review card.
     * @param ad the ad
     * @return the result
     */
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

        // image gallery
        FlowPane imagesRow = new FlowPane(6, 6);
        imagesRow.setPrefWrapLength(CARD_WIDTH - 24);
        loadImagesForCard(ad.getId(), imagesRow);

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

        // VIEW DETAILS
        Button viewDetailsBtn = new Button("مشاهده جزئیات");
        String viewDetailsStyle = "-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-background-radius: 6; -fx-cursor: hand;";
        viewDetailsBtn.setStyle(viewDetailsStyle);
        viewDetailsBtn.setMaxWidth(Double.MAX_VALUE);
        viewDetailsBtn.setOnAction(e -> {
            AdReviewDetailController.setSelectedAdvertisementId(ad.getId());
            NavigationService.switchScene(
                    "/fxml/dashboard/ad-review-detail-view.fxml",
                    "جزئیات آگهی (بررسی ادمین)"
            );
        });

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

        card.getChildren().addAll(statusChip, titleLabel, priceLabel, metaLabel, imagesRow,
                viewDetailsBtn, noteField, actionsRow);
        return card;
    }

    /**
     * Loads images for card.
     * @param adId the ad id
     * @param imagesRow the images row
     */
    private void loadImagesForCard(Long adId, FlowPane imagesRow) {
        new Thread(() -> {
            String token = UserSession.getInstance().getToken();
            AdvertisementDetail detail = advertisementService.getAdvertisementDetail(adId, token);

            if (detail == null) {
                System.err.println("[ReviewAds] آگهی " + adId + ": دریافت جزئیات ناموفق (detail=null)");
            } else if (detail.getImages() == null || detail.getImages().isEmpty()) {
                System.err.println("[ReviewAds] آگهی " + adId + ": هیچ عکسی در پاسخ سرور نیست (images خالی/null است)");
            } else {
                System.out.println("[ReviewAds] آگهی " + adId + ": " + detail.getImages().size() + " عکس دریافت شد");
                for (AdvertisementDetail.ImageInfo imageInfo : detail.getImages()) {
                    System.out.println("    -> imagePath = [" + imageInfo.getImagePath() + "]");
                }
            }

            Platform.runLater(() -> {
                if (detail == null || detail.getImages() == null || detail.getImages().isEmpty()) {
                    return;
                }

                for (AdvertisementDetail.ImageInfo imageInfo : detail.getImages()) {
                    String path = imageInfo.getImagePath();
                    if (path == null || path.isBlank()) {
                        continue;
                    }
                    String imageUrl = path.startsWith("http") ? path : SERVER_BASE_URL + path;

                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(THUMB_SIZE);
                    imageView.setFitHeight(THUMB_SIZE);
                    imageView.setPreserveRatio(false);
                    Image img = new Image(imageUrl, THUMB_SIZE, THUMB_SIZE, false, true, true);
                    img.errorProperty().addListener((obs, wasError, isError) -> {
                        if (isError) {
                            System.err.println("[ReviewAds] آگهی " + adId + ": خطا در لود عکس از " + imageUrl);
                        }
                    });
                    imageView.setImage(img);

                    imagesRow.getChildren().add(imageView);
                }
            });
        }).start();
    }

    /**
     * Submit decision.
     * @param ad the ad
     * @param decision the decision
     * @param note the note
     * @param approveBtn the approve btn
     * @param rejectBtn the reject btn
     */
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

    /**
     * Handles refresh.
     */
    @FXML
    private void handleRefresh() {
        loadPendingAdvertisements();
    }

    /**
     * Handles back to dashboard.
     */
    @FXML
    private void handleBackToDashboard() {
        NavigationService.switchScene("/fxml/dashboard/admin-dashboard-view.fxml", "پنل مدیریت (ادمین)");
    }

    /**
     * Handles manage users.
     */
    @FXML
    private void handleManageUsers() {
        NavigationService.switchScene("/fxml/dashboard/user-management-view.fxml", "مدیریت کاربران");
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