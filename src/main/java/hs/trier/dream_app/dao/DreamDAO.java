package hs.trier.dream_app.dao;

import hs.trier.dream_app.Database;
import hs.trier.dream_app.model.Dream;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

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
    private static final ObservableList<Dream> DREAMS_LIST;
    private static final ObservableMap<Integer, Dream> DREAMS_MAP;

    static {
        DREAMS_MAP = FXCollections.observableHashMap();
        updateDreamsFromDB();
        DREAMS_LIST = FXCollections.observableArrayList(DREAMS_MAP.values());

        DREAMS_MAP.addListener((MapChangeListener<Integer, Dream>) change -> {
            if (change.wasAdded() && change.wasRemoved()) {
                DREAMS_LIST.remove(change.getValueRemoved());
                DREAMS_LIST.add(change.getValueAdded());
            } else if (change.wasAdded()) {
                DREAMS_LIST.add(change.getValueAdded());
            } else {
                DREAMS_LIST.remove(change.getValueRemoved());
            }
        });
    }

    private static void updateDreamsFromDB() {
        String query = "SELECT * FROM " + TABLE_NAME + ";";

        try (Connection connection = Database.connect()) {
            assert connection != null;

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            DREAMS_MAP.clear();
            while (rs.next()) {
                DREAMS_MAP.put(rs.getInt(ID_COLUMN), new Dream(
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
            DREAMS_MAP.clear();
        }
    }

    public static int create(String title, String text, String notes, String mood, String date) {
        //Date now = CRUDHelper.now();
        int id = (int) CRUDHelper.create(
                TABLE_NAME,
                new String[]{TITLE_COLUMN, TEXT_COLUMN, DATE_COLUMN, NOTES_COLUMN, MOOD_COLUMN},
                new String[]{title, text, date, notes, mood},
                new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});

        if (id == 0) {
            throw new IllegalStateException("Dream " + title + " could not have been created. No creation possible.");
        } else
            DREAMS_MAP.put(id, new Dream(id, title, text, date, notes, mood));

        return id;
    }

    public static Optional<Dream> getDream(int id) {
        return Optional.ofNullable(DREAMS_MAP.get(id));
    }

    public static int update(Dream dream) {
        int rows = CRUDHelper.update(
                TABLE_NAME,
                new String[]{TITLE_COLUMN, TEXT_COLUMN, DATE_COLUMN, NOTES_COLUMN, MOOD_COLUMN},
                new String[]{dream.getTitle(), dream.getContent(), dream.getDate(), dream.getNotes(), dream.getMood()},
                new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR},
                ID_COLUMN,
                Types.INTEGER,
                dream.getId()
        );

        if (rows == 0)
            throw new IllegalStateException("Dream " + dream.getId() + " did not exist in database. No update possible.");

        Optional<Dream> optionalDream = getDream(dream.getId());
        optionalDream.ifPresentOrElse((oldDream) -> {
            DREAMS_LIST.remove(oldDream);
            DREAMS_LIST.add(dream);
        }, () -> {
            throw new IllegalStateException("Dream " + dream.getId() + " did not exist in database. No update possible.");
        });
        return dream.getId();
    }

    public static void delete(int id) {
        int rows = CRUDHelper.delete(TABLE_NAME, id);

        if (rows != 1)
            throw new IllegalStateException("Dream " + id + " did not exist in database. No delete operation possible.");
        else
            DREAMS_MAP.remove(id);
    }

    // return list of dreams
    public static ObservableList<Dream> getDreams() {
        return DREAMS_LIST;
    }

    /*
    public static ObservableList<Dream> getDreams() { return FXCollections.unmodifiableObservableList(DREAMS_LIST); }
    */


    public static boolean dreamExistsIgnoreCase(String dreamTitle) {                //TODO Martin: Obsolet?
        return DREAMS_LIST.stream().anyMatch(dream -> dream.getTitle().equalsIgnoreCase(dreamTitle));
    }
}
