package hs.trier.dream_app.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Symbol {
    private final int id;
    private final ReadOnlyStringProperty name;
    private final ReadOnlyStringProperty description;

    public Symbol(int symbol_id, String name, String description) {
        this.id = symbol_id;
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public ReadOnlyStringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public ReadOnlyStringProperty descriptionProperty() {
        return description;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                '}';
    }
}
