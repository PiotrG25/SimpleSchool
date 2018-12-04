package servlets;

import other.DbUtil;
import classes.Exercise;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "exercise", urlPatterns = "/exercise")
public class exercise extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String type = request.getParameter("type");

        if(type == null || type.isEmpty()){
            response.sendRedirect("/exercise");
        }

        try(
                Connection conn = DbUtil.getConn();
        ){

            String id = request.getParameter("id");
            String title = request.getParameter("title");
            String description = request.getParameter("description");

            boolean doAdd = (title != null && !title.isEmpty() && description != null && !description.isEmpty());
            boolean doEdit = (id != null && !id.isEmpty());
            boolean doDelete = (id != null && !id.isEmpty());

            if(type.equals("add") && doAdd){
                Exercise exercise = new Exercise(title, description);
                exercise.saveToDB(conn);
            }else if(type.equals("edit") && doEdit){
                Exercise exercise = Exercise.loadExerciseById(conn, Integer.parseInt(id));
                if(exercise != null){
                    if(title != null && !title.isEmpty()){
                        exercise.setTitle(title);
                    }
                    if(description != null && !description.isEmpty()){
                        exercise.setDescription(description);
                    }
                    exercise.saveToDB(conn);
                }
            }else if(type.equals("delete") && doDelete){
                Exercise exercise = Exercise.loadExerciseById(conn, Integer.parseInt(id));
                if(exercise != null){
                    exercise.delete(conn);
                }
            }

            response.sendRedirect("/exercise");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try(
                Connection conn = DbUtil.getConn();
        ){
            List<Exercise> exercises = Exercise.loadAllExercises(conn);
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            request.setAttribute("exercises", exercises);
            getServletContext().getRequestDispatcher("/WEB-INF/views/exercise.jsp").forward(request, response);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
