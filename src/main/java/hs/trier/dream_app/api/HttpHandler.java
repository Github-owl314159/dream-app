package hs.trier.dream_app.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import hs.trier.dream_app.controller.AnalyzeDream;
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

    private static boolean ACTIVE_SESSION = false;
    private static String SESSION_ID;
    private static final String URL = "https://api.replicate.com/v1/predictions";
    private static final String TOKEN_KEY = "";


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
                        "{\"version\": \"5c347a4bfa1d4523a58ae614c2194e15f2ae682b57e3797a5bb468920aa70ebf\", \"input\": {\"prompts\": \"" + prompt + "\"}}"
                )
                .asJson();
    }

    public static void start(String prompt) throws UnirestException {
        // POST request
        HttpResponse<JsonNode> jsonNode = POST(prompt);

        if (jsonNode.getStatus() == 201) {
            // server created resource

            // parse body
            JSONObject jsonBody = new JSONObject(jsonNode.getBody().toString());
            ACTIVE_SESSION = true;
            SESSION_ID = jsonBody.getString("id");
            System.out.println("Session: " + SESSION_ID + "\nPOST: " + jsonNode.getBody().toString() + "\n");

            // prepare scheduleService
            final ScheduledService<Void> service = new ScheduledService<>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<>() {
                        @Override
                        protected Void call() throws UnirestException {
                            // check progress
                            HttpResponse<JsonNode> jsonNode = GET();

                            if (jsonNode.getStatus() == 200) {
                                // parse body
                                JSONObject jsonBody = new JSONObject(jsonNode.getBody().toString());
                                // check status
                                String status = jsonBody.getString("status");

                                switch (status) {
                                    case "starting" -> System.out.println("Replicate.com is starting ...");
                                    case "processing" -> {
                                        System.out.println("Replicate.com is processing ...");
                                        JSONArray output = jsonBody.getJSONArray("output");
                                        String urlString = output.getString(output.length() - 1);
                                        Image image;
                                        try {
                                            image = SwingFXUtils.toFXImage(ImageIO.read(new URL(urlString)), null);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                        AnalyzeDream.getController().setDeepImageView(image);
                                    }
                                    case "succeeded" -> {
                                        System.out.println("Replicate.com has succeeded!");
                                        JSONArray output = jsonBody.getJSONArray("output");
                                        String urlString = output.getString(output.length() - 1);
                                        Image image;
                                        try {
                                            image = SwingFXUtils.toFXImage(ImageIO.read(new URL(urlString)), null);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
//                                        AnalyzeDream.getController().setDeepImageView(image);
                                        cancel();
                                        ACTIVE_SESSION = false;
                                    }
                                    case "failed" -> {
                                        System.out.println("Replicate.com has failed!");
                                        cancel();
                                        ACTIVE_SESSION = false;
                                    }
                                    case "canceled" -> {
                                        System.out.println("Replicate.com has been canceled!");
                                        cancel();
                                        ACTIVE_SESSION = false;
                                    }
                                }
                            } else {
                                ACTIVE_SESSION = false;
                                Alert error = new Alert(Alert.AlertType.ERROR);

                                error.setHeaderText("Check progress error");
                                System.out.println("Check progress error");
                                error.show();
                            }
                            return null;
                        }
                    };
                }
            };
            service.setPeriod(Duration.seconds(15));
            service.setDelay(Duration.ZERO);
            service.setRestartOnFailure(true);
            service.setMaximumFailureCount(100);
            service.start();
        } else {
            ACTIVE_SESSION = false;
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("API is unresponsive. Aborting.");
            error.show();
        }
    }

    public static void cancel() throws UnirestException {
        if (ACTIVE_SESSION) {
            Unirest.post(URL + "/" + SESSION_ID + "/cancel")
                    .header("Authorization", "Token " + TOKEN_KEY)
                    .asJson();
        }
    }
}