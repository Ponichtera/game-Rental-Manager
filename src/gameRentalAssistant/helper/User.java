package gameRentalAssistant.helper;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;

public class User {
    private SimpleStringProperty name, id, phone, email, address, bookCounter;
    private SimpleFloatProperty fine;


    public User(String name, String id, String phone, String email, String address, String bookCounter, Float fine) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.address = new SimpleStringProperty(address);
        this.bookCounter = new SimpleStringProperty(bookCounter);
        Float fineValue = new BigDecimal(fine).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        this.fine = new SimpleFloatProperty(fineValue);

    }

    public String getName() {
        return name.get();
    }

    public String getId() {
        return id.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public String getEmail() { return email.get();}

    public String getAddress() {
        return address.get();
    }

    public String getGamesCounter() {
        return bookCounter.get();
    }

    public double getFine() {
        return fine.get();
    }

    public SimpleFloatProperty fineProperty() {
        return fine;
    }

    @Override
    public String toString() {
        return "Id: " + id.get() +
                "\nName: " + name.get() +
                "\nAddress: " + address.get() +
                "\nPhone number: " + phone.get() +
                "\nEmail: " + email.get() +"\n" + bookCounter;
    }
}
