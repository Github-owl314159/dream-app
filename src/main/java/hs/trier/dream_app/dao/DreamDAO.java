package hs.trier.dream_app.dao;

import hs.trier.dream_app.Database;
import hs.trier.dream_app.Util;
import hs.trier.dream_app.model.Dream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
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
    private static final ObservableList<Dream> DREAMS_LIST = FXCollections.observableArrayList();

    public static void initialize() {
        updateDreamsFromDB();
    }

    private static void updateDreamsFromDB() {
        String query = "SELECT * FROM " + TABLE_NAME + ";";

        try (Connection connection = Database.connect()) {
            assert connection != null;

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            DREAMS_LIST.clear();
            while (rs.next()) {
                DREAMS_LIST.add(new Dream(
                        rs.getInt(ID_COLUMN),
                        Util.decodeBase64(rs.getString(TITLE_COLUMN)),
                        Util.decodeBase64(rs.getString(TEXT_COLUMN)),
                        Util.decodeBase64(rs.getString(DATE_COLUMN)),
                        Util.decodeBase64(rs.getString(NOTES_COLUMN)),
                        Util.decodeBase64(rs.getString(MOOD_COLUMN))
                ));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Dreams from Database."
            );
            DREAMS_LIST.clear();
        }
    }

    public static int create(String title, String text, String notes, String mood, String date) {
        int id = (int) CRUDHelper.create(
                TABLE_NAME,
                new String[]{TITLE_COLUMN, TEXT_COLUMN, DATE_COLUMN, NOTES_COLUMN, MOOD_COLUMN},
                new String[]{Util.encodeBase64(title), Util.encodeBase64(text), Util.encodeBase64(date), Util.encodeBase64(notes), Util.encodeBase64(mood)},
                new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});

        if (id == 0) {
            throw new IllegalStateException("Dream " + title + " could not have been created. No creation possible.");
        } else
            DREAMS_LIST.add(new Dream(id, title, text, date, notes, mood));

        return id;
    }

    public static int update(Dream dream) {
        int rows = CRUDHelper.update(
                TABLE_NAME,
                new String[]{TITLE_COLUMN, TEXT_COLUMN, DATE_COLUMN, NOTES_COLUMN, MOOD_COLUMN},
                new String[]{Util.encodeBase64(dream.getTitle()), Util.encodeBase64(dream.getContent()), Util.encodeBase64(dream.getDate()), Util.encodeBase64(dream.getNotes()), Util.encodeBase64(dream.getMood())},
                new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR},
                ID_COLUMN,
                Types.INTEGER,
                dream.getId()
        );

        if (rows == 0)
            throw new IllegalStateException("Dream " + dream.getId() + " did not exist in database. No update possible.");

        return dream.getId();
    }

    public static void delete(int id) {
        int rows = CRUDHelper.delete(TABLE_NAME, id);

        if (rows != 1)
            throw new IllegalStateException("Dream " + id + " did not exist in database. No delete operation possible.");
        else
            DREAMS_LIST.removeIf(dream -> dream.getId() == id);
    }

    // return list of dreams
    public static ObservableList<Dream> getDreams() {
        return DREAMS_LIST;
    }

    public static boolean dreamExistsIgnoreCase(String dreamTitle) {
        return DREAMS_LIST.stream().anyMatch(dream -> dream.getTitle().equalsIgnoreCase(dreamTitle));
    }
}