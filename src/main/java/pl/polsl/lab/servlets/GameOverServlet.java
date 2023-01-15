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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pl.polsl.lab.model.GameManager;

/**
 * A servlet which showcases the game lost message.
 *
 * @author Tomasz Zawadzki
 * @version Final 1.5
 */
@WebServlet(name = "GameOverServlet", urlPatterns = {"/GameOverServlet"})
public class GameOverServlet extends HttpServlet {

    /**
     * Local GameManager variable
     */
    private GameManager currentGame;

    /**
     * Contains the name of the player
     */
    String name;

    /**
     * Contains the surname of the player
     */
    String surname;

    /**
     * Contains the nickname of the player
     */
    String nickname;

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
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
            return;
        }
        HttpSession session = request.getSession();
        int score = -1;
        this.currentGame = (GameManager) session.getAttribute("gameObject");
        if (currentGame != null) {
            score = currentGame.calculateFinalScore();
            name = (String) session.getAttribute("name");
            surname = (String) session.getAttribute("surname");
            nickname = (String) session.getAttribute("nickname");
        }

        Connection con = (Connection) session.getAttribute("dbCon");
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("INSERT INTO USERS (NAME, SURNAME, NICKNAME) VALUES ('" + name + "', '" + surname + "', '" + nickname + "')");
            System.out.println("Data inserted");
            ResultSet rs = statement.executeQuery("SELECT ID FROM USERS");
            int id = -1;
            while (rs.next()) {
                if (rs.getInt("id") > id) {
                    id = rs.getInt("id");
                }
            }
            System.out.println(id);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            statement.executeUpdate("INSERT INTO SCORES (PLAYER_ID, SCORE, SCORE_DATE) VALUES (" + id + ", " + score + ", '" + dtf.format(now) + "')");
            System.out.print("Success scores");

        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        }

        Cookie cookie = new Cookie("lastStatus", "lost");
        response.addCookie(cookie);
        session.setAttribute("gameObject", null);
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.print("<meta charset=\"UTF-8\">\n"
                    + "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                    + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    + "    <title>Minesweeper</title>\n"
                    + "    <link rel=\"stylesheet\" href=\"minesweeper.css\">\n");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Game Over! You lose!</h1>");
            out.println("<br>");
            if (score != -1) {
                out.println("Your score was " + score);
            }
            out.println("<br>");
            out.println("<a href = http://localhost:8080/Game>");
            out.println("<button class = \"back-button\">New game</button>");
            out.println("</a>");
            out.println("<br>");
            out.println("<a href = http://localhost:8080/ScoreServlet>");
            out.println("<button class = \"back-button\">Show recent scores</button>");
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
