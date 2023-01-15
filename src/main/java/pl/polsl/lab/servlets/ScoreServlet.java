/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package pl.polsl.lab.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * A servlet to display recent game scores in a table.
 *
 * @author Tomasz Zawadzki
 * @version 1.5 Final
 */
@WebServlet(name = "ScoreServlet", urlPatterns = {"/ScoreServlet"})
public class ScoreServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>\n"
                    + "<html lang=\"pl\">\n"
                    + "\n"
                    + "<head>\n"
                    + "    <meta charset=\"UTF-8\">\n"
                    + "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                    + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    + "    <title>Scores</title>\n"
                    + "    <link rel=\"stylesheet\" href=\"minesweeper.css\">\n"
                    + "</head>\n"
                    + "\n"
                    + "<body>\n"
                    + "\n");
            out.println("<div class = \"table-div\">");
            out.println("<table class = \"styled-table\">\n"
                    + "<thead>\n"
                    + "  <tr>\n"
                    + "    <th>Name</th>\n"
                    + "    <th>Surname</th>\n"
                    + "    <th>Nickname</th>\n"
                    + "    <th>Score</th>\n"
                    + "    <th>Scoring date</th>\n"
                    + "  </tr>\n"
                    + "</thead> <tbody>");
            try {
                Connection con = (Connection) session.getAttribute("dbCon");
                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM USERS JOIN SCORES ON USERS.ID = SCORES.PLAYER_ID");
                while (rs.next()) {
                    out.println("<tr>\n"
                            + "<td>" + rs.getString("Name") + "</td>\n"
                            + "<td>" + rs.getString("surname") + "</td>\n"
                            + "<td>" + rs.getString("nickname") + "</td>\n"
                            + "<td>" + rs.getInt("score") + "</td>\n"
                            + "<td>" + rs.getString("score_date") + "</td>\n");
                }

            } catch (SQLException sqle) {
                System.err.println(sqle.getMessage());
            }
            out.println("</tbody></table>");
            out.println("</div>");

            out.println("<a href = http://localhost:8080/Game>");
            out.println("<button class = \"back-button\">New game</button>");
            out.println("</a>");

            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
