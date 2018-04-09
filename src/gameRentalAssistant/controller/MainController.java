package gameRentalAssistant.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import gameRentalAssistant.helper.*;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    public User enteredUser;
    public Game enteredGame;
    public ObservableList<Game> allGames = FXCollections.observableArrayList();
    public ObservableList<User> allUsers = FXCollections.observableArrayList();

    // handlers
    private GameHandler gameHandler;
    private UserHandler userHandler;
    private SidePanelHandler sidePanelHandler;
    private AddGameController addGameController;
    private AddUserController addUserController;
    private IssueHelper issueHelper;

//  FXML fields
    @FXML
    public JFXTextField enterGameIDField, enterUserIDField;

    @FXML
    public JFXButton borrowButton, payFineButton, renewButton, returnButton, userProfileLeftButton, userProfileRightButton,
            gameInfoRightButton, gameInfoLeftButton;

    @FXML
    public TableView<Game> allGamesTableView;

    @FXML
    public TableView<User> allUsersTableView;

    @FXML
    public TableView<IssueHelper.IssueEntry> borrowedGamesTable;

    @FXML
    public TableColumn<Game, String> allGamesColumnID, allGamesColumnLanguage, allGamesColumnName, allGamesColumnEAN, allGamesColumnPublisher, allGamesColumnStatus;

    @FXML
    public TableColumn<User, String> allMembersColumnID, allMembersColumnName, allMembersColumnAddress,
            allMembersColumnFine, allMembersColumnEmail, allMembersColumnPhone, allMembersColumnGames;

    @FXML
    public ImageView settingsButton, removeUserButton, removeGameButton;

    @FXML
    public TextField txtUserName, txtUserAddress, txtUserPhone, txtUserEmail, txtUserFine, txtGameName,
            txtGameStatus, txtGamePublisher, txtGameEAN, txtGameLanguage;

    @FXML
    public Label txtUserId, txtGameId;

    @FXML
    public JFXToggleButton gameToggleButton, userToggleButton;
    @FXML
    public TableColumn<IssueHelper.IssueEntry, Timestamp> dateIssueColumn;
    @FXML
    public TableColumn<IssueHelper.IssueEntry, Integer> renewCountIssueColumn;
    @FXML
    public TableColumn<IssueHelper.IssueEntry, Float> fineIssueColumn;
    @FXML
    public TableColumn<IssueHelper.IssueEntry, String> gameIdIssueColumn, nameIssueColumn, languageIssueColumn;
    @FXML
    public Label userIdLabel, gameIdLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameHandler = new GameHandler(this);
        userHandler = new UserHandler(this);
        sidePanelHandler = new SidePanelHandler(this);
        issueHelper = new IssueHelper(this);
    }

//    side panel

    // loads details about the user into user side panel, changes focus to enter game id field
    @FXML
    void loadUserInfo() {
        if(userHandler.loadUserInfo())
            enterGameIDField.requestFocus();
    }

    // loads details about the game into game side panel, changes focus to borrow button
    @FXML
    void loadGameInfo() {
        gameHandler.loadGameInfo();
        borrowButton.requestFocus();
    }

    // clears all fields on user side panel
    @FXML
    void clearUser() { userHandler.clearUser(); }

    // clears all fields on game side panel
    @FXML
    void clearGame() { gameHandler.clearGame(); }

    // sets Game panel fields editable or not, disables or enables game panel buttons
    @FXML
    private void toggleEditGamePanel() { sidePanelHandler.toggleEditGamePanel(); }

    // sets user panel fields editable or not, disables or enables user panel buttons
    @FXML
    private void toggleEditUserPanel( ) { sidePanelHandler.toggleEditUserPanel();}

    // saves data from side panel into DB
    @FXML
    private void saveEditedUser() {userHandler.saveUser();}

    // saves data from side panel into DB
    @FXML
    private void saveEditedGame() {
        gameHandler.saveGame(); }

    // disables editing options and buttons rollback previous data
    @FXML
    void cancelUser() {sidePanelHandler.cancelUser();}

    // disables editing options and buttons rollback previous data
    @FXML
    void cancelGame() {sidePanelHandler.cancelGame();}


