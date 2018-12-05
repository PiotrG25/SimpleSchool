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

@WebServlet(name = "users", urlPatterns = "/users")
public class users extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String type = request.getParameter("type");

        if(type == null || type.isEmpty()){
            response.sendRedirect("/users");
        }

        try(
                Connection conn = DbUtil.getConn();
        ){

            String id = request.getParameter("id");
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String user_group_id = request.getParameter("user_group_id");

            boolean doAdd = (username != null && !username.isEmpty() && email != null && !email.isEmpty() &&
                    password != null && !password.isEmpty() && user_group_id != null && !user_group_id.isEmpty());
            boolean doEdit = (id != null && !id.isEmpty());
            boolean doDelete = (id != null && !id.isEmpty());

            if(type.equals("add") && doAdd){
                User users = new User(username, email, password, Integer.parseInt(user_group_id));
                users.saveToDB(conn);
            }else if(type.equals("edit") && doEdit){
                User users = User.loadUserById(conn, Integer.parseInt(id));
                if(users != null){
                    if(username != null && !username.isEmpty()){
                        users.setUserName(username);
                    }
                    if(email != null && !email.isEmpty()){
                        users.setEmail(email);
                    }
                    if(password != null && !password.isEmpty()){
                        users.setEmail(email);
                    }
                    if(user_group_id != null && !user_group_id.isEmpty()){
                        users.setUser_group_id(Integer.parseInt(user_group_id));
                    }
                    users.saveToDB(conn);
                }
            }else if(type.equals("delete") && doDelete){
                User users = User.loadUserById(conn, Integer.parseInt(id));
                if(users != null){
                    users.delete(conn);
                }
            }

            response.sendRedirect("/users");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try(
                Connection conn = DbUtil.getConn();
        ){
            User[] users = User.loadAllUsers(conn);
            request.setAttribute("users", users);
            getServletContext().getRequestDispatcher("/WEB-INF/views/users.jsp").forward(request, response);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
