package hs.trier.dream_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static hs.trier.dream_app.Database.initializeDatabase;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
//        Presenter presenter = Presenter.getPresenter();
//
//        AnalyzeDreamView analyzeDreamView = new AnalyzeDreamView();
//        presenter.setAnalyzeDreamView(analyzeDreamView);
//
//        DreamDiaryView dreamDiaryView = new DreamDiaryView();
//        presenter.setDreamDiaryView(dreamDiaryView);
//
//        SymbolsView symbolsView = new SymbolsView();
//        presenter.setDreamSymbolsView(symbolsView);
//
//        HelpView helpView = new HelpView();
//        presenter.setHelpView(helpView);
//
//        MainView mainView = new MainView();
//        presenter.setMainView(mainView);
//
//        NewDreamView newDreamView = new NewDreamView();
//        presenter.setNewDreamView(newDreamView);
//
//        SearchView searchView = new SearchView();
//        presenter.setSearchView(searchView);
//
//        SettingsView settingsView = new SettingsView();
//        presenter.setSettingsView(settingsView);
//
//        ShowDreamView showDreamView = new ShowDreamView();
//        presenter.setShowDreamView(showDreamView);
//
//        EditDreamView editDreamView = new EditDreamView();
//        presenter.setEditDreamView(editDreamView);
//
//        presenter.onNewDream();


        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("navigation.fxml"))));
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/hs/trier/dream_app/styles/theme.css")).toExternalForm());

        stage.setTitle("Dream App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        boolean dbStatus = initializeDatabase();

        if (!dbStatus)
            System.exit(-1);

        launch();
    }
}
