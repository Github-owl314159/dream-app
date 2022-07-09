package hs.trier.dream_app.views;

import hs.trier.dream_app.model.Dream;
import hs.trier.dream_app.Presenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditDreamView {
    private VBox content, metaContent;
    private GridPane metaContentGrid;
    private Label notesLabel;
    private TextField titleTextField, moodTextField;
    private TextArea contentTextArea, notesTextArea;
    private DatePicker datePicker;
    private final Presenter presenter;
    private Dream dream;

    public EditDreamView() {
        this.presenter = Presenter.getPresenter();
        init();
    }

    public void init() {
        titleTextField = new TextField();
        contentTextArea = new TextArea();
        contentTextArea.setWrapText(true);
        content = new VBox();
        content.getChildren().addAll(titleTextField, contentTextArea);

        Label dateLabel = new Label("Date:");
        datePicker = new DatePicker();
        datePicker.setEditable(false);
        datePicker.setConverter(new StringConverter<>()                  //TODO: Oracle-Beispielcode. Schnall noch nicht ganz, wie das funktioniert. Ggfs. abspecken.
        {
            final String pattern = "yyyy-MM-dd";
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                datePicker.setPromptText(pattern.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        Label moodLabel = new Label("Mood:");
        moodTextField = new TextField();
        notesLabel = new Label("Notes:");
        notesTextArea = new TextArea();
        notesTextArea.setWrapText(true);

        Button updateButton = new Button("Update Dream");
        updateButton.setOnAction(e -> presenter.onUpdateDream());
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> presenter.onShowDream(dream.getId()));

        metaContentGrid = new GridPane();
        metaContentGrid.add(dateLabel, 0, 0);
        metaContentGrid.add(datePicker, 1, 0);
        metaContentGrid.add(moodLabel, 0, 1);
        metaContentGrid.add(moodTextField, 1, 1);
        metaContentGrid.add(notesLabel, 0, 2);
        metaContentGrid.add(notesTextArea, 1, 2);

        metaContent = new VBox();
        metaContent.getChildren().addAll(metaContentGrid, updateButton, cancelButton);

        beautify();
    }

    private void beautify() {
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(20));
        content.setSpacing(20);
        for (Node child : content.getChildren()) {
            VBox.setVgrow(child, Priority.ALWAYS);
        }
        datePicker.setPrefWidth(250);
        moodTextField.setPrefWidth(250);
        notesTextArea.setPrefWidth(250);
        metaContentGrid.setHgap(10);
        metaContentGrid.setVgap(10);
        metaContent.setPrefWidth(400);
        metaContent.setAlignment(Pos.TOP_CENTER);
        metaContent.setPadding(new Insets(10));
        metaContent.setSpacing(20);
        datePicker.setShowWeekNumbers(false);
        GridPane.setValignment(notesLabel, VPos.TOP);

    }

    public VBox getContent() {
        return content;
    }

    public VBox getMetaContent() {
        return metaContent;
    }

    public void showDream(Dream dream) {
        this.dream = dream;
        titleTextField.setText(dream.getTitle());
        contentTextArea.setText(dream.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dream.getDate(), formatter);
        datePicker.setValue(localDate);
        moodTextField.setText(dream.getMood());
        notesTextArea.setText(dream.getNotes());
    }

    public Dream getCurrentDream() {
//        dream.setTitle(dreamTitle.getText());
//        dream.setText(dreamText.getText());
//        dream.setDate(date.getValue().toString());
//        dream.setTime("00:00");                                 //TODO
//        dream.setMood(mood.getText());
//        dream.setNotes(notes.getText());
        return dream;
    }
}
