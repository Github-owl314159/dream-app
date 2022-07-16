package hs.trier.dream_app.controller;

import hs.trier.dream_app.dao.SymbolDAO;
import hs.trier.dream_app.model.Symbol;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import java.util.Optional;

public class Symbols {

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

        // set event handler
        editSymbolButton.setOnAction(this::editSymbol);
        deleteSymbolButton.setOnAction(this::deleteSymbol);

        // disable edit and delete buttons if no item is selected
        editSymbolButton.disableProperty().bind(Bindings.isEmpty(symbolsTableView.getSelectionModel().getSelectedItems()));
        deleteSymbolButton.disableProperty().bind(Bindings.isEmpty(symbolsTableView.getSelectionModel().getSelectedItems()));
    }

    private void deleteSymbol(ActionEvent event) {
        // create dialog
        Dialog<Integer> dialog = new Dialog<>();
        Symbol selectedItem = symbolsTableView.getSelectionModel().getSelectedItem();
        // prepare dialog
        dialog.setTitle("Confirm Deletion");
        dialog.setHeaderText(String.format("Do you really want to delete the Symbol '%s'?", selectedItem.getName()));
        dialog.getDialogPane().setStyle("-fx-padding: 20;");

        // add buttons to dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.NO, ButtonType.YES);

        // disable other windows
        dialog.initModality(Modality.APPLICATION_MODAL);

        dialog.setResultConverter(sel -> selectedItem.getId());

        Optional<Integer> optionalInteger = dialog.showAndWait();
        optionalInteger.ifPresent(SymbolDAO::delete);
    }

    private void editSymbol(ActionEvent event) {

    }

    @FXML
    private void addSymbol(ActionEvent event) {
        // get dialog
        Dialog<Integer> dialog = getDialog();

        // show dialog to user and wait for its completion
        Optional<Integer> optionalInteger = dialog.showAndWait();

        // if result is not null
        optionalInteger.ifPresent(
                (id) -> {
                    // show warning if result is -1
                    if (id == -1)
                        showWarning();
                        // get recently added symbol from database and cache it
                    else {
                        symbolsTableView.getItems().add(
                                SymbolDAO.getSymbol(id).orElseThrow()
                        );
                    }
                }
        );

        // consume event
        event.consume();
    }

    private void showWarning() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("");
        alert.setContentText("");
    }

    private Dialog<Integer> getDialog() {
        // create new dialog
        Dialog<Integer> dialog = new Dialog<>();

        // prepare the dialog
        dialog.setTitle("New Dream Symbol");
        dialog.setHeaderText("Add a new Dream Symbol to your list");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-padding: 20;");
        TextField nameTextField = new TextField();
        nameTextField.setPromptText("Enter a name for the Dream Symbol...");
        TextArea descriptionTextField = new TextArea();
        descriptionTextField.setPromptText("Type in a description of the Dream Symbol...");

        // add rows to dialog
        gridPane.addRow(0, new Label("Symbol Name:"), nameTextField);
        gridPane.addRow(1, new Label("Symbol Description:"), descriptionTextField);

        // add buttons to dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.getDialogPane().setContent(gridPane);

        // disable OK button if either no name or no description has been entered yet
        dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> nameTextField.getText().trim().isEmpty() || descriptionTextField.getText().trim().isEmpty(),
                        nameTextField.textProperty(),
                        descriptionTextField.textProperty()
                )
        );

        // convert user data to Symbol and save it if user clicked on OK button
        dialog.setResultConverter((buttonType) -> {
            if (buttonType == ButtonType.OK) {
                return SymbolDAO.create(nameTextField.getText(), descriptionTextField.getText());
            }
            return null;
        });

        // disable other windows
        dialog.initModality(Modality.APPLICATION_MODAL);

        return dialog;
    }
}
