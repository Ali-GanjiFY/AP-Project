package org.example.frontend.dashboard;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.frontend.advertisement.AdvertisementDetail;
import org.example.frontend.advertisement.RatingService;
import org.example.frontend.shared.NavigationService; // Import NavigationService
import org.example.frontend.shared.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import org.example.frontend.chat.ChatService;
import org.example.frontend.chat.ConversationResponse; // Import ConversationResponse

public class AdDetailController implements Initializable {

    private static final String SERVER_BASE_URL = "http://localhost:8080";

    private static AdvertisementDetail selectedAdvertisement;

    @FXML private Label titleLabel;
    @FXML private Label priceLabel;
    @FXML private Label categoryLabel;
    @FXML private Label cityLabel;
    @FXML private Label statusLabel;
    @FXML private Label imageStatusLabel;
    @FXML private ImageView mainImageView;
    @FXML private Label imageCounterLabel;
    @FXML private Button prevImageButton;
    @FXML private Button nextImageButton;
    @FXML private Label ownerLabel;
    @FXML private TextArea descriptionArea;
    @FXML private Label sellerAverageRatingLabel;
    @FXML private Label sellerRatingCountLabel;
    @FXML private VBox reviewFormBox;
    @FXML private ComboBox<Integer> scoreComboBox;
    @FXML private TextArea commentArea;
    @FXML private Button submitReviewButton;
    @FXML private Label reviewFormStatusLabel;
    @FXML private Label reviewsStatusLabel;
    @FXML private VBox reviewsListContainer;

    // دکمه شروع گفتگو - مطمئن شو در FXML تعریف شده و به این متد وصل است
    @FXML private Button startChatButton;

    private final RatingService ratingService = new RatingService();
    private List<AdvertisementDetail.ImageInfo> adImages;
    private int currentImageIndex = 0;

    // Alert helper methods (assuming they are defined in this class or a base class)
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void setSelectedAdvertisement(AdvertisementDetail advertisement) {
        selectedAdvertisement = advertisement;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (selectedAdvertisement == null) {
            // ... (initialization code for no advertisement found - unchanged)
            titleLabel.setText("آگهی پیدا نشد");
            priceLabel.setText("-");
            categoryLabel.setText("-");
            cityLabel.setText("-");
            statusLabel.setText("-");
            ownerLabel.setText("-");
            imageStatusLabel.setText("اطلاعاتی موجود نیست");
            descriptionArea.setText("هیچ اطلاعاتی برای این آگهی دریافت نشد.");
            sellerAverageRatingLabel.setText("میانگین امتیاز فروشنده: -");
            sellerRatingCountLabel.setText("تعداد نظرات: -");
            reviewFormBox.setVisible(false);
            reviewFormBox.setManaged(false);
            reviewsStatusLabel.setText("");
            // Disable chat button if no ad is selected or user is not logged in
            if (startChatButton != null) {
                startChatButton.setDisable(true);
            }
            return;
        }

        // ... (rest of the initialization code for displaying ad details - unchanged)
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

        setupRatingsSection();

        // Enable chat button only if user is logged in and not the owner
        boolean loggedIn = UserSession.getInstance().getToken() != null;
        boolean isOwner = selectedAdvertisement.isOwnedByCurrentUser();
        if (startChatButton != null) {
            startChatButton.setDisable(!loggedIn || isOwner);
        }
    }

    private void setupRatingsSection() {
        Double avg = selectedAdvertisement.getSellerAverageRating();
        Long count = selectedAdvertisement.getSellerRatingCount();
        sellerAverageRatingLabel.setText(avg != null
                ? String.format("میانگین امتیاز فروشنده: %.1f از ۵", avg)
                : "میانگین امتیاز فروشنده: بدون امتیاز");
        sellerRatingCountLabel.setText("تعداد نظرات: " + (count != null ? count : 0));

        scoreComboBox.getItems().setAll(1, 2, 3, 4, 5);
        scoreComboBox.getSelectionModel().select(Integer.valueOf(5));

        boolean loggedIn = UserSession.getInstance().getToken() != null;
        boolean isOwner = selectedAdvertisement.isOwnedByCurrentUser();

        if (!loggedIn) {
            reviewFormBox.setVisible(false);
            reviewFormBox.setManaged(false);
        } else if (isOwner) {
            reviewFormBox.setVisible(false);
            reviewFormBox.setManaged(false);
        } else {
            reviewFormBox.setVisible(true);
            reviewFormBox.setManaged(true);
        }

        loadReviews();
    }

