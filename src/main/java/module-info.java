module hs.trier.dream_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens hs.trier.dream_app to javafx.fxml;
    opens hs.trier.dream_app.model to javafx.fxml;
    exports hs.trier.dream_app;
    exports hs.trier.dream_app.model;
    exports hs.trier.dream_app.dao;
    opens hs.trier.dream_app.dao to javafx.fxml;
}