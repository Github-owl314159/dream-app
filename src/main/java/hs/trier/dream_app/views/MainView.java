package hs.trier.dream_app.views;

import hs.trier.dream_app.Presenter;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;


public class MainView
{
    private BorderPane root;
    private HBox buttons;
    private VBox top;
    private ScrollPane center, right;
    private Button newDreamButton, dreamDiaryButton, searchButton, dreamSymbolsButton, helpButton, settingsButton;
    private Label statusBar, titleLabel;
    private Presenter presenter;


    public MainView()
    {
        this.presenter = Presenter.getPresenter();
        init();
    }

    public void init()
    {
        newDreamButton = new Button("New Dream");
        newDreamButton.setOnAction(e -> presenter.onNewDream());
        dreamDiaryButton = new Button("Dream Diary");
        dreamDiaryButton.setOnAction(e -> presenter.onDreamDiary());
        searchButton = new Button("Search");
        searchButton.setOnAction(e -> presenter.onSearch());
        dreamSymbolsButton = new Button("Dream Symbols");
        dreamSymbolsButton.setOnAction(e -> presenter.onSymbols());
        helpButton = new Button("Help");
        helpButton.setOnAction(e -> presenter.onHelp());
        settingsButton = new Button("Options");
        settingsButton.setOnAction(e -> presenter.onSettings());

        buttons = new HBox();
        buttons.getChildren().addAll(newDreamButton, dreamDiaryButton, searchButton, dreamSymbolsButton, helpButton, settingsButton);
        titleLabel = new Label("New Dream");
        top = new VBox();
        top.getChildren().addAll(buttons, titleLabel);

        statusBar = new Label("");

        root = new BorderPane();
        center = new ScrollPane();
        right = new ScrollPane();
        root.setTop(top);
        root.setBottom(statusBar);
        root.setCenter(center);
        root.setRight(right);

        // TODO: hübsch machen
        buttons.setPadding(new Insets(10));
        buttons.setSpacing(10);
        titleLabel.setFont(new Font("Arial", 24));
        top.setAlignment(Pos.TOP_CENTER);
        top.setSpacing(15);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setStyle("-fx-font-size: 14pt");
        center.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        center.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        center.setFitToWidth(true);
        center.setStyle("-fx-background-color:transparent;");
        right.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        right.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        right.setStyle("-fx-background-color:transparent;");


    }

    public BorderPane getRoot()
    {
        return root;
    }

    public void setContent(VBox content)
    {
        center.setContent(content);
        VBox.setVgrow(content, Priority.ALWAYS);
    }

    public void setMetaContent(VBox metaContent)
    {
        right.setContent(metaContent);
        VBox.setVgrow(metaContent, Priority.ALWAYS);
    }

    public void setTitle(String title)
    {
        titleLabel.setText(title);
    }

    public void resetView()
    {
        center.setContent(null);
        right.setContent(null);
    }

    public void showInStatusBar(String message)
    {
        Timeline statusBarDelay = new Timeline();
        statusBarDelay.getKeyFrames().add(new KeyFrame(Duration.seconds(0), new KeyValue(statusBar.textProperty(), message)));
        statusBarDelay.getKeyFrames().add(new KeyFrame(Duration.seconds(5), new KeyValue(statusBar.textProperty(), "")));
        statusBarDelay.play();
    }
}
