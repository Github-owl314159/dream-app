package hs.trier.dream_app.views;

import hs.trier.dream_app.Presenter;
import hs.trier.dream_app.model.Symbol;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SymbolsView {
    private final Presenter presenter;
    private VBox content;
    private VBox metaContent;
    private HBox buttons;
    private TextField symbolTextField;
    private TextArea descrTextArea;

    public SymbolsView() {
        this.presenter = Presenter.getPresenter();
        init();
    }

    private void init() {
        content = new VBox();
        metaContent = new VBox();
        buttons = new HBox();

        addDreamSymbol();
        beautify();
    }

    public void beautify() {
        content.setPadding(new Insets(20));
        content.setSpacing(20);
        for (Node child : content.getChildren()) {
            VBox.setVgrow(child, Priority.ALWAYS);
        }
        metaContent.setPrefWidth(300);
        metaContent.setAlignment(Pos.TOP_CENTER);
        metaContent.setPadding(new Insets(10));
        metaContent.setSpacing(20);
    }

    public void showSymbols(ObservableList<Symbol> symbols) {
        content.getChildren().clear();
        for (Symbol symbol : symbols) {
            HBox temp = new HBox();
            Label nameLabel = new Label(symbol.getName());
            nameLabel.setStyle("-fx-font-weight: bold;");
            Label descrLabel = new Label(symbol.getDescription());
            descrLabel.setWrapText(true);
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e ->
            {
                if (presenter.onDeleteDreamSymbol(symbol)) {
                    symbols.remove(nameLabel);
                    content.getChildren().remove(temp);
                }
            });
            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> editDreamSymbol(symbol));
            temp.getChildren().addAll(editButton, deleteButton, nameLabel, descrLabel);
            temp.setPadding(new Insets(10));
            temp.setSpacing(20);
            temp.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            deleteButton.setMinWidth(Control.USE_PREF_SIZE);
            editButton.setMinWidth(Control.USE_PREF_SIZE);
            nameLabel.setMinWidth(Control.USE_PREF_SIZE);

            content.getChildren().add(temp);
        }
    }

    private void editDreamSymbol(Symbol symbol) {
        metaContent.getChildren().clear();
        buttons.getChildren().clear();
        Label editDreamSymbolLabel = new Label("Edit Dream Symbol");
        TextField editDreamSymbol = new TextField(symbol.getName());
        editDreamSymbol.setEditable(false);
        TextArea editInterpretation = new TextArea(symbol.getDescription());
        editInterpretation.setWrapText(true);
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e ->
        {
            presenter.onUpdateDreamSymbol(symbol);
            addDreamSymbol();
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> addDreamSymbol());

        buttons.getChildren().addAll(updateButton, cancelButton);
        metaContent.getChildren().addAll(editDreamSymbolLabel, editDreamSymbol, editInterpretation, buttons);
    }

    public void addDreamSymbol() {
        metaContent.getChildren().clear();
        buttons.getChildren().clear();
        Label symbolLabel = new Label("Add new Dream Symbol");
        symbolTextField = new TextField("");
        symbolTextField.setPromptText("Enter Symbol");
        descrTextArea = new TextArea("");
        descrTextArea.setPromptText("Enter Interpretation");
        descrTextArea.setWrapText(true);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e ->
        {
//            presenter.onSaveSymbol(new DreamSymbol(newDreamSymbol.getText(), newInterpretation.getText()));
            presenter.onSaveSymbol(symbolTextField.getText(), descrTextArea.getText());
            symbolTextField.setText("");
            descrTextArea.setText("");
        });

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e ->
        {
            symbolTextField.setText("");
            descrTextArea.setText("");
        });

        buttons.getChildren().addAll(saveButton, resetButton);
        metaContent.getChildren().addAll(symbolLabel, symbolTextField, descrTextArea, buttons);
    }

    public VBox getContent() {
        return content;
    }

    public VBox getMetaContent() {
        return metaContent;
    }
}
