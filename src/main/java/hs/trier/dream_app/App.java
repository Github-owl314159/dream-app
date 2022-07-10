package hs.trier.dream_app;

import hs.trier.dream_app.views.AnalyzeDreamView;
import hs.trier.dream_app.views.DreamDiaryView;
import hs.trier.dream_app.views.SymbolsView;
import hs.trier.dream_app.views.EditDreamView;
import hs.trier.dream_app.views.HelpView;
import hs.trier.dream_app.views.MainView;
import hs.trier.dream_app.views.NewDreamView;
import hs.trier.dream_app.views.SearchView;
import hs.trier.dream_app.views.SettingsView;
import hs.trier.dream_app.views.ShowDreamView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static hs.trier.dream_app.Database.initializeDatabase;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Presenter presenter = Presenter.getPresenter();

        AnalyzeDreamView analyzeDreamView = new AnalyzeDreamView();
        presenter.setAnalyzeDreamView(analyzeDreamView);

        DreamDiaryView dreamDiaryView = new DreamDiaryView();
        presenter.setDreamDiaryView(dreamDiaryView);

        SymbolsView symbolsView = new SymbolsView();
        presenter.setDreamSymbolsView(symbolsView);

        HelpView helpView = new HelpView();
        presenter.setHelpView(helpView);

        MainView mainView = new MainView();
        presenter.setMainView(mainView);

        NewDreamView newDreamView = new NewDreamView();
        presenter.setNewDreamView(newDreamView);

        SearchView searchView = new SearchView();
        presenter.setSearchView(searchView);

        SettingsView settingsView = new SettingsView();
        presenter.setSettingsView(settingsView);

        ShowDreamView showDreamView = new ShowDreamView();
        presenter.setShowDreamView(showDreamView);

        EditDreamView editDreamView = new EditDreamView();
        presenter.setEditDreamView(editDreamView);

        presenter.onNewDream();

        Scene scene = new Scene(mainView.getRoot(), 1280, 800);
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
