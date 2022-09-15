package hs.trier.dream_app.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Dream {
    private final int id;
    private final ReadOnlyStringProperty title;
    private final ReadOnlyStringProperty content;
    private final ReadOnlyStringProperty date;
    private final ReadOnlyStringProperty notes;
    private final ReadOnlyStringProperty mood;

    public Dream(int id, String title, String content, String date, String notes, String mood) {
        this.id = id;
        this.title = new SimpleStringProperty(title);
        this.content = new SimpleStringProperty(content);
        this.date = new SimpleStringProperty(date);
        this.notes = new SimpleStringProperty(notes);
        this.mood = new SimpleStringProperty(mood);
    }

    public static Dream copyOf(Dream src) {
        return new Dream(-100, src.getTitle(), src.getContent(), src.getDate(), src.getNotes(), src.getMood());
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content.get();
    }

    public ReadOnlyStringProperty contentProperty() {
        return content;
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
                ", text=" + content +
                ", title=" + title +
                ", date=" + date +
                ", notes=" + notes +
                ", mood=" + mood +
                '}';
    }
}
