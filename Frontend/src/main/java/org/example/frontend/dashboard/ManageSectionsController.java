package org.example.frontend.dashboard;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import org.example.frontend.advertisement.CategoryOption;
import org.example.frontend.advertisement.CategoryService;
import org.example.frontend.advertisement.CityOption;
import org.example.frontend.advertisement.CityService;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManageSectionsController implements javafx.fxml.Initializable {

    @FXML private StackPane contentArea;

    // City
    @FXML private TextField cityNameField;
    @FXML private TextField cityProvinceField;
    @FXML private Label cityStatusLabel;
    @FXML private TableView<CityOption> cityTable;
    @FXML private TableColumn<CityOption, String> cityNameColumn;
    @FXML private TableColumn<CityOption, String> cityProvinceColumn;
    @FXML private TableColumn<CityOption, Void> cityActionColumn;

    // Category
    @FXML private TextField categoryNameField;
    @FXML private TextField categoryDescriptionField;
    @FXML private ComboBox<CategoryOption> categoryParentCombo;
    @FXML private Label categoryStatusLabel;
    @FXML private TableView<CategoryOption> categoryTable;
    @FXML private TableColumn<CategoryOption, String> categoryNameColumn;
    @FXML private TableColumn<CategoryOption, String> categoryParentColumn;
    @FXML private TableColumn<CategoryOption, String> categoryActiveColumn;
    @FXML private TableColumn<CategoryOption, Void> categoryActionColumn;

    private final CityService cityService = new CityService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cityNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityProvinceColumn.setCellValueFactory(new PropertyValueFactory<>("province"));
        addCityDeleteButton();

        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryParentColumn.setCellValueFactory(cellData -> {
            String parentName = cellData.getValue().getParentCategoryName();
            return new javafx.beans.property.SimpleStringProperty(parentName != null ? parentName : "-");
        });
        categoryActiveColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().isActive() ? "بله" : "خیر"));
        addCategoryDeleteButton();

        loadCities();
        loadCategories();
    }

    // City

    private void loadCities() {
        cityStatusLabel.setText("در حال بارگذاری شهرها...");

        new Thread(() -> {
            List<CityOption> cities = cityService.getAllCities();

            Platform.runLater(() -> {
                cityTable.setItems(FXCollections.observableArrayList(cities));
                cityStatusLabel.setText(cities.isEmpty()
                        ? "هیچ شهری ثبت نشده است."
                        : "تعداد " + cities.size() + " شهر ثبت شده است.");
            });
        }).start();
    }

    @FXML
    private void handleAddCity() {
        String name = cityNameField.getText() == null ? "" : cityNameField.getText().trim();
        String province = cityProvinceField.getText();

        if (name.isEmpty()) {
            showAlert("لطفاً نام شهر را وارد کنید.");
            return;
        }

        String token = UserSession.getInstance().getToken();
        cityStatusLabel.setText("در حال افزودن شهر...");

        new Thread(() -> {
            String result = cityService.createCity(token, name, province);

            Platform.runLater(() -> {
                if (result.equals("SUCCESS")) {
                    cityNameField.clear();
                    cityProvinceField.clear();
                    loadCities();
                } else {
                    cityStatusLabel.setText("خطا در افزودن شهر.");
                    showAlert(result);
                }
            });
        }).start();
    }

    private void addCityDeleteButton() {
        cityActionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("حذف");

            {
                deleteBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");
                deleteBtn.setOnAction(e -> {
                    CityOption city = getTableView().getItems().get(getIndex());
                    confirmAndDeleteCity(city);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
    }

    private void confirmAndDeleteCity(CityOption city) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "آیا از حذف شهر «" + city.getName() + "» مطمئن هستید؟", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                String token = UserSession.getInstance().getToken();
                new Thread(() -> {
                    String result = cityService.deleteCity(token, city.getId());
                    Platform.runLater(() -> {
                        if (result.equals("SUCCESS")) {
                            loadCities();
                        } else {
                            showAlert(result);
                        }
                    });
                }).start();
            }
        });
    }

    //  Category

    private void loadCategories() {
        categoryStatusLabel.setText("در حال بارگذاری دسته‌بندی‌ها...");

        new Thread(() -> {
            List<CategoryOption> categories = categoryService.getAllCategoriesForAdmin();

            Platform.runLater(() -> {
                categoryTable.setItems(FXCollections.observableArrayList(categories));
                categoryParentCombo.setItems(FXCollections.observableArrayList(categories));
                categoryStatusLabel.setText(categories.isEmpty()
                        ? "هیچ دسته‌بندی‌ای ثبت نشده است."
                        : "تعداد " + categories.size() + " دسته‌بندی ثبت شده است.");
            });
        }).start();
    }

    @FXML
    private void handleAddCategory() {
        String name = categoryNameField.getText() == null ? "" : categoryNameField.getText().trim();
        String description = categoryDescriptionField.getText();
        CategoryOption parent = categoryParentCombo.getValue();
        Long parentId = (parent != null) ? parent.getId() : null;

        if (name.isEmpty()) {
            showAlert("لطفاً نام دسته‌بندی را وارد کنید.");
            return;
        }

        String token = UserSession.getInstance().getToken();
        categoryStatusLabel.setText("در حال افزودن دسته‌بندی...");

        new Thread(() -> {
            String result = categoryService.createCategory(token, name, description, parentId);

            Platform.runLater(() -> {
                if (result.equals("SUCCESS")) {
                    categoryNameField.clear();
                    categoryDescriptionField.clear();
                    categoryParentCombo.setValue(null);
                    loadCategories();
                } else {
                    categoryStatusLabel.setText("خطا در افزودن دسته‌بندی.");
                    showAlert(result);
                }
            });
        }).start();
    }

    private void addCategoryDeleteButton() {
        categoryActionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("حذف");

            {
                deleteBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");
                deleteBtn.setOnAction(e -> {
                    CategoryOption category = getTableView().getItems().get(getIndex());
                    confirmAndDeleteCategory(category);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
    }

    private void confirmAndDeleteCategory(CategoryOption category) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "آیا از حذف دسته‌بندی «" + category.getName() + "» مطمئن هستید؟", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                String token = UserSession.getInstance().getToken();
                new Thread(() -> {
                    String result = categoryService.deleteCategory(token, category.getId());
                    Platform.runLater(() -> {
                        if (result.equals("SUCCESS")) {
                            loadCategories();
                        } else {
                            showAlert(result);
                        }
                    });
                }).start();
            }
        });
    }

    // both of them

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText("خطا");
        alert.showAndWait();
    }

    @FXML
    private void handleBackToDashboard() {
        NavigationService.switchScene("/fxml/dashboard/admin-dashboard-view.fxml", "پنل مدیریت (ادمین)");
    }

    @FXML
    private void handleManageUsers() {
        // TODO: بارگذاری صفحه مدیریت کاربران
    }

    @FXML
    private void handleReviewAds() {
        NavigationService.switchScene("/fxml/dashboard/review-ads-view.fxml", "بررسی آگهی‌ها");
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().cleanSession();
        NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری");
    }
}