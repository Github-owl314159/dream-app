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

import java.util.ArrayList;
import java.util.List;

public class AnalyzeDream
{
    @FXML
    private TableView<Symbol> matchesTableView;
    @FXML
    private Label titleLabel;
    @FXML
    private HTMLEditor contentHTMLEditor;
    @FXML
    private Button getDeepDream;
    @FXML
    private TextArea descriptionTextArea;

    private ObservableList<Symbol> matches;

    @FXML
    private void initialize() {
        contentHTMLEditor.setDisable(true);
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
            for (AnalyzedToken analyzedToken : filteredTokens) {
                if (token.toLowerCase().equals(analyzedToken.getToken())) {
                    List<Symbol> temp = SymbolDAO.searchSymbols(analyzedToken.getLemma());
                    for (Symbol symbol : temp) {
                        matches.add(symbol);
                        foundsomething = true;
                    }
                    sb.append("<span style=\"color: red;\">" + token + "</span>");
                }
            }
            if (foundsomething) {
                sb.append(token);
                foundsomething = false;
            }
        }

        matchesTableView.setItems(matches);

        contentHTMLEditor.setHtmlText(sb.toString());
    }

    private void getDeepDream(ActionEvent actionEvent) {
    }
}
