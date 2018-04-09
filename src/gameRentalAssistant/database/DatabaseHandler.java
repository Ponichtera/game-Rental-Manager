package gameRentalAssistant.database;

import javax.swing.*;
import java.sql.*;

public class DatabaseHandler  {

    private static DatabaseHandler handler;

    private static final String DB_URL = "jdbc:derby:database";
    private static Connection connection = null;
    private static Statement statement = null;

    private DatabaseHandler() {
        connection = createConnection(DB_URL);

//        dropTable("ISSUE");
//        dropTable("GAME");
//        dropTable("MEMBER");

        setupTables();
    }

    public static DatabaseHandler getInstance() {
        if (handler == null | connection==null)
            handler = new DatabaseHandler();
        return handler;
    }

    private Connection createConnection(String DB_URL) {
        try {
            System.out.println("Connecting to database " + DB_URL +";create=true");
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            Connection connection = DriverManager.getConnection(DB_URL +";create=true");

            System.out.println("Connection successful!!!");
            return connection;

        } catch (Exception e) {
            // SQL State XJO15 and SQLCode 50000 mean an OK shutdown.
            SQLException sqlException = (SQLException) e;
            if (!(sqlException.getErrorCode() == 50000) && (sqlException.getSQLState().equals("XJ015"))) {

                System.out.println("Error while connecting to database " + DB_URL);
                System.err.println(e);
            }
            return null;
        }
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            statement = connection.createStatement();
            System.out.println(query);
            result = statement.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler " + ex.getLocalizedMessage());
            System.out.println(query);

            return null;
        }
        return result;
    }

    public boolean execAction(String query) {
        try {
            statement = connection.createStatement();
            System.out.println(query);
            statement.execute(query);
            return true;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error Occurred ", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler " + ex.getLocalizedMessage());
            System.out.println(query);
            return false;
        }
    }

    private boolean setupGameTable(Connection connection) {
        try {
            statement = connection.createStatement();

            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "GAME", null);

            if (tables.next())
                System.out.println("Table GAME already exists. Ready to go!");
            else{
                statement.execute("CREATE TABLE GAME ("
                        + "	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
                        + "	name varchar(100),\n"
                        + "	language varchar(100),\n"
                        + "	publisher varchar(100),\n"
                        + "	EAN varchar(40),\n"
                        + "	isAvail boolean default true,\n"
                        + " CONSTRAINT game_PK PRIMARY KEY (id) )");
            System.out.println("Table GAME successfully created. Ready to go!");
            }
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupGameDatabase");
            return false;
        }
    }

    private boolean setupMemberTable(Connection connection) {
        try {
            statement = connection.createStatement();

            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "MEMBER", null);

            if (tables.next())
                System.out.println("Table MEMBER already exists. Ready to go!");
            else {
                statement.execute("CREATE TABLE MEMBER ("
                        + " id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
                        + "	name VARCHAR(100),\n"
                        + "	address VARCHAR(200),\n"
                        + "	phone VARCHAR(20),\n"
                        + "	email VARCHAR(100),\n"
                        + "	game_counter INTEGER DEFAULT 0,\n"
                        + " CONSTRAINT member_PK PRIMARY KEY (id))");
                System.out.println("Table MEMBER successfully created. Ready to go!");
            }
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupMemberDatabase");
            return false;
        }
    }

    private boolean setupIssueTable(Connection connection) {
        try {
           statement = connection.createStatement();

           DatabaseMetaData dmd = connection.getMetaData();
           ResultSet tables = dmd.getTables(null, null, "ISSUE", null);
           if(tables.next())
               System.out.println("Table ISSUE already exists. Ready to go!");
           else {
               statement.execute("CREATE TABLE ISSUE (" +
                       " gameID INTEGER NOT NULL PRIMARY KEY," +
                       " memberID INTEGER NOT NULL ," +
                       " issueTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ," +
                       " renew_count INTEGER DEFAULT 0," +
                       " FOREIGN KEY (gameID) REFERENCES GAME(id)," +
                       " FOREIGN KEY (memberID) REFERENCES MEMBER(id) )");
               System.out.println("Table ISSUE successfully created. Ready to go!");
           } return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupMemberDatabase");
            return false;
        }
    }

    public boolean closeConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL+";shutdown=true");
            connection.close();
            System.out.println("Connection closed");
            return true;
        } catch (SQLException sqlException) {
            // SQL State XJO15 and SQLCode 50000 mean an OK shutdown.
            if (!(sqlException.getErrorCode() == 50000) && (sqlException.getSQLState().equals("XJ015"))) {

                System.out.println("Error while connecting to database " + DB_URL);
                System.err.println(sqlException);

            }
        } return false;
    }

    public boolean dropTable(String table) {
        String query = "DROP TABLE " + table;
        boolean result = execAction(query);

        System.out.println( result ? table + " Deleted." : "Error occurred while deleting table: " + table);
        return result;
    }

    public void setupTables() {
        setupGameTable(connection);
        setupMemberTable(connection);
        setupIssueTable(connection);
    }
}
