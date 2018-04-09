package gameRentalAssistant.controller;


import gameRentalAssistant.helper.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import gameRentalAssistant.database.DatabaseHandler;
import gameRentalAssistant.helper.AlertHelper;

import java.net.URL;
import java.util.ResourceBundle;

public class AddGameController implements Initializable {

    private DatabaseHandler databaseHandler;
    private MainController controller;
    private Game game;

    public void setController(MainController controller) {
        this.controller = controller;
    }

    @FXML
    private TextField name, language, publisher, ean;

    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler  = DatabaseHandler.getInstance();
    }

    @FXML
    public void addGame() {
        String gameLanguage = language.getText();
        String gamePublisher = publisher.getText();
        String gameEAN = ean.getText();
        String gameName = name.getText();

        if ( gameLanguage.isEmpty() || gamePublisher.isEmpty() || gameName.isEmpty() || gameEAN.isEmpty()) {
            AlertHelper.insufficientData();
            return;
        }

        String query = "";
        String windowTitle = ((Stage) rootPane.getScene().getWindow()).getTitle();

        if (windowTitle.toLowerCase().contains("add"))
            query = "INSERT INTO GAME (name, language, ean, publisher, isAvail)  VALUES (" +
                    "'" + gameName + "', " +
                    "'" + gameLanguage + "', " +
                    "'" + gameEAN + "', " +
                    "'" + gamePublisher + "', " +
                    "true )";
        else if(windowTitle.toLowerCase().contains("edit"))
            query = "UPDATE GAME SET " +
                    "name='" + gameName + "', " +
                    "language='" + gameLanguage + "'," +
                    "publisher='" + gamePublisher +"'," +
                    "ean='" + gameEAN + "WHERE id=" + game.getId();

//
//        System.out.println(query);

        if (databaseHandler.execAction(query))
            AlertHelper.addedSuccessfully();
        else
            AlertHelper.somethingWentWrong();

        cancel();
    }

    @FXML
    public void cancel( ) {
        ((Stage) rootPane.getScene().getWindow()).close();
        controller.loadGameTable();
    }

    @FXML
    private void clearForm() {
        name.setText("");
        language.setText("");
        publisher.setText("");
        ean.setText("");
    }
}

