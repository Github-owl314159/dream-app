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

    public Navigation() {
    }

    @FXML
    private void initialize() {
        FXMLLoader loader = new FXMLLoader(Util.getAbsoluteURL("views/page.fxml"));

        Node page;
        try {
            page = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        contentPane.getChildren().clear();
        contentPane.getChildren().add(page);
    }

    @FXML
    private void onPageButton() {
        loadFXML("views/page.fxml");
    }

    @FXML
    private void onLibraryButton() {
        loadFXML("views/library.fxml");
    }

    @FXML
    private void onSymbolsButton() {
        loadFXML("views/symbols/symbols.fxml");
    }

    private void loadFXML(String fxml) {
        Node root;
        try {
            root = FXMLLoader.load(Util.getAbsoluteURL(fxml));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }
}
