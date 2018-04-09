package gameRentalAssistant.controller;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import gameRentalAssistant.database.DatabaseHandler;
import gameRentalAssistant.helper.AlertHelper;
import gameRentalAssistant.helper.User;

import java.net.URL;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {

    private DatabaseHandler databaseHandler;
    private User user;
    private MainController controller;

    public void setController(MainController controller) {
        this.controller = controller;
    }

    @FXML
    private TextField name, phone, email, address;

    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler  = DatabaseHandler.getInstance();
    }


    @FXML
    public void addUser() {
        String name = this.name.getText();
        String address = this.address.getText();
        String phone = this.phone.getText();
        String email = this.email.getText();

        if( name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
            AlertHelper.insufficientData();
            return;
        }

        String query = "INSERT INTO MEMBER (name, address, phone, email) VALUES (" +
                "'" + name + "', " +
                "'" + address + "', " +
                "'" + phone + "', " +
                "'" + email + "' " +
                ")";

        if (databaseHandler.execAction(query))
            AlertHelper.addedSuccessfully();
        else
            AlertHelper.somethingWentWrong();

        cancel();
    }

    @FXML
    public void cancel() {
        ((Stage) rootPane.getScene().getWindow()).close();
        controller.loadUsersTable();
    }

    @FXML
    private void clearForm() {
        name.setText("");
        phone.setText("");
        email.setText("");
        address.setText("");
    }
}

