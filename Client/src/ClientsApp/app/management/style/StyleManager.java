package ClientsApp.app.management.style;

import javafx.scene.Scene;

public class StyleManager {
    private static final String DEFAULT_THEME = "app/management/style/darkTheme.css";

    private static final String LIGHT_THEME = "app/management/style/lightTheme.css";

    private static String currentTheme = DEFAULT_THEME;

    public static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(currentTheme);
    }
    public static void setTheme(String lightOrDark) {
        if (lightOrDark.equals("light"))
            currentTheme = LIGHT_THEME;
        else if (lightOrDark.equals("dark"))
            currentTheme = DEFAULT_THEME;
    }
    public static void onlySetTheme(String lightOrDark) {
        if (lightOrDark.equals("light"))
            currentTheme = LIGHT_THEME;
        else if (lightOrDark.equals("dark"))
            currentTheme = DEFAULT_THEME;

    }

    public static String getCurrentTheme() {
        if (currentTheme.equals(LIGHT_THEME))
            return "light";
        else
            return "dark";
    }
}