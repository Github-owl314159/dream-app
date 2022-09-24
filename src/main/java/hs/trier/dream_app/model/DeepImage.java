package hs.trier.dream_app.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class DeepImage {
    private final static ObjectProperty<Image> DEEP_IMG = new SimpleObjectProperty<>();

    public static Image getDeepImage() {
        return DEEP_IMG.get();
    }

    public static ObjectProperty<Image> deepImageProperty() {
        return DEEP_IMG;
    }

    public static void loadNewDeepImage() {
//        AnalyzeDream.getImage().ifPresent(DeepImage::updateDeepImage);
    }

    private static void updateDeepImage(BufferedImage image) {
        DEEP_IMG.set(SwingFXUtils.toFXImage(image, null));
    }
}
