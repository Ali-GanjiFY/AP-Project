package org.example.frontend.chat;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.frontend.shared.NavigationService;

public class ChatController {

    @FXML
    private Label chatTitleLabel;

    @FXML
    private Label chatSubtitleLabel;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox messageContainer;

    @FXML
    private TextField messageTextField;

    @FXML
    private Button sendButton;

    private Long conversationId;

    @FXML
    private void initialize() {
        if (chatTitleLabel != null) {
            chatTitleLabel.setText("گفتگو درباره آگهی");
        }

        if (chatSubtitleLabel != null) {
            chatSubtitleLabel.setText("در انتظار اطلاعات گفتگو...");
        }

        if (messageContainer != null) {
            messageContainer.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }

        if (messageTextField != null) {
            messageTextField.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;

        if (chatSubtitleLabel != null) {
            chatSubtitleLabel.setText("شماره گفتگو: " + conversationId);
        }

        addSystemMessage("گفتگو با موفقیت باز شد.");
        addSystemMessage("Conversation ID: " + conversationId);
    }

    @FXML
    private void handleSendMessage() {
        if (conversationId == null) {
            addSystemMessage("خطا: شناسه گفتگو هنوز تنظیم نشده است.");
            return;
        }

        String message = messageTextField.getText();

        if (message == null || message.trim().isEmpty()) {
            return;
        }

        addUserMessage(message.trim());
        messageTextField.clear();

        // فعلاً فقط پیام را در UI نشان می‌دهیم.
        // بعداً اینجا sendMessage به بک‌اند وصل می‌شود.
    }

    @FXML
    private void handleBack() {
        NavigationService.switchScene("/fxml/dashboard/dashboard-view.fxml", "داشبورد");
    }

    private void addUserMessage(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setStyle(
                "-fx-background-color: #10b981;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10 14 10 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-font-size: 14;"
        );

        HBox wrapper = new HBox(label);
        wrapper.setStyle("-fx-alignment: center-right;");

        messageContainer.getChildren().add(wrapper);
        scrollToBottom();
    }

    private void addSystemMessage(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setStyle(
                "-fx-background-color: #e2e8f0;" +
                        "-fx-text-fill: #334155;" +
                        "-fx-padding: 8 12 8 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-font-size: 12;"
        );

        HBox wrapper = new HBox(label);
        wrapper.setStyle("-fx-alignment: center;");

        messageContainer.getChildren().add(wrapper);
        scrollToBottom();
    }

    private void scrollToBottom() {
        Platform.runLater(() -> {
            if (scrollPane != null) {
                scrollPane.setVvalue(1.0);
            }
        });
    }
}
