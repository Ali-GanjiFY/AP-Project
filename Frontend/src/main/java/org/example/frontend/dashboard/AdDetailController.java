package org.example.frontend.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.example.frontend.advertisement.AdvertisementDetail;
import org.example.frontend.shared.NavigationService;

import java.net.URL;
import java.util.ResourceBundle;

public class AdDetailController implements Initializable {

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
        imageStatusLabel.setText(hasImage ? "این آگهی تصویر دارد" : "برای این آگهی تصویری ثبت نشده");

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