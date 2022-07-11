module hs.trier.dream_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.xerial.sqlitejdbc;
    requires unirest.java;
    requires com.google.gson;
    requires javafx.swing;

    opens hs.trier.dream_app to javafx.fxml;
    opens hs.trier.dream_app.model to javafx.fxml;
    opens hs.trier.dream_app.dao to javafx.fxml;
    opens hs.trier.dream_app.api to com.google.gson;
    exports hs.trier.dream_app;
    exports hs.trier.dream_app.model;
    exports hs.trier.dream_app.dao;
}