package classes;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class Solution {
    private int id;
    private Calendar created;
    private Calendar updated;
    private String description;
    private int exercise_id;
    private int users_id;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Solution(Calendar created, Calendar updated, String description, int exercise_id, int users_id) {
        this.created = created;
        this.updated = updated;
        this.description = description;
        this.exercise_id = exercise_id;
        this.users_id = users_id;
    }

    public String saveToDB(Connection conn) throws SQLException {
        if (created == null) {
            System.err.println("Brak daty stworzenia");
            return "created";
        }
        if (updated == null) {
            System.err.println("Brak daty aktualizacji");
            return "updated";
        }
        if (description == null) {
            System.err.println("Brak opisu");
            return "description";
        }
        if (exercise_id == 0) {
            System.err.println("Brak id zadania");
            return "exercise_id";
        }
        if (users_id == 0) {
            System.err.println("Brak id uzytkownika");
            return "users_id";
        }
        Statement stm = conn.createStatement();

        //check exercise_id existance
        String check = "SELECT id FROM exercise;";
        ResultSet rs = stm.executeQuery(check);
        boolean exeists = false;
        while (rs.next()) {
            if (rs.getInt("id") == exercise_id) {
                exeists = true;
                break;
            }
        }
        rs.close();
        if (exeists == false) {
            System.err.println("Nie ma zadania o takim id");
            return "exercise";
        }

        //check users_id existance
        check = "SELECT id FROM users;";
        rs = stm.executeQuery(check);
        exeists = false;
        while (rs.next()) {
            if (rs.getInt("id") == users_id) {
                exeists = true;
                break;
            }
        }
        rs.close();
        if (exeists == false) {
            System.err.println("Nie ma uzytkownika o takim id");
            return "users";
        }
        //Koniec walidacji

        if (id == 0) {
            String insert = "INSERT INTO solution (created, updated, description, exercise_id, users_id) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement pstm = conn.prepareStatement(insert, RETURN_GENERATED_KEYS);

            pstm.setString(1, dateFormat.format(created.getTime()));//new java.sql.Date(created.getTimeInMillis())
            pstm.setString(2, dateFormat.format(updated.getTime()));
            pstm.setString(3, description);
            pstm.setInt(4, exercise_id);
            pstm.setInt(5, users_id);

            pstm.executeUpdate();
            rs = pstm.getGeneratedKeys();
            rs.next();
            this.id = rs.getInt(1);
            rs.close();
        } else {
            String select = "SELECT * FROM solution WHERE id=?;";
            PreparedStatement selectStatement = conn.prepareStatement(select);
            selectStatement.setInt(1, id);
            ResultSet selectResult = selectStatement.executeQuery();
            selectResult.next();

            String dbDescription = selectResult.getString("description");
            int dbExercise_id = selectResult.getInt("exercise_id");
            int dbUsers_id = selectResult.getInt("users_id");

            if(!description.equals(dbDescription)){
                String update = "UPDATE solution SET description=? WHERE id=?";
                PreparedStatement updateStatement = conn.prepareStatement(update);
                updateStatement.setInt(2, id);
                updateStatement.setString(1, description);
                updateStatement.executeUpdate();
            }
            if(exercise_id != dbExercise_id){
                String update = "UPDATE solution SET exercise_id=? WHERE id=?";
                PreparedStatement updateStatement = conn.prepareStatement(update);
                updateStatement.setInt(2, id);
                updateStatement.setInt(1, exercise_id);
                updateStatement.executeUpdate();
            }
            if(users_id != dbUsers_id){
                String update = "UPDATE solution SET users_id=? WHERE id=?";
                PreparedStatement updateStatement = conn.prepareStatement(update);
                updateStatement.setInt(2, id);
                updateStatement.setInt(1, users_id);
                updateStatement.executeUpdate();
            }
        }
        return "0";
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


    public Solution setCreated(Calendar created) {
        this.created = created;
        return this;
    }

    public Solution setUpdated(Calendar updated) {
        this.updated = updated;
        return this;
    }

    public Solution setDescription(String description) {
        this.description = description;
        return this;
    }

    public Solution setExercise_id(int exercise_id) {
        this.exercise_id = exercise_id;
        return this;
    }

    public Solution setUsers_is(int users_id) {
        this.users_id = users_id;
        return this;
    }


    public int getId() {
        return id;
    }

    public String getCreated() {
        return dateFormat.format(created.getTime());
    }

    public String getUpdated() {
        return dateFormat.format(updated.getTime());
    }

    public String getDescription() {
        return description;
    }

    public int getExercise_id() {
        return exercise_id;
    }

    public int getUsers_id() {
        return users_id;
    }
}
