package gameRentalAssistant.controller;

import gameRentalAssistant.helper.Game;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import gameRentalAssistant.database.DatabaseHandler;
import gameRentalAssistant.helper.AlertHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class GameHandler {

    private DatabaseHandler databaseHandler;
    private MainController c;

    GameHandler(MainController controller) {
        c = controller;
        initColumns();
        loadAllGames();
    }

    // loads table with all games on resources tab
    void loadAllGames() {
        c.allGames.clear();

        if(databaseHandler==null) databaseHandler = DatabaseHandler.getInstance();

        String query = "SELECT * FROM GAME";
        ResultSet rs = databaseHandler.execQuery(query);
        try {
            while(rs.next()) {
                String name = rs.getString("name");
                String id = rs.getString("id");
                String language = rs.getString("language");
                String publisher = rs.getString("publisher");
                String EAN = rs.getString("EAN");
                Boolean availability = rs.getBoolean("isAvail");

                c.allGames.add(new Game(name, id, language, publisher, EAN, availability));
            }
        } catch (SQLException e) {e.printStackTrace();}

        c.allGamesTableView.getItems().setAll(c.allGames);
    }

    void initColumns() {
        //all games table columns
        c.allGamesColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        c.allGamesColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        c.allGamesColumnLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));
        c.allGamesColumnPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        c.allGamesColumnEAN.setCellValueFactory(new PropertyValueFactory<>("EAN"));
        c.allGamesColumnStatus.setCellValueFactory(new PropertyValueFactory<>("availability"));

        //issue table columns
        c.allGamesColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

    }

    // clears all fields on game side panel
    void clearGame() {
        c.txtGameName.setText("");
        c.txtGameLanguage.setText("");
        c.txtGamePublisher.setText("");
        c.txtGameEAN.setText("");
        c.txtGameStatus.setText("");
        c.txtGameId.setText("");
    }

    // saves data from side panel into DB
    void saveGame() {
        String id = c.enteredGame.getId();
        String name = c.txtGameName.getText();
        String language = c.txtGameLanguage.getText();
        String publisher = c.txtGamePublisher.getText();
        String EAN = c.txtGameEAN.getText();

        if (id.isEmpty() || name.isEmpty()||language.isEmpty()||publisher.isEmpty()||EAN.isEmpty()) {
            AlertHelper.insufficientData();
            return;
        }

        editGame(id, name, language, publisher, EAN);

        c.cancelGame();
        c.loadGameTable();
    }


    // loads details about the game into game side panel
    void loadGameInfo() {

        //takes id from input field
        String id = c.enterGameIDField.getText();
        if(id.isEmpty()) return;

        //check if string is a number
        try {
            Integer.parseInt(id);
        } catch (NumberFormatException e) {
            c.enterGameIDField.setText("");
            return;
        }

        String query = "SELECT * FROM GAME WHERE id=" + id;
        ResultSet resultSet = databaseHandler.execQuery(query);
        try {
            if(resultSet.next()) {
                String name = resultSet.getString("name");
                String language = resultSet.getString("language");
                String ean = resultSet.getString("EAN");
                String publisher = resultSet.getString("publisher");
                boolean statusFlag = resultSet.getBoolean("isAvail");
                String status = statusFlag?"Available":"Unavailable";

                c.enteredGame = new Game(name, id, language, publisher, ean, statusFlag);

                c.txtGameName.setText(name);
                c.gameIdLabel.setText(name);
                c.txtGameLanguage.setText(language);
                c.txtGamePublisher.setText(publisher);
                c.txtGameEAN.setText(ean);
                c.txtGameStatus.setText(status);
                c.txtGameId.setText(id);


                // toggle edit button on game side panel
                c.gameToggleButton.setDisable(false);

            } else {
                // clear enter id field and game side panel
                c.enterGameIDField.setText("");
                clearGame();
            }

            c.refreshBorrowButton();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteGame(Game enteredGame) {
        if (enteredGame == null) {
            System.out.println("Cannot delete null.");
            return;
        }
        if (!enteredGame.isAvailability()) {
            AlertHelper.message(Alert.AlertType.ERROR, "Selected game is currently borrowed. ");
            return;
        }

        String id = enteredGame.getId();
        Optional<ButtonType> response = AlertHelper.confirmation("Delete game id:" +id+ " ?");
        if (response.get() == ButtonType.OK) {
            if(databaseHandler.execAction("DELETE FROM GAME WHERE id=" + id))
                AlertHelper.success();
            else AlertHelper.somethingWentWrong();
        }
        loadAllGames();
        c.removeGameButton.setVisible(false);
        clearGame();
    }

    void editGame(String id, String name, String language, String publisher, String EAN) {
        String query = "UPDATE GAME SET " +
                "name = '" + name +"', " +
                "language = '" + language +"', " +
                "publisher = '" + publisher +"', " +
                "EAN = '" + EAN +"' " +
                "WHERE id=" + id;
        databaseHandler.execAction(query);
    }

    boolean borrowGame() {
        if (c.enteredUser == null | (c.enteredGame == null || !c.enteredGame.isAvailability()))
            return false;

        String gameId = c.enteredGame.getId();
        String userId = c.enteredUser.getId();
        String issueQuery, gameQuery, memberQuery;

        Optional<ButtonType> result = AlertHelper.confirmation("Borrow: " + c.enteredGame.getName() + " to: " + c.enteredUser.getName() + "?");
        if (result.get() == ButtonType.OK) {
            issueQuery = "INSERT INTO ISSUE (memberID, gameID) VALUES (" + userId + "," + gameId + ")";

            if (databaseHandler.execAction(issueQuery)) {
                gameQuery = "UPDATE GAME SET isAvail = false WHERE id=" + gameId;
                if (databaseHandler.execAction(gameQuery)) {
                    memberQuery = "UPDATE MEMBER SET game_counter = game_counter+1 WHERE id=" + userId;
                    if (databaseHandler.execAction(memberQuery)) {
                        AlertHelper.success();
                        c.borrowButton.setDisable(true);
                        c.loadIssueTable();
                        return true;
                    } else AlertHelper.somethingWentWrong();
                } else AlertHelper.somethingWentWrong();
            } else AlertHelper.somethingWentWrong();
        }
        return false;
    }


    // renews time stamp in db
    void renewGame() {
        String gameId = c.borrowedGamesTable.getSelectionModel().getSelectedItem().getGameID();
        String query = "UPDATE ISSUE SET issueTime = CURRENT_TIMESTAMP, renew_count = renew_count+1 WHERE gameID = " + gameId;
        if(databaseHandler.execAction(query)) {
            AlertHelper.success();
            c.renewButton.setDisable(true);
            c.returnButton.setDisable(true);
            c.loadIssueTable();
        }

        else AlertHelper.somethingWentWrong();
    }

    void returnGame() {
        String gameId = c.borrowedGamesTable.getSelectionModel().getSelectedItem().getGameID();
        String issueQuery = "DELETE FROM ISSUE WHERE gameID=" + gameId;
        String memberQuery = "UPDATE MEMBER SET game_counter = game_counter-1 WHERE id=" + c.enteredUser.getId();
        String gameQQuery = "UPDATE GAME SET isAvail=true WHERE id=" + gameId;


        boolean firstResult = databaseHandler.execAction(issueQuery);
        boolean secondResult = databaseHandler.execAction(memberQuery);
        boolean thirdResult = databaseHandler.execAction(gameQQuery);

        if(firstResult && secondResult && thirdResult) {
            AlertHelper.success();
            c.returnButton.setDisable(true);
            c.renewButton.setDisable(true);

            c.loadIssueTable();
            c.loadGameTable();
            c.loadUsersTable();
        }

    }
}
