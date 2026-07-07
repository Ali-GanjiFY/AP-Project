package org.example.frontend;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.frontend.shared.NavigationService;

import java.net.URL;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        NavigationService.setPrimaryStage(stage);

        URL splashUrl = getClass().getResource("/images/splash.png");
        if (splashUrl == null) {
            throw new IllegalStateException("Splash image not found: /images/splash.png");
        }

        ImageView splashImage = new ImageView(new Image(splashUrl.toExternalForm()));
        splashImage.setPreserveRatio(true);
        splashImage.setFitWidth(520);

        StackPane splashRoot = new StackPane(splashImage);
        splashRoot.setStyle("-fx-background-color: #0d1117;");

        Scene splashScene = new Scene(splashRoot, 1280, 720);
        stage.setScene(splashScene);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.setTitle("ورود به حساب کاربری");
        stage.show();

        splashRoot.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), splashRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition stay = new PauseTransition(Duration.seconds(2));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), splashRoot);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeIn.setOnFinished(e -> stay.play());
        stay.setOnFinished(e -> fadeOut.play());

        fadeOut.setOnFinished(e ->
                NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری")
        );

        fadeIn.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

