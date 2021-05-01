
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
        String nome_compagnia = "";
        try (PrintWriter out = response.getWriter()) {

            query = "SELECT DISTINCT aeroporto FROM volo";
            Statement statement = connessione.createStatement();
            ResultSet result = statement.executeQuery(query);

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
                    + "<input class=\"button\" type=\"submit\" value=\"Cerca\" name=\"Cerca\">\n"
                    + "</form>");
            out.println("<form id=\"codNome\" action=\"PostazioneMultimediale\" method=\"POST\">"
                    + "<input type=\"text\" id=\"sigla\" name=\"sigla\" placeholder=\"Sigla\">\n"
                    + "<input type=\"submit\" class=\"button\" id=\"Corrisponde\" name=\"Corrisponde\" value=\"Corrisponde\">"
                    + "<input type=\"text\" id=\"nome\" name=\"nome\" placeholder=\"Nome aeroporto\" value=\"\">"
                    + "</div>");

            String aeroporto = request.getParameter("aeroporto");
            String scelta = request.getParameter("scelta");
            if (aeroporto != null && scelta != null) {
                query = "SELECT compagnia, codice_volo, partenza, arrivo FROM volo WHERE aeroporto = '" + aeroporto + "' ORDER BY partenza";
                Statement statement1 = connessione.createStatement();
                ResultSet result1 = statement1.executeQuery(query);
                switch (scelta) {
                    case "tutti":
                        out.println("<table>");
                        out.println("<tr><th>Codice Volo</th><th>Compagnia</th><th>Partenza</th><th>Arrivo</th>");
                        while (result1.next()) {
                            String id = result1.getString(1);
                            PreparedStatement prepared = connessione.prepareStatement("SELECT nome_compagnia FROM compagnia WHERE id_compagnia = ?");
                            prepared.setString(1, id);
                            prepared.execute();
                            ResultSet rs = prepared.getResultSet();
                            while (rs.next()) {
                                nome_compagnia = rs.getString("nome_compagnia");
                            }
                            out.println("<tr>"
                                    + "<td>" + result1.getString(2) + "</td>"
                                    + "<td>" + nome_compagnia + "</td>"
                                    + "<td>" + result1.getString(3) + "</td>"
                                    + "<td>" + result1.getString(4) + "</td>"
                                    + "</tr>");
                        }
                        out.println("</table>");
                        break;
                    case "partenze":
                        out.println("<table>");
                        out.println("<tr><th>Codice Volo</th><th>Compagnia</th><th>Partenza</th>");

                        while (result1.next()) {
                            String id = result1.getString(1);
                            PreparedStatement prepared = connessione.prepareStatement("SELECT nome_compagnia FROM compagnia WHERE id_compagnia = ?");
                            prepared.setString(1, id);
                            prepared.execute();
                            ResultSet rs = prepared.getResultSet();
                            while (rs.next()) {
                                nome_compagnia = rs.getString("nome_compagnia");
                            }
                            out.println("<tr>"
                                    + "<td>" + result1.getString(2) + "</td>"
                                    + "<td>" + nome_compagnia + "</td>"
                                    + "<td>" + result1.getString(3) + "</td>"
                                    + "</tr>");
                        }
                        out.println("</table>");
                        break;
                    case "arrivi":
                        out.println("<table>");
                        out.println("<tr><th>Codice Volo</th><th>Compagnia</th><th>Arrivo</th>");

                        while (result1.next()) {
                            String id = result1.getString(1);
                            PreparedStatement prepared = connessione.prepareStatement("SELECT nome_compagnia FROM compagnia WHERE id_compagnia = ?");
                            prepared.setString(1, id);
                            prepared.execute();
                            ResultSet rs = prepared.getResultSet();
                            while (rs.next()) {
                                nome_compagnia = rs.getString("nome_compagnia");
                            }
                            out.println("<tr>"
                                    + "<td>" + result1.getString(2) + "</td>"
                                    + "<td>" + nome_compagnia + "</td>"
                                    + "<td>" + result1.getString(4) + "</td>"
                                    + "</tr>");
                        }
                        out.println("</table>");
                        break;
                    default:
                        break;
                }
            } else {
                query = "SELECT compagnia, codice_volo, partenza, arrivo FROM volo ORDER BY partenza";
                Statement statement2 = connessione.createStatement();
                ResultSet result2 = statement2.executeQuery(query);
                out.println("<table>");
                out.println("<tr><th>Codice Volo</th><th>Compagnia</th><th>Partenza</th><th>Arrivo</th>");

                while (result2.next()) {
                    String id = result2.getString(1);
                    PreparedStatement prepared = connessione.prepareStatement("SELECT nome_compagnia FROM compagnia WHERE id_compagnia = ?");
                    prepared.setString(1, id);
                    prepared.execute();
                    ResultSet rs = prepared.getResultSet();
                    while (rs.next()) {
                        nome_compagnia = rs.getString("nome_compagnia");
                    }
                    out.println("<tr>"
                            + "<td>" + result2.getString(2) + "</td>"
                            + "<td>" + nome_compagnia + "</td>"
                            + "<td>" + result2.getString(3) + "</td>"
                            + "<td>" + result2.getString(4) + "</td>"
                            + "</tr>");
                }
                out.println("</table>");
            }
            String sigla = request.getParameter("sigla");
            String nome = request.getParameter("nome");
            String nomeCorr="";
            if (sigla != null) {
                query = "SELECT DISTINCT aeroporto FROM volo WHERE sigla = '" + sigla + "'";
                Statement statement3 = connessione.createStatement();
                ResultSet result3 = statement3.executeQuery(query);
                while (result3.next()) {
                    nomeCorr = result3.getString(1);
                }
                if(nomeCorr.equals("")){
                    nomeCorr = "Inesistente";
                }
                out.println("<script>\n"
                        + "document.getElementById(\"nome\").value = \"" + nomeCorr + "\";\n"
                        + "document.getElementById(\"sigla\").value = \"" + sigla + "\";\n"     
                        + "</script>");
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
            Logger.getLogger(PostazioneMultimediale.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(PostazioneMultimediale.class
                    .getName()).log(Level.SEVERE, null, ex);
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
