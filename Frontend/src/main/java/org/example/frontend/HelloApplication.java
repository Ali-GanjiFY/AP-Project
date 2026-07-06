package org.example.frontend; // پکیج اصلی فرانت‌اند شما

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.frontend.shared.NavigationService;

public class HelloApplication extends Application {

    @Override  
    public void start(Stage stage) {
        // ثبت استیج اصلی در سرویس ناوبری
        NavigationService.setPrimaryStage(stage);

        // باز کردن صفحه لاگین به عنوان صفحه شروع
        NavigationService.switchScene("/fxml/auth/login-view.fxml", "ورود به حساب کاربری");

        stage.setWidth(450);
        stage.setHeight(650);
        stage.setResizable(false); // ثابت نگه داشتن اندازه پنجره برای فرم‌های ورود/ثبت‌نام
    }

    public static void main(String[] args) {
        launch(args);
    }
}
