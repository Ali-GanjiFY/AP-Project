package org.example.frontend.shared;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

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
     * تغییر صفحه فعلی به یک FXML جدید به همراه اعمال خودکار تم‌ها
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
                // سایز اولیه پنجره (می‌توانید به دلخواه تغییر دهید، مثلاً 800 در 600)
                currentScene = new Scene(root, 800, 600);
                primaryStage.setScene(currentScene);
            } else {
                currentScene.setRoot(root);
            }

            // پاک کردن استایل‌های قبلی برای جلوگیری از تداخل استایل صفحات مختلف
            currentScene.getStylesheets().clear();

            // لود کردن و اعمال فایل‌های CSS به صورت پویا و امن
            URL variablesUri = NavigationService.class.getResource("/css/variables.css");
            URL authUri = NavigationService.class.getResource("/css/auth.css");

            if (variablesUri != null) {
                currentScene.getStylesheets().add(variablesUri.toExternalForm());
            } else {
                System.err.println("هشدار: فایل variables.css در مسیر resources/css یافت نشد!");
            }

            if (authUri != null) {
                currentScene.getStylesheets().add(authUri.toExternalForm());
            } else {
                System.err.println("هشدار: فایل auth.css در مسیر resources/css یافت نشد!");
            }

            primaryStage.setTitle(title);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("خطا در بارگذاری فایل FXML: " + fxmlPath);
            e.printStackTrace();
        }
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);

    }
}
