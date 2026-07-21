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
    requires com.google.gson;
    requires java.desktop;

    opens org.example.frontend to javafx.fxml;

    opens org.example.frontend.chat to com.google.gson, javafx.fxml;
    exports org.example.frontend.chat;

    opens org.example.frontend.auth to javafx.fxml;

    opens org.example.frontend.shared to javafx.fxml;

    opens org.example.frontend.dashboard to com.google.gson, javafx.fxml;

    opens org.example.frontend.advertisement to com.google.gson, javafx.fxml;

    exports org.example.frontend;
    exports org.example.frontend.auth;
    exports org.example.frontend.shared;
    exports org.example.frontend.dashboard;
    exports org.example.frontend.advertisement;
}
