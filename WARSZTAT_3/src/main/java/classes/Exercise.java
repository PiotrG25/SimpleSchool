package classes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class Exercise {
    private int id;
    private String title;
    private String description;


    public Exercise(String title, String description){
        this.title = title;
        this.description = description;
    }


    public void saveToDB(Connection conn) throws SQLException {

        if(id == 0){
            String insert = "INSERT INTO exercise (title, description) VALUES (?, ?);";
            PreparedStatement pstm = conn.prepareStatement(insert, RETURN_GENERATED_KEYS);
            pstm.setString(1, title);
            pstm.setString(2, description);

            pstm.executeUpdate();
            ResultSet rs = pstm.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);

            rs.close();
        }else{
            String update = "UPDATE exercise SET title = ?, description = ? WHERE id = ?;";
            PreparedStatement updatePreparedStatement = conn.prepareStatement(update);

            updatePreparedStatement.setString(1, title);
            updatePreparedStatement.setString(2, description);
            updatePreparedStatement.setInt(3, id);

            updatePreparedStatement.executeUpdate();
        }
    }

    public static Exercise loadExerciseById(Connection conn, int id) throws SQLException {

        String select = "SELECT * FROM exercise WHERE id = ?;";
        PreparedStatement selectPreparedStatement = conn.prepareStatement(select);
        selectPreparedStatement.setInt(1, id);
        ResultSet rs = selectPreparedStatement.executeQuery();

        if(rs.next()){
            String title = rs.getString("title");
            String description = rs.getString("description");

            Exercise exercise = new Exercise(title, description);
            exercise.id = id;
            rs.close();
            return exercise;
        }
        rs.close();
        return null;
    }

    public static List<Exercise> loadAllExercises(Connection conn) throws SQLException {
        String selectIds = "SELECT id FROM exercise;";
        ResultSet rs = (conn.createStatement()).executeQuery(selectIds);
        List<Exercise> exercises = new ArrayList<>();

        while(rs.next()){
            exercises.add(loadExerciseById(conn, rs.getInt("id")));
        }
        rs.close();

        return exercises;
    }

    public void delete(Connection conn) throws SQLException {
        if(id != 0){
            String delete = "DELETE FROM exercise WHERE id = ?;";
            PreparedStatement pstm = conn.prepareStatement(delete);
            pstm.setInt(1, id);
            pstm.executeUpdate();
            id = 0;
        }
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
