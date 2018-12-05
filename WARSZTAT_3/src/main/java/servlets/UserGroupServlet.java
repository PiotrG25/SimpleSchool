package servlets;

import other.DbUtil;
import classes.UserGroup;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "UserGroupServlet", urlPatterns = "/userGroup")
public class UserGroupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String type = request.getParameter("type");
        if(type == null || type.isEmpty()){
            response.sendRedirect("/userGroup");
        }

        String id = request.getParameter("id");
        String name = request.getParameter("name");

        switch(type){
            case "add":
                addUserGroup(name);
                break;
            case "edit":
                editUserGroup(id, name);
                break;
            case "delete":
                deleteUserGroup(id);
                break;
        }

        response.sendRedirect("/userGroup");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try(
                Connection conn = DbUtil.getConn();
        ){
            List<UserGroup> userGroups = UserGroup.loadAllUser_groups(conn);
            request.setAttribute("groups", userGroups);
        }catch(SQLException e){
            e.printStackTrace();
        }
        getServletContext().getRequestDispatcher("/WEB-INF/views/userGroup.jsp").forward(request, response);
    }


    private void addUserGroup(String name) {
        try(Connection conn = DbUtil.getConn();){

            UserGroup user_group = new UserGroup(name);
            user_group.saveToDB(conn);

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void editUserGroup(String id, String name){

        try(Connection conn = DbUtil.getConn();){

            if(id == null || !id.matches("^[0-9]+$")){
                conn.close();
                return;
            }

            UserGroup userGroup = UserGroup.loadUserGroupById(conn, Integer.parseInt(id));
            if(userGroup != null){

                if(name != null && !name.isEmpty()) {
                    userGroup.setName(name);
                }
                userGroup.saveToDB(conn);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void deleteUserGroup(String id){

        try(Connection conn = DbUtil.getConn();){

            if(id == null || !id.matches("^[0-9]+$")){
                conn.close();
                return;
            }

            UserGroup user_group = UserGroup.loadUserGroupById(conn, Integer.parseInt(id));
            if(user_group != null){
                user_group.delete(conn);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
