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

    private static void addStylesheetIfExists(Scene scene, String cssPath) {
        URL uri = NavigationService.class.getResource(cssPath);
        if (uri != null) {
            scene.getStylesheets().add(uri.toExternalForm());
        } else {
            System.err.println("[Navigation] هشدار: فایل CSS پیدا نشد: " + cssPath);
        }
    }

    public static void switchScene(String fxmlPath, String title) {
        switchScene(fxmlPath, title, null);
    }

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


            addStylesheetIfExists(currentScene, "/css/variables.css");
            addStylesheetIfExists(currentScene, "/css/main.css");

            if (fxmlPath.contains("/auth/")) {
                addStylesheetIfExists(currentScene, "/css/auth.css");
            } else if (fxmlPath.contains("/advertisement/")) {
                addStylesheetIfExists(currentScene, "/css/advertisement.css");
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