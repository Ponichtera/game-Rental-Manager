package gameRentalAssistant.helper;

import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;

public class Settings {
    private int daysFineFree;
    private float fine;
    private String username, password;
    public static final String CONFIG_FILE = "config.txt";


    public Settings() {
        daysFineFree = 14;
        fine = 2.2f;
        username = "admin";
        password = DigestUtils.sha1Hex("admin");
    }

    public int getDaysFineFree() {
        return daysFineFree;
    }

    public void setDaysFineFree(int daysFineFree) {
        this.daysFineFree = daysFineFree;
    }

    public float getFine() {
        return fine;
    }

    public void setFine(float fine) {
        this.fine = fine;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        String pass = DigestUtils.sha1Hex(password);
        this.password = pass;
    }

    public void initConfig() {
            saveConfig(new Settings());
    }

    public static void saveConfig(Settings settings) {

        try (Writer writer = new FileWriter(CONFIG_FILE)) {
            Gson gson = new Gson();
            gson.toJson(settings, writer);

        } catch (IOException e) {
            System.out.println("Exception in initConfig()\n");
            e.printStackTrace();
        }
    }
    public boolean isValid(String username, String password) {
        return (this.username.equals(username) && this.password.equals(DigestUtils.sha1Hex(password)));
    }

    public static Settings getSettings() {
        Settings settings = new Settings();
        try(Reader reader = new FileReader(CONFIG_FILE)) {
            Gson gson = new Gson();
            settings = gson.fromJson(reader, Settings.class);

        }catch (IOException e) {
            System.out.println("File 'config.txt' not found, creating file.");
            settings.initConfig();
        }
        return  settings;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "daysFineFree=" + daysFineFree +
                ", fine=" + fine +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
