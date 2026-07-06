module org.example.frontend {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;

    // باز کردن پکیج اصلی برای بارگذاری FXMLهای عمومی
    opens org.example.frontend to javafx.fxml;

    // باز کردن پکیج احراز هویت برای دسترسی کنترلرهای Login و Register توسط لودر FXML
    opens org.example.frontend.auth to javafx.fxml;

    // باز کردن پکیج کدهای مشترک (مثل سرویس ناوبری) در صورت ارجاع متقابل از فایل‌های FXML
    opens org.example.frontend.shared to javafx.fxml;

    // اکسپورت کردن پکیج‌ها جهت استفاده و دسترسی ران‌تایم جاوااف‌ایکس
    exports org.example.frontend;
    exports org.example.frontend.auth;
    exports org.example.frontend.shared;
}
