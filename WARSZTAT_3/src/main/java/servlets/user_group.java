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

@WebServlet(name = "user_group", urlPatterns = "/user_group")
public class user_group extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String type = request.getParameter("type");

        if(type == null || type.isEmpty()){
            response.sendRedirect("/user_group");
        }

        try(
                Connection conn = DbUtil.getConn();
        ){


            String id = request.getParameter("id");
            String name = request.getParameter("name");

            boolean doAdd = (name != null && !name.isEmpty());
            boolean doDelete = (id != null && !id.isEmpty());
            boolean doEdit = (doAdd && doDelete);

            if(type.equals("add") && doAdd){
                UserGroup user_group = new UserGroup(name);
                user_group.saveToDB(conn);
            }else if(type.equals("edit") && doEdit){
                UserGroup user_group = UserGroup.loadUser_groupById(conn, Integer.parseInt(id));
                if(user_group != null){
                    user_group.setName(name);
                    user_group.saveToDB(conn);
                }
            }else if(type.equals("delete") && doDelete){
                UserGroup user_group = UserGroup.loadUser_groupById(conn, Integer.parseInt(id));
                if(user_group != null){
                    user_group.delete(conn);
                }
            }

            response.sendRedirect("/user_group");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try(
                Connection conn = DbUtil.getConn();
        ){
            UserGroup[] user_groups = UserGroup.loadAllUser_groups(conn);
            request.setAttribute("groups", user_groups);
            getServletContext().getRequestDispatcher("/WEB-INF/views/user_group.jsp").forward(request, response);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