    private void loadReviews() {
        reviewsStatusLabel.setText("در حال بارگذاری نظرات...");
        Long adId = selectedAdvertisement.getId();

        new Thread(() -> {
            List<RatingService.RatingDto> reviews = ratingService.getRatingsByAdvertisement(adId);

            Platform.runLater(() -> {
                reviewsListContainer.getChildren().clear();

                if (reviews.isEmpty()) {
                    reviewsStatusLabel.setText("هنوز هیچ نظری برای این آگهی ثبت نشده است.");
                    return;
                }

                reviewsStatusLabel.setText("تعداد " + reviews.size() + " نظر ثبت‌شده:");

                String currentUsername = UserSession.getInstance().getUsername();
                boolean alreadyReviewed = false;
                for (RatingService.RatingDto review : reviews) {
                    reviewsListContainer.getChildren().add(buildReviewCard(review));
                    if (currentUsername != null && currentUsername.equals(review.getBuyerUsername())) {
                        alreadyReviewed = true;
                    }
                }

                if (alreadyReviewed) {
                    reviewFormBox.setVisible(false);
                    reviewFormBox.setManaged(false);
                }
            });
        }).start();
    }

    private VBox buildReviewCard(RatingService.RatingDto review) {
        VBox card = new VBox(6);
        card.setStyle("-fx-background-color: #f8fafa; -fx-background-radius: 8; " // Changed to a softer color
                + "-fx-border-color: #e2e8f0; -fx-border-radius: 8; -fx-padding: 10;");

        Label headerLabel = new Label(safeText(review.getBuyerUsername(), "کاربر")
                + "  —  امتیاز: " + review.getScore() + " / ۵");
        headerLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #d97706;");
        card.getChildren().add(headerLabel);

        String comment = review.getComment();
        if (comment != null && !comment.isBlank()) {
            Label commentLabel = new Label(comment);
            commentLabel.setWrapText(true);
            commentLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #334155;");
            card.getChildren().add(commentLabel);
        }

        return card;
    }

    @FXML
    private void handleSubmitReview() {
        Integer score = scoreComboBox.getValue();
        if (score == null) {
            reviewFormStatusLabel.setText("لطفاً یک امتیاز انتخاب کنید.");
            return;
        }

        String token = UserSession.getInstance().getToken();
        if (token == null) {
            reviewFormStatusLabel.setText("برای ثبت نظر ابتدا وارد حساب کاربری خود شوید.");
            return;
        }

        String comment = commentArea.getText();
        Long adId = selectedAdvertisement.getId();

        submitReviewButton.setDisable(true);
        reviewFormStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");
        reviewFormStatusLabel.setText("در حال ثبت نظر...");

        new Thread(() -> {
            String outcome = ratingService.createRating(token, adId, score, comment);

            Platform.runLater(() -> {
                submitReviewButton.setDisable(false);
                if ("SUCCESS".equals(outcome)) {
                    reviewFormStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #16a34a;");
                    reviewFormStatusLabel.setText("نظر شما با موفقیت ثبت شد.");
                    commentArea.clear();
                    reviewFormBox.setVisible(false);
                    reviewFormBox.setManaged(false);
                    loadReviews();
                } else {
                    reviewFormStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #dc2626;");
                    reviewFormStatusLabel.setText(outcome);
                }
            });
        }).start();
    }

    @FXML
    private void handleBack() {
        NavigationService.switchScene("/fxml/dashboard/dashboard-view.fxml", "داشبورد");
    }

    // --- Updated handleStartChat method ---
    @FXML
    private void handleStartChat() {
        String token = UserSession.getInstance().getToken();
        if (token == null || token.isBlank()) {
            showError("خطا", "لطفاً ابتدا وارد حساب کاربری خود شوید.");
            // Optionally navigate to login page here
            // NavigationService.switchScene("/fxml/login/login-view.fxml", "ورود");
            return;
        }

        if (selectedAdvertisement == null || selectedAdvertisement.getId() == null) {
            showError("خطا", "شناسه آگهی معتبر نیست.");
            return;
        }

        // Disable button to prevent multiple clicks
        startChatButton.setDisable(true);

        new Thread(() -> {
            try {
                ChatService chatService = new ChatService();
                ConversationResponse conversation = chatService.startOrGetConversation(selectedAdvertisement.getId());

                Platform.runLater(() -> {
                    if (conversation != null && conversation.getId() != null) {
                        // Successfully got conversation ID, now navigate
                        System.out.println("Conversation started/retrieved. ID: " + conversation.getId());
                        // TODO: Pass conversation.getId() to ChatController
                        // NavigationService.switchScene("/fxml/chat/chat-view.fxml", "گفتگو", conversation.getId());
                        showInfo("موفق", "گفتگو با موفقیت آغاز شد. در حال انتقال به صفحه چت...");
                    } else {
                        showError("خطا", "دریافت اطلاعات گفتگو ناموفق بود.");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace(); // Log the exception for debugging
                Platform.runLater(() -> {
                    showError("خطا", "شروع گفتگو ناموفق بود: " + e.getMessage());
                    startChatButton.setDisable(false); // Re-enable button on error
                });
            }
        }).start();
    }
    // --- End of updated handleStartChat ---


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
