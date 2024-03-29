package hs.trier.dream_app;

import hs.trier.dream_app.model.Database;
import hs.trier.dream_app.model.dao.DreamDAO;
import hs.trier.dream_app.model.dao.SymbolDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static hs.trier.dream_app.model.Database.initializeDatabase;

public class DreamApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // phase: initialize
        SymbolDAO.initialize();
        DreamDAO.initialize();

        Scene scene = new Scene(FXMLLoader.load(Util.getAbsoluteURL("views/navigation.fxml")));
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/hs/trier/dream_app/styles/theme.css")).toExternalForm());

        stage.setTitle("Dream App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        boolean dbStatus = Database.initializeDatabase();

        if (!dbStatus)
            System.exit(-1);

        launch();
    }
}
