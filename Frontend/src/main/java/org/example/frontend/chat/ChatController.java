package org.example.frontend.chat;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.frontend.shared.NavigationService;
import org.example.frontend.shared.UserSession;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    private final ChatService chatService = new ChatService();

    /*
     * برای جلوگیری از نمایش دوباره یک پیام هنگام polling.
     */
    private final Set<Long> displayedMessageIds = new HashSet<>();

    private Long conversationId;

    /*
     * هر دو ثانیه سرور را برای پیام‌های جدید بررسی می‌کند.
     */
    private Timeline messagePolling;

    /*
     * جلوگیری از اجرای هم‌زمان چند درخواست دریافت پیام.
     */
    private boolean loadingMessages;

    /*
     * اگر صفحه بسته شود، نتیجه درخواست قبلی دیگر روی UI اعمال نمی‌شود.
     */
    private boolean controllerActive = true;

    @FXML
    private void initialize() {
        chatTitleLabel.setText("گفتگو");
        chatSubtitleLabel.setText("در حال آماده‌سازی...");

        messageContainer.setNodeOrientation(
                NodeOrientation.LEFT_TO_RIGHT
        );

        messageTextField.setNodeOrientation(
                NodeOrientation.RIGHT_TO_LEFT
        );

        sendButton.setDisable(true);

        /*
         * خالی و پر بودن فیلد، وضعیت دکمه ارسال را کنترل می‌کند.
         */
        messageTextField.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        updateSendButtonState()
        );
    }

    /**
     * این متد توسط AdDetailController بعد از بارگذاری FXML صدا زده می‌شود.
     */
    public void setConversationId(Long conversationId) {
        if (conversationId == null || conversationId <= 0) {
            showError("شناسه گفتگو نامعتبر است.");
            return;
        }

        this.conversationId = conversationId;
        this.controllerActive = true;

        chatSubtitleLabel.setText(
                "شماره گفتگو: " + conversationId
        );

        updateSendButtonState();

        loadConversationInformation();
        loadMessages(true);
        startMessagePolling();
    }

    /**
     * دریافت نام کاربر مقابل و عنوان آگهی.
     */
    private void loadConversationInformation() {
        runInBackground(() -> {
            ConversationResponse conversation =
                    chatService.getConversation(conversationId);

            Platform.runLater(() -> {
                if (!controllerActive) {
                    return;
                }

                String advertisementTitle =
                        conversation.getAdvertisementTitle();

                String otherUsername =
                        conversation.getOtherUserUsername();

                chatTitleLabel.setText(
                        advertisementTitle == null ||
                                advertisementTitle.isBlank()
                                ? "گفتگو درباره آگهی"
                                : advertisementTitle
                );

                chatSubtitleLabel.setText(
                        otherUsername == null ||
                                otherUsername.isBlank()
                                ? "گفتگو شماره " + conversationId
                                : "گفتگو با " + otherUsername
                );
            });
        }, "دریافت اطلاعات گفتگو");
    }

    /**
     * دریافت تاریخچه یا پیام‌های جدید.
     *
     * @param showLoading آیا هنگام اولین بار پیام در حال بارگذاری نمایش داده شود؟
     */
    private void loadMessages(boolean showLoading) {
        if (conversationId == null || loadingMessages) {
            return;
        }

        loadingMessages = true;

        if (showLoading && displayedMessageIds.isEmpty()) {
            chatSubtitleLabel.setText("در حال دریافت پیام‌ها...");
        }

        runInBackground(() -> {
            List<ChatMessageResponse> messages =
                    chatService.getMessages(conversationId);

            /*
             * خوانده‌شدن پیام‌های دریافتی را هم ثبت می‌کنیم.
             */
            chatService.markMessagesAsSeen(conversationId);

            Platform.runLater(() -> {
                if (!controllerActive) {
                    loadingMessages = false;
                    return;
                }

                for (ChatMessageResponse message : messages) {
                    addMessageIfNotDisplayed(message);
                }

                loadingMessages = false;

                if (messages.isEmpty() &&
                        displayedMessageIds.isEmpty()) {
                    chatSubtitleLabel.setText(
                            "هنوز پیامی ارسال نشده است."
                    );
                }
            });
        }, "دریافت پیام‌ها", () -> loadingMessages = false);
    }

    @FXML
    private void handleSendMessage() {
        if (conversationId == null) {
            showError("شناسه گفتگو تنظیم نشده است.");
            return;
        }

        String content = messageTextField.getText();

        if (content == null || content.isBlank()) {
            return;
        }

        String trimmedContent = content.trim();

        sendButton.setDisable(true);
        messageTextField.setDisable(true);
        chatSubtitleLabel.setText("در حال ارسال پیام...");

        runInBackground(() -> {
            ChatMessageResponse sentMessage =
                    chatService.sendMessage(
                            conversationId,
                            trimmedContent
                    );

            Platform.runLater(() -> {
                if (!controllerActive) {
                    return;
                }

                messageTextField.clear();
                messageTextField.setDisable(false);
                messageTextField.requestFocus();

                addMessageIfNotDisplayed(sentMessage);

                chatSubtitleLabel.setText(
                        "پیام با موفقیت ارسال شد."
                );

                updateSendButtonState();
            });
        }, "ارسال پیام", () -> {
            messageTextField.setDisable(false);
            updateSendButtonState();
        });
    }

    /**
     * پیام را فقط درصورتی نمایش می‌دهد که قبلاً نمایش داده نشده باشد.
     */
    private void addMessageIfNotDisplayed(
            ChatMessageResponse message
    ) {
        if (message == null) {
            return;
        }

        Long messageId = message.getId();

        if (messageId != null &&
                displayedMessageIds.contains(messageId)) {
            return;
        }

        if (messageId != null) {
            displayedMessageIds.add(messageId);
        }

        boolean ownMessage = isCurrentUserMessage(message);
        addMessageBubble(message, ownMessage);
    }

    /**
     * تشخیص می‌دهد پیام متعلق به کاربر فعلی است یا طرف مقابل.
     */
    private boolean isCurrentUserMessage(
            ChatMessageResponse message
    ) {
        UserSession session = UserSession.getInstance();

        Long currentUserId = session.getUserId();

        if (currentUserId != null &&
                message.getSenderId() != null) {
            return Objects.equals(
                    currentUserId,
                    message.getSenderId()
            );
        }

        /*
         * اگر userId داخل Session مقداردهی نشده بود،
         * از username استفاده می‌کنیم.
         */
        String currentUsername = session.getUsername();

        return currentUsername != null &&
                message.getSenderUsername() != null &&
                currentUsername.equalsIgnoreCase(
                        message.getSenderUsername()
                );
    }

    /**
     * ساخت ظاهر یک پیام.
     */
    private void addMessageBubble(
            ChatMessageResponse message,
            boolean ownMessage
    ) {
        VBox bubble = new VBox(4);
        bubble.setMaxWidth(500);
        bubble.setPadding(new Insets(9, 14, 9, 14));

        String bubbleStyle;

        if (ownMessage) {
            bubbleStyle =
                    "-fx-background-color: #10b981;" +
                            "-fx-background-radius: 16 16 4 16;";
        } else {
            bubbleStyle =
                    "-fx-background-color: #e2e8f0;" +
                            "-fx-background-radius: 16 16 16 4;";
        }

        bubble.setStyle(bubbleStyle);

        Label senderLabel = new Label(
                ownMessage
                        ? "شما"
                        : safeText(
                        message.getSenderUsername(),
                        "کاربر"
                )
        );

        senderLabel.setStyle(
                ownMessage
                        ? "-fx-text-fill: #d1fae5;" +
                          "-fx-font-size: 11px;"
                        : "-fx-text-fill: #64748b;" +
                          "-fx-font-size: 11px;"
        );

        Label contentLabel = new Label(
                safeText(message.getContent(), "")
        );

        contentLabel.setWrapText(true);
        contentLabel.setMinWidth(Region.USE_PREF_SIZE);
        contentLabel.setNodeOrientation(
                NodeOrientation.RIGHT_TO_LEFT
        );

        contentLabel.setStyle(
                ownMessage
                        ? "-fx-text-fill: white;" +
                          "-fx-font-size: 14px;"
                        : "-fx-text-fill: #1e293b;" +
                          "-fx-font-size: 14px;"
        );

        Label timeLabel = new Label(
                formatDateTime(message.getSentAt())
        );

        timeLabel.setStyle(
                ownMessage
                        ? "-fx-text-fill: #d1fae5;" +
                          "-fx-font-size: 10px;"
                        : "-fx-text-fill: #94a3b8;" +
                          "-fx-font-size: 10px;"
        );

        bubble.getChildren().addAll(
                senderLabel,
                contentLabel,
                timeLabel
        );

        HBox wrapper = new HBox(bubble);
        wrapper.setMaxWidth(Double.MAX_VALUE);

        /*
         * پیام خودمان سمت راست و پیام طرف مقابل سمت چپ.
         */
        wrapper.setAlignment(
                ownMessage
                        ? Pos.CENTER_RIGHT
                        : Pos.CENTER_LEFT
        );

        messageContainer.getChildren().add(wrapper);
        scrollToBottom();
    }

    private void startMessagePolling() {
        stopMessagePolling();

        messagePolling = new Timeline(
                new KeyFrame(
                        Duration.seconds(2),
                        event -> loadMessages(false)
                )
        );

        messagePolling.setCycleCount(
                Timeline.INDEFINITE
        );

        messagePolling.play();
    }

    private void stopMessagePolling() {
        if (messagePolling != null) {
            messagePolling.stop();
            messagePolling = null;
        }
    }

    @FXML
    private void handleBack() {
        controllerActive = false;
        stopMessagePolling();

        NavigationService.switchScene(
                "/fxml/chat/conversation-list-view.fxml",
                "چت‌ها"
        );
    }


    private void updateSendButtonState() {
        boolean invalidConversation =
                conversationId == null;

        boolean emptyMessage =
                messageTextField.getText() == null ||
                        messageTextField.getText().isBlank();

        boolean fieldDisabled =
                messageTextField.isDisabled();

        sendButton.setDisable(
                invalidConversation ||
                        emptyMessage ||
                        fieldDisabled
        );
    }

    private void scrollToBottom() {
        Platform.runLater(() -> {
            scrollPane.applyCss();
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        });
    }

    private String safeText(
            String value,
            String fallback
    ) {
        if (value == null || value.isBlank()) {
            return fallback;
        }

        return value;
    }

    /**
     * فعلاً تاریخ ISO را کمی خواناتر می‌کند.
     * مثال:
     * 2026-07-13T15:20:10 -> 2026-07-13 15:20
     */
    private String formatDateTime(String sentAt) {
        if (sentAt == null || sentAt.isBlank()) {
            return "";
        }

        String formatted = sentAt.replace('T', ' ');

        if (formatted.length() >= 16) {
            return formatted.substring(0, 16);
        }

        return formatted;
    }

    private void runInBackground(
            CheckedTask task,
            String operation
    ) {
        runInBackground(task, operation, null);
    }

    private void runInBackground(
            CheckedTask task,
            String operation,
            Runnable failureCleanup
    ) {
        Thread thread = new Thread(() -> {
            try {
                task.run();
            } catch (Exception exception) {
                exception.printStackTrace();

                Platform.runLater(() -> {
                    if (failureCleanup != null) {
                        failureCleanup.run();
                    }

                    if (controllerActive) {
                        showError(
                                operation + " ناموفق بود:\n" +
                                        getUsefulErrorMessage(exception)
                        );
                    }
                });
            }
        });

        /*
         * Daemon بودن باعث می‌شود Thread مانع بسته‌شدن برنامه نشود.
         */
        thread.setDaemon(true);
        thread.setName(
                "chat-" + operation.replace(" ", "-")
        );
        thread.start();
    }

    private String getUsefulErrorMessage(
            Exception exception
    ) {
        if (exception.getMessage() != null &&
                !exception.getMessage().isBlank()) {
            return exception.getMessage();
        }

        return exception.getClass().getSimpleName();
    }

    private void showError(String message) {
        Alert alert = new Alert(
                Alert.AlertType.ERROR
        );

        alert.setTitle("خطای چت");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FunctionalInterface
    private interface CheckedTask {
        void run() throws Exception;
    }
}
