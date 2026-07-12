package org.example.frontend.chat;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.frontend.shared.NavigationService;

import java.util.List;

public class ConversationListController {

    @FXML
    private Label statusLabel;

    @FXML
    private VBox conversationContainer;

    private final ChatService chatService = new ChatService();

    @FXML
    private void initialize() {
        loadConversations();
    }

    private void loadConversations() {
        statusLabel.setText("در حال بارگذاری گفتگوها...");
        conversationContainer.getChildren().clear();

        Thread thread = new Thread(() -> {
            try {
                List<ConversationResponse> conversations =
                        chatService.getMyConversations();

                Platform.runLater(() -> updateConversationList(conversations));

            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() -> {
                    statusLabel.setText("خطا در دریافت گفتگوها.");
                    showError("دریافت لیست گفتگوها ناموفق بود:\n" + getUsefulErrorMessage(e));
                });
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void updateConversationList(List<ConversationResponse> conversations) {
        conversationContainer.getChildren().clear();

        if (conversations == null || conversations.isEmpty()) {
            statusLabel.setText("هنوز هیچ گفتگویی ندارید.");
            return;
        }

        statusLabel.setText("تعداد " + conversations.size() + " گفتگو یافت شد.");

        for (ConversationResponse conversation : conversations) {
            conversationContainer.getChildren().add(buildConversationItem(conversation));
        }
    }

    private VBox buildConversationItem(ConversationResponse conversation) {
        VBox root = new VBox(8);
        root.setPadding(new Insets(14));
        root.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #e2e8f0;" +
                        "-fx-border-radius: 12;" +
                        "-fx-cursor: hand;"
        );
        root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        String otherUser = safeText(conversation.getOtherUserUsername(), "کاربر");
        String adTitle = safeText(conversation.getAdvertisementTitle(), "آگهی بدون عنوان");
        String lastMessage = safeText(conversation.getLastMessagePreview(), "هنوز پیامی رد و بدل نشده است.");
        String lastTime = formatDateTime(conversation.getLastMessageAt());

        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_RIGHT);

        Label nameLabel = new Label("گفتگو با: " + otherUser);
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label timeLabel = new Label(lastTime);
        timeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #94a3b8;");

        topRow.getChildren().addAll(timeLabel, spacer, nameLabel);

        Label adTitleLabel = new Label("آگهی: " + adTitle);
        adTitleLabel.setWrapText(true);
        adTitleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #334155; -fx-font-weight: bold;");

        Label previewLabel = new Label(lastMessage);
        previewLabel.setWrapText(true);
        previewLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        root.getChildren().addAll(topRow, adTitleLabel, previewLabel);

        root.setOnMouseClicked(event -> openConversation(conversation));

        return root;
    }

    private void openConversation(ConversationResponse conversation) {
        if (conversation == null || conversation.getId() == null) {
            showError("شناسه گفتگو نامعتبر است.");
            return;
        }

        NavigationService.switchScene(
                "/fxml/chat/chat-view.fxml",
                "گفتگو",
                (ChatController controller) -> controller.setConversationId(conversation.getId())
        );
    }

    @FXML
    private void handleBack() {
        NavigationService.switchScene(
                "/fxml/dashboard/dashboard-view.fxml",
                "داشبورد"
        );
    }

    private String safeText(String value, String fallback) {
        return (value == null || value.isBlank()) ? fallback : value;
    }

    private String formatDateTime(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        String formatted = value.replace('T', ' ');

        if (formatted.length() >= 16) {
            return formatted.substring(0, 16);
        }

        return formatted;
    }

    private String getUsefulErrorMessage(Exception e) {
        if (e.getMessage() != null && !e.getMessage().isBlank()) {
            return e.getMessage();
        }
        return e.getClass().getSimpleName();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("خطا");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

