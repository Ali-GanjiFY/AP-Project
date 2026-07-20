package org.example.frontend.advertisement;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateAdvertisementController implements javafx.fxml.Initializable {

    @FXML private Label formTitleLabel;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private TextField priceField;
    @FXML private ComboBox<CategoryOption> categoryComboBox;
    @FXML private ComboBox<CityOption> cityComboBox;
    @FXML private Label imagesCountLabel;
    @FXML private VBox imagesSection;
    @FXML private FlowPane imagesPreviewContainer;
    @FXML private Label statusLabel;
    @FXML private Button submitButton;

    private final CategoryService categoryService = new CategoryService();
    private final CityService cityService = new CityService();
    private final AdvertisementService advertisementService = new AdvertisementService();
    private final ImageUploadService imageUploadService = new ImageUploadService();

    private final List<File> selectedImages = new ArrayList<>();

    // When non-null, the form is in "edit" mode for this advertisement instead of "create" mode.
    private Long editingAdId;
    private Long pendingCategoryId;
    private Long pendingCityId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCategoriesAndCities();
    }

    // Called by NavigationService's controllerInitializer when navigating here to edit an existing ad.
    public void setEditMode(AdvertisementDetail ad) {
        this.editingAdId = ad.getId();
        this.pendingCategoryId = ad.getCategory() != null ? ad.getCategory().getId() : null;
        this.pendingCityId = ad.getCity() != null ? ad.getCity().getId() : null;

        titleField.setText(ad.getTitle());
        descriptionField.setText(ad.getDescription());
        if (ad.getPrice() != null) {
            priceField.setText(String.valueOf(ad.getPrice()));
        }

        if (formTitleLabel != null) {
            formTitleLabel.setText("ویرایش آگهی");
        }
        if (submitButton != null) {
            submitButton.setText("ذخیره تغییرات");
        }
        // Images are not editable through the update endpoint, so hide the picker in edit mode.
        if (imagesSection != null) {
            imagesSection.setVisible(false);
            imagesSection.setManaged(false);
        }

        applyPendingSelections();
    }

    private void applyPendingSelections() {
        if (pendingCategoryId != null) {
            for (CategoryOption option : categoryComboBox.getItems()) {
                if (option.getId().equals(pendingCategoryId)) {
                    categoryComboBox.setValue(option);
                    break;
                }
            }
        }
        if (pendingCityId != null) {
            for (CityOption option : cityComboBox.getItems()) {
                if (option.getId().equals(pendingCityId)) {
                    cityComboBox.setValue(option);
                    break;
                }
            }
        }
    }

    private void loadCategoriesAndCities() {
        new Thread(() -> {
            List<CategoryOption> categories = categoryService.getAllCategories();
            List<CityOption> cities = cityService.getAllCities();

            Platform.runLater(() -> {
                categoryComboBox.getItems().setAll(categories);
                cityComboBox.getItems().setAll(cities);

                if (categories.isEmpty()) {
                    showError("دسته‌بندی‌ای برای انتخاب یافت نشد. با مدیر سیستم تماس بگیرید.");
                }
                if (cities.isEmpty()) {
                    showError("شهری برای انتخاب یافت نشد. با مدیر سیستم تماس بگیرید.");
                }

                // If setEditMode ran before the lists were populated, apply the selection now.
                applyPendingSelections();
            });
        }).start();
    }

    @FXML
    private void handleChooseImages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("انتخاب تصاویر آگهی");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("فایل‌های تصویری", "*.png", "*.jpg", "*.jpeg", "*.webp")
        );

        List<File> files = fileChooser.showOpenMultipleDialog(NavigationService.getPrimaryStage());
        if (files != null && !files.isEmpty()) {
            selectedImages.addAll(files);

            imagesCountLabel.setText(selectedImages.size() + " عکس انتخاب شد");

            imagesPreviewContainer.getChildren().clear();
            for (File file : selectedImages) {
                Label fileLabel = new Label(file.getName());
                fileLabel.setStyle(
                        "-fx-background-color: #f1f5f9; -fx-padding: 4 10; " +
                                "-fx-background-radius: 12; -fx-font-size: 11px; -fx-text-fill: #334155;"
                );
                imagesPreviewContainer.getChildren().add(fileLabel);
            }
        }
    }

    @FXML
    private void handleSubmit() {
        String title = titleField.getText() == null ? "" : titleField.getText().trim();
        String description = descriptionField.getText() == null ? "" : descriptionField.getText().trim();
        String priceText = priceField.getText() == null ? "" : priceField.getText().trim();
        CategoryOption category = categoryComboBox.getValue();
        CityOption city = cityComboBox.getValue();

        if (title.isEmpty() || description.isEmpty() || priceText.isEmpty()) {
            showError("لطفاً عنوان، توضیحات و قیمت را وارد کنید.");
            return;
        }
        if (category == null) {
            showError("لطفاً یک دسته‌بندی انتخاب کنید.");
            return;
        }
        if (city == null) {
            showError("لطفاً یک شهر انتخاب کنید.");
            return;
        }

        Double price;
        try {
            price = Double.parseDouble(priceText);
            if (price <= 0) {
                showError("قیمت باید عددی مثبت باشد.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("قیمت وارد شده معتبر نیست.");
            return;
        }

        String token = UserSession.getInstance().getToken();
        if (token == null || token.isBlank()) {
            showError("برای ثبت آگهی باید وارد حساب کاربری خود شوید.");
            return;
        }

        Long categoryId = category.getId();
        Long cityId = city.getId();

        if (editingAdId != null) {
            submitEdit(token, title, description, price, categoryId, cityId);
        } else {
            submitCreate(token, title, description, price, categoryId, cityId);
        }
    }

    private void submitCreate(String token, String title, String description, Double price,
                              Long categoryId, Long cityId) {
        statusLabel.setText("در حال ثبت آگهی...");
        statusLabel.setStyle("-fx-text-fill: #64748b;");

        List<File> imagesToUpload = new ArrayList<>(selectedImages);

        new Thread(() -> {
            List<String> imagePaths = imageUploadService.uploadImages(token, imagesToUpload);

            if (!imagesToUpload.isEmpty() && imagePaths.isEmpty()) {
                Platform.runLater(() -> showError("آپلود عکس‌ها ناموفق بود. لطفاً دوباره تلاش کنید."));
                return;
            }

            String result = advertisementService.createAdvertisement(
                    token, title, description, price, categoryId, cityId, imagePaths
            );

            Platform.runLater(() -> {
                if ("SUCCESS".equals(result)) {
                    showSuccess("آگهی با موفقیت ثبت شد و پس از تأیید ادمین نمایش داده می‌شود.");
                    NavigationService.switchScene("/fxml/dashboard/manage-my-ads-view.fxml", "مدیریت آگهی‌های من");
                } else {
                    showError(result);
                }
            });
        }).start();
    }

    private void submitEdit(String token, String title, String description, Double price,
                            Long categoryId, Long cityId) {
        statusLabel.setText("در حال ذخیره تغییرات...");
        statusLabel.setStyle("-fx-text-fill: #64748b;");

        Long adId = editingAdId;

        new Thread(() -> {
            String result = advertisementService.updateAdvertisement(
                    token, adId, title, description, price, categoryId, cityId
            );

            Platform.runLater(() -> {
                if ("SUCCESS".equals(result)) {
                    showSuccess("آگهی با موفقیت ویرایش شد و مجدداً برای تأیید ادمین ارسال شد.");
                    NavigationService.switchScene("/fxml/dashboard/manage-my-ads-view.fxml", "مدیریت آگهی‌های من");
                } else {
                    showError(result);
                }
            });
        }).start();
    }

    @FXML
    private void handleCancel() {
        if (editingAdId != null) {
            NavigationService.switchScene("/fxml/dashboard/manage-my-ads-view.fxml", "مدیریت آگهی‌های من");
        } else {
            NavigationService.switchScene("/fxml/dashboard/dashboard-view.fxml", "داشبورد اصلی");
        }
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        statusLabel.getStyleClass().removeAll("success-text");
        if (!statusLabel.getStyleClass().contains("error-text")) {
            statusLabel.getStyleClass().add("error-text");
        }
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        statusLabel.getStyleClass().removeAll("error-text");
        if (!statusLabel.getStyleClass().contains("success-text")) {
            statusLabel.getStyleClass().add("success-text");
        }
    }
}