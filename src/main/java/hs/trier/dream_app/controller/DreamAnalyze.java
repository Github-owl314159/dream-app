package hs.trier.dream_app.controller;

import hs.trier.dream_app.dao.SymbolDAO;
import hs.trier.dream_app.model.Dream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.web.HTMLEditor;

import java.util.ArrayList;
import java.util.List;

public class DreamAnalyze {
    @FXML
    private ListView<String> matchesListView;
    @FXML
    private Label titleLabel;
    @FXML
    private HTMLEditor contentHTMLEditor;

    @FXML
    private void initialize() {
    }

    public void analyze(Dream selectedItem) {
        titleLabel.setText(selectedItem.getTitle());

        String content = selectedItem.getContent();
        String[] tokens = content.split(" ");

        ObservableList<String> matches = FXCollections.observableArrayList();
        StringBuilder sb = new StringBuilder();
        for (String token : tokens) {
            if (SymbolDAO.symbolExistsIgnoreCase(token)) {
                matches.add(token.toUpperCase());
                token = "<span style=\"color: red;\">" + token + "</span>";
            }
            sb.append(token).append(" ");
        }
        matchesListView.setItems(matches);

        matches.forEach(System.out::println);
        contentHTMLEditor.setDisable(true);
        contentHTMLEditor.setHtmlText(sb.toString());
    }
}
