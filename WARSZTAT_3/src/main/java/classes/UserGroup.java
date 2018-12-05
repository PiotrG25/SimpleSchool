package classes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserGroup {
    private int id;
    private String name;


    public UserGroup(String name){
        this.name = name;
    }


    public void saveToDB(Connection conn) throws SQLException {

        if(id == 0){
            String insert = "INSERT INTO user_group (name) VALUES (?);";
            PreparedStatement insertPreparedStatement = conn.prepareStatement(insert, RETURN_GENERATED_KEYS);

            insertPreparedStatement.setString(1, name);
            insertPreparedStatement.executeUpdate();
            ResultSet rs = insertPreparedStatement.getGeneratedKeys();

            rs.next();
            id = rs.getInt(1);
            rs.close();
        }else{
            String update = "UPDATE user_group SET name = ? WHERE id = ?;";
            PreparedStatement updatePreparedStatement = conn.prepareStatement(update);
            updatePreparedStatement.setString(1, name);
            updatePreparedStatement.setInt(2, id);
            updatePreparedStatement.executeUpdate();
        }
    }

    public static UserGroup loadUserGroupById(Connection conn, int id) throws SQLException {
        String select = "SELECT * FROM user_group WHERE id = ?;";
        PreparedStatement selectPreparedStatement = conn.prepareStatement(select);
        selectPreparedStatement.setInt(1, id);
        ResultSet rs = selectPreparedStatement.executeQuery();

        if(rs.next()){
            String name = rs.getString("name");
            UserGroup userGroup = new UserGroup(name);
            userGroup.setId(id);

            rs.close();
            return userGroup;
        }
        rs.close();
        return null;
    }

    public static List<UserGroup> loadAllUser_groups(Connection conn) throws SQLException {
        String select = "SELECT id FROM user_group;";
        ResultSet rs = conn.createStatement().executeQuery(select);
        List<UserGroup> userGroups = new ArrayList<>();

        while(rs.next()){
            userGroups.add(loadUserGroupById(conn, rs.getInt("id")));
        }
        rs.close();

        return userGroups;
    }

    public void delete(Connection conn) throws SQLException {
        if(this.id != 0){
            String delete = "DELETE FROM user_group WHERE id = ?;";
            PreparedStatement pstm = conn.prepareStatement(delete);
            pstm.setInt(1, this.id);
            pstm.executeUpdate();
            this.id = 0;
        }
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
