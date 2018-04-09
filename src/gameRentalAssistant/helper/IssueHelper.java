package gameRentalAssistant.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import gameRentalAssistant.controller.MainController;
import gameRentalAssistant.database.DatabaseHandler;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class IssueHelper {

    private MainController controller;
    private DatabaseHandler databaseHandler;

    private ObservableList<IssueEntry> issuedGames = FXCollections.observableArrayList();

    public IssueHelper(MainController controller) {
        this.controller = controller;
        initColumns();
    }


    // loads table with borrowed games on action tab
    public void loadBorrowedGames() {

        issuedGames.clear();
        if(databaseHandler==null) databaseHandler = DatabaseHandler.getInstance();

        String query = "SELECT ISSUE.gameID, ISSUE.issueTime, ISSUE.renew_count, GAME.name, GAME.language FROM ISSUE LEFT JOIN GAME ON ISSUE.gameID=GAME.id WHERE ISSUE.memberID=" + controller.enteredUser.getId();
        ResultSet rs = databaseHandler.execQuery(query);
        try {
            while(rs.next()) {
                String  gameID = rs.getString("gameID");
                Timestamp dateBorrowed = rs.getTimestamp("issueTime");
                Integer renewCount = rs.getInt("renew_count");
                String name = rs.getString("name");
                String language = rs.getString("language");

                Float fine = calculateFine(dateBorrowed);
                issuedGames.add(new IssueHelper.IssueEntry(gameID, name, language, dateBorrowed, fine, renewCount));
            }

        } catch (SQLException e) {e.printStackTrace();}

        controller.borrowedGamesTable.getItems().setAll(issuedGames);
    }

    public static Float calculateFine(Timestamp dateBorrowed) {
        Float fine = 0.0F;
        Settings settings = Settings.getSettings();

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        long timeBorrowed = currentTime.getTime() - dateBorrowed.getTime();
        long daysBorrowed = timeBorrowed / (1000 * 60 * 60 * 24);
        if (daysBorrowed > settings.getDaysFineFree())
            fine = (daysBorrowed - settings.getDaysFineFree()) * settings.getFine();

        return new BigDecimal(fine).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    private void initColumns() {
        controller.gameIdIssueColumn.setCellValueFactory(new PropertyValueFactory<>("gameID"));
        controller.nameIssueColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));
        controller.languageIssueColumn.setCellValueFactory(new PropertyValueFactory<>("gameLanguage"));
        controller.dateIssueColumn.setCellValueFactory(new PropertyValueFactory<>("dateBorrowed"));
        controller.renewCountIssueColumn.setCellValueFactory(new PropertyValueFactory<>("renewCount"));
        controller.fineIssueColumn.setCellValueFactory(new PropertyValueFactory<>("fine"));
    }

    public class IssueEntry {
        String gameID, gameName, gameLanguage;
        Timestamp dateBorrowed;
        Float fine;
        Integer renewCount;

        IssueEntry(String gameID, String gameName, String gameLanguage, Timestamp dateBorrowed, Float fine, Integer renewCount) {
            this.gameID = gameID;
            this.gameName = gameName;
            this.gameLanguage = gameLanguage;
            this.dateBorrowed = dateBorrowed;
            this.fine = fine;
            this.renewCount = renewCount;
        }

        public String getGameID() {
            return gameID;
        }

        public void setGameID(String gameID) {
            this.gameID = gameID;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public String getGameLanguage() {
            return gameLanguage;
        }

        public void setGameLanguage(String gameLanguage) {
            this.gameLanguage = gameLanguage;
        }

        public Timestamp getDateBorrowed() {
            return dateBorrowed;
        }

        public void setDateBorrowed(Timestamp dateBorrowed) {
            this.dateBorrowed = dateBorrowed;
        }

        public Float getFine() {
            return fine;
        }

        public void setFine(Float fine) {
            this.fine = fine;
        }

        public Integer getRenewCount() {
            return renewCount;
        }

        public void setRenewCount(Integer renewCount) {
            this.renewCount = renewCount;
        }
    }
}
