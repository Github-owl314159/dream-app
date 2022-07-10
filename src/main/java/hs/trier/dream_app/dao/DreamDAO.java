package hs.trier.dream_app.dao;

import hs.trier.dream_app.Database;
import hs.trier.dream_app.model.Dream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DreamDAO {
    private static final String TABLE_NAME = "dream";
    private static final String ID_COLUMN = "id";
    private static final String TITLE_COLUMN = "title";
    private static final String TEXT_COLUMN = "text";
    private static final String DATE_COLUMN = "date";
    private static final String NOTES_COLUMN = "notes";
    private static final String MOOD_COLUMN = "mood";
    private static final ObservableList<Dream> DREAMS;

    static {
        DREAMS = FXCollections.observableArrayList();
        updateDreamsFromDB();
    }

    private static void updateDreamsFromDB() {
        String query = "SELECT * FROM " + TABLE_NAME + ";";

        try (Connection connection = Database.connect()) {
            assert connection != null;

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            DREAMS.clear();
            while (rs.next()) {
                DREAMS.add(new Dream(
                        rs.getInt(ID_COLUMN),
                        rs.getString(TITLE_COLUMN),
                        rs.getString(TEXT_COLUMN),
                        rs.getString(DATE_COLUMN),
                        rs.getString(NOTES_COLUMN),
                        rs.getString(MOOD_COLUMN)
                ));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Dreams from Database."
            );
            DREAMS.clear();
        }
    }

    public static int create(String title, String text, String notes, String mood) {
        Date now = CRUDHelper.now();
        int id = (int) CRUDHelper.create(TABLE_NAME,
                new String[]{TITLE_COLUMN, TEXT_COLUMN, DATE_COLUMN, NOTES_COLUMN, MOOD_COLUMN},
                new String[]{title, text, now.toString(), notes, mood},
                new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});

        DREAMS.add(new Dream(id, title, text, now.toString(), notes, mood));

        return id;
    }

    public static Optional<Dream> getDream(int id) {
        for (Dream dream : DREAMS) {
            if (dream.getId() == id)
                return Optional.of(dream);
        }
        return Optional.empty();
    }

    public static void update(Dream dream) {
        int rows = CRUDHelper.update(
                TABLE_NAME,
                new String[]{TITLE_COLUMN, TEXT_COLUMN, NOTES_COLUMN},
                new String[]{dream.getTitle(), dream.getText(), dream.getNotes()},
                new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR},
                ID_COLUMN,
                Types.INTEGER,
                dream.getId()
        );

        if (rows == 0)
            throw new IllegalStateException("Dream " + dream.getId() + " did not exist in database. No update possible.");

        Optional<Dream> optionalDream = getDream(dream.getId());
        optionalDream.ifPresentOrElse((oldDream) -> {
            DREAMS.remove(oldDream);
            DREAMS.add(dream);
        }, () -> {
            throw new IllegalStateException("Dream " + dream.getId() + " did not exist in database. No update possible.");
        });
    }

    public static void delete(int id) {
        int rows = CRUDHelper.delete(TABLE_NAME, id);

        if (rows != 1)
            throw new IllegalStateException("Dream " + id + " did not exist in database. No delete operation possible.");

        Optional<Dream> optionalDream = getDream(id);

        optionalDream.ifPresentOrElse(DREAMS::remove, () -> {
            throw new IllegalStateException("Dream did not exist in cache.");
        });
    }

    public static ObservableList<Dream> getDreams() {
        return FXCollections.observableArrayList(DREAMS);
    }
}
