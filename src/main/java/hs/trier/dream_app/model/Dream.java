package hs.trier.dream_app.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Dream {
    private int id;
    private SimpleStringProperty title;
    private SimpleStringProperty content;
    private SimpleStringProperty date;
    private SimpleStringProperty notes;
    private SimpleStringProperty mood;

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

    public SimpleStringProperty contentProperty() {
        return content;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public String getNotes() {
        return notes.get();
    }

    public SimpleStringProperty notesProperty() {
        return notes;
    }

    public String getMood() {
        return mood.get();
    }

    public SimpleStringProperty moodProperty() {
        return mood;
    }

    public void setTitle(String title)
    {
        this.title.set(title);
    }

    public void setContent(String content) { this.content.set(content); }

    public void setDate(String date)
    {
        this.date.set(date);
    }

    public void setNotes(String notes)
    {
        this.notes.set(notes);
    }

    public void setMood(String mood)
    {
        this.mood.set(mood);
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
