package org.example.frontend.dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.frontend.admin.AdminReviewService;
import org.example.frontend.advertisement.AdvertisementDetail;
import org.example.frontend.advertisement.AdvertisementService;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Represents ad review detail controller.
 */
public class AdReviewDetailController implements Initializable {

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
    @FXML private TextField noteField;
    @FXML private Button approveButton;
    @FXML private Button rejectButton;
    @FXML private Label decisionStatusLabel;

    private final AdvertisementService advertisementService = new AdvertisementService();
    private final AdminReviewService adminReviewService = new AdminReviewService();

    private AdvertisementDetail currentAd;
    private List<AdvertisementDetail.ImageInfo> adImages;
    private int currentImageIndex = 0;

    /**
     * این متد را قبل از رفتن به این صفحه صدا بزن تا بداند کدام آگهی را نشان بدهد.
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
            decisionStatusLabel.setText("شناسه آگهی معتبر نیست.");
            setButtonsDisabled(true);
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
        setButtonsDisabled(true);

        new Thread(() -> {
            String token = UserSession.getInstance().getToken();
            AdvertisementDetail detail = advertisementService.getAdvertisementDetail(adId, token);

            Platform.runLater(() -> {
                if (detail == null) {
                    titleLabel.setText("آگهی پیدا نشد");
                    decisionStatusLabel.setText("دریافت اطلاعات آگهی ناموفق بود.");
                    return;
                }

                currentAd = detail;
                setButtonsDisabled(false);
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
     * Handles approve.
     */
    @FXML
    private void handleApprove() {
        submitDecision("APPROVED");
    }

    /**
     * Handles reject.
     */
    @FXML
    private void handleReject() {
        submitDecision("REJECTED");
    }

    /**
     * Submit decision.
     * @param decision the decision
     */
    private void submitDecision(String decision) {
        if (currentAd == null || currentAd.getId() == null) {
            decisionStatusLabel.setText("شناسه آگهی معتبر نیست.");
            return;
        }

        String token = UserSession.getInstance().getToken();
        String note = noteField.getText();

        setButtonsDisabled(true);
        decisionStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");
        decisionStatusLabel.setText("در حال ثبت تصمیم...");

        new Thread(() -> {
            String result = adminReviewService.reviewAdvertisement(token, currentAd.getId(), decision, note);

            Platform.runLater(() -> {
                if (result.equals("SUCCESS")) {
                    decisionStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #16a34a;");
                    decisionStatusLabel.setText("تصمیم با موفقیت ثبت شد.");
                    handleBack();
                } else {
                    setButtonsDisabled(false);
                    decisionStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #dc2626;");
                    decisionStatusLabel.setText(result);
                }
            });
        }).start();
    }

    /**
     * Sets buttons disabled.
     * @param disabled the disabled
     */
    private void setButtonsDisabled(boolean disabled) {
        approveButton.setDisable(disabled);
        rejectButton.setDisable(disabled);
    }

    /**
     * Handles back.
     */
    @FXML
    private void handleBack() {
        NavigationService.switchScene("/fxml/dashboard/review-ads-view.fxml", "بررسی آگهی‌ها");
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