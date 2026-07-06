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

        // ۱. بارگذاری تصویر اسپلش
        URL splashUrl = getClass().getResource("/images/splash.png");
        if (splashUrl == null) {
            throw new IllegalStateException("Splash image not found: /images/splash.png");
        }

        ImageView splashImage = new ImageView(new Image(splashUrl.toExternalForm()));

        // حفظ نسبت ابعاد تصویر (جلوگیری از کش آمدن و تغییر شکل غیرعادی)
        splashImage.setPreserveRatio(true);

        // ۲. قرار دادن تصویر در یک StackPane برای تراز شدن در مرکز و اعمال رنگ پس‌زمینه هماهنگ
        StackPane splashRoot = new StackPane(splashImage);
        splashRoot.setStyle("-fx-background-color: #0d1117;");

        // ۳. ساخت Scene با ابعاد اولیه
        Scene splashScene = new Scene(splashRoot, 1280, 720);

        // ۴. اتصال ابعاد تصویر به ابعاد Scene به‌صورت پویا و واکنش‌گرا
        splashImage.fitWidthProperty().bind(splashScene.widthProperty());
        splashImage.fitHeightProperty().bind(splashScene.heightProperty());

        // ۵. تنظیمات اولیه Stage
        stage.setScene(splashScene);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.setTitle("ورود به حساب کاربری");
        stage.show();

        // ۶. پیاده‌سازی انیمیشن‌های ورود و خروج (Fade Transitions)
        splashRoot.setOpacity(0);

        // انیمیشن ظاهر شدن (Fade In)
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), splashRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // وقفه ۲ ثانیه‌ای در حالت نمایان کامل
        PauseTransition stay = new PauseTransition(Duration.seconds(2));

        // انیمیشن محو شدن (Fade Out)
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), splashRoot);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        // مدیریت زنجیره اجرای انیمیشن‌ها
        fadeIn.setOnFinished(e -> stay.play());
        stay.setOnFinished(e -> fadeOut.play());

        // انتقال به صفحه ورود پس از پایان انیمیشن محو شدن
        fadeOut.setOnFinished(e ->
                NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری")
        );

        fadeIn.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


