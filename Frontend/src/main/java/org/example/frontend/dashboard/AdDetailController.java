package org.example.frontend.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import org.example.frontend.advertisement.AdvertisementDetail;
import org.example.frontend.shared.NavigationService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdDetailController implements Initializable {

    private static final String SERVER_BASE_URL = "http://localhost:8080";
    private static final double THUMB_SIZE = 160;

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
    private FlowPane imagesGalleryContainer;

    @FXML
    private Label ownerLabel;

    @FXML
    private TextArea descriptionArea;


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
            renderImageGallery(selectedAdvertisement.getImages());
        } else {
            imageStatusLabel.setVisible(true);
            imageStatusLabel.setText("برای این آگهی تصویری ثبت نشده");
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

    private void renderImageGallery(List<AdvertisementDetail.ImageInfo> images) {
        imagesGalleryContainer.getChildren().clear();

        for (AdvertisementDetail.ImageInfo imageInfo : images) {
            String path = imageInfo.getImagePath();
            if (path == null || path.isBlank()) {
                continue;
            }

            String imageUrl = path.startsWith("http") ? path : SERVER_BASE_URL + path;

            ImageView imageView = new ImageView();
            imageView.setFitWidth(THUMB_SIZE);
            imageView.setFitHeight(THUMB_SIZE);
            imageView.setPreserveRatio(false);

            Image image = new Image(imageUrl, THUMB_SIZE, THUMB_SIZE, false, true, true);
            imageView.setImage(image);

            imagesGalleryContainer.getChildren().add(imageView);
        }
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