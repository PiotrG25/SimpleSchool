package servlets;

import other.DbUtil;
import classes.Solution;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

@WebServlet(name = "solution", urlPatterns = "/solution")
public class solution extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String type = request.getParameter("type");

        if(type == null || type.isEmpty()){
            response.sendRedirect("/solution");
        }

        try(
                Connection conn = DbUtil.getConn();
        ){

            String id = request.getParameter("id");
            String description = request.getParameter("description");
            String exercise_id = request.getParameter("exercise_id");
            String users_id = request.getParameter("users_id");

            boolean doAdd = (description != null && !description.isEmpty() &&
                    exercise_id != null && !exercise_id.isEmpty() && users_id != null && !users_id.isEmpty());
            boolean doEdit = (id != null && !id.isEmpty());
            boolean doDelete = (id != null && !id.isEmpty());

            if(type.equals("add") && doAdd){
                Calendar now = Calendar.getInstance();
                Solution solution = new Solution(now, now, description, Integer.parseInt(exercise_id), Integer.parseInt(users_id));
                solution.saveToDB(conn);
            }else if(type.equals("edit") && doEdit){
                Solution solution = Solution.loadSolutionById(conn, Integer.parseInt(id));
                if(solution != null){
                    boolean updated = false;
                    if(description != null && !description.isEmpty()){
                        solution.setDescription(description);
                        updated = true;
                    }
                    if(exercise_id != null && !exercise_id.isEmpty()){
                        solution.setExercise_id(Integer.parseInt(exercise_id));
                        updated = true;
                    }
                    if(users_id != null && !users_id.isEmpty()){
                        solution.setUsers_is(Integer.parseInt(users_id));
                        updated = true;
                    }
                    if(updated){
                        solution.setUpdated(Calendar.getInstance());
                    }
                    solution.saveToDB(conn);
                }
            }else if(type.equals("delete") && doDelete){
                Solution solution = Solution.loadSolutionById(conn, Integer.parseInt(id));
                if(solution != null){
                    solution.delete(conn);
                }
            }

            response.sendRedirect("/solution");

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try(
                Connection conn = DbUtil.getConn();
        ){
            Solution[] solutions = Solution.loadAllSolutions(conn);
            request.setAttribute("solutions", solutions);
            getServletContext().getRequestDispatcher("/WEB-INF/views/solution.jsp").forward(request, response);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
