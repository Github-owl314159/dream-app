package hs.trier.dream_app.model;

import javafx.beans.property.SimpleStringProperty;

public class Dream {
    private final int id;
    private final SimpleStringProperty title;
    private final SimpleStringProperty content;
    private final SimpleStringProperty date;
    private final SimpleStringProperty notes;
    private final SimpleStringProperty mood;

    public Dream(int id, String title, String content, String date, String notes, String mood) {
        this.id = id;
        this.title = new SimpleStringProperty(title);
        this.content = new SimpleStringProperty(content);
        this.date = new SimpleStringProperty(date);
        this.notes = new SimpleStringProperty(notes);
        this.mood = new SimpleStringProperty(mood);
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getNotes() {
        return notes.get();
    }

    public String getMood() {
        return mood.get();
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
