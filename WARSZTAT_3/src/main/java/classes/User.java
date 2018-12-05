package classes;

import other.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private int userGroupId;


    public User(String name, String email, String password, int userGroupId) {
        this.name = name;
        this.email = email;
        setPassword(password);
        this.userGroupId = userGroupId;
    }


    public void saveToDB(Connection conn) throws SQLException {

        if(!isUserGroupId(conn) || !isEmailUnique(conn)){
            return;
        }

        if(id == 0){
            PreparedStatement pstm = prepareInsert(conn);
            pstm.executeUpdate();

            ResultSet rs = pstm.getGeneratedKeys();
            rs.next();
            setId(rs.getInt(1));
            rs.close();
        }else{
            PreparedStatement pstm = prepareUpdate(conn);
            pstm.executeUpdate();
        }
    }

    private boolean isUserGroupId(Connection conn)throws SQLException{
        Statement stm = conn.createStatement();
        String select = "SELECT id FROM user_group;";
        ResultSet rs = stm.executeQuery(select);
        while(rs.next()){
            if(rs.getInt("id") == userGroupId){
                rs.close();
                return true;
            }
        }
        rs.close();
        return false;
    }

    private boolean isEmailUnique(Connection conn)throws SQLException{
        String select = "SELECT * FROM users WHERE email = ?;";
        PreparedStatement pstm = conn.prepareStatement(select);
        pstm.setString(1, email);
        ResultSet rs = pstm.executeQuery();

        if(rs.next() && rs.getInt("id") != id){
            rs.close();
            return false;
        }
        rs.close();
        return true;
    }

    private PreparedStatement prepareInsert(Connection conn)throws SQLException{
        String insert = "INSERT INTO users (username, email, password, user_group_id) VALUES (?, ?, ?, ?);";
        PreparedStatement pstm = conn.prepareStatement(insert, RETURN_GENERATED_KEYS);

        pstm.setString(1, name);
        pstm.setString(2, email);
        pstm.setString(3, password);
        pstm.setInt(4, userGroupId);

        return pstm;
    }

    private PreparedStatement prepareUpdate(Connection conn)throws SQLException{
        String update = "UPDATE users SET username = ?, email = ?, password = ?, user_group_id = ? WHERE id = ?;";
        PreparedStatement pstm = conn.prepareStatement(update);

        pstm.setString(1, name);
        pstm.setString(2, email);
        pstm.setString(3, password);
        pstm.setInt(4, userGroupId);
        pstm.setInt(5, id);

        return pstm;
    }

    public static User loadUserById(Connection conn, int id) throws SQLException {
        String selectById = "SELECT * FROM users WHERE id = ?;";
        PreparedStatement pstm = conn.prepareStatement(selectById);
        pstm.setInt(1, id);
        ResultSet rs = pstm.executeQuery();

        if(rs.next()){
            String name = rs.getString("username");
            String email = rs.getString("email");
            String password = rs.getString("password");
            int user_group_id = rs.getInt("user_group_id");

            User u = new User(name, email, password, user_group_id);
            u.id = id;
            rs.close();
            return u;
        }
        rs.close();
        return null;
    }

    public static List<User> loadAllUsers(Connection conn) throws SQLException {
        String selectIds = "SELECT id FROM users;";
        ResultSet rs = (conn.createStatement()).executeQuery(selectIds);
        List<User> users = new ArrayList<>();

        while(rs.next()){
            users.add(loadUserById(conn, rs.getInt("id")));
        }
        rs.close();

        return users;
    }

    public void delete(Connection conn) throws SQLException{
        if(id != 0){
            String delete = "DELETE FROM users WHERE id = ?;";
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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password){
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public int getUserGroupId() {
        return userGroupId;
    }
    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }
}
