package hs.trier.dream_app.dao;

import hs.trier.dream_app.Database;
import hs.trier.dream_app.model.Symbol;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    private static final ObservableList<Symbol> SYMBOLS;

    static {
        SYMBOLS = FXCollections.observableArrayList();
        updateSymbolsFromDB();
    }

    private static void updateSymbolsFromDB() {
        String query = "SELECT * FROM " + TABLE_NAME + ";";

        try (Connection connection = Database.connect()) {
            assert connection != null;

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            SYMBOLS.clear();
            while (rs.next()) {
                SYMBOLS.add(new Symbol(
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
            SYMBOLS.clear();
        }
    }

    public static Optional<Symbol> getSymbol(int id) {
        for (Symbol symbol : SYMBOLS) {
            if (symbol.getId() == id)
                return Optional.of(symbol);
        }
        return Optional.empty();
    }

    public static void create(String name, String description) {
        int id = (int) CRUDHelper.create(TABLE_NAME,
                new String[]{NAME_COLUMN, DESCRIPTION_COLUMN},
                new String[]{name, description},
                new int[]{Types.VARCHAR, Types.VARCHAR});

        SYMBOLS.add(new Symbol(id, name, description));
    }

    public static void update(Symbol symbol) {
        int rows = CRUDHelper.update(
                TABLE_NAME,
                new String[]{NAME_COLUMN, DESCRIPTION_COLUMN},
                new String[]{symbol.getName(), symbol.getDescription()},
                new int[]{Types.VARCHAR, Types.VARCHAR},
                ID_COLUMN,
                Types.INTEGER,
                symbol.getId()
        );

        if (rows == 0)
            throw new IllegalStateException("Symbol " + symbol.getId() + " did not exist in database. No update possible.");

        Optional<Symbol> optionalSymbol = getSymbol(symbol.getId());
        optionalSymbol.ifPresentOrElse((oldSymbol) -> {
            SYMBOLS.remove(oldSymbol);
            SYMBOLS.add(symbol);
        }, () -> {
            throw new IllegalStateException("Symbol " + symbol.getId() + " did not exist in database. No update possible.");
        });
    }

    public static void delete(int id) {
        int rows = CRUDHelper.delete(TABLE_NAME, id);

        if (rows != 1)
            throw new IllegalStateException("Symbol " + id + " did not exist in database. No delete operation possible.");

        Optional<Symbol> optionalSymbol = getSymbol(id);

        optionalSymbol.ifPresentOrElse(SYMBOLS::remove, () -> {
            throw new IllegalStateException("Symbol did not exist in cache.");
        });
    }

    public static ObservableList<Symbol> getSymbols() {
        return FXCollections.observableArrayList(SYMBOLS);
    }
}
