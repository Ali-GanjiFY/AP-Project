package org.example.frontend.dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.List;

public class UserManagementController {

    @FXML private TableView<UserResponse> usersTable;
    @FXML private TableColumn<UserResponse, Long> idCol;
    @FXML private TableColumn<UserResponse, String> fullNameCol;
    @FXML private TableColumn<UserResponse, String> usernameCol;
    @FXML private TableColumn<UserResponse, String> phoneCol;
    @FXML private TableColumn<UserResponse, String> emailCol;
    @FXML private TableColumn<UserResponse, String> roleCol;
    @FXML private TableColumn<UserResponse, String> statusCol;
    @FXML private TableColumn<UserResponse, Void> actionsCol;

    private final AdminUserService userService = new AdminUserService();

    @FXML
    public void initialize() {
        // مپ کردن ستون‌ها با فیلدهای DTO
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // استایل دهی زیباتر به ستون وضعیت (Status)
        statusCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    if ("ACTIVE".equals(status)) {
                        setText("فعال");
                        setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold;");
                    } else if ("BLOCKED".equals(status)) {
                        setText("مسدود شده");
                        setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
                    } else if ("DELETED".equals(status)) {
                        setText("حذف شده");
                        setStyle("-fx-text-fill: #94a3b8; -fx-font-weight: bold;");
                    } else {
                        setText(status);
                    }
                }
            }
        });

        // ایجاد دکمه‌های پویا برای ستون عملیات (Actions)
        setupActionsColumn();

        // لود کردن اولیه اطلاعات
        loadUsers();
    }

    @FXML
    public void loadUsers() {
        new Thread(() -> {
            try {
                List<UserResponse> users = userService.getAllUsers();
                Platform.runLater(() -> usersTable.getItems().setAll(users));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showError("خطا", "امکان لود لیست کاربران وجود ندارد:\n" + e.getMessage()));
            }
        }).start();
    }

    private void setupActionsColumn() {
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button actionBtn = new Button();
            private final HBox container = new HBox(actionBtn);

            {
                container.setStyle("-fx-alignment: CENTER;");
                actionBtn.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 4px;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                UserResponse user = getTableView().getItems().get(getIndex());

                if ("DELETED".equals(user.getStatus())) {
                    setGraphic(new Label("بدون اقدام"));
                    return;
                }

                if ("ACTIVE".equals(user.getStatus())) {
                    actionBtn.setText("مسدود سازی");
                    actionBtn.setStyle(actionBtn.getStyle() + "-fx-background-color: #ef4444;"); // قرمز
                    actionBtn.setOnAction(event -> handleToggleBlock(user, true));
                } else if ("BLOCKED".equals(user.getStatus())) {
                    actionBtn.setText("فعال سازی");
                    actionBtn.setStyle(actionBtn.getStyle() + "-fx-background-color: #10b981;"); // سبز
                    actionBtn.setOnAction(event -> handleToggleBlock(user, false));
                }

                setGraphic(container);
            }
        });
    }

    private void handleToggleBlock(UserResponse user, boolean shouldBlock) {
        String actionTitle = shouldBlock ? "مسدود سازی" : "فعال سازی";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("تایید عملیات");
        alert.setHeaderText(null);
        alert.setContentText("آیا از " + actionTitle + " کاربر «" + user.getFullName() + "» مطمئن هستید؟");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            new Thread(() -> {
                try {
                    UserResponse updatedUser;
                    if (shouldBlock) {
                        updatedUser = userService.blockUser(user.getId());
                    } else {
                        updatedUser = userService.unblockUser(user.getId());
                    }

                    Platform.runLater(() -> {
                        // آپدیت کردن آبجکت در جدول بدون نیاز به درخواست مجدد کل لیست
                        user.setStatus(updatedUser.getStatus());
                        usersTable.refresh();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> showError("خطا", "عملیات ناموفق بود: " + e.getMessage()));
                }
            }).start();
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
