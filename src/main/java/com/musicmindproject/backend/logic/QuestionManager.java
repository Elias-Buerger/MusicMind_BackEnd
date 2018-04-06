package com.musicmindproject.backend.logic;

import javax.ejb.Stateless;
import java.sql.*;
import java.util.List;

/**
 * Provides access to the MySql-Database. SINGLETON
 */

@Stateless
public class QuestionManager extends DatabaseManager<String>{

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
    double[][] getFactors(){
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

    @Override
    public void store(String item) {
        System.err.println("ADDING QUESTION NOT IMPLEMENTED");
    }

    @Override
    public List<String> retrieveAll() {
        return null;
    }

    /**
     * @param id Id of the question in the database
     * @return The text of the wanted question in the given language
     */
    @Override
    public String retrieve(Object id) {
        if((int)id > getNumberOfAvailableQuestions())
            return "Question does not exist...";

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD)){
            PreparedStatement stmt = connection.prepareStatement("SELECT TEXT_ENGLISH FROM QUESTION WHERE QUESTIONID = ?");

            stmt.setInt(1, (int)id);

            ResultSet rs = stmt.executeQuery();
            if(rs.next())
                return rs.getString("TEXT_ENGLISH");
            return "";
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());
            return "";
        }
    }

    @Override
    public List<String> retrieveMany(int min, int max) {
        return null;
    }

    public String retrieve(Object id, String lang){
        //TODO translate
        return retrieve(id);
    }
}
