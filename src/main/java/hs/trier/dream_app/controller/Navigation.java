package hs.trier.dream_app.controller;

import hs.trier.dream_app.Util;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class Navigation {

    @FXML
    private VBox contentPane;

    @FXML
    private void initialize() {
        Util.getInstance().setContentPane(contentPane);
        Util.getInstance().loadFXML("views/new-dream.fxml");
    }

    @FXML
    private void onNewDreamButton() {
        Util.getInstance().loadFXML("views/new-dream.fxml");
    }

    @FXML
    private void onLibraryButton() {
        Util.getInstance().loadFXML("views/library.fxml");
    }

    @FXML
    private void onSymbolsButton() {
        Util.getInstance().loadFXML("views/symbols/symbols.fxml");
    }
}
