package gameRentalAssistant.controller;

import gameRentalAssistant.helper.IssueHelper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import gameRentalAssistant.database.DatabaseHandler;
import gameRentalAssistant.helper.AlertHelper;
import gameRentalAssistant.helper.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserHandler {

    private DatabaseHandler databaseHandler;
    private MainController c;

    UserHandler(MainController c) {
        this.c = c;
        initColumns();
        loadAllUsers();
    }

    //fills up table on resources tab
    public void loadAllUsers() {
        c.allUsers.clear();

        if(databaseHandler==null) databaseHandler = DatabaseHandler.getInstance();

        String query = "SELECT * FROM MEMBER";
        ResultSet rs = databaseHandler.execQuery(query);
        try {
            while(rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String address = rs.getString("address");
                Integer b =rs.getInt("game_counter");
                String games = Integer.toString(b);
                Float fine = 0.0f;

                if (b > 0)
                    fine += calculateFine(id);

                c.allUsers.add(new User(name, id, phone, email, address, games, fine));
            }
        } catch (SQLException e) {e.printStackTrace();}

        c.allUsersTableView.getItems().setAll(c.allUsers);
    }

    private void initColumns() {
        c.allMembersColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        c.allMembersColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        c.allMembersColumnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        c.allMembersColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        c.allMembersColumnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        c.allMembersColumnGames.setCellValueFactory(new PropertyValueFactory<>("gameCounter"));
        c.allMembersColumnFine.setCellValueFactory(new PropertyValueFactory<>("fine"));
    }


    // clears all fields on user side panel
    public void clearUser() {
        c.userToggleButton.setDisable(true);

        c.txtUserAddress.setText("");
        c.txtUserName.setText("");
        c.txtUserPhone.setText("");
        c.txtUserEmail.setText("");
        c.txtUserFine.setText("");
        c.txtUserId.setText("");
    }

    // saves data from side panel into DB
    void saveUser() {
        String id = c.enteredUser.getId();
        String name = c.txtUserName.getText();
        String address = c.txtUserAddress.getText();
        String email = c.txtUserEmail.getText();
        String phone = c.txtUserPhone.getText();

        if(id.isEmpty() || name.isEmpty() || address.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            AlertHelper.insufficientData();
            return;
        }

        editUser(id, name, address, email, phone);

        c.cancelUser();
        c.loadUsersTable();
    }

    boolean loadUserInfo() {
        String id = c.enterUserIDField.getText();
        if(id.isEmpty()) return false;
        try {
            Integer.parseInt(id);
        } catch (NumberFormatException e) {
            c.enterUserIDField.setText("");
            return false;
        }
        String query = "SELECT * FROM MEMBER WHERE id=" + id;
        ResultSet resultSet = databaseHandler.execQuery(query);

        try {
            if(resultSet.next()){
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                Float fine = calculateFine(id);

                Integer b =resultSet.getInt("game_counter");
                String games = Integer.toString(b);

                c.enteredUser = new User(name, id, phone, email, address, games, fine);

                c.txtUserAddress.setText(address);
                c.txtUserName.setText(name);
                c.userIdLabel.setText(name);
                c.txtUserPhone.setText(phone);
                c.txtUserEmail.setText(email);
                c.txtUserFine.setText(fine.toString());
                c.txtUserId.setText(id);

                c.userToggleButton.setDisable(false);
                c.loadIssueTable();
            } else {
                c.enterUserIDField.setText("");
                clearUser();
                return false;
            }
            c.refreshBorrowButton();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    void deleteUser(User enteredUser) {
        if (enteredUser == null) {
            System.out.println("Cannot delete null.");
            return;
        }
        int games = Integer.parseInt(enteredUser.getGamesCounter());
        System.out.println(games);
        if (games > 0) {
            AlertHelper.message(Alert.AlertType.ERROR, "Selected user has some games borrowed.\nReturn games before deletion.");
            return;
        }

        String id = enteredUser.getId();
        Optional<ButtonType> response = AlertHelper.confirmation("Delete member id:" +id+ " ?");
        if (response.get() == ButtonType.OK) {
            if(databaseHandler.execAction("DELETE FROM MEMBER WHERE id=" + id))
                AlertHelper.success();
            else AlertHelper.somethingWentWrong();
        }
        loadAllUsers();
        c.removeUserButton.setVisible(false);
        c.enteredUser = null;
        clearUser();
    }

    private void editUser(String id, String name, String address, String email, String phone) {
        String query = "UPDATE MEMBER SET " +
                "name = '" + name +"', " +
                "address = '" + address +"', " +
                "email = '" + email +"', " +
                "phone = '" + phone +"' " +
                "WHERE id=" + id;
        databaseHandler.execAction(query);
    }

    private Float calculateFine(String memberID) {
        Float fine = 0.0F;
        if (memberID.isEmpty()) return fine;

        String query =  "SELECT issueTime\n" +
                        "FROM ISSUE\n" +
                        "where memberID=" + memberID;

        ResultSet rs = databaseHandler.execQuery(query);

        try {
            while(rs.next())
                //calculate fine
                fine += IssueHelper.calculateFine(rs.getTimestamp("issueTime"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fine;
    }
}
