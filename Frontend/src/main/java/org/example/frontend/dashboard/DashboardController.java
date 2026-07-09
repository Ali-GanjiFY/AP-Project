package org.example.frontend.dashboard;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.frontend.advertisement.Advertisement;
import org.example.frontend.advertisement.AdvertisementService;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements javafx.fxml.Initializable {

    private static final double CARD_WIDTH = 220;
    private static final double CARD_HEIGHT = 260;
    private static final double IMAGE_AREA_HEIGHT = 140;

    @FXML
    private StackPane contentArea;

    @FXML
    private FlowPane adsListContainer;

    @FXML
    private Label statusLabel;

    private final AdvertisementService advertisementService = new AdvertisementService();

    private Timeline autoRefreshTimeline;
    private volatile boolean loading;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAdvertisements();
        startAutoRefresh();
    }

    private void startAutoRefresh() {
        autoRefreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(10), event -> {
                    if (!loading) {
                        loadAdvertisements();
                    }
                })
        );
        autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        autoRefreshTimeline.play();
    }

    private void loadAdvertisements() {
        loading = true;
        statusLabel.setText("در حال بارگذاری آگهی‌ها...");

        new Thread(() -> {
            List<Advertisement> ads;
            try {
                ads = advertisementService.getAllActiveAds();
                if (ads == null) {
                    ads = Collections.emptyList();
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("خطا در دریافت آگهی‌ها.");
                    adsListContainer.getChildren().clear();
                    loading = false;
                });
                return;
            }

            List<Advertisement> finalAds = ads;
            Platform.runLater(() -> {
                adsListContainer.getChildren().clear();

                if (finalAds.isEmpty()) {
                    statusLabel.setText("در حال حاضر هیچ آگهی فعالی برای نمایش وجود ندارد.");
                    loading = false;
                    return;
                }

                statusLabel.setText("تعداد " + finalAds.size() + " آگهی یافت شد.");

                for (Advertisement ad : finalAds) {
                    adsListContainer.getChildren().add(buildAdCard(ad));
                }

                loading = false;
            });
        }).start();
    }

    private VBox buildAdCard(Advertisement ad) {
        VBox card = new VBox();
        card.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        card.setMaxSize(CARD_WIDTH, CARD_HEIGHT);
        card.setMinSize(CARD_WIDTH, CARD_HEIGHT);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #e2e8f0;" +
                        "-fx-border-radius: 10;" +
                        "-fx-cursor: hand;"
        );

        StackPane imageArea = new StackPane();
        imageArea.setPrefSize(CARD_WIDTH, IMAGE_AREA_HEIGHT);
        imageArea.setMinHeight(IMAGE_AREA_HEIGHT);
        imageArea.setMaxHeight(IMAGE_AREA_HEIGHT);
        imageArea.setStyle(
                "-fx-background-color: #f1f5f9;" +
                        "-fx-background-radius: 10 10 0 0;"
        );

        boolean hasImage = ad.getMainImagePath() != null
                && !ad.getMainImagePath().isBlank()
                && !ad.getMainImagePath().equalsIgnoreCase("string");

        if (hasImage) {
            Label imgPlaceholder = new Label("🖼");
            imgPlaceholder.setStyle("-fx-font-size: 32px;");
            imageArea.getChildren().add(imgPlaceholder);
        } else {
            Label noImageLabel = new Label("بدون تصویر");
            noImageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
            imageArea.getChildren().add(noImageLabel);
        }

        VBox infoArea = new VBox(4);
        infoArea.setPadding(new Insets(10));
        infoArea.setAlignment(Pos.TOP_RIGHT);

        Label titleLabel = new Label(ad.getTitle() != null && !ad.getTitle().isBlank() ? ad.getTitle() : "بدون عنوان");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(CARD_WIDTH - 20);

        String priceText = ad.getPrice() != null
                ? String.format("%,.0f تومان", ad.getPrice())
                : "توافقی";
        Label priceLabel = new Label(priceText);
        priceLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #16a34a; -fx-font-weight: bold;");

        String metaText = String.format("%s | %s",
                ad.getCategoryName() != null && !ad.getCategoryName().isBlank() ? ad.getCategoryName() : "-",
                ad.getCityName() != null && !ad.getCityName().isBlank() ? ad.getCityName() : "-");
        Label metaLabel = new Label(metaText);
        metaLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");

        infoArea.getChildren().addAll(titleLabel, priceLabel, metaLabel);
        card.getChildren().addAll(imageArea, infoArea);

        return card;
    }

    @FXML
    private void handleLogout() {
        stopAutoRefresh();
        UserSession.getInstance().cleanSession();
        NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری");
    }

    @FXML
    private void handleCreateAdvertisement() {
        stopAutoRefresh();
        NavigationService.switchScene("/fxml/advertisement/create-advertisement-view.fxml", "ثبت آگهی جدید");
    }

    private void stopAutoRefresh() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
    }
}
