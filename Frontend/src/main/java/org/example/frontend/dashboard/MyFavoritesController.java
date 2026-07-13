package org.example.frontend.dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.example.frontend.advertisement.Advertisement;
import org.example.frontend.advertisement.AdvertisementDetail;
import org.example.frontend.advertisement.AdvertisementService;
import org.example.frontend.advertisement.FavoriteService;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyFavoritesController implements javafx.fxml.Initializable {

    private static final double CARD_WIDTH = 220;
    private static final double CARD_HEIGHT = 260;
    private static final double IMAGE_AREA_HEIGHT = 140;
    private static final String SERVER_BASE_URL = "http://localhost:8080";

    @FXML private StackPane contentArea;
    @FXML private FlowPane adsListContainer;
    @FXML private Label statusLabel;

    private final FavoriteService favoriteService = new FavoriteService();
    private final AdvertisementService advertisementService = new AdvertisementService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFavorites();
    }

    private void loadFavorites() {
        statusLabel.setText("در حال بارگذاری علاقه‌مندی‌ها...");
        String token = UserSession.getInstance().getToken();

        new Thread(() -> {
            List<FavoriteService.FavoriteItem> favorites = favoriteService.getMyFavorites(token);

            Platform.runLater(() -> {
                adsListContainer.getChildren().clear();

                if (favorites.isEmpty()) {
                    statusLabel.setText("شما هنوز هیچ آگهی‌ای را به علاقه‌مندی‌ها اضافه نکرده‌اید.");
                    return;
                }

                statusLabel.setText("تعداد " + favorites.size() + " آگهی مورد علاقه.");

                for (FavoriteService.FavoriteItem favorite : favorites) {
                    adsListContainer.getChildren().add(buildAdCard(favorite));
                }
            });
        }).start();
    }

    private VBox buildAdCard(FavoriteService.FavoriteItem favorite) {
        Advertisement ad = favorite.getAdvertisement();

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
        imageArea.setStyle("-fx-background-color: #f1f5f9; -fx-background-radius: 10 10 0 0;");

        boolean hasImage = ad.getMainImagePath() != null
                && !ad.getMainImagePath().isBlank()
                && !ad.getMainImagePath().equalsIgnoreCase("string");

        if (hasImage) {
            String imageUrl = ad.getMainImagePath().startsWith("http")
                    ? ad.getMainImagePath()
                    : SERVER_BASE_URL + ad.getMainImagePath();

            ImageView imageView = new ImageView();
            imageView.setFitWidth(CARD_WIDTH);
            imageView.setFitHeight(IMAGE_AREA_HEIGHT);
            imageView.setPreserveRatio(false);

            try {
                Image image = new Image(imageUrl, CARD_WIDTH, IMAGE_AREA_HEIGHT, false, true, true);
                imageView.setImage(image);
                image.errorProperty().addListener((obs, wasError, isError) -> {
                    if (isError) {
                        imageArea.getChildren().setAll(buildNoImageLabel());
                    }
                });
            } catch (Exception e) {
                imageArea.getChildren().add(buildNoImageLabel());
            }
            imageArea.getChildren().add(imageView);
        } else {
            imageArea.getChildren().add(buildNoImageLabel());
        }

        VBox infoArea = new VBox(4);
        infoArea.setPadding(new Insets(10));
        infoArea.setAlignment(Pos.TOP_RIGHT);

        Label titleLabel = new Label(ad.getTitle() != null ? ad.getTitle() : "بدون عنوان");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(CARD_WIDTH - 20);

        String priceText = ad.getPrice() != null ? String.format("%,.0f تومان", ad.getPrice()) : "-";
        Label priceLabel = new Label(priceText);
        priceLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #16a34a; -fx-font-weight: bold;");

        String metaText = String.format("%s | %s",
                ad.getCategoryName() != null ? ad.getCategoryName() : "-",
                ad.getCityName() != null ? ad.getCityName() : "-");
        Label metaLabel = new Label(metaText);
        metaLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");

        infoArea.getChildren().addAll(titleLabel, priceLabel, metaLabel);

        Button removeBtn = new Button("حذف از علاقه‌مندی‌ها");
        removeBtn.setMaxWidth(Double.MAX_VALUE);
        removeBtn.setStyle("-fx-background-color: #f43f5e; -fx-text-fill: white; -fx-font-size: 10px; "
                + "-fx-background-radius: 0 0 10 10; -fx-cursor: hand;");
        removeBtn.setOnAction(e -> {
            e.consume();
            handleRemoveFavorite(ad);
        });

        card.getChildren().addAll(imageArea, infoArea, removeBtn);
        card.setOnMouseClicked(event -> openAdDetail(ad));

        return card;
    }

    private Label buildNoImageLabel() {
        Label noImageLabel = new Label("بدون تصویر");
        noImageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
        return noImageLabel;
    }

    private void handleRemoveFavorite(Advertisement ad) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "آیا از حذف این آگهی از علاقه‌مندی‌ها مطمئن هستید؟", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("حذف از علاقه‌مندی‌ها");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.YES) {
            return;
        }

        String token = UserSession.getInstance().getToken();
        new Thread(() -> {
            String outcome = favoriteService.removeFavorite(token, ad.getId());
            Platform.runLater(() -> {
                if ("SUCCESS".equals(outcome)) {
                    loadFavorites();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, outcome, ButtonType.OK);
                    alert.setHeaderText("خطا");
                    alert.showAndWait();
                }
            });
        }).start();
    }

    private void openAdDetail(Advertisement ad) {
        if (ad.getId() == null) {
            return;
        }
        new Thread(() -> {
            AdvertisementDetail detail = advertisementService.getAdvertisementDetail(ad.getId());
            Platform.runLater(() -> {
                if (detail == null) {
                    statusLabel.setText("خطا در دریافت جزئیات آگهی.");
                    return;
                }
                AdDetailController.setSelectedAdvertisement(detail);
                NavigationService.switchScene("/fxml/dashboard/ad-detail-view.fxml", "جزئیات آگهی");
            });
        }).start();
    }

    @FXML
    private void handleBackToDashboard() {
        NavigationService.switchScene("/fxml/dashboard/dashboard-view.fxml", "داشبورد کاربر");
    }

    @FXML
    private void handleManageMyAds() {
        NavigationService.switchScene("/fxml/dashboard/manage-my-ads-view.fxml", "مدیریت آگهی‌های من");
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().cleanSession();
        NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری");
    }
}