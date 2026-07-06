package org.example.frontend.shared;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class NavigationService {

    private static Stage primaryStage;

    // این متد در ابتدای اجرای برنامه (کلاس Main یا App) صدا زده می‌شود تا استیج اصلی را ذخیره کند
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * تغییر صفحه فعلی به یک FXML جدید
     * @param fxmlPath مسیر فایل FXML نسبت به پوشه resources (مثلاً "/fxml/auth/register-view.fxml")
     * @param title عنوان پنجره
     */
    public static void switchScene(String fxmlPath, String title) {
        if (primaryStage == null) {
            System.err.println("خطا: PrimaryStage مقداردهی نشده است!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(NavigationService.class.getResource(fxmlPath));
            Parent root = loader.load();

            Scene currentScene = primaryStage.getScene();
            if (currentScene == null) {
                currentScene = new Scene(root, 450, 650);
                primaryStage.setScene(currentScene);
            } else {
                currentScene.setRoot(root);
            }

            primaryStage.setTitle(title);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("خطا در بارگذاری فایل FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
}

