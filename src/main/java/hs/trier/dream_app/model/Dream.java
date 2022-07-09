package hs.trier.dream_app.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Dream {
    private final int id;
    private final ReadOnlyStringProperty title;
    private final ReadOnlyStringProperty text;
    private final ReadOnlyStringProperty date;
    private final ReadOnlyStringProperty notes;
    private final ReadOnlyStringProperty mood;

    public Dream(int id, String title, String text, String date, String notes, String mood) {
        this.id = id;
        this.title = new SimpleStringProperty(title);
        this.text = new SimpleStringProperty(text);
        this.date = new SimpleStringProperty(date);
        this.notes = new SimpleStringProperty(notes);
        this.mood = new SimpleStringProperty(mood);
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text.get();
    }

    public ReadOnlyStringProperty textProperty() {
        return text;
    }

    public String getTitle() {
        return title.get();
    }

    public ReadOnlyStringProperty titleProperty() {
        return title;
    }

    public String getDate() {
        return date.get();
    }

    public ReadOnlyStringProperty dateProperty() {
        return date;
    }

    public String getNotes() {
        return notes.get();
    }

    public ReadOnlyStringProperty notesProperty() {
        return notes;
    }

    public String getMood() {
        return mood.get();
    }

    public ReadOnlyStringProperty moodProperty() {
        return mood;
    }

    @Override
    public String toString() {
        return "Dream{" +
                "id=" + id +
                ", text=" + text +
                ", title=" + title +
                ", date=" + date +
                ", notes=" + notes +
                ", mood=" + mood +
                '}';
    }
}
