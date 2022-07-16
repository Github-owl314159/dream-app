package hs.trier.dream_app.controller;

import hs.trier.dream_app.dao.SymbolDAO;
import hs.trier.dream_app.model.Symbol;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class Symbols {

    @FXML
    private Button addSymbolButton;
    @FXML
    private Button editSymbolButton;
    @FXML
    private Button deleteSymbolButton;
    @FXML
    private TableView<Symbol> symbolsTableView;
    @FXML
    private TextField symbolTextField;
    @FXML
    private TextArea symbolTextArea;

    @FXML
    private void initialize() {
        // populate tableView with symbol items
        symbolsTableView.setItems(SymbolDAO.getSymbols());

        // create table columns and set bindings
        TableColumn<Symbol, String> nameTableColumn = new TableColumn<>("Name");
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Symbol, String> descriptionTableColumn = new TableColumn<>("Description");
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // add columns to table
        //noinspection unchecked
        symbolsTableView.getColumns().addAll(nameTableColumn, descriptionTableColumn);

        // select first row when symbolsTableView is not empty
        if (!symbolsTableView.getItems().isEmpty()) {
            // get first item
            Symbol symbol = symbolsTableView.getItems().get(0);

            // populate preview fields
            symbolTextField.setText(symbol.getName());
            symbolTextArea.setText(symbol.getDescription());

            // visual selection
            symbolsTableView.requestFocus();
            symbolsTableView.getFocusModel().focus(0);
            symbolsTableView.getSelectionModel().selectFirst();
        }

        // callback: populate preview fields whenever selected item changes
        symbolsTableView.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            symbolTextField.setText(nv.getName());
            symbolTextArea.setText(nv.getDescription());
        });

        // disable edit and delete buttons if no item is selected
        editSymbolButton.disableProperty().bind(Bindings.isEmpty(symbolsTableView.getSelectionModel().getSelectedItems()));
        deleteSymbolButton.disableProperty().bind(Bindings.isEmpty(symbolsTableView.getSelectionModel().getSelectedItems()));
    }
}
