package gameRentalAssistant.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import gameRentalAssistant.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import gameRentalAssistant.helper.Settings;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {


    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXButton loginButton;
    @FXML
    private ImageView redKey, redUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void handleClose(MouseEvent mouseEvent) {
        System.exit(0);
    }

    @FXML
    private void login(ActionEvent actionEvent) {
        String username = this.username.getText();
        String password = this.password.getText();
        if (Settings.getSettings().isValid(username, password)) {
            ((Stage) rootPane.getScene().getWindow()).close();
            loadMain();
        }   else {
            this.username.setText("");
            this.password.setText("");
//            redKey.setVisible(true);
//            redUser.setVisible(true);
            loginButton.setStyle("-fx-background-color: red; -fx-background-radius: 15");
            this.username.requestFocus();
        }
    }

    private void loadMain() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/gameRentalAssistant/view/main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Game rental manager");
            stage.getIcons().add(new Image("/gameRentalAssistant/resources/icon.png"));
            stage.setScene(new Scene(parent));
            stage.show();
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> DatabaseHandler.getInstance().closeConnection()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
