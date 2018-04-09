package gameRentalAssistant;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import gameRentalAssistant.database.DatabaseHandler;

public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/login.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle(null);
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image("/gameRentalAssistant/resources/icon.png"));
        stage.show();

        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());


        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        new Thread(() -> DatabaseHandler.getInstance()).start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> DatabaseHandler.getInstance().closeConnection()));

    }


    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("-demo"))
            createDemoDatabase();
        launch(args);
    }

    private static void createDemoDatabase() {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

        databaseHandler.dropTable("ISSUE");
        databaseHandler.dropTable("GAME");
        databaseHandler.dropTable("MEMBER");
        databaseHandler.setupTables();

        String name, address, phone, email;

        String[] names = {"Adam", "Marcin", "Piotr", "Michał", "Paweł", "Wojtek", "Marek", "Radosław", "Norbert", "Filip",
                "Jan", "Rafał", "Anna", "Monika", "Aneta", "Marta", "Joanna", "Ewa", "Barbara", "Teresa", "Genowefa",
                "Gracjan", "Jarosław"};
        String[] surnames = {"Kowalek", "Nowak", "Luresuch", "Chwaciuk", "Zabrawolno", "Rybiałko", "Maruda", "Dobromił",
                "Sekunda", "Kochaneczko", "Mielc", "Rosucha"};

        String[] streets = {"Poleska", "Kopernika", "Dplistowska", "Mickiewicza", "Popiełuszki", "Solidarnosci", "Dobra",
                "Długa", "Warszawska", "Poznańska", "Litewska", "Węgierska", "Habrowa", "Słonecznikowa", "Tulipanowa",
                "Skoczna", "Bajkowa"};
        String[] emails = {"@buziaczek.pl", "@wp.pl", "@gmail.com", "@yahoo.com", "@interia.pl", "@o2.pl"};

        for (int i = 0; i < 100; i++) {
            String selectedName = names[(int) (Math.random() * names.length)];
            name = selectedName + " " + surnames[(int) (Math.random() * surnames.length)];
            address = "ul. " + streets[(int) (Math.random() * streets.length)] + (int) (Math.random() * 154) + ", 15-"
                    + (int) (Math.random() * 9) + (int) (Math.random() * 9) + (int) (Math.random() * 9) + " Białystok";
            phone = "" + (int) (Math.random() * 9) + (int) (Math.random() * 9) + (int) (Math.random() * 9) + " "
                    + (int) (Math.random() * 9) + (int) (Math.random() * 9) + (int) (Math.random() * 9) + " " +
                    (int) (Math.random() * 9) + (int) (Math.random() * 9) + (int) (Math.random() * 9);
            email = selectedName + (int) (Math.random() * 9) + (int) (Math.random() * 9) + (int) (Math.random() * 9) +
                    (int) (Math.random() * 9) + emails[(int) (Math.random() * emails.length)];
            databaseHandler.execAction("INSERT INTO MEMBER (name, address, phone, email) VALUES (" +
                    "'" + name + "', " +
                    "'" + address + "', " +
                    "'" + phone + "', " +
                    "'" + email + "' " +
                    ")");
        }


        // create game table
        String gameName, gameLanguage, gameEAN, gamePublisher;

        String[] games = {"Monopoly", "Osadnicy z Catanu", "Scrabble", "Dixit", "Milionerzy", "Rebel", "La Cucaracha",
                "Story cubes: Podróże", "Sabotażysta", "Sherlock"};

        String[] languages = {"Polski", "Angielski"};

        String[] publishers = {"U.S. Playing Card Company", "Rebel", "Granna", "Winning moves", "Hasbro Gaming"};

        for (int i = 0; i < 100; i++) {
            gameName = games[(int) (Math.random() * games.length)];
            gameLanguage = languages[(int) (Math.random() * languages.length)];
            gameEAN = "" + (int) (Math.random() * 9)+ (int) (Math.random() * 9)+ (int) (Math.random() * 9)+ (int) (Math.random() * 9)
                    + (int) (Math.random() * 9)+ (int) (Math.random() * 9)+ (int) (Math.random() * 9)+ (int) (Math.random() * 9)
                    + (int) (Math.random() * 9)+ (int) (Math.random() * 9);
            gamePublisher = publishers[(int) (Math.random() * publishers.length)];

                    databaseHandler.execAction("INSERT INTO GAME (name, language, EAN, publisher, isAvail)  VALUES (" +
                    "'" + gameName + "', " +
                    "'" + gameLanguage + "', " +
                    "'" + gameEAN + "', " +
                    "'" + gamePublisher + "', " +
                    "true )");
        }
    }
}
