package pl.polsl.lab.servlets;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import pl.polsl.lab.model.CellException;
import pl.polsl.lab.model.GameManager;
import pl.polsl.lab.model.PositionOnBoard;

/**
 * Main game servlet. All of the session management is done here.
 *
 * @author Tomasz Zawadzki
 * @version Final 1.5
 */
@WebServlet("/Game")
public class GameServlet extends HttpServlet {

    /**
     * Local GameManager variable
     */
    private GameManager currentGame;

    /**
     * Parsed input x value
     */
    int xInt;
    /**
     * Parsed input y value
     */
    int yInt;
    /**
     * Information whether the user wants to flag a cell or not.
     */
    boolean wantToFlag;

    /**
     * Contains the nickname of the player
     */
    String nickname;

    /**
     * Contains the name of the player
     */
    String name;

    /**
     * Contains the surname of the player
     */
    String surname;

    /**
     * Contains information whether the game is being initialized
     */
    boolean firstLaunch = true;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=ISO-8859-2");
        String x = (String) request.getParameter("X");
        String y = (String) request.getParameter("Y");
        String flag = (String) request.getParameter("Flag");
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
            return;
        }

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();

        if (firstLaunch) {
            nickname = (String) request.getParameter("nickname");
            name = (String) request.getParameter("name");
            surname = (String) request.getParameter("surname");
            session.setAttribute("nickname", nickname);
            session.setAttribute("name", name);
            session.setAttribute("surname", surname);
            firstLaunch = false;
        }

        if (session.getAttribute("gameObject") == null || session.getAttribute("dbCon") == null) {
            this.currentGame = new GameManager();
            this.currentGame.initialize(10, 8);
            session.setAttribute("gameObject", this.currentGame);
            try {
                Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/minesweeper", "adm", "adm");
                session.setAttribute("dbCon", con);
                Statement statement = con.createStatement();
                statement.executeUpdate("CREATE TABLE Users "
                        + "(id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), name VARCHAR(50), "
                        + "surname VARCHAR(50), nickname VARCHAR(50) )");
                System.out.println("Table User created");
                statement.executeUpdate("CREATE TABLE Scores "
                        + "(PLAYER_ID INTEGER REFERENCES Users(id), "
                        + "Score INTEGER, "
                        + "score_date VARCHAR(50) )");

            } catch (Exception e) {
                System.out.println(e);
            }

        } else {
            this.currentGame = (GameManager) session.getAttribute("gameObject");
        }

        if (flag != null) {
            wantToFlag = !wantToFlag;
        }

        if (x != null && y != null) {
            try {
                xInt = Integer.parseInt(x);
                yInt = Integer.parseInt(y);
            } catch (NumberFormatException e) {
                response.sendError(response.SC_BAD_REQUEST, "Invalid parameters!");
            }
            PositionOnBoard pos = new PositionOnBoard(xInt, yInt);
            try {
                if (!wantToFlag) {
                    if (currentGame.getBoard().getCells().get(pos.getX()).get(pos.getY()).getIsMine()) {

                        response.sendRedirect("/GameOverServlet");
                    }
                    currentGame.getBoard().revealSingleCell(pos);
                    if (currentGame.getBoard().isGameWon()) {
                        response.sendRedirect("/GameWonServlet");
                    }
                } else {
                    currentGame.getBoard().getCells().get(pos.getX()).get(pos.getY()).setIsFlag();
                    wantToFlag = false;
                    response.sendRedirect("http://localhost:8080/Game");
                    out.print(currentGame.getBoard().getCells().get(pos.getX()).get(pos.getY()).getIsFlag());

                }
            } catch (CellException e) {
                out.print("This cell is already flagged!");
            }
        }

        out.println("<!DOCTYPE html>\n"
                + "<html lang=\"pl\">\n"
                + "\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>Minesweeper</title>\n"
                + "    <link rel=\"stylesheet\" href=\"minesweeper.css\">\n"
                + "</head>\n"
                + "\n"
                + "<body>\n"
                + "\n");
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lastStatus")) {
                    out.print("<div>Welcome back! Last time you " + cookie.getValue() + "</div>");
                    break;
                }
            }
        }
        out.println("<h2>" + nickname + "</h2>");
        for (int i = 0; i < this.currentGame.getBoard().getWidth(); i++) {
            out.print("<div class = \"parent\">");
            for (int j = 0; j < this.currentGame.getBoard().getHeight(); j++) {
                if (!this.currentGame.getBoard().getCells().get(i).get(j).getIsRevealed()) {
                    out.print("<a href = \"http://localhost:8080/Game?X=");
                    out.print(i);
                    out.print("&");
                    out.print("Y=");
                    out.print(j);
                    out.print("\">");
                    out.print("<button type = \"button\" class = \"button" + (currentGame.getBoard().getCells().get(i).get(j).getIsFlag() ? " type-flag" : "") + "\">");
                    out.print(currentGame.getBoard().getCells().get(i).get(j));
                    out.print("</button>");
                    out.print("</a>");
                } else {
                    out.print("<button type = \"button\" class = \"clicked-button type-" + currentGame.getBoard().getCells().get(i).get(j) + "\">");
                    out.print(currentGame.getBoard().getCells().get(i).get(j));
                    out.print("</button>");
                }
            }
            out.print("</div>");
        }
        out.print("<a href = \"http://localhost:8080/Game?Flag=1\">");
        out.print("<button type = \"button\" class = \"back-button\">Flag a cell </button>");
        out.print("</a>");
        out.println("\n"
                + "</body>\n"
                + "\n"
                + "</html>");
    }
}
