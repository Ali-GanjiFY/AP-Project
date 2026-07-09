package org.example.frontend.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.example.frontend.advertisement.Advertisement;
import org.example.frontend.shared.NavigationService;

import java.net.URL;
import java.util.ResourceBundle;

public class AdDetailController implements Initializable {

    private static Advertisement selectedAdvertisement;

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
    private TextArea descriptionArea;

    public static void setSelectedAdvertisement(Advertisement advertisement) {
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
            imageStatusLabel.setText("اطلاعاتی موجود نیست");
            descriptionArea.setText("هیچ اطلاعاتی برای این آگهی دریافت نشد.");
            return;
        }

        titleLabel.setText(safeText(selectedAdvertisement.getTitle(), "بدون عنوان"));
        priceLabel.setText(formatPrice(selectedAdvertisement.getPrice()));
        categoryLabel.setText("دسته‌بندی: " + safeText(selectedAdvertisement.getCategoryName(), "-"));
        cityLabel.setText("شهر: " + safeText(selectedAdvertisement.getCityName(), "-"));
        statusLabel.setText("وضعیت: " + safeText(selectedAdvertisement.getStatus(), "-"));

        boolean hasImage = selectedAdvertisement.getMainImagePath() != null
                && !selectedAdvertisement.getMainImagePath().isBlank()
                && !"string".equalsIgnoreCase(selectedAdvertisement.getMainImagePath());

        imageStatusLabel.setText(hasImage ? "این آگهی تصویر دارد" : "برای این آگهی تصویری ثبت نشده");

        descriptionArea.setText(
                "توضیحات این آگهی فعلاً در مدل فرانت‌اند وجود ندارد.\n\n" +
                        "اگر بخواهی، در قدم بعدی `Advertisement` و API را هم طوری اصلاح می‌کنیم که description از بک‌اند دریافت شود."
        );
    }

    @FXML
    private void handleBack() {
        NavigationService.switchScene("/fxml/dashboard/dashboard-view.fxml", "داشبورد");
    }

    private String safeText(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private String formatPrice(Double price) {
        if (price == null) {
            return "توافقی";
        }
        return String.format("%,.0f تومان", price);
    }
}


