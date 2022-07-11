package hs.trier.dream_app.api;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

public class DeepImages {

    private static String getDeepImage() {
        try {
            HttpResponse<JsonNode> httpResponse = Unirest.get("https://dog.ceo/api/breeds/image/random").asJson();
            Gson gson = new Gson();
            DeepImagesResponse deepImagesResponse = gson.fromJson(httpResponse.getBody().toString(), DeepImagesResponse.class);

            return deepImagesResponse.message();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Optional<BufferedImage> getImage() {
        try {
            return Optional.ofNullable(ImageIO.read(new URL(Objects.requireNonNull(getDeepImage()))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
