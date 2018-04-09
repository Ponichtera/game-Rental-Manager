package gameRentalAssistant.helper;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Game {
    private SimpleStringProperty name, id, language, publisher, EAN;
    private SimpleBooleanProperty availability;

    public Game(String name, String id, String language, String publisher, String EAN, Boolean availability) {
         this.name = new SimpleStringProperty(name);
         this.id = new SimpleStringProperty(id);
         this.language = new SimpleStringProperty(language);
         this.publisher = new SimpleStringProperty(publisher);
         this.EAN = new SimpleStringProperty(EAN);
         this.availability = new SimpleBooleanProperty(availability);
     }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public void setEAN(String EAN) {
        this.EAN.set(EAN);
    }

    public void setAvailability(boolean availability) {
        this.availability.set(availability);
    }

    public String getName() {
         return name.get();
     }

     public String getId() {
         return id.get();
     }

     public String getLanguage() {
         return language.get();
     }

     public String getPublisher() {
         return publisher.get();
     }

     public String getEAN() {
        return EAN.get();
    }

    public boolean isAvailability() {
        return availability.get();
    }

    public SimpleBooleanProperty availabilityProperty() {
        return availability;
    }

    @Override
    public String toString() {
        return "\nGame details:" +
                "\nname: " + name.get() +
                "\nid; " + id.get() +
                "\nlanguage: " + language.get() +
                "\npublisher: " + publisher.get() +
                "\nEAN: " + EAN.get() +". " +
                availability;
    }
}
