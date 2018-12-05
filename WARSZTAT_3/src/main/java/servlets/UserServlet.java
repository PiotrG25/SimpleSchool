package servlets;

import other.DbUtil;
import classes.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String type = request.getParameter("type");

        if(type == null || type.isEmpty()){
            response.sendRedirect("/user");
        }

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userGroupId = request.getParameter("userGroupId");

        switch(type){
            case "add":
                addUser(name, email, password, userGroupId);
                break;
            case "edit":
                editUser(id, name, email, password, userGroupId);
                break;
            case "delete":
                deleteUser(id);
                break;
        }

        response.sendRedirect("/user");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try(
                Connection conn = DbUtil.getConn();
        ){
            List<User> users = User.loadAllUsers(conn);
            request.setAttribute("users", users);
        }catch(SQLException e){
            e.printStackTrace();
        }
        getServletContext().getRequestDispatcher("/WEB-INF/views/user.jsp").forward(request, response);
    }

    private void addUser(String name, String email, String password, String userGroupId){

        try(Connection conn = DbUtil.getConn();){

            if(name == null || name.isEmpty() || email == null || email.isEmpty() ||
                password == null || password.isEmpty() || userGroupId == null || !userGroupId.matches("^[0-9]+$")){
                conn.close();
                return;
            }

            User user = new User(name, email, password, Integer.parseInt(userGroupId));
            user.saveToDB(conn);

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void editUser(String id, String name, String email, String password, String userGroupId) {

        try(Connection conn = DbUtil.getConn();){

            if(id == null || !id.matches("^[0-9]+$")){
                conn.close();
                return;
            }

            User user = User.loadUserById(conn, Integer.parseInt(id));
            if(user != null){

                if(name != null && !name.isEmpty()){
                    user.setName(name);
                }
                if(email != null && !email.isEmpty()){
                    user.setEmail(email);
                }
                if(password != null && !password.isEmpty()){
                    user.setPassword(password);
                }
                if(userGroupId != null && userGroupId.matches("^[0-9]+$")){
                    user.setUserGroupId(Integer.parseInt(userGroupId));
                }
                user.saveToDB(conn);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void deleteUser(String id) {

        try(Connection conn = DbUtil.getConn();){

            if(id == null || !id.matches("^[0-9]+$")){
                conn.close();
                return;
            }

            User user = User.loadUserById(conn, Integer.parseInt(id));
            if(user != null){
                user.delete(conn);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
