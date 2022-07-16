package hs.trier.dream_app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class Navigation {

    @FXML
    private VBox contentPane;

    public Navigation() {
    }

    @FXML
    private void initialize() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hs/trier/dream_app/page.fxml"));

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
        loadFXML("/hs/trier/dream_app/page.fxml");
    }

    @FXML
    private void onLibraryButton() {
        loadFXML("/hs/trier/dream_app/library.fxml");
    }

    @FXML
    private void onSymbolsButton() {
        loadFXML("/hs/trier/dream_app/symbols.fxml");
    }

    private void loadFXML(String fxml) {
        Node root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }
}
