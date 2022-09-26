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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.stage.Modality;

import java.io.IOException;
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

    private Dream currentDream;
    private StringBuilder prompt;

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
        currentDream = selectedItem;

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
        for (String token : tokens) {
            foundsomething = false;
            for (AnalyzedToken analyzedToken : filteredTokens) {
                if (token.toLowerCase().equals(analyzedToken.getToken())) {
                    List<Symbol> foundSymbols = SymbolDAO.searchSymbols(analyzedToken.getLemma());
                    if (!foundSymbols.isEmpty()) {
                        foundsomething = true;
                        sb.append("<span style=\"color: #815ADD;\"><b>" + token + "</b></span>");
                        for (Symbol symbol : foundSymbols) {
                            matchesSet.add(symbol);
                            //System.out.println("Added match: " + symbol.getName());             //TODO
                        }
                    }
                }
            }
            if (!foundsomething) {
                sb.append(token);
            }
        }

        // Convert Set to Observable List and populate Listview
        ObservableList<Symbol> matchesList = FXCollections.observableArrayList(new ArrayList<>());
        for (Object symbol : matchesSet) {
            matchesList.add((Symbol) symbol);
        }

        // sort list and select first item
        matchesListView.setItems(matchesList.sorted());
        if (!matchesListView.getItems().isEmpty()) {
            matchesListView.getSelectionModel().select(0);
        }

        // Load rebuilt string into WebView
        dreamContent.getEngine().loadContent(sb.toString(), "text/html");
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
//        try {
//            File file = new File("downloaded.png");
//            BufferedImage image = ImageIO.read(new URL("https://replicate.com/api/models/pixray/text2image/files/d49b56e4-489c-49b8-8de8-29655076a177/tempfile.png"));
//            ImageIO.write((RenderedImage) image, "png", file);
//            byte[] bytes = Files.readAllBytes(file.toPath());
//            currentDream.setThumbnail(bytes);
//            DreamDAO.update(currentDream);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        // create prompt for API
        prompt = new StringBuilder();
        ListIterator<Symbol> iterator = matchesListView.getItems().listIterator();
        while (iterator.hasNext()) {
            Symbol symbol = iterator.next();
            prompt.append(symbol.getName());
            if (iterator.nextIndex() != matchesListView.getItems().size())
                prompt.append(" | ");
        }
        System.out.println("prompt.toString() = " + prompt.toString());

        deepDreamDialog();
    }

    public void saveDeepImage(Image image) {
        currentDream.setThumbnail(image);
    }

    public void deepDreamDialog() {
        // init dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setResizable(true);

        // load dialog fxml
        DialogPane dialogPane;
        try {
            dialogPane = FXMLLoader.load(Objects.requireNonNull(Util.getAbsoluteURL("views/dialogs/deepdream-dialog.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // get handles on input fields/labels
        ListView<String> symbolsListView = (ListView<String>) dialogPane.lookup("#symbolsListView");
        TextArea promptTextArea = (TextArea) dialogPane.lookup("#promptTextArea");
        TextField promptExtTextField = (TextField) dialogPane.lookup("#promptExtTextField");

        promptExtTextField.textProperty().addListener((observableValue, oldV, newV) -> {
            promptTextArea.clear();
            promptTextArea.setText(prompt.toString() + " | " + newV);
        });

        matchesListView.getItems().forEach(symbol -> symbolsListView.getItems().add(symbol.getName()));
        promptTextArea.setText(prompt.toString());

        // convert user data to Symbol and save it if user clicked on OK button
        dialog.setResultConverter((buttonType) -> {
            String result = null;

            if (buttonType == ButtonType.OK)
                result = promptTextArea.getText();

            return result;
        });

        // disable other windows
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(matchesListView.getScene().getWindow());

        dialog.setDialogPane(dialogPane);
        Optional<String> resultOpt = dialog.showAndWait();

        // start API request
        resultOpt.ifPresent(prompt1 -> {
            try {
                HttpHandler.connectWithAPI(prompt1, currentDream);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
