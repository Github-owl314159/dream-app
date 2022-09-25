package hs.trier.dream_app.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import hs.trier.dream_app.controller.AnalyzeDream;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class HttpHandler {

    public static boolean ACTIVE_SESSION = false;
    private static String SESSION_ID;
    private static final String URL = "https://api.replicate.com/v1/predictions";
    private static final String TOKEN_KEY = "48823b03d23f50184e879b36287408f9aacbbea5";
    private static final ScheduledService<Void> subroutine = new ScheduledService<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {

                @Override
                protected Void call() throws UnirestException {
                    System.out.println("1");

                    // check progress
                    HttpResponse<JsonNode> httpResponse = GET();

                    if (httpResponse.getStatus() == 200) {
                        System.out.println("2");
                        // parse body
                        JSONObject jsonObject = new JSONObject(httpResponse.getBody().toString());
                        System.out.println("3");

                        // check status
                        String status = jsonObject.getString("status");
                        System.out.println("4");
                        System.out.println("6");
                        String imageURL = null;
                        switch (status) {
                            case "starting" -> System.out.println("Replicate.com is starting ...");

                            case "processing" -> {
                                System.out.println("Replicate.com is processing ...");

                                JSONArray output = jsonObject.getJSONArray("output");
                                System.out.println("output.length() = " + output.length());
                                if (output.length() != 0) {
                                    imageURL = output.getString(output.length() - 1);
                                    System.out.println("imageURL = " + imageURL);

                                    // load deepDream image
                                    if (imageURL != null) {
                                        try {
                                            Image image = SwingFXUtils.toFXImage(ImageIO.read(new URL(imageURL)), null);
                                            Platform.runLater(() -> AnalyzeDream.getController().setDeepImageView(image));
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            }

                            case "succeeded" -> {
                                System.out.println("Replicate.com has succeeded!");

                                JSONArray output = jsonObject.getJSONArray("output");
                                System.out.println("output.length() = " + output.length());
                                if (output.length() != 0) {
                                    imageURL = output.getString(output.length() - 1);
                                    System.out.println("imageURL = " + imageURL);

                                    // load deepDream image
                                    if (imageURL != null) {
                                        try {
                                            Image image = SwingFXUtils.toFXImage(ImageIO.read(new URL(imageURL)), null);
                                            Platform.runLater(() -> AnalyzeDream.getController().setDeepImageView(image));
                                        } catch (IOException e) {
//                                        endTask();
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
//                                endTask();
                            }

                            case "failed" -> {
                                System.out.println("Replicate.com has failed!");

//                                endTask();
                            }

                            case "canceled" -> {
                                System.out.println("Replicate.com has been canceled!");

//                                endTask();
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

    public static void connectWithAPI(String prompt) throws UnirestException {
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


