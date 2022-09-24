package hs.trier.dream_app.controller;

import hs.trier.dream_app.Util;
import hs.trier.dream_app.dao.SymbolDAO;
import hs.trier.dream_app.model.AnalyzedToken;
import hs.trier.dream_app.model.Dream;
import hs.trier.dream_app.model.NLP;
import hs.trier.dream_app.model.Symbol;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class AnalyzeDream {
    public ListView<Symbol> matchesListView;
    @FXML
    private ImageView deepImageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Button getDeepDream;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private WebView dreamContent = new WebView();

    private ObservableList<Symbol> matches;

    @FXML
    private void initialize() {
        // display name of symbol in listView
        matchesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Symbol item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        // init list for found matches
        matches = FXCollections.observableArrayList();

        // set event listener
        getDeepDream.setOnAction(this::getDeepDream);

        // callback: populate preview fields whenever selected item changes
        matchesListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, ov, nv) -> descriptionTextArea.setText(nv.getDescription()));
    }

    public void analyze(Dream selectedItem) {
        // clear list
        matches.clear();

        // set title
        titleLabel.setText(selectedItem.getTitle());

        // tokenize content of selected dream
        List<String> tokens = List.of(NLP.tokenize(selectedItem.getContent()));

        // get tokens to search for
        List<AnalyzedToken> analyzedTokens = NLP.lemmatize(NLP.filter(tokens));

        StringBuilder sb = new StringBuilder();
        boolean match = false;
        for (String token : tokens) {
            match = false;
            for (AnalyzedToken analyzedToken : analyzedTokens) {

                if (token.equalsIgnoreCase(analyzedToken.getToken())) {

                    List<Symbol> foundSymbols = SymbolDAO.searchSymbols(analyzedToken.getLemma());
                    if (!foundSymbols.isEmpty()) {
                        match = true;
                        sb.append("<span style=\"color: red;\">").append(token).append("</span>");
                        matches.addAll(foundSymbols);
                    }
                }
            }
            if (!match)
                sb.append(token);
        }

        matchesListView.setItems(matches);
        dreamContent.getEngine().loadContent(sb.toString(), "text/html");
    }

    public static AnalyzeDream getController() {
        FXMLLoader loader = new FXMLLoader(Util.getAbsoluteURL("views/analyze.fxml"));
        return loader.getController();
    }

    public void setDeepImageView(Image image) {
        deepImageView.setImage(image);
    }

    private void getDeepDream(ActionEvent actionEvent) {
        try {
            Image image = SwingFXUtils.toFXImage(ImageIO.read(new URL("https://replicate.com/api/models/pixray/text2image/files/d49b56e4-489c-49b8-8de8-29655076a177/tempfile.png")), null);
            deepImageView.setImage(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        try {
//            DeepImages.routing("Alice");
//            DeepImages.routing("Water");
//        } catch (UnirestException e) {
//            throw new RuntimeException(e);
//        }
//        deepImageView.setImage(image);
    }
}
