package hs.trier.dream_app.controller;

import hs.trier.dream_app.Util;
import hs.trier.dream_app.dao.SymbolDAO;
import hs.trier.dream_app.model.Symbol;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.Objects;
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

        // add columns to table
        symbolsTableView.getColumns().addAll(nameTableColumn);

        // sorting
        nameTableColumn.setSortable(true);
        nameTableColumn.setSortType(TableColumn.SortType.ASCENDING);
        symbolsTableView.getSortOrder().add(nameTableColumn);
        symbolsTableView.sort();

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

        // get selected item
        Symbol selectedItem = symbolsTableView.getSelectionModel().getSelectedItem();

        // prepare dialog
        dialog.setTitle("Confirm Deletion");
        dialog.setHeaderText(String.format("Do you really want to delete the Symbol '%s'?", selectedItem.getName()));
        dialog.getDialogPane().setStyle("-fx-padding: 20;");

        // add buttons to dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.NO, ButtonType.YES);

        // disable other windows
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(symbolsTableView.getScene().getWindow());

        // configure result converter
        dialog.setResultConverter(buttonType -> {
            Integer result = null;

            if (buttonType == ButtonType.YES)
                result = selectedItem.getId();

            return result;
        });

        // show dialog to user and wait for its completion
        Optional<Integer> optionalInteger = dialog.showAndWait();

        // delete symbol if result is available
        optionalInteger.ifPresent(SymbolDAO::delete);
    }

    private void editSymbol(ActionEvent event) {
        // get selected item
        Symbol symbol = symbolsTableView.getSelectionModel().getSelectedItem();

        // init dialog
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Edit Dialog");
        dialog.setResizable(true);

        // load dialog fxml
        DialogPane dialogPane;
        try {
            dialogPane = FXMLLoader.load(Objects.requireNonNull(Util.getAbsoluteURL("views/dialogs/edit-dialog.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // get handles on input fields/labels
        TextField nameTextField = (TextField) dialogPane.lookup("#nameTextField");
        TextArea descriptionTextArea = (TextArea) dialogPane.lookup("#descriptionTextArea");

        // set textFields
        nameTextField.setText(symbol.getName());
        descriptionTextArea.setText(symbol.getDescription());

        // get handle on OK button
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);

        /*
         disable OK button if:
         -> name is empty OR
         -> description is empty OR
         -> neither name nor description has changed
        */
        okButton.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> nameTextField.getText().trim().isEmpty() ||
                                descriptionTextArea.getText().trim().isEmpty() ||
                                       (nameTextField.getText().trim().equals(symbol.getName()) &&
                                        descriptionTextArea.getText().trim().equals(symbol.getDescription())),
                        nameTextField.textProperty(), descriptionTextArea.textProperty()
                )
        );

        // configure result converter
        dialog.setResultConverter((buttonType) -> {
            Integer result = null;

            if (buttonType == ButtonType.OK)
                result = SymbolDAO.update(symbol.getId(), nameTextField.getText(), descriptionTextArea.getText());

            return result;
        });

        // disable other windows
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(symbolsTableView.getScene().getWindow());

        // show dialog to user and wait for its completion
        dialog.setDialogPane(dialogPane);
        Optional<Integer> optionalInteger = dialog.showAndWait();

        optionalInteger.ifPresent(rowsAffected -> {
            if (rowsAffected == 0)
                showWarning();
        });

        // consume event
        event.consume();
    }

    @FXML
    private void addSymbol(ActionEvent event) {
        // create new dialog
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Add Dialog");
        dialog.setResizable(true);

        // load dialog fxml
        DialogPane dialogPane;
        try {
            dialogPane = FXMLLoader.load(Objects.requireNonNull(Util.getAbsoluteURL("views/dialogs/add-dialog.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // get handles on input fields/labels
        TextField nameTextField = (TextField) dialogPane.lookup("#nameTextField");
        TextArea descriptionTextArea = (TextArea) dialogPane.lookup("#descriptionTextArea");
        Label symbolExistsLabel = (Label) dialogPane.lookup("#symbolExistsLabel");

        // create booleanBinding
        BooleanBinding symbolExistsIgnoreCaseBinding = Bindings.createBooleanBinding(
                () -> SymbolDAO.symbolExistsIgnoreCase(nameTextField.getText().trim()),
                nameTextField.textProperty()
        );

        /*
         disable OK button if:
         -> name is empty OR
         -> description is empty OR
         -> name already exists
        */
        Bindings.createBooleanBinding(
                () -> nameTextField.getText().trim().isEmpty() || descriptionTextArea.getText().trim().isEmpty()
                        || symbolExistsIgnoreCaseBinding.get(),
                nameTextField.textProperty(),
                descriptionTextArea.textProperty()
        );

        // show info label when given symbol name already exists
        symbolExistsLabel.visibleProperty().bind(symbolExistsIgnoreCaseBinding);

        // convert user data to Symbol and save it if user clicked on OK button
        dialog.setResultConverter((buttonType) -> {
            Integer result = null;

            if (buttonType == ButtonType.OK)
                result = SymbolDAO.create(nameTextField.getText(), descriptionTextArea.getText());

            return result;
        });

        // disable other windows
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(symbolsTableView.getScene().getWindow());

        // show dialog to user and wait for its completion
        dialog.setDialogPane(dialogPane);
        Optional<Integer> optionalInteger = dialog.showAndWait();

        // if result is not null
        optionalInteger.ifPresent(
                (id) -> {
                    // show warning if result is -1
                    if (id == -1)
                        showWarning();
                    else {
                        // make new item visible in view
                        symbolsTableView.getSelectionModel().selectLast();
                        SymbolDAO.getSymbol(id).ifPresent(symbol -> symbolsTableView.scrollTo(symbol.getId()));
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
}
