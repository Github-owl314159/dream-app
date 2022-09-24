package hs.trier.dream_app.dao;

import hs.trier.dream_app.Database;
import hs.trier.dream_app.model.Symbol;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SymbolDAO {
    private static final String TABLE_NAME = "symbol";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final ObservableList<Symbol> SYMBOLS_LIST = FXCollections.observableArrayList();


    public static void initialize() {
        updateSymbolsFromDB();
    }

    private static void updateSymbolsFromDB() {
        String query = "SELECT * FROM " + TABLE_NAME + ";";

        try (Connection connection = Database.connect()) {
            assert connection != null;

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            SYMBOLS_LIST.clear();
            while (rs.next()) {
                SYMBOLS_LIST.add(new Symbol(
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
            SYMBOLS_LIST.clear();
        }
    }

    public static Optional<Symbol> getSymbol(int id) {
        return SYMBOLS_LIST.stream().filter(symbol -> symbol.getId() == id).findAny();
    }

    public static boolean symbolExistsIgnoreCase(String symbolName) {
        return SYMBOLS_LIST.stream().anyMatch(symbol -> symbol.getName().equalsIgnoreCase(symbolName));
    }

    public static int create(String name, String description) {
        int id = (int) CRUDHelper.create(
                TABLE_NAME,
                new String[]{NAME_COLUMN, DESCRIPTION_COLUMN},
                new String[]{name, description},
                new int[]{Types.VARCHAR, Types.VARCHAR});

        if (id == 0) {
            throw new IllegalStateException("Symbol " + name + " could not have been created. No creation possible.");
        } else
            SYMBOLS_LIST.add(new Symbol(id, name, description));

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
            SYMBOLS_LIST.removeIf(symbol -> symbol.getId() == id);
    }

    // return list of symbols
    public static ObservableList<Symbol> getSymbols() {
        return SYMBOLS_LIST;
    }

    public static List<Symbol> searchSymbols(String word) {

        List<Symbol> foundSymbols = new ArrayList<>();
        for (Symbol symbol : SYMBOLS_LIST) {
            if (symbol.getName().toLowerCase().contains(word.toLowerCase())) {
                foundSymbols.add(symbol);
            }
        }
        return foundSymbols;
    }
}