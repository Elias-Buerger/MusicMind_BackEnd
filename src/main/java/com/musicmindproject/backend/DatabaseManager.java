package com.musicmindproject.backend;

import java.sql.*;

/**
 * Provides access to the MySql-Database. SINGLETON
 */
public class DatabaseManager {

    private static final String CONNECTION_STRING = "jdbc:mysql://musicmindproject.com/MusicMindDB";
    private static final String USER = "root";
    private static final String PASSWORD = "G6GzwxHT";
    private static DatabaseManager instance;
    private DatabaseManager(){}

    public void persistPersonality(String userID, String userName, double[] personality) {
        //TODO
    }

    /**
     *
     * @return Returns the total number of stored questions from the database.
     */
    public int getNumberOfAvailableQuestions(){
        try(Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD)){
            ResultSet rs = connection.prepareStatement("SELECT COUNT(QUESTIONID) AS COUNTER FROM QUESTION").executeQuery();

            if(rs.next())
                return rs.getInt("COUNTER");
            return -1;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
            return -1;
        }
    }

    /**
     * @return Every double[] contains 5 values for each category, the total length of the array is the total number of questions.
     */
    public double[][] getFactors(){
        try(Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD)){
            PreparedStatement ps = connection.prepareStatement("SELECT EXTRAVERSION, NEUROTICISM, OPENNESS, AGREEABLENESS, CONSCIENTIOUSNESS FROM QUESTION WHERE QUESTIONID < ? ORDER BY QUESTIONID");

            ps.setInt(1, getNumberOfAvailableQuestions() - 1);

            ResultSet rs = ps.executeQuery();

            double[][] factors = new double[5][getNumberOfAvailableQuestions() - 1];
            int row = 0;
            while(rs.next()){
                factors[0][row] = rs.getDouble("NEUROTICISM");
                factors[1][row] = rs.getDouble("CONSCIENTIOUSNESS");
                factors[2][row] = rs.getDouble("AGREEABLENESS");
                factors[3][row] = rs.getDouble("EXTRAVERSION");
                factors[4][row] = rs.getDouble("OPENNESS");
                row++;
            }

            return factors;
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * @param questionId Id of the question in the database
     * @param language The given language of the returned question. Possible: de, en
     * @return The text of the wanted question in the given language
     */
    public String getQuestion(int questionId, String language){
        if(questionId > getNumberOfAvailableQuestions())
            return "Question does not exist...";

        String langRef = language.equals("de") ? "TEXT_GERMAN" : "TEXT_ENGLISH";
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD)){
            PreparedStatement stmt = connection.prepareStatement("SELECT " + langRef + " FROM QUESTION WHERE QUESTIONID = ?");

            stmt.setInt(1, questionId);

            ResultSet rs = stmt.executeQuery();
            if(rs.next())
                return rs.getString(langRef);
            return "";
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
            return "";
        }
    }

    public static DatabaseManager getInstance() {
        if(instance == null){
            instance = new DatabaseManager();
        }

        return instance;
    }
}
