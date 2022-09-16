package hs.trier.dream_app.controller;

import hs.trier.dream_app.dao.SymbolDAO;
import hs.trier.dream_app.model.Dream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.web.HTMLEditor;

public class DreamAnalyze {
    @FXML
    private ListView<String> matchesListView;
    @FXML
    private Label titleLabel;
    @FXML
    private HTMLEditor contentHTMLEditor;
    @FXML
    private Button getDeepDream;
    private ObservableList<String> matches;

    @FXML
    private void initialize() {
        contentHTMLEditor.setDisable(true);
        // init list for found matches
        matches = FXCollections.observableArrayList();

        // set event listener
        getDeepDream.setOnAction(this::getDeepDream);
    }

    public void analyze(Dream selectedItem) {
        // clear list
        matches.clear();

        // set title
        titleLabel.setText(selectedItem.getTitle());

        // get content of dream
        String content = selectedItem.getContent();
        // split content into words
        String[] tokens = content.split(" ");

        StringBuilder sb = new StringBuilder();
        for (String token : tokens) {
            // found a match?
            if (SymbolDAO.symbolExistsIgnoreCase(token)) {
                // is found match NOT a duplicate?
                if (!matches.contains(token.toUpperCase())) {
                    matches.add(token.toUpperCase());
                }
                // inject some html for colorizing within the html editor
                token = "<span style=\"color: red;\">" + token + "</span>";
            }
            sb.append(token).append(" ");
        }
        matchesListView.setItems(matches);

        contentHTMLEditor.setHtmlText(sb.toString());
    }

    private void getDeepDream(ActionEvent actionEvent) {
    }
}
