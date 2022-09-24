package hs.trier.dream_app.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import hs.trier.dream_app.controller.AnalyzeDream;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class Router {

    private static final String URL = "https://api.replicate.com/v1/predictions";

    private static HttpResponse<JsonNode> GET(String id) throws UnirestException {
        return Unirest.get(URL + "/" + id)
                .header("Authorization", "Token 48823b03d23f50184e879b36287408f9aacbbea5")
                .header("Content-Type", "application/json")
                .asJson();
    }

    private static HttpResponse<JsonNode> POST(String prompt) throws UnirestException {
        Unirest.setTimeouts(0, 0);
        return Unirest.post(URL)
                .header("Authorization", "Token 48823b03d23f50184e879b36287408f9aacbbea5")
                .header("Content-Type", "application/json")
                .body(
                        "{\"version\": \"5c347a4bfa1d4523a58ae614c2194e15f2ae682b57e3797a5bb468920aa70ebf\", \"input\": {\"text\": \"" + prompt + "\"}}"
                )
                .asJson();
    }

    public static void routing(String prompt) throws UnirestException {
        HttpResponse<JsonNode> jsonNode = POST(prompt);
        if (jsonNode.getStatus() == 201) {
            JSONObject jsonBody = new JSONObject(jsonNode.getBody().toString());
            String id = jsonBody.getString("id");
            subroutine(id);
        }
    }

    private static void subroutine(String id) throws UnirestException {
        HttpResponse<JsonNode> jsonNode = GET(id);
        if (jsonNode.getStatus() == 200) {
            JSONObject jsonBody = new JSONObject(jsonNode.getBody().toString());
            String status = jsonBody.getString("status");
            switch (status) {
                case "starting" -> {
                    System.out.println("starting");
                    System.out.println(jsonBody.toString());
                    try {
                        Thread.sleep(60_000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    subroutine(id);
                }
                case "processing" -> {
                    System.out.println("processing");
                    System.out.println(jsonBody.toString());
                    JSONArray output = jsonBody.getJSONArray("output");
                    String urlString = output.getString(output.length() - 1);
                    System.out.println(urlString);
                    try {
                        Image image = SwingFXUtils.toFXImage(ImageIO.read(new URL(urlString)), null);
                        AnalyzeDream.getController().setDeepImageView(image);
                        Thread.sleep(60_000);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    subroutine(id);
                }
                case "succeeded" -> {
                    System.out.println("succeeded");
                    System.out.println(jsonBody.toString());
                    JSONArray output = jsonBody.getJSONArray("output");
                    String urlString = output.getString(output.length() - 1);
                    System.out.println(urlString);
                    try {
                        Image image = SwingFXUtils.toFXImage(ImageIO.read(new URL(urlString)), null);
                        AnalyzeDream.getController().setDeepImageView(image);
                        Thread.sleep(60_000);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "failed" -> {
                    System.out.println("failed");
                    System.out.println(jsonBody.toString());
                }
                case "canceled" -> {
                    System.out.println("canceled");
                    System.out.println(jsonBody.toString());
                }
            }
        }
    }
}