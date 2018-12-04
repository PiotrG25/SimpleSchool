package classes;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class Solution {
    private int id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String description;
    private int exerciseId;
    private int userId;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public Solution(LocalDateTime created, LocalDateTime updated, String description, int exerciseId, int userId) {
        this.created = created;
        this.updated = updated;
        this.description = description;
        this.exerciseId = exerciseId;
        this.userId = userId;
    }


    public String saveToDB(Connection conn) throws SQLException {

        if (!isExercise(conn)) {
            return "exercise";
        }

        if(!isUser(conn)){
            return "user";
        }

        if(id == 0){
            PreparedStatement insertPreparedStatement = prepareInsert(conn);
            insertPreparedStatement.executeUpdate();

            ResultSet rs = insertPreparedStatement.getGeneratedKeys();
            rs.next();
            this.id = rs.getInt(1);
            rs.close();
        }else{
            PreparedStatement updatePreparedStatement = prepareUpdate(conn);
            updatePreparedStatement.executeUpdate();
        }

        return "0";
    }

    private boolean isExercise(Connection conn)throws SQLException{
        Statement stm = conn.createStatement();
        String select = "SELECT id FROM exercise;";
        ResultSet rs = stm.executeQuery(select);

        while (rs.next()) {
            if (rs.getInt("id") == exerciseId) {
                rs.close();
                return true;
            }
        }
        rs.close();
        return false;
    }

    private boolean isUser(Connection conn)throws SQLException{
        Statement stm = conn.createStatement();
        String select = "SELECT id FROM users;";
        ResultSet rs = stm.executeQuery(select);
        while (rs.next()) {
            if (rs.getInt("id") == userId) {
                rs.close();
                return true;
            }
        }
        rs.close();
        return false;
    }

    private PreparedStatement prepareInsert(Connection conn)throws SQLException{
        String insert = "INSERT INTO solution (created, updated, description, exercise_id, users_id) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement insertPreparedStatement = conn.prepareStatement(insert, RETURN_GENERATED_KEYS);

        insertPreparedStatement.setString(1, dateFormat.format(created));
        insertPreparedStatement.setString(2, dateFormat.format(updated));
        insertPreparedStatement.setString(3, description);
        insertPreparedStatement.setInt(4, exerciseId);
        insertPreparedStatement.setInt(5, userId);

        return insertPreparedStatement;
    }

    private PreparedStatement prepareUpdate(Connection conn)throws SQLException{
        String update = "UPDATE solution SET description = ?, exercise_id = ?, users_id = ?, updated = ? WHERE id = ?;";
        PreparedStatement updatePreparedStatement = conn.prepareStatement(update);

        updatePreparedStatement.setString(1, description);
        updatePreparedStatement.setInt(2, exerciseId);
        updatePreparedStatement.setInt(3, userId);
        updatePreparedStatement.setString(4, dateFormat.format(LocalDateTime.now()));
        updatePreparedStatement.setInt(5, id);

        return updatePreparedStatement;
    }

    public static Solution loadSolutionById(Connection conn, int id) throws SQLException {
        String select = "SELECT * FROM solution WHERE id=?;";
        PreparedStatement pstm = conn.prepareStatement(select);
        pstm.setInt(1, id);
        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            Calendar created = Calendar.getInstance();
            created.setTime(rs.getDate("created"));
            Calendar updated = Calendar.getInstance();
            updated.setTime(rs.getDate("updated"));
            String description = rs.getString("description");
            int exercise_id = rs.getInt("exercise_id");
            int users_id = rs.getInt("users_id");

            Solution solution = new Solution(created, updated, description, exercise_id, users_id);
            solution.id = id;
            rs.close();
            return solution;
        }
        rs.close();
        System.err.println("Brak rozwiazania o takim id");
        return null;
    }

    public static Solution[] loadAllSolutions(Connection conn) throws SQLException {
        String select = "SELECT id FROM solution;";
        ResultSet rs = (conn.createStatement()).executeQuery(select);
        Solution[] solutions = new Solution[1];

        while(rs.next()){
            solutions[solutions.length - 1] = loadSolutionById(conn, rs.getInt("id"));
            solutions = Arrays.copyOf(solutions, solutions.length + 1);
        }
        rs.close();
        solutions = Arrays.copyOf(solutions, solutions.length - 1);

        if(solutions.length == 0){
            System.err.println("Brak rozwiazan");
            return null;
        }else{
            return solutions;
        }
    }

//    Nowosci warsztat3
    public static Solution[] loadAllSolutions(Connection conn, int five)throws SQLException{
        String select = "SELECT id FROM solution ORDER BY updated DESC LIMIT " + five + ";";
        ResultSet rs = (conn.createStatement()).executeQuery(select);
        Solution[] solutions = new Solution[1];

        while(rs.next()){
            solutions[solutions.length - 1] = loadSolutionById(conn, rs.getInt("id"));
            solutions = Arrays.copyOf(solutions, solutions.length + 1);
        }
        rs.close();
        solutions = Arrays.copyOf(solutions, solutions.length - 1);

        if(solutions.length == 0){
            System.err.println("Brak rozwiazan");
            return null;
        }else{
            return solutions;
        }
    }
//    Koniec nowosci

    public void delete(Connection conn) throws SQLException {
        if(id != 0){
            String delete = "DELETE FROM solution WHERE id=?";
            PreparedStatement pstm = conn.prepareStatement(delete);
            pstm.setInt(1, id);
            pstm.executeUpdate();
            id = 0;
        }
    }

    public static Solution loadAllByUserId(Connection conn, int id) throws SQLException {
//        todo: pobranie wszystkich rozwiazan uzytkownika
//        todo:
        return null;
    }

    public static Solution[] loadAllByExerciseId(Connection conn, int id) throws SQLException {
//        todo: pobranie wszystkich rozwiazan danego zadania
//        todo:posortowane od najnowszego do najstarszego
//        todo:
        return null;
    }


}