//  Tables

    // loads table with borrowed games on action tab
    @FXML
    public void loadIssueTable() {
        issueHelper.loadBorrowedGames();
    }


    // loads table with all users on resources tab
    @FXML
    void loadUsersTable() {userHandler.loadAllUsers();  }

    // loads table with all games on resources tab
    @FXML
    void loadGameTable() { gameHandler.loadAllGames(); }

    // when clicked on all games table row in resources tab, changes visibility of remove button
    @FXML
    private void gameSelect() {
        Game game = allGamesTableView.getSelectionModel().getSelectedItem();
        if(game ==null) {
            removeGameButton.setVisible(false);
            return;
        }

        removeGameButton.setVisible(true);
        enterGameIDField.setText(game.getId());
        loadGameInfo();
        enteredGame = game;
    }

    // when clicked on all user table row in resources tab, changes visibility of remove button
    @FXML
    private void memberSelect() {
        User user = allUsersTableView.getSelectionModel().getSelectedItem();
        if(user==null) {
            removeUserButton.setVisible(false);
            return;
        }
        removeUserButton.setVisible(true);
        enterUserIDField.setText(user.getId());
        loadUserInfo();
        enteredUser = user;
    }

//  Buttons

    void refreshBorrowButton() {
        borrowButton.setDisable(!(enteredGame !=null && enteredGame.isAvailability() && enteredUser !=null)); }

    //when borrow button clicked, makes relation betwin user and game, reloads tables
    @FXML
    private void borrowGame(){
        gameHandler.borrowGame();
        loadGameTable();
        loadUsersTable();
        loadIssueTable();
    }

    // renews time stamp in db
    @FXML
    private void renewGame(){ gameHandler.renewGame();}

    @FXML
    private void deleteUser() { userHandler.deleteUser(enteredUser); }

    @FXML
    private void deleteGame() { gameHandler.deleteGame(enteredGame); }

    @FXML
    private void returnGame(ActionEvent actionEvent) {
        gameHandler.returnGame();
    }

    @FXML
    private void payFine() {
        IssueHelper.IssueEntry selectedItem = borrowedGamesTable.getSelectionModel().getSelectedItem();

        AlertHelper.message(Alert.AlertType.INFORMATION, "Collect " + String.format("%.2f", selectedItem.getFine()) + "$ from member.");
        Optional<ButtonType> response = AlertHelper.confirmation("Do you want to clear fine value?");
        if (response.get() == ButtonType.OK) {
            renewGame();
        }
    }

//    on each click into table, buttons visibility vary on selection
    @FXML
    private void refreshTableButtons(MouseEvent mouseEvent) {
        if(borrowedGamesTable.getSelectionModel().getSelectedItem()!=null) {
            if (borrowedGamesTable.getSelectionModel().getSelectedItem().getFine() > 0) {
                payFineButton.setDisable(false);
                renewButton.setDisable(true);
                returnButton.setDisable(true); }
            else {
                payFineButton.setDisable(true);
                renewButton.setDisable(false);
                returnButton.setDisable(false);
            }
        }
    }

// load windows section

    @FXML
    private void loadSettingsWindow() {
        WindowLoader.loadWindow(getClass().getResource("/gameRentalAssistant/view/settings.fxml"), "Settings");
    }

    @FXML
    private void addUser() {
        FXMLLoader loader = WindowLoader.loadWindow(getClass().getResource("/gameRentalAssistant/view/addUser.fxml"), "Add new user");
        addUserController = loader.<AddUserController>getController();
        addUserController.setController(this);
    }

    @FXML
    private void addGame( ) {
        FXMLLoader loader = WindowLoader.loadWindow(getClass().getResource("/gameRentalAssistant/view/addGame.fxml"), "Add new game");
        addGameController = loader.<AddGameController>getController();
        addGameController.setController(this);
    }

//    --------------------------------------------------------------    //
}
