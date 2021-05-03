/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author giosu
 */
@WebServlet(urlPatterns = {"/ComputerCentrale"})
public class ComputerCentrale extends HttpServlet {

    private final String URL = "jdbc:mysql://localhost:3306/aeroporto";
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String USER = "root";
    private final String PSW = "";
    private String query = "";
    private Statement statement;
    private ResultSet result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        Connection connessione = null;

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
            System.err.println("Driver non trovato. " + ex.getMessage());
        }

        connessione = DriverManager.getConnection(URL, USER, PSW);

        HttpSession sessione = request.getSession();
        if (sessione.getAttribute("username") != null) {
            response.setContentType("text/html;charset=UTF-8");
            String nome_compagnia = "";
            try (PrintWriter out = response.getWriter()) {

                query = "SELECT * FROM volo ORDER BY partenza;";

                statement = connessione.createStatement();
                result = statement.executeQuery(query);

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Gestione Voli</title>");
                out.println("<script src=\"js/jquery-3.6.0.min.js\"></script>");
                out.println("<script src=\"js/app.js\"></script>");
                out.println("<link rel=\"stylesheet\" href=\"style/style.css\">");
                out.println("</head>");
                out.println("<body>");
                request.getRequestDispatcher("config/menu.jsp").include(request, response);
                out.println("<div class='tablesContainer'>");
                out.println("<div class='leftTable'>");
                out.println("<h1>Gestione voli</h1>");
                out.println("<table>");
                out.println("<tr><th>Sigla</th><th>Aeroporto</th><th>Compagnia</th><th>Codice volo</th><th>Partenza</th><th>Arrivo</th><th>Tipo</th><th class=\"centered\">Modifica</th><th class=\"centered\">Elimina</th>");

                while (result.next()) {
                    String id = result.getString(4);
                    PreparedStatement prepared = connessione.prepareStatement("SELECT nome_compagnia FROM compagnia WHERE id_compagnia = ?");
                    prepared.setString(1, id);
                    prepared.execute();
                    ResultSet rs = prepared.getResultSet();
                    while (rs.next()) {
                        nome_compagnia = rs.getString("nome_compagnia");
                    }
                    out.println("<tr>"
                            + "<td>" + result.getString(2) + "</td>"
                            + "<td>" + result.getString(3) + "</td>"
                            + "<td>" + nome_compagnia + "</td>"
                            + "<td>" + result.getString(5) + "</td>"
                            + "<td>" + result.getString(6) + "</td>"
                            + "<td>" + result.getString(7) + "</td>"
                            + "<td>" + result.getString(8) + "</td>"
                            + "<td class=\"centered\"><button class=\"editFlight\">Modifica</button></td>"
                            + "<td class=\"centered\"><button class=\"delFlight\">Elimina</button></td>"
                            + "</tr>");
                }
                out.println("</table>");
                out.println("</div>");

                query = "SELECT nome_compagnia FROM compagnia ORDER BY nome_compagnia";

                statement = connessione.createStatement();
                result = statement.executeQuery(query);

                out.println("<div class='rightTable'>");
                out.println("<h1>Gestione compagnie</h1>");
                out.println("<table>");
                out.println("<tr><th>Nome compagnia</th><th class=\"centered\">Modifica</th><th class=\"centered\">Elimina</th>");
                while (result.next()) {
                    out.println("<tr>"
                            + "<td>" + result.getString(1) + "</td>"
                            + "<td class=\"centered\"><button class=\"editCompany\">Modifica</button></td>"
                            + "<td class=\"centered\"><button class=\"delCompany\">Elimina</button></td>"
                            + "</tr>");
                }
                out.println("</table>");
                out.println("</div>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            }
        } else {
            response.sendRedirect("index.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ComputerCentrale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ComputerCentrale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
