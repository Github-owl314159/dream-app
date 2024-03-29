module hs.trier.dream_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.xerial.sqlitejdbc;
    requires unirest.java;
    requires javafx.swing;
    requires javafx.web;
    requires json;
    requires stanford.corenlp;

    opens hs.trier.dream_app to javafx.fxml;
    opens hs.trier.dream_app.model to javafx.fxml;
    opens hs.trier.dream_app.controller to javafx.fxml;
    opens hs.trier.dream_app.model.dao to javafx.fxml;
    exports hs.trier.dream_app;
    exports hs.trier.dream_app.model;
    exports hs.trier.dream_app.model.dao;
}