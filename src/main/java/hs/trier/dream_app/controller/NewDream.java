package hs.trier.dream_app.controller;

import hs.trier.dream_app.Util;
import hs.trier.dream_app.model.dao.CRUDHelper;
import hs.trier.dream_app.model.dao.DreamDAO;
import hs.trier.dream_app.model.Dream;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.time.format.DateTimeFormatter;

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
    private DatePicker datePicker;

    private boolean editMode = false;
    private Dream editDream;

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
                        () -> (titleTextField.getText().trim().isEmpty() || contentTextArea.getText().trim().isEmpty()),
                        //   || dreamExistsIgnoreCaseBinding.get(),
                        titleTextField.textProperty(),
                        contentTextArea.textProperty()
                ));
    }

    private void saveDream(ActionEvent actionEvent) {
        int result;
        String date;

        if (editMode) {
            editDream.setContent(contentTextArea.getText());
            editDream.setDate(datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            editDream.setMood(moodTextField.getText());
            editDream.setNotes(notesTextArea.getText());
            editDream.setTitle(titleTextField.getText());
            result = DreamDAO.update(editDream);

        } else {
            if (datePicker.getValue() == null) {
                date = CRUDHelper.now().toString();
            } else {
                date = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            result = DreamDAO.create(
                    titleTextField.getText(),
                    contentTextArea.getText(),
                    notesTextArea.getText().trim(),
                    moodTextField.getText().trim(),
                    date
            );
        }

        Alert alert;
        if (result != -1) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Successfully saved Dream!");
            editMode = false;
            editDream = null;
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An Error occurred!");
        }
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();

        // consume event
        actionEvent.consume();
        Util.getInstance().loadFXML("views/library.fxml");
    }

    public void editDream(Dream selectedItem) {
        editDream = selectedItem;
        editMode = true;
        titleTextField.setText(editDream.getTitle());
        contentTextArea.setText(editDream.getContent());
        moodTextField.setText(editDream.getMood());
        notesTextArea.setText(editDream.getNotes());
        datePicker.setValue(Util.convertDate(editDream.getDate()));
    }
}
