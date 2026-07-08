package org.example.frontend.shared;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class NavigationService {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }


    public static void switchScene(String fxmlPath, String title) {
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
            Parent root = loader.load(); // اگر کنترلر داشبورد در initialize خطا بدهد، اینجا Exception پرتاب می‌شود.

            Scene currentScene = primaryStage.getScene();
            if (currentScene == null) {
                currentScene = new Scene(root, 1280, 720);
                primaryStage.setScene(currentScene);
            } else {
                currentScene.setRoot(root);
            }

            // پاک کردن استایل‌های قبلی
            currentScene.getStylesheets().clear();

            // اعمال استایل‌های پایه (variables.css)
            URL variablesUri = NavigationService.class.getResource("/css/variables.css");
            if (variablesUri != null) {
                currentScene.getStylesheets().add(variablesUri.toExternalForm());
            }

            // اعمال استایل اختصاصی بر اساس صفحه ای که لود می‌شود
            if (fxmlPath.contains("/auth/")) {
                URL authUri = NavigationService.class.getResource("/css/auth.css");
                if (authUri != null) {
                    currentScene.getStylesheets().add(authUri.toExternalForm());
                }
            }  else if (fxmlPath.contains("/advertisement/")) {
            URL advertisementUri = NavigationService.class.getResource("/css/advertisement.css");
            if (advertisementUri != null) {
                currentScene.getStylesheets().add(advertisementUri.toExternalForm());
            }
            } else if (fxmlPath.contains("/dashboard/")) {
                // اگر فایل css مخصوص داشبورد دارید (مثلا dashboard.css) آن را اینجا لود کنید:
                URL dashboardUri = NavigationService.class.getResource("/css/dashboard.css");
                if (dashboardUri != null) {
                    currentScene.getStylesheets().add(dashboardUri.toExternalForm());
                }
            }

            primaryStage.setTitle(title);
            primaryStage.setMaximized(true);
            primaryStage.setResizable(true);
            primaryStage.show();

            System.out.println("[Navigation] Successfully switched to: " + title);

        } catch (Throwable t) {
            System.err.println("!!! خطا در حین انتقال صفحه به " + fxmlPath + " !!!");
            t.printStackTrace(); // چاپ کامل StackTrace شامل خطاهای NullPointer یا خطای کنترلر مقصد
        }
    }
}
