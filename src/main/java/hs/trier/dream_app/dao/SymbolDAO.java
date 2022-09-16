package hs.trier.dream_app.dao;

import hs.trier.dream_app.Database;
import hs.trier.dream_app.model.Symbol;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SymbolDAO {
    private static final String TABLE_NAME = "symbol";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String DESCRIPTION_COLUMN = "description";
    private static ObservableMap<Integer, Symbol> SYMBOLS_MAP;
    private static ObservableList<Symbol> SYMBOLS_LIST;


    public static void initialize() {
        SYMBOLS_MAP = FXCollections.observableHashMap();
        updateSymbolsFromDB();
        SYMBOLS_LIST = FXCollections.observableArrayList(SYMBOLS_MAP.values());

        SYMBOLS_MAP.addListener((MapChangeListener<Integer, Symbol>) change -> {
            if (change.wasAdded() && change.wasRemoved()) {
                SYMBOLS_LIST.remove(change.getValueRemoved());
                SYMBOLS_LIST.add(change.getValueAdded());
            } else if (change.wasAdded()) {
                SYMBOLS_LIST.add(change.getValueAdded());
            } else {
                SYMBOLS_LIST.remove(change.getValueRemoved());
            }
        });
    }

    private static void updateSymbolsFromDB() {
        String query = "SELECT * FROM " + TABLE_NAME + ";";

        try (Connection connection = Database.connect()) {
            assert connection != null;

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            SYMBOLS_MAP.clear();
            while (rs.next()) {
                SYMBOLS_MAP.put(rs.getInt(ID_COLUMN), new Symbol(
                        rs.getInt(ID_COLUMN),
                        rs.getString(NAME_COLUMN),
                        rs.getString(DESCRIPTION_COLUMN)
                ));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Symbols from Database."
            );
            SYMBOLS_MAP.clear();
        }
    }

    public static Optional<Symbol> getSymbol(int id) {
        return Optional.ofNullable(SYMBOLS_MAP.get(id));
    }

    public static boolean symbolExistsIgnoreCase(String symbolName) {
        return SYMBOLS_MAP.values().stream().anyMatch(symbol -> symbol.getName().equalsIgnoreCase(symbolName));
    }

    public static int create(String name, String description) {
        int id = (int) CRUDHelper.create(TABLE_NAME,
                new String[]{NAME_COLUMN, DESCRIPTION_COLUMN},
                new String[]{name, description},
                new int[]{Types.VARCHAR, Types.VARCHAR});

        if (id == 0) {
            throw new IllegalStateException("Symbol " + name + " could not have been created. No creation possible.");
        } else
            SYMBOLS_MAP.put(id, new Symbol(id, name, description));

        return id;
    }

    public static int update(int id, String name, String description) {
        int rows = CRUDHelper.update(
                TABLE_NAME,
                new String[]{NAME_COLUMN, DESCRIPTION_COLUMN},
                new String[]{name, description},
                new int[]{Types.VARCHAR, Types.VARCHAR},
                ID_COLUMN,
                Types.INTEGER,
                id
        );

        if (rows == 0)
            throw new IllegalStateException("Symbol " + name + " did not exist in database. No update possible.");
        else {
            Optional<Symbol> optionalSymbol = getSymbol(id);
            optionalSymbol.ifPresent(symbol -> {
                symbol.setName(name);
                symbol.setDescription(description);
            });
        }

        return rows;
    }

    public static void delete(int id) {
        int rows = CRUDHelper.delete(TABLE_NAME, id);

        if (rows != 1)
            throw new IllegalStateException("Symbol " + id + " did not exist in database. No delete operation possible.");
        else
            SYMBOLS_MAP.remove(id);
    }

    // return list of symbols
    public static ObservableList<Symbol> getSymbols() {
        return SYMBOLS_LIST;
    }
}
