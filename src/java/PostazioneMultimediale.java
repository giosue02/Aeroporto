
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PostazioneMultimediale extends HttpServlet {

    private final String URL = "jdbc:mysql://localhost:3306/aeroporto";
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String USER = "root";
    private final String PSW = "";
    private String query = "";
    private Statement statement;
    private ResultSet result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        Connection connessione = null;

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
            System.err.println("Driver non trovato. " + ex.getMessage());
        }

        connessione = DriverManager.getConnection(URL, USER, PSW);

        response.setContentType("text/html;charset=UTF-8");

        String lista_aeroporti = "";
        boolean check = false;
        if (request.getParameter("Invia") != null) {
            check = true;
        }
        try (PrintWriter out = response.getWriter()) {

            query = "SELECT DISTINCT aeroporto FROM volo";

            statement = connessione.createStatement();
            result = statement.executeQuery(query);

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Postazione Multimediale</title>");
            out.println("<script src=\"js/jquery-3.6.0.min.js\"></script>");
            out.println("<script src=\"js/app.js\"></script>");
            out.println("<link rel=\"stylesheet\" href=\"style/style.css\">");
            out.println("</head>");
            out.println("<body>");
            request.getRequestDispatcher("config/menu.html").include(request, response);
            out.println("<h1>Postazione multimediale</h1>");
            out.println("<div class=\"formContainer\"><form action=\"PostazioneMultimediale\" method=\"POST\">");
            lista_aeroporti = "<select name=\"aeroporto\" id=\"aeroporto\"><option value=\"\" selected disabled hidden>Seleziona aeroporto</option>";
            while (result.next()) {
                String nome_aeroporto = result.getString(1);
                lista_aeroporti += "<option value=\"" + nome_aeroporto + "\">" + nome_aeroporto + "</option>";
            }
            lista_aeroporti += "</select>";
            out.println(lista_aeroporti);
            out.println("<select name=\"scelta\" id=\"scelta\">\n"
                    + "<option value=\"\" selected disabled hidden>Partenze/Arrivi</option>\n"
                    + "<option value=\"tutti\">Vedi tutti</option>\n"
                    + "<option value=\"partenze\">Partenze</option>\n"
                    + "<option value=\"arrivi\">Arrivi</option>\n"
                    + "</select>\n"
                    + "<input class=\"button\" type=\"submit\" value=\"Cerca\" name=\"Invia\">\n"
                    + "</form>");
            out.println("<form id=\"codNome\" action=\"PostazioneMultimediale\" method=\"POST\">"
                    + "<input type=\"text\" name=\"sigla\" placeholder=\"Sigla\">\n"
                    + "<input type=\"submit\" class=\"button\" id=\"Corrisponde\" name=\"Corrisponde\" value=\"Corrisponde\">"
                    + "<input type=\"text\" name=\"nome\" placeholder=\"Nome\">"
                    + "</div>");
            if (check == true) {
                String aeroporto = request.getParameter("aeroporto");
                if (!aeroporto.equals("")) {
                    String scelta = request.getParameter("scelta");
                    query = "SELECT compagnia, codice_volo, partenza, arrivo FROM volo WHERE aeroporto = '" + aeroporto + "' ORDER BY partenza";
                    statement = connessione.createStatement();
                    result = statement.executeQuery(query);
                    String nome_compagnia = "";
                    switch (scelta) {
                        case "tutti":
                            out.println("<table>");
                            out.println("<tr><th>Codice Volo</th><th>Compagnia</th><th>Partenza</th><th>Arrivo</th>");

                            while (result.next()) {
                                String id = result.getString(1);
                                PreparedStatement prepared = connessione.prepareStatement("SELECT nome_compagnia FROM compagnia WHERE id_compagnia = ?");
                                prepared.setString(1, id);
                                prepared.execute();
                                ResultSet rs = prepared.getResultSet();
                                while (rs.next()) {
                                    nome_compagnia = rs.getString("nome_compagnia");
                                }
                                out.println("<tr>"
                                        + "<td>" + result.getString(2) + "</td>"
                                        + "<td>" + nome_compagnia + "</td>"
                                        + "<td>" + result.getString(3) + "</td>"
                                        + "<td>" + result.getString(4) + "</td>"
                                        + "</tr>");
                            }
                            out.println("</table>");
                            break;
                        case "partenze":
                            out.println("<table>");
                            out.println("<tr><th>Codice Volo</th><th>Compagnia</th><th>Partenza</th>");

                            while (result.next()) {
                                String id = result.getString(1);
                                PreparedStatement prepared = connessione.prepareStatement("SELECT nome_compagnia FROM compagnia WHERE id_compagnia = ?");
                                prepared.setString(1, id);
                                prepared.execute();
                                ResultSet rs = prepared.getResultSet();
                                while (rs.next()) {
                                    nome_compagnia = rs.getString("nome_compagnia");
                                }
                                out.println("<tr>"
                                        + "<td>" + result.getString(2) + "</td>"
                                        + "<td>" + nome_compagnia + "</td>"
                                        + "<td>" + result.getString(3) + "</td>"
                                        + "</tr>");
                            }
                            out.println("</table>");
                            break;
                        case "arrivi":
                            out.println("<table>");
                            out.println("<tr><th>Codice Volo</th><th>Compagnia</th><th>Arrivo</th>");

                            while (result.next()) {
                                String id = result.getString(1);
                                PreparedStatement prepared = connessione.prepareStatement("SELECT nome_compagnia FROM compagnia WHERE id_compagnia = ?");
                                prepared.setString(1, id);
                                prepared.execute();
                                ResultSet rs = prepared.getResultSet();
                                while (rs.next()) {
                                    nome_compagnia = rs.getString("nome_compagnia");
                                }
                                out.println("<tr>"
                                        + "<td>" + result.getString(2) + "</td>"
                                        + "<td>" + nome_compagnia + "</td>"
                                        + "<td>" + result.getString(4) + "</td>"
                                        + "</tr>");
                            }
                            out.println("</table>");
                            break;
                        default:
                            break;
                    }
                }
            }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(PostazioneMultimediale.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(PostazioneMultimediale.class.getName()).log(Level.SEVERE, null, ex);
        }
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
