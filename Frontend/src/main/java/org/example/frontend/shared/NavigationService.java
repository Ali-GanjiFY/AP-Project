package org.example.frontend.shared;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.function.Consumer;

public class NavigationService {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    // متد استاندارد قدیمی برای سازگاری با بقیه بخش‌های برنامه
    public static void switchScene(String fxmlPath, String title) {
        switchScene(fxmlPath, title, null);
    }

    // متد جنریک جدید برای ارسال داده‌ها و کنترلر به صفحه مقصد
    public static <T> void switchScene(String fxmlPath, String title, Consumer<T> controllerInitializer) {
        if (primaryStage == null) {
            System.err.println("خطا: PrimaryStage مقداردهی نشده است! ابتدا setPrimaryStage را صدا بزنید.");
            return;
        }

        try {
            System.out.println("[Navigation] Attempting to load FXML: " + fxmlPath);

            URL fxmlUrl = NavigationService.class.getResource(fxmlPath);
            if (fxmlUrl == null) {
                throw new IllegalArgumentException("فایل FXML در مسیر مشخص شده پیدا نشد: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // اگر نیاز به مقداردهی اولیه کنترلر باشد
            if (controllerInitializer != null) {
                T controller = loader.getController();
                controllerInitializer.accept(controller);
            }

            Scene currentScene = primaryStage.getScene();
            if (currentScene == null) {
                currentScene = new Scene(root, 1280, 720);
                primaryStage.setScene(currentScene);
            } else {
                currentScene.setRoot(root);
            }

            currentScene.getStylesheets().clear();

            URL variablesUri = NavigationService.class.getResource("/css/variables.css");
            if (variablesUri != null) {
                currentScene.getStylesheets().add(variablesUri.toExternalForm());
            }

            if (fxmlPath.contains("/auth/")) {
                URL authUri = NavigationService.class.getResource("/css/auth.css");
                if (authUri != null) {
                    currentScene.getStylesheets().add(authUri.toExternalForm());
                }
            } else if (fxmlPath.contains("/advertisement/")) {
                URL advertisementUri = NavigationService.class.getResource("/css/advertisement.css");
                if (advertisementUri != null) {
                    currentScene.getStylesheets().add(advertisementUri.toExternalForm());
                }
            } else if (fxmlPath.contains("/dashboard/")) {
                URL dashboardUri = NavigationService.class.getResource("/css/dashboard.css");
                if (dashboardUri != null) {
                    currentScene.getStylesheets().add(dashboardUri.toExternalForm());
                }
            } else if (fxmlPath.contains("/chat/")) {
                // در صورت تمایل به داشتن استایل اختصاصی چت، این فایل را بعداً می‌توانید بسازید
                URL chatUri = NavigationService.class.getResource("/css/chat.css");
                if (chatUri != null) {
                    currentScene.getStylesheets().add(chatUri.toExternalForm());
                }
            }

            primaryStage.setTitle(title);
            primaryStage.setMaximized(true);
            primaryStage.setResizable(true);
            primaryStage.show();

            System.out.println("[Navigation] Successfully switched to: " + title);

        } catch (Throwable t) {
            System.err.println("!!! خطا در حین انتقال صفحه به " + fxmlPath + " !!!");
            t.printStackTrace();
        }
    }
}
