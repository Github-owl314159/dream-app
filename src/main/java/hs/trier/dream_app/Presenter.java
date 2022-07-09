package hs.trier.dream_app;

import hs.trier.dream_app.model.*;
import hs.trier.dream_app.views.*;
import hs.trier.dream_app.views.AnalyzeDreamView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.Optional;

public class Presenter {
    private static Presenter presenter = null; // Singleton

    private AnalyzeDreamView analyzeDreamView;
    private DreamDiaryView dreamDiaryView;
    private SymbolsView symbolsView;
    private HelpView helpView;
    private MainView mainView;
    private NewDreamView newDreamView;
    private SearchView searchView;
    private SettingsView settingsView;
    private ShowDreamView showDreamView;
    private EditDreamView editDreamView;

    private Presenter() {
    }

// ============================== Getter ==============================

    public static Presenter getPresenter() {
        if (presenter == null) {
            presenter = new Presenter();
        }
        return presenter;
    }

// ============================== Setter ==============================

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public void setNewDreamView(NewDreamView newDreamView) {
        this.newDreamView = newDreamView;
    }

    public void setShowDreamView(ShowDreamView showDreamView) {
        this.showDreamView = showDreamView;
    }

    public void setEditDreamView(EditDreamView editDreamView) {
        this.editDreamView = editDreamView;
    }

    public void setAnalyzeDreamView(AnalyzeDreamView analyzeDreamView) {
        this.analyzeDreamView = analyzeDreamView;
    }

    public void setDreamDiaryView(DreamDiaryView dreamDiaryView) {
        this.dreamDiaryView = dreamDiaryView;
    }

    public void setDreamSymbolsView(SymbolsView symbolsView) {
        this.symbolsView = symbolsView;
    }

    public void setHelpView(HelpView helpView) {
        this.helpView = helpView;
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    public void setSettingsView(SettingsView settingsView) {
        this.settingsView = settingsView;
    }

// ============================== Rest ==============================

    public void showInStatusBar(String message) {
        mainView.showInStatusBar(message);
    }

    public void onNewDream() {
        mainView.setContent(newDreamView.getContent());
        mainView.setMetaContent(newDreamView.getMetaContent());
        mainView.setTitle("New Dream");
    }

    public void onDreamDiary() {
        mainView.resetView();
        dreamDiaryView.resetPreview();
        mainView.setContent(dreamDiaryView.getContent());
        mainView.setMetaContent(dreamDiaryView.getMetaContent());
        mainView.setTitle("Dream Diary");
        dreamDiaryView.refreshDreamList();                                //TODO: Schmutzig, fixme
    }

    public void onShowDream(int id) {
        mainView.resetView();
        Optional<Dream> optionalDream = DreamDAO.getDream(id);
        showDreamView.showDream(optionalDream.get());
        mainView.setContent(showDreamView.getContent());
        mainView.setMetaContent(showDreamView.getMetaContent());
        mainView.setTitle("Show Dream");
    }

    public void onEditDream() {
        mainView.resetView();
        Dream dream = showDreamView.getCurrentDream();
        editDreamView.showDream(dream);
        mainView.setContent(editDreamView.getContent());
        mainView.setMetaContent(editDreamView.getMetaContent());
        mainView.setTitle("Edit Dream");
    }

    public void onSaveDream() {
        Dream dream = newDreamView.getCurrentDream();
        DreamDAO.create(
                dream.getTitle(),
                dream.getText(),
                dream.getNotes(),
                dream.getMood()
        );
//        model.saveDream(dream);
        newDreamView.resetView();
//        dream = model.getLatestDream();
        showDreamView.showDream(dream);
        mainView.setContent(showDreamView.getContent());
        mainView.setMetaContent(showDreamView.getMetaContent());
        mainView.setTitle("Show Dream");
        mainView.showInStatusBar("Dream '" + dream.getTitle() + "' saved.");
    }

    public void onUpdateDream() {
        Dream dream = editDreamView.getCurrentDream();
//        model.updateDream(dream);
        DreamDAO.update(dream);
        showDreamView.showDream(dream);
        mainView.setContent(showDreamView.getContent());
        mainView.setMetaContent(showDreamView.getMetaContent());
        mainView.showInStatusBar("Dream '" + dream.getTitle() + "' updated.");
    }

    public void onDeleteDream(int id, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm");
        alert.setHeaderText("Delete this Dream?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
//            model.deleteDream(id, title);
            DreamDAO.delete(id);
            onDreamDiary();
            mainView.showInStatusBar("Dream '" + title + "' deleted.");
        }
    }

    public void onSaveSymbol(String name, String descr) {
        SymbolDAO.create(name, descr);
        mainView.showInStatusBar("Dream Symbol '" + name + "' saved.");
        symbolsView.showSymbols(SymbolDAO.getSymbols());
    }

    public void onUpdateDreamSymbol(Symbol symbol) {
        mainView.showInStatusBar("Dream Symbol '" + symbol.getName() + "' updated.");
        symbolsView.showSymbols(SymbolDAO.getSymbols());
    }

    public Boolean onDeleteDreamSymbol(Symbol symbol) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please Confirm");
        alert.setHeaderText("Delete the Dream Symbol '" + symbol + "'?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SymbolDAO.delete(symbol.getId());
            mainView.showInStatusBar("Dream Symbol '" + symbol.getName() + "' deleted.");
            return true;
        } else {
            return false;
        }
    }

    public void onAnalyzeDream(int id) {
        mainView.resetView();
        Optional<Dream> dream = DreamDAO.getDream(id);
//        model.testAnalyzeDream(dream);
        mainView.setContent(analyzeDreamView.getContent());
        mainView.setMetaContent(analyzeDreamView.getMetaContent());
        mainView.setTitle("Analyze Dream");
    }

    public void showAnalyzedDream(ArrayList<Symbol> foundSymbols, String text, String token, String tokenWithoutDupes, String tokenWithoutStopwords, String stemmedToken) {
        analyzeDreamView.showAnalyzedDream(foundSymbols, text, token, tokenWithoutDupes, tokenWithoutStopwords, stemmedToken);
    }


    // ============================== Stubs ==============================  //TODO: Views fertig machen, wenn wir wissen was reinsoll

    public void onSearch() {
        mainView.resetView();
        mainView.setContent(searchView.getContent());
        mainView.setTitle("Search");
    }

    public void onSymbols() {
        mainView.resetView();
        mainView.setContent(symbolsView.getContent());
        mainView.setMetaContent(symbolsView.getMetaContent());
        mainView.setTitle("Dream Symbols");
        symbolsView.showSymbols(SymbolDAO.getSymbols());
    }

    public void onHelp() {
        mainView.resetView();
        mainView.setContent(helpView.getContent());
        mainView.setTitle("Help");
    }

    public void onSettings() {
        mainView.resetView();
        mainView.setContent(settingsView.getContent());
        mainView.setTitle("Options");
    }
}