package gameRentalAssistant.UILauncher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import gameRentalAssistant.database.DatabaseHandler;

import java.io.IOException;

public class MainLauncher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("../view/main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Library assistant");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(() -> DatabaseHandler.getInstance()).start();

        Runtime.getRuntime().addShutdownHook(new Thread( () -> DatabaseHandler.getInstance().closeConnection()));

    }


    public static void main(String[] args) {
        launch(args);

    }
}
