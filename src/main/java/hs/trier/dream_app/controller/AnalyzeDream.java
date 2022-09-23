package hs.trier.dream_app.controller;

import hs.trier.dream_app.dao.SymbolDAO;
import hs.trier.dream_app.model.AnalyzedToken;
import hs.trier.dream_app.model.Dream;
import hs.trier.dream_app.model.NLP;
import hs.trier.dream_app.model.Symbol;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeDream
{
    @FXML
    private TableView<Symbol> matchesTableView;
    @FXML
    private Label titleLabel;
    @FXML
    private Button getDeepDream;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private WebView dreamContent = new WebView();
   // WebEngine webEngine = webView.getEngine();

    private ObservableList<Symbol> matches;

    @FXML
    private void initialize() {
        // init list for found matches
        matches = FXCollections.observableArrayList();

        // set event listener
        getDeepDream.setOnAction(this::getDeepDream);

        // create table columns and set bindings
        TableColumn<Symbol, String> nameTableColumn = new TableColumn<>("Name");
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // add columns to table
        matchesTableView.getColumns().addAll(nameTableColumn);

        // sorting
        nameTableColumn.setSortable(true);
        nameTableColumn.setSortType(TableColumn.SortType.ASCENDING);
        matchesTableView.getSortOrder().add(nameTableColumn);
        matchesTableView.sort();

        // callback: populate preview fields whenever selected item changes
        matchesTableView.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            descriptionTextArea.setText(nv.getDescription());
        });


    }

    public void analyze(Dream selectedItem) {
        // clear list
        matches.clear();

        // set title
        titleLabel.setText(selectedItem.getTitle());

        // tokenize content of selected dream
        String[] tokens = NLP.tokenize(selectedItem.getContent());

        // get tokens to search for
        List<AnalyzedToken> filteredTokens = NLP.lemmatize(NLP.filter(tokens));

        StringBuilder sb = new StringBuilder();
        Boolean foundsomething = false;
        for ( String token : tokens) {
            foundsomething = false;
            for (AnalyzedToken analyzedToken : filteredTokens) {

                if (token.toLowerCase().equals(analyzedToken.getToken())) {

                    List<Symbol> foundSymbols = SymbolDAO.searchSymbols(analyzedToken.getLemma());
                    if (!foundSymbols.isEmpty()) {
                        foundsomething = true;
                        sb.append("<span style=\"color: red;\">" + token + "</span>");
                        for (Symbol symbol : foundSymbols) {
                            matches.add(symbol);
                        }
                    }
                }
            }
            if (!foundsomething) {
                sb.append(token);
            }
        }

        matchesTableView.setItems(matches);
        dreamContent.getEngine().loadContent(sb.toString(),"text/html");
        System.out.println(sb.toString());
    }

    private void getDeepDream(ActionEvent actionEvent) {
    }
}
