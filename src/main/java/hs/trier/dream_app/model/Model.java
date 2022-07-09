//package hs.trier.dream_diary.model;
//
//import hs.trier.dream_diary.Presenter;
//import hs.trier.traumapp.frontend.Presenter;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//
//import java.util.*;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//public class Model {
//    private static Model model;
//    private static Presenter presenter;
//
//    private static final String url = "jdbc:sqlite:data.db"; // Speicherort der Datenbank
//
//    private Model() {
//    }
//
//    public static Model getModel() {
//        if (model == null) {
//            model = new Model();
//            presenter = Presenter.getPresenter();
//        }
//        return model;
//    }
//
//
//// ============================== Dream Database ==============================
//
//
//    public void saveDream(Dream dream) {
//        String sql = "INSERT INTO Dreams (Date,Time,Title,Text,Notes,Mood) VALUES(?,?,?,?,?,?)";
//
//        try (Connection conn = DriverManager.getConnection(url);
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, dream.getDate());
//            pstmt.setString(2, dream.getTime());
//            pstmt.setString(3, dream.getTitle());
//            pstmt.setString(4, dream.getText());
//            pstmt.setString(5, dream.getNotes());
//            pstmt.setString(6, dream.getMood());
//            pstmt.executeUpdate();
//            System.out.println("Dream '" + dream.getTitle() + "' saved.");
//            presenter.showInStatusBar("Dream '" + dream.getTitle() + "' saved.");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public Dream getDream(int id) {
//        String sql = "SELECT ID, Date, Time, Title, Text, Notes, Mood FROM Dreams WHERE ID = " + id;
//
//        Dream dream = null;
//
//        try (Connection conn = DriverManager.getConnection(url);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            dream = new Dream(
//                    rs.getInt("ID"),
//                    rs.getString("Date"),
//                    rs.getString("Time"),
//                    rs.getString("Title"),
//                    rs.getString("Text"),
//                    rs.getString("Notes"),
//                    rs.getString("Mood"));
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return dream;
//    }
//
///*    public Dream getLatestDream()
//    {
//
//        String sql = "SELECT * FROM Dreams ORDER BY ROWID DESC LIMIT 1";
//
//        Dream dream = null;
//
//        try (Connection conn = DriverManager.getConnection(url);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql))
//        {
//            dream = new Dream(
//                    rs.getInt("ID"),
//                    rs.getString("Date"),
//                    rs.getString("Time"),
//                    rs.getString("Title"),
//                    rs.getString("Text"),
//                    rs.getString("Notes"),
//                    rs.getString("Mood"));
//        }
//        catch (SQLException e)
//        {
//            System.out.println(e.getMessage());
//        }
//        return dream;
//    } */
//
//    public void updateDream(Dream dream) {
//        String sql = "UPDATE Dreams SET "
//                + "Date = ?, "
//                + "Time = ?, "
//                + "Title = ?, "
//                + "Text = ?, "
//                + "Notes = ?, "
//                + "Mood = ? "
//                + "WHERE id = ?";
//
//        try (Connection conn = DriverManager.getConnection(url);
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, dream.getDate());
//            pstmt.setString(2, dream.getTime());
//            pstmt.setString(3, dream.getTitle());
//            pstmt.setString(4, dream.getText());
//            pstmt.setString(5, dream.getNotes());
//            pstmt.setString(6, dream.getMood().toString());
//            pstmt.setInt(7, dream.getID());
//            pstmt.executeUpdate();
//            System.out.println("Dream '" + dream.getTitle() + "' updated.");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public void deleteDream(int id, String title) {
//        String sql = "DELETE FROM Dreams WHERE id = ?";
//
//        try (Connection conn = DriverManager.getConnection(url);
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, id);
//            pstmt.executeUpdate();
//            System.out.println("Dream '" + title + "' deleted.");
//            presenter.showInStatusBar("Dream '" + title + "' deleted.");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public ObservableList<Dream> getDreamList() {
//        String sql = "SELECT ID, Date Time, Title, Text, Notes, Mood FROM Dreams ORDER BY Date ASC";
//        ObservableList<Dream> dreamList = FXCollections.observableArrayList();
//
//        try (Connection conn = DriverManager.getConnection(url);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            while (rs.next()) {
//                Dream dream = new Dream(rs.getInt("ID"),
//                        rs.getString("Date"),
//                        rs.getString("Time"),
//                        rs.getString("Title"),
//                        rs.getString("Text"),
//                        rs.getString("Notes"),
//                        rs.getString("Mood"));
//                dreamList.add(dream);
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        System.out.println("Dreamlist created.");
//        return dreamList;
//    }
//
//
//// ============================== Dream Symbol Database ==============================
//
//
//    public boolean checkSymbol(String symbol, String table) {
//        String sql = "SELECT Symbol, Interpretation FROM " + table + " WHERE Symbol LIKE '%" + symbol + "%'";
//
//        boolean result = false;
//
//        try (Connection conn = DriverManager.getConnection(url);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            if (rs.next()) {
//                result = true;
//                System.out.println("Symbol '" + symbol + "' found in table '" + table + "'.");
//            } else {
//                System.out.println("Symbol '" + symbol + "' not found in table '" + table + "'.");
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return result;
//    }
//
//    public DreamSymbol getSymbol(String symbol, String table) {
//        String sql = "SELECT Symbol, Interpretation FROM " + table + " WHERE Symbol = '" + symbol + "'";
//
//        DreamSymbol result = null;
//
//        try (Connection conn = DriverManager.getConnection(url);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            result = new DreamSymbol(
//                    rs.getString("Symbol"),
//                    rs.getString("Interpretation"));
//            System.out.println("Symbol: " + result.getSymbol());
//            System.out.println("Interpretation: " + result.getInterpretation());
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return result;
//    }
//
//    public void deleteDreamSymbol(String symbol, String table) {
//        String sql = "DELETE FROM " + table + " WHERE Symbol = ?";
//
//        try (Connection conn = DriverManager.getConnection(url);
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, symbol);
//            pstmt.executeUpdate();
//            System.out.println("Dream Symbol '" + symbol + "' deleted.");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public ObservableList<DreamSymbol> getDreamSymbolList(String table) {
//        //String sql = "SELECT Symbol, Interpretation FROM " + table;
//        String sql = "SELECT Symbol, Interpretation FROM " + table + " ORDER BY Symbol ASC";
//        //String sql = "SELECT Symbol, Interpretation FROM MyDreamSymbols SORT BY Symbol ASC";
//
//        ObservableList<DreamSymbol> dreamSymbolList = FXCollections.observableArrayList();
//
//        try (Connection conn = DriverManager.getConnection(url);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            while (rs.next()) {
//                DreamSymbol dreamSymbol = new DreamSymbol(
//                        rs.getString("Symbol"),
//                        rs.getString("Interpretation"));
//                dreamSymbolList.add(dreamSymbol);
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        System.out.println("Symbollist created.");
//        return dreamSymbolList;
//    }
//
//
//// ============================== My Dream Symbol Database ==============================
//
//
//    public void saveMySymbol(DreamSymbol symbol) {
//        String sql = "INSERT INTO MyDreamSymbols (Symbol,Interpretation) VALUES(?,?)";
//
//        try (Connection conn = DriverManager.getConnection(url);
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, symbol.getSymbol());
//            pstmt.setString(2, symbol.getInterpretation());
//            pstmt.executeUpdate();
//            System.out.println("Dream Symbol '" + symbol.getSymbol() + "' saved.");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public void updateMySymbol(DreamSymbol symbol) {
//        String sql = "UPDATE MyDreamSymbols SET "
//                + "Interpretation = ? "
//                + "WHERE Symbol = ?";
//
//        try (Connection conn = DriverManager.getConnection(url);
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, symbol.getInterpretation());
//            pstmt.setString(2, symbol.getSymbol());
//            pstmt.executeUpdate();
//            System.out.println("Dream Symbol '" + symbol.getSymbol() + "' updated.");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//
//// ============================== Language Processing ==============================
//
//
//    public String[] getTokens(String input) {
//        List<String> tokens = new ArrayList<>();
//        StringTokenizer tokenizer = new StringTokenizer(input, " ,.:;?![]()-_'");
//
//        while (tokenizer.hasMoreElements()) {
//            tokens.add(tokenizer.nextToken());
//        }
//        String[] output = tokens.toArray(new String[tokens.size()]);
//        System.out.println(Arrays.toString(output));
//        return output;
//    }
//
//    public String[] deleteDupes(String[] input) {
//        ArrayList<String> temp = new ArrayList<String>();
//
//        for (int i = 0; i < input.length; i++) {
//            if (!temp.contains(input[i])) {
//                temp.add(input[i]);
//            }
//        }
//        String[] output = temp.toArray(new String[temp.size()]);
//        System.out.println(Arrays.toString(output));
//        return output;
//    }
//
//    public boolean checkStopword(String word) {
//        String sql = "SELECT Word FROM Stopwords WHERE Word = '" + word + "'";
//
//        boolean result = false;
//
//        try (Connection conn = DriverManager.getConnection(url);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            if (rs.next()) {
//                result = true;
//                System.out.println("Word '" + word + "' found in table 'Stopwords'.");
//            } else {
//                System.out.println("Word '" + word + "' not found in table 'Stopwords'.");
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return result;
//    }
//
//    public String[] deleteStopwords(String[] input) {
//        ArrayList<String> temp = new ArrayList<String>();
//
//        for (int i = 0; i < input.length; i++) {
//            if (!checkStopword(input[i])) {
//                temp.add(input[i]);
//            }
//        }
//        String[] output = temp.toArray(new String[temp.size()]);
//        System.out.println(Arrays.toString(output));
//        return output;
//    }
//
//    public String[] testStem(String[] input) {
//        ArrayList<String> temp = new ArrayList<String>();
//
//        for (int i = 0; i < input.length; i++) {
//            temp.add(Cistem.stem(input[i]));
//        }
//        String[] output = temp.toArray(new String[temp.size()]);
//        System.out.println(Arrays.toString(output));
//        return output;
//    }
//
//
//// ============================== zum Testen ==============================
//
//
//    public void testAnalyzeDream(Dream dream) {
//        String input = dream.getText();
//        String[] token = getTokens(input);
//        String[] tokenWithoutDupes = deleteDupes(getTokens(input));
//        String[] tokenWithoutStopwords = deleteStopwords(deleteDupes(getTokens(input)));
//        String[] tokenStemmed = testStem(deleteStopwords(deleteDupes(getTokens(input))));
//
//        ArrayList<DreamSymbol> foundSymbols = new ArrayList();
//        //for (String element : tokenStemmed)
//        for (String element : tokenWithoutStopwords) {
//            if (checkSymbol(element, "MyDreamSymbols")) {
//                foundSymbols.add(getSymbol(element, "MyDreamSymbols"));
//            }
//        }
//
//        presenter.showInStatusBar("Found " + foundSymbols.size() + " Dream Symbols.");
//        presenter.showAnalyzedDream(
//                foundSymbols,
//                input,
//                Arrays.toString(token),
//                Arrays.toString(tokenWithoutDupes),
//                Arrays.toString(tokenWithoutStopwords),
//                Arrays.toString(tokenStemmed));
//    }
//}
