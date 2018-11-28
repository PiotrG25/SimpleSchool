package classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class User_group {
    private int id;
    private String name;

    public User_group(String name){
        this.name = name;
    }

    public String saveToDB(Connection conn) throws SQLException {

        if(this.name == null){
            System.err.println("Brakuje kilku argumentów");
            return "name";
        }
        if(id == 0){
            String insert = "INSERT INTO user_group (name) VALUES (?)";
            PreparedStatement pstm = conn.prepareStatement(insert, RETURN_GENERATED_KEYS);

            pstm.setString(1, name);
            pstm.executeUpdate();
            ResultSet rs = pstm.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);

            rs.close();
        }else{
            String update = "UPDATE user_group SET name=? WHERE id=?;";
            PreparedStatement pstm = conn.prepareStatement(update);
            pstm.setString(1, name);
            pstm.setInt(2, id);
            pstm.executeUpdate();
        }
        return "0";
    }
    public static User_group loadUser_groupById(Connection conn, int id) throws SQLException {
        String selectById = "SELECT * FROM user_group WHERE id = ?";
        PreparedStatement pstm = conn.prepareStatement(selectById);
        pstm.setInt(1, id);
        ResultSet rs = pstm.executeQuery();

        if(rs.next()){
            String name = rs.getString("name");
            User_group user_group = new User_group(name);
            user_group.id = id;

            rs.close();
            return user_group;
        }
        rs.close();
        System.err.println("Brak grupy o takim id");
        return null;
    }

    public static User_group[] loadAllUser_groups(Connection conn) throws SQLException {
        String selectIds = "SELECT id FROM user_group";
        ResultSet rs = (conn.createStatement()).executeQuery(selectIds);
        User_group[] ug = new User_group[1];

        while(rs.next()){
            ug[ug.length - 1] = loadUser_groupById(conn, rs.getInt("id"));
            ug = Arrays.copyOf(ug, ug.length + 1);
        }
        rs.close();
        ug = Arrays.copyOf(ug, ug.length - 1);

        if(ug.length == 0){
            System.err.println("Brak grup");
            return null;
        }else{
            return ug;
        }
    }

    public void delete(Connection conn) throws SQLException {
        if(this.id != 0){
            String delete = "DELETE FROM user_group WHERE id=?";
            PreparedStatement pstm = conn.prepareStatement(delete);
            pstm.setInt(1, this.id);
            pstm.executeUpdate();
            this.id = 0;
        }
    }

    public User_group setName(String name){
        this.name = name;
        return this;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
}
