package gameRentalAssistant.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import gameRentalAssistant.helper.AlertHelper;
import gameRentalAssistant.helper.Settings;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    private Settings settings;

    @FXML
    private TextField daysFineFree, fine, login;

    @FXML
    private PasswordField password;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private void confirm(ActionEvent actionEvent) {
        Integer daysFineFree;
        Float fine;
        String username, password;
        try {
            daysFineFree = Integer.parseInt(this.daysFineFree.getText());
            fine = Float.parseFloat(this.fine.getText());
            username = this.login.getText();
            password = this.password.getText();
        } catch (NumberFormatException e) {
            AlertHelper.message(Alert.AlertType.WARNING, "Days without fine and fine should be number values.");
            return;
        }
        if (this.daysFineFree.getText().isEmpty() || this.fine.getText().isEmpty()||username.isEmpty()||password.isEmpty()) return;

        settings = new Settings();
        settings.setDaysFineFree(daysFineFree);
        settings.setFine(fine);
        settings.setUsername(username);
        settings.setPassword(password);
        Settings.saveConfig(settings);
        System.out.println(settings);
        AlertHelper.success();

        cancel(actionEvent);
    }

    @FXML
    private void cancel(ActionEvent actionEvent) {
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    private void initDefaultValues(){
        settings = Settings.getSettings();
        daysFineFree.setText(String.valueOf(settings.getDaysFineFree()));
        fine.setText(String.valueOf(settings.getFine()));
        login.setText(String.valueOf(settings.getUsername()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDefaultValues();
    }
}
