package hs.trier.dream_app.views;

import hs.trier.dream_app.api.DeepImages;
import hs.trier.dream_app.dao.SymbolDAO;
import hs.trier.dream_app.model.DeepImage;
import hs.trier.dream_app.model.Symbol;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Optional;


public class AnalyzeDreamView {
    private VBox content, metaContent, addSymbolVBox;
    private HBox buttonsHBox;
    private Label testText, testToken, testTokenWithoutDupes, testTokenWithoutStopwords, testStemmedToken, addSymbolLabel;
    private Button addButton;
    private Button saveButton;
    private Button cancelButton;
    private TextField symbolTextField;
    private TextArea descriptionTextArea;

    private ImageView deepImageView;

    public AnalyzeDreamView() {
        init();
    }

    public VBox getContent() {
        return content;
    }

    public VBox getMetaContent() {
        return metaContent;
    }

    private void init() {
//        testText = new Label("");
//        testText.setWrapText(true);
//        testToken = new Label("");
//        testToken.setWrapText(true);
//        testTokenWithoutDupes = new Label("");
//        testTokenWithoutDupes.setWrapText(true);
//        testTokenWithoutStopwords = new Label("");
//        testTokenWithoutStopwords.setWrapText(true);
//        testStemmedToken = new Label("");
//        testStemmedToken.setWrapText(true);

        double maxWidth = 200;
        double maxHeight = 300;

        DeepImage.loadNewDeepImage();
        deepImageView = new ImageView(DeepImage.getDeepImage());
        deepImageView.setFitWidth(maxWidth);
        deepImageView.setFitHeight(maxHeight);

        deepImageView.imageProperty().addListener((obs, ov, nv) -> {
            double aspectRatio = nv.getWidth() / nv.getHeight();

            if (aspectRatio > 1.5) {
                deepImageView.setFitWidth(maxWidth);
                deepImageView.setFitHeight(maxWidth / aspectRatio);
            } else {
                deepImageView.setFitHeight(maxHeight);
                deepImageView.setFitWidth(maxHeight * aspectRatio);
            }
        });

        deepImageView.imageProperty().bind(DeepImage.deepImageProperty());

        content = new VBox();
        content.getChildren().addAll(deepImageView);

        addButton = new Button("Add new Dream Symbol");
        addButton.setOnAction(e -> addNewSymbol());

        Button deepImageButton = new Button("Get Dream Image");
        deepImageButton.setOnAction(e -> {
            DeepImage.loadNewDeepImage();
            e.consume();
        });

        saveButton = new Button("Save Dream Symbol");
        saveButton.setOnAction(e -> saveNewSymbol());

        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            buttonsHBox.getChildren().addAll(addButton);
            addSymbolVBox.getChildren().addAll(buttonsHBox);
        });

        buttonsHBox = new HBox();
        buttonsHBox.getChildren().addAll(addButton, deepImageButton);

        addSymbolVBox = new VBox();
        addSymbolVBox.getChildren().addAll(buttonsHBox);

        metaContent = new VBox();
        metaContent.getChildren().addAll(addSymbolVBox);

        addSymbolLabel = new Label("Add new Dream Symbol:");
        symbolTextField = new TextField();
        descriptionTextArea = new TextArea();
        descriptionTextArea.setWrapText(true);

        beautify();
    }

    public void beautify() {
        content.setPadding(new Insets(20));
        content.setSpacing(20);
        for (Node child : content.getChildren()) {
            VBox.setVgrow(child, Priority.ALWAYS);
        }
        metaContent.setPrefWidth(400);
        metaContent.setAlignment(Pos.TOP_CENTER);
        metaContent.setPadding(new Insets(10));
        metaContent.setSpacing(20);
        addSymbolVBox.setPadding(new Insets(10));
        addSymbolVBox.setSpacing(20);
        addSymbolVBox.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

    public void showAnalyzedDream(ArrayList<Symbol> symbols, String text, String token, String tokenWithoutDupes, String tokenWithoutStopwords, String stemmedToken) {
        testText.setText("Original Text: \n" + text);
        testToken.setText("Tokenized: \n" + token);
        testTokenWithoutDupes.setText("Dupes killed: \n" + tokenWithoutDupes);
        testTokenWithoutStopwords.setText("Stopwords killed: \n" + tokenWithoutStopwords);
        testStemmedToken.setText("Stemmed: \n" + stemmedToken);
        metaContent.getChildren().clear();
        metaContent.getChildren().add(addSymbolVBox);

        for (Symbol symbol : symbols) {
            VBox temporaryVBox = new VBox();
            Label symbolLabel = new Label(symbol.getName());
            Label descriptionLabel = new Label(symbol.getDescription());
            descriptionLabel.setWrapText(true);
            HBox buttonsHBox = new HBox();
            Button acceptButton = new Button("Accept Symbol");
            Button discardButton = new Button("Discard Symbol");
            buttonsHBox.getChildren().addAll(acceptButton, discardButton);
            temporaryVBox.getChildren().addAll(symbolLabel, descriptionLabel, buttonsHBox);
            temporaryVBox.setPadding(new Insets(10));
            temporaryVBox.setSpacing(20);
            temporaryVBox.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            metaContent.getChildren().add(temporaryVBox);
            metaContent.setPrefWidth(400);
            acceptButton.setOnAction(e -> temporaryVBox.getChildren().removeAll(buttonsHBox));
            discardButton.setOnAction(e -> metaContent.getChildren().remove(temporaryVBox));
        }
    }

    public void addNewSymbol() {
        buttonsHBox.getChildren().clear();
        addSymbolVBox.getChildren().clear();

        symbolTextField.setText("");
        symbolTextField.setPromptText("Enter name");
        descriptionTextArea.setText("");
        descriptionTextArea.setPromptText("Enter description");
        buttonsHBox.getChildren().addAll(saveButton, cancelButton);
        addSymbolVBox.getChildren().addAll(addSymbolLabel, symbolTextField, descriptionTextArea, buttonsHBox);
    }

    private void saveNewSymbol() {


//        int id = SymbolDAO.create(symbolTextField.getText(), descriptionTextArea.getText());
//        Optional<Symbol> newSymbol = SymbolDAO.getSymbol(id);
//
//        presenter.onSaveSymbol();
//        addNewSymbol(false);
//
//        VBox temp = new VBox();
//        Label symbol = new Label(newSymbol.getSymbol());
//        Label interpretation = new Label(newSymbol.getInterpretation());
//        interpretation.setWrapText(true);
//        temp.getChildren().addAll(symbol, interpretation);
//        temp.setPadding(new Insets(10));
//        temp.setSpacing(20);
//        temp.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//        metaContent.getChildren().add(temp);
    }
}
