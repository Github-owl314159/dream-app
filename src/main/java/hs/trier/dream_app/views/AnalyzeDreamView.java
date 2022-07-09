package hs.trier.dream_app.views;

import hs.trier.dream_app.model.Symbol;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class AnalyzeDreamView {
    private VBox content, metaContent, addSymbol;
    private HBox buttons;
    private Label testText, testToken, testTokenWithoutDupes, testTokenWithoutStopwords, testStemmedToken, addSymbolLabel;
    private Button addButton;
    private Button saveButton;
    private Button cancelButton;
    private TextField newSymbol;
    private TextArea newInterpretation;

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
        testText = new Label("");
        testText.setWrapText(true);
        testToken = new Label("");
        testToken.setWrapText(true);
        testTokenWithoutDupes = new Label("");
        testTokenWithoutDupes.setWrapText(true);
        testTokenWithoutStopwords = new Label("");
        testTokenWithoutStopwords.setWrapText(true);
        testStemmedToken = new Label("");
        testStemmedToken.setWrapText(true);

        content = new VBox();
        content.getChildren().addAll(testText, testToken, testTokenWithoutDupes, testTokenWithoutStopwords, testStemmedToken);

        addButton = new Button("Add new Dream Symbol");
        addButton.setOnAction(e -> addNewSymbol(true));
        saveButton = new Button("Save Dream Symbol");
        saveButton.setOnAction(e -> saveNewSymbol());
        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> addNewSymbol(false));

        buttons = new HBox();
        buttons.getChildren().addAll(addButton);

        addSymbol = new VBox();
        addSymbol.getChildren().addAll(buttons);

        metaContent = new VBox();
        metaContent.getChildren().addAll(addSymbol);

        addSymbolLabel = new Label("Add new Dream Symbol:");
        newSymbol = new TextField();
        newInterpretation = new TextArea();
        newInterpretation.setWrapText(true);

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
        addSymbol.setPadding(new Insets(10));
        addSymbol.setSpacing(20);
        addSymbol.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

    public void showAnalyzedDream(ArrayList<Symbol> symbols, String text, String token, String tokenWithoutDupes, String tokenWithoutStopwords, String stemmedToken) {
        testText.setText("Original Text: \n" + text);
        testToken.setText("Tokenized: \n" + token);
        testTokenWithoutDupes.setText("Dupes killed: \n" + tokenWithoutDupes);
        testTokenWithoutStopwords.setText("Stopwords killed: \n" + tokenWithoutStopwords);
        testStemmedToken.setText("Stemmed: \n" + stemmedToken);
        metaContent.getChildren().clear();
        metaContent.getChildren().add(addSymbol);

        for (Symbol symbol : symbols) {
            VBox temp = new VBox();
            Label symbolLabel = new Label(symbol.getName());
            Label interpretation = new Label(symbol.getDescription());
            interpretation.setWrapText(true);
            HBox buttons = new HBox();
            Button accept = new Button("Accept Symbol");
            Button discard = new Button("Discard Symbol");
            buttons.getChildren().addAll(accept, discard);
            temp.getChildren().addAll(symbolLabel, interpretation, buttons);
            temp.setPadding(new Insets(10));
            temp.setSpacing(20);
            temp.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            metaContent.getChildren().add(temp);
            metaContent.setPrefWidth(400);
            accept.setOnAction(e -> temp.getChildren().removeAll(buttons));
            discard.setOnAction(e -> metaContent.getChildren().remove(temp));
        }
    }

    public void addNewSymbol(boolean bool) {
        buttons.getChildren().clear();
        addSymbol.getChildren().clear();

        if (bool) {
            newSymbol.setText("");
            newSymbol.setPromptText("Enter Symbol");
            newInterpretation.setText("");
            newInterpretation.setPromptText("Enter Interpretation");
            buttons.getChildren().addAll(saveButton, cancelButton);
            addSymbol.getChildren().addAll(addSymbolLabel, newSymbol, newInterpretation, buttons);
        } else {
            buttons.getChildren().addAll(addButton);
            addSymbol.getChildren().addAll(buttons);
        }
    }

    private void saveNewSymbol() {
//        Symbol newSymbol = new Symbol(this.newSymbol.getText(), newInterpretation.getText());
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
