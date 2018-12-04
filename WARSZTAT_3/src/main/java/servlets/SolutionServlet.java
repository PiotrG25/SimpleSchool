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
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "SolutionServlet", urlPatterns = "/solution")
public class SolutionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String type = request.getParameter("type");
        if(type == null || type.isEmpty()){
            response.sendRedirect("/SolutionServlet");
        }

        String id = request.getParameter("id");
        String description = request.getParameter("description");
        String exerciseId = request.getParameter("exerciseId");
        String userId = request.getParameter("userId");

        switch(type){
            case "add":
                addSolution(description, exerciseId, userId);
                break;
            case "edit":
                editSolution(id, description, exerciseId, userId);
                break;
            case "delete":
                deleteSolution(id);
                break;
        }

        response.sendRedirect("/SolutionServlet");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try(
                Connection conn = DbUtil.getConn();
        ){
            List<Solution> solutions = Solution.loadAllSolutions(conn);
            request.setAttribute("solutions", solutions);
        }catch(SQLException e){
            e.printStackTrace();
        }
        getServletContext().getRequestDispatcher("/WEB-INF/views/SolutionServlet.jsp").forward(request, response);
    }


    private void addSolution(String description, String exerciseId, String userId){

        try(Connection conn = DbUtil.getConn();){

            if(exerciseId == null || !exerciseId.matches("^[0-9]+$") || userId == null || !userId.matches("^[0-9]+$")){
                conn.close();
                return;
            }

            LocalDateTime now = LocalDateTime.now();
            Solution solution = new Solution(now, now, description, Integer.parseInt(exerciseId), Integer.parseInt(userId));
            solution.saveToDB(conn);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void editSolution(String id, String description, String exerciseId, String userId){

        try(Connection conn = DbUtil.getConn();){

            if(id == null || !id.matches("^[0-9]+$")) {
                conn.close();
                return;
            }

            Solution solution = Solution.loadSolutionById(conn, Integer.parseInt(id));
            if(solution != null) {
                if(description != null && !description.isEmpty()) {
                    solution.setDescription(description);
                }
                if(exerciseId != null && exerciseId.matches("^[0-9]+$")){
                    solution.setExerciseId(Integer.parseInt(exerciseId));
                }
                if(userId != null && userId.matches("^[0-9]+$")){
                    solution.setUserId(Integer.parseInt(userId));
                }
                solution.saveToDB(conn);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    private void deleteSolution(String id){

        try(Connection conn = DbUtil.getConn();){

            if(id == null || !id.matches("^[0-9]+$")){
                conn.close();
                return;
            }

            Solution solution = Solution.loadSolutionById(conn, Integer.parseInt(id));
            if(solution != null){
                solution.delete(conn);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
