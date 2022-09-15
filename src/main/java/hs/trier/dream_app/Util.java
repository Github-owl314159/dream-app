package hs.trier.dream_app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Util {

    private static Util singleton;
    private VBox contentPane;
    public static final String BASE_URL;
    public static final String ABSOLUTE_BASE_URL;

    static {
        BASE_URL = Util.class.getPackageName().replaceAll("[/.]", "/");
        ABSOLUTE_BASE_URL = "/" + BASE_URL;
    }

    private Util() {

    }

    public static Util getInstance() {
        if (singleton == null) {
            singleton = new Util();
        }
        return singleton;
    }

    public void setContentPane(VBox contentPane) {
        this.contentPane = contentPane;
    }

    public static URL getAbsoluteURL(String path) {
        URL url;
        if (path.startsWith("/"))
            url = Util.class.getResource(ABSOLUTE_BASE_URL + path);
        else
            url = Util.class.getResource(ABSOLUTE_BASE_URL + "/" + path);

        return url;
    }

    public Object loadFXML(String fxml) {
        Node root;
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(Util.getAbsoluteURL(fxml));
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        contentPane.getChildren().clear();               //TODO Martin: Dass der Content in der Util-Klasse geändert wird, da wär ich nach GBO nicht drauf gekommen^^
        contentPane.getChildren().add(root);

        return loader.getController();
    }

    public static LocalDate convertDate (String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

}
