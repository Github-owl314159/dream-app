package hs.trier.dream_app.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import hs.trier.dream_app.Util;
import hs.trier.dream_app.api.HttpHandler;
import hs.trier.dream_app.dao.SymbolDAO;
import hs.trier.dream_app.model.AnalyzedToken;
import hs.trier.dream_app.model.Dream;
import hs.trier.dream_app.model.NLP;
import hs.trier.dream_app.model.Symbol;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import java.util.*;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;

public class AnalyzeDream {
    @FXML
    private ListView<Symbol> matchesListView;
    @FXML
    private ImageView deepImageView;
    @FXML
    private Button getDeepDreamButton;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private WebView dreamContent = new WebView();

    private ObservableList<Symbol> matchesList;

    @FXML
    private void initialize() {

        // set event listener
        getDeepDreamButton.setOnAction(this::getDeepDream);

        // callback: populate preview fields whenever selected item changes
        matchesListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, ov, nv) -> descriptionTextArea.setText(nv.getDescription()));

        // disable button if no match is available
        //getDeepDreamButton.disableProperty().bind(Bindings.isEmpty(matchesList));
    }

    public void analyze(Dream selectedItem) {

        //merge dream title and content in string to analyze, change linebreaks to HTML
        String stringToAnalyze = "Title: " + selectedItem.getTitle() + "<br><br>";
        stringToAnalyze = stringToAnalyze + selectedItem.getContent().replaceAll(Pattern.quote("\n"), "<br>");

        // tokenize the string
        String[] tokens = NLP.tokenize(stringToAnalyze);

        // get tokens to search for
        List<AnalyzedToken> filteredTokens = NLP.lemmatize(NLP.filter(tokens));

        // rebuild original string, mark found dream symbols red and search for dream symbols. Use Set to remove duplicates.
        StringBuilder sb = new StringBuilder();
        Set<Symbol> matchesSet = new HashSet<>();
        Boolean foundsomething;
        for ( String token : tokens) {
            foundsomething = false;
            for (AnalyzedToken analyzedToken : filteredTokens) {
                if (token.toLowerCase().equals(analyzedToken.getToken())) {
                    List<Symbol> foundSymbols = SymbolDAO.searchSymbols(analyzedToken.getLemma());
                    if (!foundSymbols.isEmpty()) {
                        foundsomething = true;
                        sb.append("<span style=\"color: red;\">" + token + "</span>");
                        for (Symbol symbol : foundSymbols) {
                            matchesSet.add(symbol);
                            System.out.println("Added match: " + symbol.getName());             //TODO

                        }
                    }
                }
            }
            if (!foundsomething) {
                sb.append(token);
            }
        }

        // Convert Set to Observable List and populate Listview
        matchesList = FXCollections.observableArrayList(new ArrayList<>());
        for (Object symbol : matchesSet) {
            matchesList.add((Symbol) symbol);
        }
        matchesListView.setItems(matchesList.sorted());

        // Load rebuilt string into WebView
        dreamContent.getEngine().loadContent(sb.toString(),"text/html");
    }

    public static AnalyzeDream getController() {
        return (AnalyzeDream) Util.getInstance().loadFXML("views/analyze.fxml");
    }

    public void setDeepImageView(Image image) {
        deepImageView.setImage(image);
    }

    @FXML
    private void cancel(ActionEvent actionEvent) {
        try {
            HttpHandler.cancel();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

        actionEvent.consume();
    }

    private void getDeepDream(ActionEvent actionEvent) {
        // create prompt for API
        StringBuilder prompt = new StringBuilder();
        ListIterator<Symbol> iterator = matchesList.listIterator();
        while (iterator.hasNext()) {
            Symbol symbol = iterator.next();
            prompt.append(symbol.getName());
            if (iterator.nextIndex() != matchesList.size())
                prompt.append(" | ");
        }

        // start API request
        try {
            HttpHandler.start(prompt.toString());
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }
}
