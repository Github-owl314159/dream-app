package hs.trier.dream_app.views;

import hs.trier.dream_app.model.Dream;
import hs.trier.dream_app.Presenter;
import hs.trier.dream_app.dao.DreamDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DreamDiaryView {
    private final Presenter presenter;
    private VBox content, metaContent;
    private HBox buttons;
    private Label titleLabel;
    private Label title;
    private Label date;
    private Label mood;
    private Label notesLabel;
    private Label notes;
    private Label textLabel;
    private Label text;
    private GridPane metaContentGrid;

    private TableView<Dream> dreamList;

    public DreamDiaryView() {
        this.presenter = Presenter.getPresenter();
        init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        dreamList = new TableView<>(DreamDAO.getDreams());

        TableColumn<Dream, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Dream, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Dream, String> moodColumn = new TableColumn<>("Mood");
        moodColumn.setCellValueFactory(new PropertyValueFactory<>("mood"));

        dreamList.getColumns().addAll(dateColumn, titleColumn, moodColumn);

        dreamList.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                showDream();
            } else {
                if (dreamList.getSelectionModel().getSelectedItem() != null) {
                    showPreview();
                }
            }
        });

        Button openButton = new Button("Open Dream");
        openButton.setOnAction(e -> showDream());

        Button deleteButton = new Button("Delete Dream");
        deleteButton.setOnAction(e -> deleteDream());

        buttons = new HBox();
        buttons.getChildren().addAll(openButton, deleteButton);

        content = new VBox();
        content.getChildren().addAll(dreamList, buttons);

        titleLabel = new Label("Title: ");
        title = new Label("");
        Label dateLabel = new Label("Date: ");
        date = new Label("");
        Label moodLabel = new Label("Mood: ");
        mood = new Label("");
        notesLabel = new Label("Notes: ");
        notes = new Label("");
        notes.setWrapText(true);
        textLabel = new Label("Dream: ");
        text = new Label("");
        text.setWrapText(true);

        metaContentGrid = new GridPane();
        metaContentGrid.add(titleLabel, 0, 0);
        metaContentGrid.add(title, 1, 0);
        metaContentGrid.add(dateLabel, 0, 1);
        metaContentGrid.add(date, 1, 1);
        metaContentGrid.add(moodLabel, 0, 2);
        metaContentGrid.add(mood, 1, 2);
        metaContentGrid.add(notesLabel, 0, 3);
        metaContentGrid.add(notes, 1, 3);
        metaContentGrid.add(textLabel, 0, 4);
        metaContentGrid.add(text, 1, 4);

        metaContent = new VBox();
        metaContent.getChildren().addAll(buttons, metaContentGrid);

        beautify();
    }

    private void beautify() {
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.TOP_CENTER);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(20));
        content.setSpacing(10);
        metaContentGrid.setHgap(10);
        metaContentGrid.setVgap(10);
        metaContent.setPrefWidth(500);
        metaContent.setAlignment(Pos.TOP_CENTER);
        metaContent.setPadding(new Insets(10));
        metaContent.setSpacing(20);
        notes.setPrefWidth(300);
        text.setPrefWidth(300);
        GridPane.setValignment(titleLabel, VPos.TOP);
        GridPane.setValignment(notesLabel, VPos.TOP);
        GridPane.setValignment(textLabel, VPos.TOP);
    }

    public VBox getContent() {
        return content;
    }

    public VBox getMetaContent() {
        return metaContent;
    }

    public void showPreview() {
        Dream dream = dreamList.getSelectionModel().getSelectedItem();
        title.setText(dream.getTitle());
        date.setText(dream.getDate());
        mood.setText(dream.getMood());
        notes.setText(dream.getNotes());
        text.setText(dream.getText());
    }

    public void resetPreview() {
        title.setText("");
        date.setText("");
        mood.setText("");
        notes.setText("");
        text.setText("");
    }

    public void showDream() {
        Dream dream = dreamList.getSelectionModel().getSelectedItem();
        presenter.onShowDream(dream.getId());
    }

    private void deleteDream() {
        Dream dream = dreamList.getSelectionModel().getSelectedItem();
        presenter.onDeleteDream(dream.getId(), dream.getTitle());
    }

    public void refreshDreamList() {
        dreamList.getItems().clear();
        dreamList.setItems(DreamDAO.getDreams());
        if(!dreamList.getItems().isEmpty())
            dreamList.getSelectionModel().select(0);
    }
}
