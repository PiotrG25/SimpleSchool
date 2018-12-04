package servlets;

import other.DbUtil;
import classes.Exercise;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebEndpoint;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ExerciseServlet", urlPatterns = "/exercise")
public class ExerciseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String type = request.getParameter("type");
        if(type == null || type.isEmpty()){
            response.sendRedirect("/exercise");
        }

        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        switch(type){
            case "add":
                addExercise(title, description);
                break;
            case "edit":
                editExercise(id, title, description);
                break;
            case "delete":
                deleteExercise(id);
                break;
        }

        response.sendRedirect("/exercise");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try(
                Connection conn = DbUtil.getConn();
        ){
            List<Exercise> exercises = Exercise.loadAllExercises(conn);
            request.setAttribute("exercises", exercises);
        }catch(SQLException e){
            e.printStackTrace();
        }
        getServletContext().getRequestDispatcher("/WEB-INF/views/exercise.jsp").forward(request, response);
    }


    private void addExercise(String title, String description){
        try(Connection conn = DbUtil.getConn();){

            if(title == null || title.isEmpty() || description == null || description.isEmpty()){
                conn.close();
                return;
            }

            Exercise exercise = new Exercise(title, description);
            exercise.saveToDB(conn);

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void editExercise(String id, String title, String description){

        try(Connection conn = DbUtil.getConn();){

            if (id == null || !id.matches("^[0-9]+$")) {
                conn.close();
                return;
            }

            Exercise exercise = Exercise.loadExerciseById(conn, Integer.parseInt(id));
            if (exercise != null) {
                if (title != null && !title.isEmpty()) {
                    exercise.setTitle(title);
                }
                if (description != null && !description.isEmpty()) {
                    exercise.setDescription(description);
                }
                exercise.saveToDB(conn);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteExercise(String id){

        try(Connection conn = DbUtil.getConn();){

            if(id == null || !id.matches("^[0-9]+$")){
                conn.close();
                return;
            }

            Exercise exercise = Exercise.loadExerciseById(conn, Integer.parseInt(id));
            if(exercise != null){
                exercise.delete(conn);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
