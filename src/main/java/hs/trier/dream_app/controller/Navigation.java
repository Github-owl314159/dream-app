package hs.trier.dream_app.controller;

import hs.trier.dream_app.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;

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
        NewDream controller = (NewDream) Util.getInstance().loadFXML("views/new-dream.fxml");
        controller.clear();
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
