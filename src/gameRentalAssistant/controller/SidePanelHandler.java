package gameRentalAssistant.controller;

class SidePanelHandler {
    MainController c;

    SidePanelHandler(MainController c) {
        this.c = c;
    }

    // sets game panel fields editable or not, disables or enables game panel buttons
    void toggleEditGamePanel() {
        c.gameInfoLeftButton.setVisible(!c.gameInfoRightButton.isVisible());
        c.gameInfoRightButton.setVisible(!c.gameInfoRightButton.isVisible());

        c.txtGameLanguage.setEditable(!c.txtGameLanguage.isEditable());
        c.txtGameName.setEditable(!c.txtGameName.isEditable());
        c.txtGameEAN.setEditable(!c.txtGameEAN.isEditable());
        c.txtGamePublisher.setEditable(!c.txtGamePublisher.isEditable());

        String color = c.txtGameLanguage.isEditable()?"000000":"AAAAAA";

        c.txtGameName.setStyle("-fx-text-fill: #" + color);
        c.txtGameLanguage.setStyle("-fx-text-fill: #" + color);
        c.txtGameEAN.setStyle("-fx-text-fill: #" + color);
        c.txtGamePublisher.setStyle("-fx-text-fill: #" + color);
        c.gameToggleButton.setDisable(true);
    }

    // sets user panel fields editable or not, disables or enables user panel buttons
    void toggleEditUserPanel() {
         c.userProfileRightButton.setVisible(!c.userProfileRightButton.isVisible());
         c.userProfileLeftButton.setVisible(!c.userProfileLeftButton.isVisible());

         c.txtUserName.setEditable(!c.txtUserName.isEditable());
         c.txtUserAddress.setEditable(!c.txtUserAddress.isEditable());
         c.txtUserPhone.setEditable(!c.txtUserPhone.isEditable());
         c.txtUserEmail.setEditable(!c.txtUserEmail.isEditable());

         String color = c.txtUserName.isEditable()?"000000":"AAAAAA";

         c.txtUserEmail.setStyle("-fx-text-fill: #" + color);
         c.txtUserName.setStyle("-fx-text-fill: #" + color);
         c.txtUserPhone.setStyle("-fx-text-fill: #" + color);
         c.txtUserAddress.setStyle("-fx-text-fill: #" + color);
         c.userToggleButton.setDisable(true);
     }
    // disables
    // editing options and buttons rollback previous data
     void cancelGame() {
        toggleEditGamePanel();
        c.loadGameInfo();
        c.gameToggleButton.setSelected(false);
     }

     // disables editing options and buttons rollback previous data
     void cancelUser() {
        toggleEditUserPanel();
        c.loadUserInfo();
        c.userToggleButton.setSelected(false);
     }

}
