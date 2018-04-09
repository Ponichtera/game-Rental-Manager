package gameRentalAssistant.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class WindowLoader {
    MainController c;

    public WindowLoader(MainController c) {
        this.c = c;
    }

    static FXMLLoader loadWindow(URL path, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(path);
            Parent parent = loader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.getIcons().add(new Image("/gameRentalAssistant/resources/icon.png"));
            stage.show();
            return loader;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
