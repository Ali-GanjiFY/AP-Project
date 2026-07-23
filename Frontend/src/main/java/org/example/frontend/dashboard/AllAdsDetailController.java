package org.example.frontend.dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.frontend.advertisement.AdvertisementDetail;
import org.example.frontend.advertisement.AdvertisementService;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Represents the admin read-only ad detail controller, reached from the "کل آگهی‌ها"
 * (all advertisements) list. Shows full ad details and lets the admin delete the ad.
 */
public class AllAdsDetailController implements Initializable {

    private static final String SERVER_BASE_URL = "http://localhost:8080";

    private static Long selectedAdvertisementId;

    @FXML private Label titleLabel;
    @FXML private Label priceLabel;
    @FXML private Label categoryLabel;
    @FXML private Label cityLabel;
    @FXML private Label statusLabel;
    @FXML private Label ownerLabel;
    @FXML private Label sellerAverageRatingLabel;
    @FXML private Label imageStatusLabel;
    @FXML private ImageView mainImageView;
    @FXML private Label imageCounterLabel;
    @FXML private Button prevImageButton;
    @FXML private Button nextImageButton;
    @FXML private TextArea descriptionArea;
    @FXML private Button deleteButton;
    @FXML private Label actionStatusLabel;

    private final AdvertisementService advertisementService = new AdvertisementService();

    private AdvertisementDetail currentAd;
    private List<AdvertisementDetail.ImageInfo> adImages;
    private int currentImageIndex = 0;

    /**
     * Call this before navigating to this page so it knows which ad to show.
     * @param advertisementId the advertisement id
     */
    public static void setSelectedAdvertisementId(Long advertisementId) {
        selectedAdvertisementId = advertisementId;
    }

    /**
     * Initialize.
     * @param location the location
     * @param resources the resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (selectedAdvertisementId == null) {
            titleLabel.setText("آگهی پیدا نشد");
            actionStatusLabel.setText("شناسه آگهی معتبر نیست.");
            deleteButton.setDisable(true);
            return;
        }

        loadAdvertisementDetail(selectedAdvertisementId);
    }

    /**
     * Loads advertisement detail.
     * @param adId the ad id
     */
    private void loadAdvertisementDetail(Long adId) {
        titleLabel.setText("در حال بارگذاری...");
        deleteButton.setDisable(true);
        String token = UserSession.getInstance().getToken();

        new Thread(() -> {
            AdvertisementDetail detail = advertisementService.getAdvertisementDetail(adId, token);

            Platform.runLater(() -> {
                if (detail == null) {
                    titleLabel.setText("آگهی پیدا نشد");
                    actionStatusLabel.setText("دریافت اطلاعات آگهی ناموفق بود.");
                    return;
                }

                currentAd = detail;
                deleteButton.setDisable(false);
                showAdvertisementDetail(detail);
            });
        }).start();
    }

    /**
     * Shows advertisement detail.
     * @param ad the ad
     */
    private void showAdvertisementDetail(AdvertisementDetail ad) {
        titleLabel.setText(safeText(ad.getTitle(), "بدون عنوان"));
        priceLabel.setText(formatPrice(ad.getPrice()));
        categoryLabel.setText("دسته‌بندی: " + safeText(ad.getCategoryName(), "-"));
        cityLabel.setText("شهر: " + safeText(ad.getCityName(), "-"));
        statusLabel.setText("وضعیت: " + safeText(statusToPersian(ad.getStatus()), "-"));
        ownerLabel.setText("فروشنده: " + safeText(ad.getOwnerFullName(), safeText(ad.getOwnerUsername(), "-")));

        if (ad.getSellerAverageRating() != null) {
            sellerAverageRatingLabel.setText(String.format("میانگین امتیاز فروشنده: %.1f از 5 (%d نظر)",
                    ad.getSellerAverageRating(),
                    ad.getSellerRatingCount() != null ? ad.getSellerRatingCount() : 0));
        } else {
            sellerAverageRatingLabel.setText("میانگین امتیاز فروشنده: بدون نظر");
        }

        descriptionArea.setText(safeText(ad.getDescription(), "توضیحاتی برای این آگهی ثبت نشده است."));

        if (ad.hasImages()) {
            imageStatusLabel.setVisible(false);
            adImages = ad.getImages();
            currentImageIndex = 0;
            mainImageView.setVisible(true);
            showCurrentImage();
        } else {
            imageStatusLabel.setVisible(true);
            imageStatusLabel.setText("برای این آگهی تصویری ثبت نشده");
            mainImageView.setVisible(false);
            prevImageButton.setVisible(false);
            nextImageButton.setVisible(false);
            imageCounterLabel.setText("");
        }
    }

