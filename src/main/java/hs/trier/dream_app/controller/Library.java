package hs.trier.dream_app.controller;

import hs.trier.dream_app.Util;
import hs.trier.dream_app.dao.DreamDAO;
import hs.trier.dream_app.model.Dream;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;

import java.util.Optional;

public class Library {
    @FXML
    private TableView<Dream> dreamsTableView;
    @FXML
    private TextArea contentTextArea;
    @FXML
    private TextField titleTextField;
    @FXML
    private Button editDreamButton;
    @FXML
    private Button deleteDreamButton;
    @FXML
    private Button searchDreamSymbolsButton;
    @FXML
    private TextField dateTextField;
    @FXML
    private TextField moodTextField;
    @FXML
    private TextArea notesTextArea;
    @FXML
    private ImageView deepDreamImageView;

    @FXML
    private void initialize() {

        // create table columns and set bindings
        TableColumn<Dream, String> dateTableColumn = new TableColumn<>("Date");
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Dream, String> titleTableColumn = new TableColumn<>("Title");
        titleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        // add columns to table
        dreamsTableView.getColumns().addAll(dateTableColumn, titleTableColumn);

        // populate tableView with dream items
        dreamsTableView.setItems(DreamDAO.getDreams());

        // sorting
        dateTableColumn.setSortable(true);
        titleTableColumn.setSortable(true);
        dateTableColumn.setSortType(TableColumn.SortType.DESCENDING);
        dreamsTableView.getSortOrder().addAll(dateTableColumn);
        dreamsTableView.sort();

        // select first row when dreamsTableView is not empty
        if (!dreamsTableView.getItems().isEmpty()) {
            // get first item
            Dream dream = dreamsTableView.getItems().get(0);

            // populate preview fields
            titleTextField.setText(dream.getTitle());
            contentTextArea.setText(dream.getContent());
            dateTextField.setText(dream.getDate());
            moodTextField.setText(dream.getMood());
            notesTextArea.setText(dream.getNotes());

            // visual selection
            dreamsTableView.requestFocus();
            dreamsTableView.getFocusModel().focus(0);
            dreamsTableView.getSelectionModel().selectFirst();
        }

        // callback: populate preview fields whenever selected item changes
        dreamsTableView.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            if (nv != null) {
                titleTextField.setText(nv.getTitle());
                contentTextArea.setText(nv.getContent());
                dateTextField.setText(nv.getDate());
                moodTextField.setText(nv.getMood());
                notesTextArea.setText(nv.getNotes());
            } else {
                titleTextField.clear();
                contentTextArea.clear();
                dateTextField.clear();
                moodTextField.clear();
                notesTextArea.clear();
            }
        });

        // set event handler
        editDreamButton.setOnAction(this::editDream);
        deleteDreamButton.setOnAction(this::deleteDream);
        searchDreamSymbolsButton.setOnAction(this::searchDreamSymbols);

        // disable edit and delete buttons if no item is selected
        editDreamButton.disableProperty().bind(Bindings.isEmpty(dreamsTableView.getSelectionModel().getSelectedItems()));
        deleteDreamButton.disableProperty().bind(Bindings.isEmpty(dreamsTableView.getSelectionModel().getSelectedItems()));
        searchDreamSymbolsButton.disableProperty().bind(Bindings.isEmpty(dreamsTableView.getSelectionModel().getSelectedItems()));
    }

    private void deleteDream(ActionEvent actionEvent) {
        // create dialog
        Dialog<Integer> dialog = new Dialog<>();

        // get selected item
        Dream selectedItem = dreamsTableView.getSelectionModel().getSelectedItem();

        // prepare dialog
        dialog.setTitle("Confirm Deletion");
        dialog.setHeaderText(String.format("Do you really want to delete the Dream '%s'?", selectedItem.getTitle()));
        dialog.getDialogPane().setStyle("-fx-padding: 20;");

        // add buttons to dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.NO, ButtonType.YES);

        // disable other windows
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(contentTextArea.getScene().getWindow());

        // configure result converter
        dialog.setResultConverter(buttonType -> {
            Integer result = null;

            if (buttonType == ButtonType.YES)
                result = selectedItem.getId();

            return result;
        });

        // show dialog to user and wait for its completion
        Optional<Integer> optionalInteger = dialog.showAndWait();

        // delete dream if result is available
        optionalInteger.ifPresent(DreamDAO::delete);
    }

    private void editDream(ActionEvent actionEvent) {
        Dream selectedItem = dreamsTableView.getSelectionModel().getSelectedItem();
        NewDream controller = (NewDream) Util.getInstance().loadFXML("views/new-dream.fxml");

        controller.editDream(selectedItem);
        actionEvent.consume();
    }

    private void searchDreamSymbols(ActionEvent actionEvent) {
        Dream selectedItem = dreamsTableView.getSelectionModel().getSelectedItem();
        AnalyzeDream controller = (AnalyzeDream) Util.getInstance().loadFXML("views/analyze.fxml");

        controller.analyze(selectedItem);
        actionEvent.consume();
    }
}
