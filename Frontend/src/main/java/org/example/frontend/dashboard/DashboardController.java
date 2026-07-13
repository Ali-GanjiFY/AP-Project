package org.example.frontend.dashboard;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.frontend.advertisement.Advertisement;
import org.example.frontend.advertisement.AdvertisementDetail;
import org.example.frontend.advertisement.AdvertisementService;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements javafx.fxml.Initializable {

    private static final double CARD_WIDTH = 220;
    private static final double CARD_HEIGHT = 260;
    private static final double IMAGE_AREA_HEIGHT = 140;
    private static final String SERVER_BASE_URL = "http://localhost:8080";

    @FXML
    private StackPane contentArea;

    @FXML
    private FlowPane adsListContainer;

    @FXML
    private Label statusLabel;

    // FXML
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<AdvertisementService.CategoryDto> categoryComboBox;
    @FXML
    private ComboBox<AdvertisementService.CityDto> cityComboBox;
    @FXML
    private TextField minPriceField;
    @FXML
    private TextField maxPriceField;
    @FXML
    private ComboBox<String> sortByComboBox;

    private final AdvertisementService advertisementService = new AdvertisementService();

    private Timeline autoRefreshTimeline;
    private boolean isFilteredMode = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFilters();
        loadAdvertisements();
        startAutoRefresh();
    }
    @FXML
    private void handleOpenChats() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }

        NavigationService.switchScene(
                "/fxml/chat/conversation-list-view.fxml",
                "چت‌ها"
        );
    }

    private void initFilters() {
        sortByComboBox.getItems().addAll(
                "جدیدترین آگهی‌ها",
                "ارزان‌ترین",
                "گران‌ترین"
        );

        new Thread(() -> {
            List<AdvertisementService.CityDto> cities = advertisementService.getAllCities();
            List<AdvertisementService.CategoryDto> categories = advertisementService.getAllCategories();

            Platform.runLater(() -> {
                cityComboBox.getItems().clear();
                cityComboBox.getItems().addAll(cities);

                categoryComboBox.getItems().clear();
                categoryComboBox.getItems().addAll(categories);
            });
        }).start();
    }

    private void startAutoRefresh() {
        autoRefreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(10), event -> {
                    if (!isFilteredMode) {
                        loadAdvertisements();
                    }
                })
        );
        autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        autoRefreshTimeline.play();
    }

    private void loadAdvertisements() {
        new Thread(() -> {
            List<Advertisement> ads = advertisementService.getAllActiveAds();
            updateAdsContainer(ads);
        }).start();
    }

    private void updateAdsContainer(List<Advertisement> ads) {
        Platform.runLater(() -> {
            adsListContainer.getChildren().clear();

            if (ads == null || ads.isEmpty()) {
                statusLabel.setText("در حال حاضر هیچ آگهی منطبق با شرایط یافت نشد.");
                return;
            }

            statusLabel.setText("تعداد " + ads.size() + " آگهی یافت شد.");

            for (Advertisement ad : ads) {
                adsListContainer.getChildren().add(buildAdCard(ad));
            }
        });
    }

    @FXML
    private void handleApplyFilters() {
        isFilteredMode = true; // غیر فعال کردن رفرش خودکار دوره ای

        String keyword = searchField.getText();

        AdvertisementService.CategoryDto selectedCat = categoryComboBox.getValue();
        Long categoryId = selectedCat != null ? selectedCat.getId() : null;

        AdvertisementService.CityDto selectedCity = cityComboBox.getValue();
        Long cityId = selectedCity != null ? selectedCity.getId() : null;

        Double minPrice = null;
        try {
            if (minPriceField.getText() != null && !minPriceField.getText().isBlank()) {
                minPrice = Double.parseDouble(minPriceField.getText().trim());
            }
        } catch (NumberFormatException ignored) {}

        Double maxPrice = null;
        try {
            if (maxPriceField.getText() != null && !maxPriceField.getText().isBlank()) {
                maxPrice = Double.parseDouble(maxPriceField.getText().trim());
            }
        } catch (NumberFormatException ignored) {}

        // نگاشت انتخاب مرتب‌سازی به پارامترهای سمت بک‌اند
        String sortBy = "date";
        String sortDirection = "desc";
        String selectedSort = sortByComboBox.getValue();
        if (selectedSort != null) {
            switch (selectedSort) {
                case "ارزان‌ترین":
                    sortBy = "price";
                    sortDirection = "asc";
                    break;
                case "گران‌ترین":
                    sortBy = "price";
                    sortDirection = "desc";
                    break;
                default:
                    sortBy = "date";
                    sortDirection = "desc";
            }
        }

        statusLabel.setText("در حال اعمال فیلترها...");

        final Double finalMin = minPrice;
        final Double finalMax = maxPrice;
        final String finalSortBy = sortBy;
        final String finalSortDir = sortDirection;

        new Thread(() -> {
            List<Advertisement> filteredAds = advertisementService.searchAdvertisements(
                    keyword, categoryId, cityId, finalMin, finalMax, finalSortBy, finalSortDir
            );
            updateAdsContainer(filteredAds);
        }).start();
    }

    @FXML
    private void handleResetFilters() {
        searchField.clear();
        categoryComboBox.setValue(null);
        cityComboBox.setValue(null);
        minPriceField.clear();
        maxPriceField.clear();
        sortByComboBox.setValue(null);

        isFilteredMode = false;
        loadAdvertisements();
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

        // picture
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

        // details
        VBox infoArea = new VBox(4);
        infoArea.setPadding(new Insets(10));
        infoArea.setAlignment(Pos.TOP_RIGHT);

        Label titleLabel = new Label(ad.getTitle() != null ? ad.getTitle() : "بدون عنوان");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(CARD_WIDTH - 20);

        String priceText = String.format("%,.0f تومان", ad.getPrice());
        Label priceLabel = new Label(priceText);
        priceLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #16a34a; -fx-font-weight: bold;");

        String metaText = String.format("%s | %s",
                ad.getCategoryName() != null ? ad.getCategoryName() : "-",
                ad.getCityName() != null ? ad.getCityName() : "-");
        Label metaLabel = new Label(metaText);
        metaLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");

        infoArea.getChildren().addAll(titleLabel, priceLabel, metaLabel);

        card.getChildren().addAll(imageArea, infoArea);

        card.setOnMouseClicked(event -> openAdDetail(ad));

        return card;
    }

    private Label buildNoImageLabel() {
        Label noImageLabel = new Label("بدون تصویر");
        noImageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
        return noImageLabel;
    }

    private void openAdDetail(Advertisement ad) {
        if (ad.getId() == null) {
            return;
        }

        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }

        new Thread(() -> {
            AdvertisementDetail detail = advertisementService.getAdvertisementDetail(ad.getId());

            Platform.runLater(() -> {
                if (detail == null) {
                    statusLabel.setText("خطا در دریافت جزئیات آگهی. لطفاً دوباره تلاش کنید.");
                    startAutoRefresh();
                    return;
                }
                AdDetailController.setSelectedAdvertisement(detail);
                NavigationService.switchScene("/fxml/dashboard/ad-detail-view.fxml", "جزئیات آگهی");
            });
        }).start();
    }

    @FXML
    private void handleLogout() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
        UserSession.getInstance().cleanSession();
        NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری");
    }

    @FXML
    private void handleCreateAdvertisement() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
        NavigationService.switchScene("/fxml/advertisement/create-advertisement-view.fxml", "ثبت آگهی جدید");
    }

    @FXML
    private void handleManageMyAds() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
        NavigationService.switchScene("/fxml/dashboard/manage-my-ads-view.fxml", "مدیریت آگهی‌های من");
    }

    @FXML
    private void handleShowFavorites() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
        NavigationService.switchScene("/fxml/dashboard/my-favorites-view.fxml", "علاقه‌مندی‌های من");
    }
}