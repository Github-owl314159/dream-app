package hs.trier.dream_app.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import hs.trier.dream_app.Util;
import hs.trier.dream_app.model.Dream;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class HttpHandler {

    public static boolean ACTIVE_SESSION = false;
    private static String SESSION_ID;
    private static final String URL = "https://api.replicate.com/v1/predictions";
    private static final String TOKEN_KEY = "";
    private static ImageView deepDreamImageView;
    private static Dream currentDream;
    private static Label statusLabel;
    private static ProgressIndicator progressIndicator;
    private static final ScheduledService<Void> subroutine = new ScheduledService<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {

                private void endTask() {
                    ACTIVE_SESSION = false;
                    cancel();
                    System.out.println("Session finished.");
                }

                @Override
                protected Void call() throws UnirestException {
                    // check progress
                    HttpResponse<JsonNode> httpResponse = GET();

                    if (httpResponse.getStatus() == 200) {
                        // parse body
                        JSONObject jsonObject = new JSONObject(httpResponse.getBody().toString());
                        JSONArray outputJSON = null;
                        if (!jsonObject.isNull("output")) {
                            outputJSON = jsonObject.getJSONArray("output");
                        }

                        // check status
                        String status = jsonObject.getString("status");
                        String imageURL;
                        switch (status) {
                            case "starting" -> {
                                String statusMessage = "Replicate.com is starting ...\n";
                                System.out.println(statusMessage);
                                Platform.runLater(() -> {
                                    statusLabel.setText(statusMessage);
                                });
                            }
                            case "processing" -> {
                                String statusMessage = "Replicate.com is processing ...\n";
                                System.out.println(statusMessage);
                                Platform.runLater(() -> {
                                    statusLabel.setText(statusMessage);
                                });

                                if (outputJSON != null && outputJSON.length() != 0) {
                                    imageURL = outputJSON.getString(outputJSON.length() - 1);

                                    // load deepDream image
                                    if (imageURL != null) {
                                        try {
                                            Image image = SwingFXUtils.toFXImage(ImageIO.read(new URL(imageURL)), null);
                                            Platform.runLater(() -> deepDreamImageView.setImage(image));
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            }
                            case "succeeded" -> {
                                String statusMessage = "Replicate.com has succeeded!\nYou can close the window now.";
                                System.out.println(statusMessage);
                                Platform.runLater(() -> {
                                    statusLabel.setText(statusMessage);
                                    progressIndicator.setDisable(true);
                                });

                                if (outputJSON != null && outputJSON.length() != 0) {
                                    imageURL = outputJSON.getString(outputJSON.length() - 1);

                                    if (imageURL != null) {
                                        // load deepDream image
                                        try {
                                            Image image = SwingFXUtils.toFXImage(ImageIO.read(new URL(imageURL)), null);
                                            Platform.runLater(() -> {
                                                        deepDreamImageView.setImage(image);
                                                        currentDream.setThumbnail(image);
                                                    }
                                            );
                                        } catch (IOException e) {
                                            endTask();
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                                endTask();
                            }
                            case "failed" -> {
                                String statusMessage = "Replicate.com has failed!\n";
                                System.out.println(statusMessage);
                                Platform.runLater(() -> {
                                    statusLabel.setText(statusMessage);
                                });

                                endTask();
                            }
                            case "canceled" -> {
                                String statusMessage = "Replicate.com has been cancelled!\n";
                                System.out.println(statusMessage);
                                Platform.runLater(() -> {
                                    statusLabel.setText(statusMessage);
                                    progressIndicator.setDisable(true);
                                });

                                if (outputJSON != null && outputJSON.length() != 0) {
                                    imageURL = outputJSON.getString(outputJSON.length() - 1);

                                    // load deepDream image
                                    if (imageURL != null) {
                                        try {
                                            Image image = SwingFXUtils.toFXImage(ImageIO.read(new URL(imageURL)), null);
                                            Platform.runLater(() -> {
                                                        deepDreamImageView.setImage(image);
                                                        currentDream.setThumbnail(image);
                                                    }
                                            );
                                        } catch (IOException e) {
                                            endTask();
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }

                                endTask();
                            }
                        }
                    } else {
                        System.out.println("Error: could not check progress.");
                    }

                    return null;
                }
            };
        }
    };

    private static HttpResponse<JsonNode> GET() throws UnirestException {
        return Unirest.get(URL + "/" + SESSION_ID)
                .header("Authorization", "Token " + TOKEN_KEY)
                .header("Content-Type", "application/json")
                .asJson();
    }

    private static HttpResponse<JsonNode> POST(String prompt) throws UnirestException {
        Unirest.setTimeouts(0, 0);
        return Unirest.post(URL)
                .header("Authorization", "Token " + TOKEN_KEY)
                .header("Content-Type", "application/json")
                .body(
                        String.format("{\"version\": \"5c347a4bfa1d4523a58ae614c2194e15f2ae682b57e3797a5bb468920aa70ebf\"," +
                                " \"input\": {\"prompts\": \"%s\"}}", prompt)

                )
                .asJson();
    }

    public static void connectWithAPI(String prompt, Dream dream) throws UnirestException {
        // save current dream
        currentDream = dream;

        // POST request
        HttpResponse<JsonNode> httpResponse = POST(prompt);

        if (httpResponse.getStatus() == 201) { // <-- server created resource
            ACTIVE_SESSION = true;
            // parse body
            JSONObject jsonObject = new JSONObject(httpResponse.getBody().toString());
            // get session id
            SESSION_ID = jsonObject.getString("id");
            // print information
            System.out.println("Session: " + SESSION_ID + "\nPOST: " + httpResponse.getBody().toString() + "\n");

            // init dialog
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Deep Dream Image");
            dialog.setResizable(true);

            // load dialog fxml
            DialogPane dialogPane;
            try {
                dialogPane = FXMLLoader.load(Objects.requireNonNull(Util.getAbsoluteURL("views/dialogs/deepdream-waiting-room.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            deepDreamImageView = (ImageView) dialogPane.lookup("#deepDreamImageView");
            statusLabel = (Label) dialogPane.lookup("#statusLabel");
            progressIndicator = (ProgressIndicator) dialogPane.lookup("#progressIndicator");

            dialogPane.getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(dialogPane);

            dialog.show();

            startSubroutine();
        } else {
            ACTIVE_SESSION = false;
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("API is unresponsive. Aborting.");
            error.show();
        }
    }

    private static void startSubroutine() {
        // task repeats after X seconds
        subroutine.setPeriod(Duration.seconds(15));
        subroutine.setDelay(Duration.seconds(1));
        // restart on failure, maximum of X tries
        subroutine.setRestartOnFailure(true);
        subroutine.setMaximumFailureCount(4);

        // finally start subroutine
        subroutine.start();
    }

    public static void cancel() throws UnirestException {
        if (ACTIVE_SESSION) {
            Unirest.post(URL + "/" + SESSION_ID + "/cancel")
                    .header("Authorization", "Token " + TOKEN_KEY)
                    .asJson();
        }
    }
}


