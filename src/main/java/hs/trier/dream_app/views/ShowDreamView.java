package hs.trier.dream_app.views;

import hs.trier.dream_app.model.Dream;
import hs.trier.dream_app.Presenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ShowDreamView {
    private VBox content, metaContent;
    private GridPane metaContentGrid;
    private Label dreamTitle;
    private Label dreamText;
    private Label date;
    private Label mood;
    private Label notesLabel;
    private Label notes;
    private final Presenter presenter;
    private Dream dream;

    public ShowDreamView() {
        this.presenter = Presenter.getPresenter();
        init();
    }

    public void init() {
        dreamTitle = new Label("");
        dreamText = new Label("");
        dreamText.setWrapText(true);
        content = new VBox();
        content.getChildren().addAll(dreamTitle, dreamText);

        Label dateLabel = new Label("Date:");
        date = new Label("");
        Label moodLabel = new Label("Mood:");
        mood = new Label("");
        notesLabel = new Label("Notes:");
        notes = new Label("");
        notes.setWrapText(true);

        Button editButton = new Button("Edit Dream");
        editButton.setOnAction(e -> presenter.onEditDream());
        Button analyzeButton = new Button("Analyze Dream");
        analyzeButton.setOnAction(e -> presenter.onAnalyzeDream(dream.getId()));
        Button deleteButton = new Button("Delete Dream");
        deleteButton.setOnAction(e -> presenter.onDeleteDream(dream.getId(), dream.getTitle()));
        Button updateButton = new Button("Update Dream");
        updateButton.setOnAction(e -> presenter.onUpdateDream());

        metaContentGrid = new GridPane();
        metaContentGrid.add(dateLabel, 0, 0);
        metaContentGrid.add(date, 1, 0);
        metaContentGrid.add(moodLabel, 0, 1);
        metaContentGrid.add(mood, 1, 1);
        metaContentGrid.add(notesLabel, 0, 2);
        metaContentGrid.add(notes, 1, 2);

        metaContent = new VBox();
        metaContent.getChildren().addAll(metaContentGrid, editButton, analyzeButton, deleteButton);

        beautify();
    }

    private void beautify() {
        //content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(20));
        content.setSpacing(20);
        for (Node child : content.getChildren()) {
            VBox.setVgrow(child, Priority.ALWAYS);
        }
        date.setPrefWidth(250);
        mood.setPrefWidth(250);
        notes.setPrefWidth(250);
        metaContentGrid.setHgap(10);
        metaContentGrid.setVgap(10);
        metaContent.setPrefWidth(400);
        metaContent.setAlignment(Pos.TOP_CENTER);
        metaContent.setPadding(new Insets(10));
        metaContent.setSpacing(20);
        dreamTitle.setStyle("-fx-font-weight: bold;");
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
        dreamTitle.setText(dream.getTitle());
        dreamText.setText(dream.getText());
        date.setText(dream.getDate());
        mood.setText(dream.getMood());
        notes.setText(dream.getNotes());
    }

    public Dream getCurrentDream() {
//        dream.setTitle(dreamTitle.getText());
//        dream.setText(dreamText.getText());
//        dream.setDate(date.getText());
//        dream.setTime("00:00");                                     //TODO
//        dream.setMood(mood.getText());
//        dream.setNotes(notes.getText());
        return dream;
    }
}
