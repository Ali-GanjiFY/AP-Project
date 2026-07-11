package org.example.frontend.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.frontend.advertisement.AdvertisementDetail;
import org.example.frontend.shared.NavigationService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdDetailController implements Initializable {

    private static final String SERVER_BASE_URL = "http://localhost:8080";

    private static AdvertisementDetail selectedAdvertisement;

    @FXML
    private Label titleLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label imageStatusLabel;

    @FXML
    private ImageView mainImageView;

    @FXML
    private Label imageCounterLabel;

    @FXML
    private Button prevImageButton;

    @FXML
    private Button nextImageButton;

    @FXML
    private Label ownerLabel;

    @FXML
    private TextArea descriptionArea;

    private List<AdvertisementDetail.ImageInfo> adImages;
    private int currentImageIndex = 0;


    public static void setSelectedAdvertisement(AdvertisementDetail advertisement) {
        selectedAdvertisement = advertisement;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (selectedAdvertisement == null) {
            titleLabel.setText("آگهی پیدا نشد");
            priceLabel.setText("-");
            categoryLabel.setText("-");
            cityLabel.setText("-");
            statusLabel.setText("-");
            ownerLabel.setText("-");
            imageStatusLabel.setText("اطلاعاتی موجود نیست");
            descriptionArea.setText("هیچ اطلاعاتی برای این آگهی دریافت نشد.");
            return;
        }

        titleLabel.setText(safeText(selectedAdvertisement.getTitle(), "بدون عنوان"));
        priceLabel.setText(formatPrice(selectedAdvertisement.getPrice()));
        categoryLabel.setText("دسته‌بندی: " + safeText(selectedAdvertisement.getCategoryName(), "-"));
        cityLabel.setText("شهر: " + safeText(selectedAdvertisement.getCityName(), "-"));
        statusLabel.setText("وضعیت: " + safeText(statusToPersian(selectedAdvertisement.getStatus()), "-"));
        ownerLabel.setText("فروشنده: " + safeText(selectedAdvertisement.getOwnerFullName(),
                safeText(selectedAdvertisement.getOwnerUsername(), "-")));

        boolean hasImage = selectedAdvertisement.hasImages();
        if (hasImage) {
            imageStatusLabel.setVisible(false);
            adImages = selectedAdvertisement.getImages();
            currentImageIndex = 0;
            showCurrentImage();
        } else {
            imageStatusLabel.setVisible(true);
            imageStatusLabel.setText("برای این آگهی تصویری ثبت نشده");
            mainImageView.setVisible(false);
            prevImageButton.setVisible(false);
            nextImageButton.setVisible(false);
            imageCounterLabel.setText("");
        }

        descriptionArea.setText(safeText(selectedAdvertisement.getDescription(),
                "توضیحاتی برای این آگهی ثبت نشده است."));
    }

    @FXML
    private void handleBack() {
        NavigationService.switchScene("/fxml/dashboard/dashboard-view.fxml", "داشبورد");
    }

    // TODO: implement real chat creation.
    @FXML
    private void handleStartChat() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("قابلیت گفتگو با فروشنده به‌زودی اضافه می‌شود.");
        alert.showAndWait();
    }

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

    @FXML
    private void handlePrevImage() {
        if (adImages == null || adImages.isEmpty()) {
            return;
        }
        currentImageIndex = (currentImageIndex - 1 + adImages.size()) % adImages.size();
        showCurrentImage();
    }

    @FXML
    private void handleNextImage() {
        if (adImages == null || adImages.isEmpty()) {
            return;
        }
        currentImageIndex = (currentImageIndex + 1) % adImages.size();
        showCurrentImage();
    }

    private String safeText(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private String formatPrice(Double price) {
        if (price == null) {
            return "-";
        }
        return String.format("%,.0f تومان", price);
    }

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