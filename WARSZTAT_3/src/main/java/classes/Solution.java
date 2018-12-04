package classes;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

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

    public static Solution loadSolutionById(Connection conn, int id) throws SQLException {
        String select = "SELECT * FROM solution WHERE id = ?;";
        PreparedStatement selectPreparedStatement = conn.prepareStatement(select);
        selectPreparedStatement.setInt(1, id);
        ResultSet rs = selectPreparedStatement.executeQuery();

        if (rs.next()) {
            LocalDateTime created = rs.getTimestamp("created").toLocalDateTime();
            LocalDateTime updated = rs.getTimestamp("updated").toLocalDateTime();
            String description = rs.getString("description");
            int exercise_id = rs.getInt("exercise_id");
            int users_id = rs.getInt("users_id");

            Solution solution = new Solution(created, updated, description, exercise_id, users_id);
            solution.id = id;
            rs.close();
            return solution;
        }
        rs.close();
        return null;
    }

    public static List<Solution> loadAllSolutions(Connection conn) throws SQLException {
        String select = "SELECT id FROM solution;";
        ResultSet rs = (conn.createStatement()).executeQuery(select);
        List<Solution> solutions = new ArrayList<>();

        while(rs.next()){
            solutions.add(loadSolutionById(conn, rs.getInt("id")));
        }
        rs.close();

        return solutions;
    }

    public void delete(Connection conn) throws SQLException {
        if(id != 0){
            String delete = "DELETE FROM solution WHERE id = ?";
            PreparedStatement pstm = conn.prepareStatement(delete);
            pstm.setInt(1, id);
            pstm.executeUpdate();
            id = 0;
        }
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

        insertPreparedStatement.setTimestamp(1, Timestamp.valueOf(created));
        insertPreparedStatement.setTimestamp(2, Timestamp.valueOf(updated));
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
        updatePreparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        updatePreparedStatement.setInt(5, id);

        return updatePreparedStatement;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }
    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getExerciseId() {
        return exerciseId;
    }
    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static DateFormat getDateFormat() {
        return dateFormat;
    }
    public static void setDateFormat(DateFormat dateFormat) {
        Solution.dateFormat = dateFormat;
    }
}
