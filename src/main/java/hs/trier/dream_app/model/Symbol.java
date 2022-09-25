package hs.trier.dream_app.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Symbol {
    private final Integer id;
    private final StringProperty name;
    private final StringProperty description;

    public Symbol(Integer id, String name, String description) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public String toString() {
        return name.get();
        /*
        return "Symbol{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                '}';
         */
    }
}
