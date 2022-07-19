package hs.trier.dream_app;

import java.net.URL;

public class Util {
    public static final String BASE_URL;
    public static final String ABSOLUTE_BASE_URL;

    static {
        BASE_URL = Util.class.getPackageName().replaceAll("[/.]", "/");
        ABSOLUTE_BASE_URL = "/" + BASE_URL;
    }

    public static URL getAbsoluteURL(String path) {
        URL url;
        if (path.startsWith("/"))
            url = Util.class.getResource(ABSOLUTE_BASE_URL + path);
        else
            url = Util.class.getResource(ABSOLUTE_BASE_URL + "/" + path);

        return url;
    }
}