    /**
     * Shows current image.
     */
    private void showCurrentImage() {
        if (adImages == null || adImages.isEmpty()) {
            return;
        }

        String path = adImages.get(currentImageIndex).getImagePath();
        if (path != null && !path.isBlank()) {
            String imageUrl = path.startsWith("http") ? path : SERVER_BASE_URL + path;
            mainImageView.setImage(new Image(imageUrl, 500, 300, true, true, true));
        }

        imageCounterLabel.setText((currentImageIndex + 1) + " / " + adImages.size());

        boolean multipleImages = adImages.size() > 1;
        prevImageButton.setVisible(multipleImages);
        nextImageButton.setVisible(multipleImages);
    }

    /**
     * Handles prev image.
     */
    @FXML
    private void handlePrevImage() {
        if (adImages == null || adImages.isEmpty()) {
            return;
        }
        currentImageIndex = (currentImageIndex - 1 + adImages.size()) % adImages.size();
        showCurrentImage();
    }

    /**
     * Handles next image.
     */
    @FXML
    private void handleNextImage() {
        if (adImages == null || adImages.isEmpty()) {
            return;
        }
        currentImageIndex = (currentImageIndex + 1) % adImages.size();
        showCurrentImage();
    }

    /**
     * Handles delete.
     */
    @FXML
    private void handleDelete() {
        if (currentAd == null || currentAd.getId() == null) {
            actionStatusLabel.setText("شناسه آگهی معتبر نیست.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "آیا از حذف این آگهی مطمئن هستید؟", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("حذف آگهی");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.YES) {
            return;
        }

        String token = UserSession.getInstance().getToken();
        deleteButton.setDisable(true);
        actionStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");
        actionStatusLabel.setText("در حال حذف آگهی...");

        new Thread(() -> {
            String outcome = advertisementService.deleteAdvertisement(token, currentAd.getId());

            Platform.runLater(() -> {
                if ("SUCCESS".equals(outcome)) {
                    actionStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #16a34a;");
                    actionStatusLabel.setText("آگهی با موفقیت حذف شد.");
                    handleBack();
                } else {
                    deleteButton.setDisable(false);
                    actionStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #dc2626;");
                    actionStatusLabel.setText(outcome);
                }
            });
        }).start();
    }

    /**
     * Handles back.
     */
    @FXML
    private void handleBack() {
        NavigationService.switchScene("/fxml/dashboard/all-ads-view.fxml", "کل آگهی‌ها");
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
     * Formats price.
     * @param price the price
     * @return the result
     */
    private String formatPrice(Double price) {
        if (price == null) {
            return "-";
        }
        return String.format("%,.0f تومان", price);
    }

    /**
     * Status to persian.
     * @param status the status
     * @return the result
     */
    private String statusToPersian(String status) {
        if (status == null) {
            return null;
        }
        return switch (status) {
            case "ACTIVE" -> "فعال";
            case "PENDING" -> "در انتظار تایید";
            case "REJECTED" -> "رد شده";
            case "SOLD" -> "فروخته شده";
            case "DELETED" -> "حذف شده";
            default -> status;
        };
    }
}
