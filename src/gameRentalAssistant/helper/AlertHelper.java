package gameRentalAssistant.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertHelper {
    public static void message(Alert.AlertType alertType, String contextText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(contextText);
        alert.showAndWait();
    }

    public static void insufficientData(){
        message(Alert.AlertType.ERROR, "Please enter all fields." );
    }

    public static void addedSuccessfully() {
        message(Alert.AlertType.INFORMATION, "Item added successfully!");
    }

    public static void success() {
        message(Alert.AlertType.INFORMATION, "Success!");
    }

    public static void somethingWentWrong() {
        message(Alert.AlertType.ERROR, "Something went wrong :(");
    }

    public static Optional<ButtonType> confirmation(String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, contentText);
        alert.setTitle(null);
        alert.setHeaderText(null);
        Optional<ButtonType> response = alert.showAndWait();
        return response;
    }
}