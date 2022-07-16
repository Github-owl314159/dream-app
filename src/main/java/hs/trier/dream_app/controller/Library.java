package hs.trier.dream_app.controller;

import hs.trier.dream_app.dao.DreamDAO;
import hs.trier.dream_app.model.Dream;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Library {
    @FXML
    private TableView<Dream> dreamsTableView;
    @FXML
    private TextArea contentTextArea;
    @FXML
    private TextField titleTextField;

    @FXML
    private void initialize() {
        // populate tableView with dream items
        dreamsTableView.setItems(DreamDAO.getDreams());

        // create table columns and set bindings
        TableColumn<Dream, String> titleTableColumn = new TableColumn<>("Title");
        titleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Dream, String> contentTableColumn = new TableColumn<>("Content");
        contentTableColumn.setCellValueFactory(new PropertyValueFactory<>("content"));

        TableColumn<Dream, String> dateTableColumn = new TableColumn<>("Date");
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Dream, String> notesTableColumn = new TableColumn<>("Notes");
        notesTableColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        TableColumn<Dream, String> moodTableColumn = new TableColumn<>("Mood");
        moodTableColumn.setCellValueFactory(new PropertyValueFactory<>("mood"));

        // add columns to table
        //noinspection unchecked
        dreamsTableView.getColumns().addAll(titleTableColumn, contentTableColumn, dateTableColumn, notesTableColumn, moodTableColumn);

        // select first row when dreamsTableView is not empty
        if (!dreamsTableView.getItems().isEmpty()) {
            // get first item
            Dream dream = dreamsTableView.getItems().get(0);

            // populate preview fields
            titleTextField.setText(dream.getTitle());
            contentTextArea.setText(dream.getContent());

            // visual selection
            dreamsTableView.requestFocus();
            dreamsTableView.getFocusModel().focus(0);
            dreamsTableView.getSelectionModel().selectFirst();
        }

        // callback: populate preview fields whenever selected item changes
        dreamsTableView.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            titleTextField.setText(nv.getTitle());
            contentTextArea.setText(nv.getContent());
        });
    }
}
