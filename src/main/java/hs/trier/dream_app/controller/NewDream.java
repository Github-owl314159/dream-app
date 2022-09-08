package hs.trier.dream_app.controller;

import hs.trier.dream_app.dao.DreamDAO;
import hs.trier.dream_app.model.Dream;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;

public class NewDream {
    @FXML
    private TextArea contentTextArea;
    @FXML
    private TextField titleTextField;
    @FXML
    private Button saveDreamButton;
    @FXML
    private TextArea notesTextArea;
    @FXML
    private TextField moodTextField;

    @FXML
    private void initialize() {

        // set event handler
        saveDreamButton.setOnAction(this::saveDream);

        // create booleanBinding
        BooleanBinding dreamExistsIgnoreCaseBinding = Bindings.createBooleanBinding(
                () -> DreamDAO.dreamExistsIgnoreCase(titleTextField.getText().trim()),
                titleTextField.textProperty()
        );

        // disable save button if either title or content is empty AND dream does not exist already
        saveDreamButton.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> (titleTextField.getText().trim().isEmpty() ||
                                contentTextArea.getText().trim().isEmpty()) ||
                                dreamExistsIgnoreCaseBinding.get(),
                        titleTextField.textProperty(), contentTextArea.textProperty()
                ));
    }

    private void saveDream(ActionEvent actionEvent) {
        int result = DreamDAO.create(
                titleTextField.getText(),
                contentTextArea.getText(),
                notesTextArea.getText().trim(),
                moodTextField.getText().trim()
        );

        Alert alert;
        if (result != -1) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Successfully saved Dream!");
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An Error occurred!");
        }
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();

        // consume event
        actionEvent.consume();
    }

    public void editDream(Dream selectedItem) {
        titleTextField.setText(selectedItem.getTitle());
        contentTextArea.setText(selectedItem.getContent());
        moodTextField.setText(selectedItem.getMood());
        notesTextArea.setText(selectedItem.getNotes());
    }
}
