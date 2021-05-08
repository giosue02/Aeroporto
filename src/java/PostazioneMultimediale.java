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

@WebServlet(urlPatterns={"/PostazioneMultimediale"})
public class PostazioneMultimediale extends HttpServlet {

    private final String URL = "jdbc:mysql://localhost:3306/aeroporto";
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String USER = "root";
    private final String PSW = "";
    private String query = "";
    private String nomeGlob;
    private String siglaGlob;

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
        String lista_codvoli = "";
        String nome_compagnia = "";
        try (PrintWriter out = response.getWriter()) {

            query = "SELECT DISTINCT aeroporto FROM volo";
            Statement statement = connessione.createStatement();
            ResultSet result = statement.executeQuery(query);

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Postazione Multimediale</title>");
            out.println("<link rel=\"stylesheet\" href=\"style/style.css\">");
            out.println("<script src=\"js/jquery-3.6.0.min.js\"></script>");
            out.println("<script src=\"js/app.js\"></script>");
            out.println("</head>");
            out.println("<body>");
            request.getRequestDispatcher("config/menu.jsp").include(request, response);
            out.println("<h1>Postazione multimediale</h1>");
            out.println("<div class=\"formContainer\">"
                    + "<div id=\"searchContainer\"><form id=\"PartenzeArrivi\" action=\"PostazioneMultimediale\" method=\"POST\">");
            lista_aeroporti = "<select name=\"aeroporto\" id=\"aeroporto\" required><option value=\"\" selected disabled hidden>Seleziona aeroporto</option>";
            while (result.next()) {
                String nome_aeroporto = result.getString(1);
                lista_aeroporti += "<option value=\"" + nome_aeroporto + "\">" + nome_aeroporto + "</option>";
            }
            lista_aeroporti += "</select>";
            out.println(lista_aeroporti);
            out.println("<select name=\"scelta\" id=\"scelta\" required>\n"
                    + "<option value=\"\" selected disabled hidden>Partenze/Arrivi</option>\n"
                    + "<option value=\"tutti\">Vedi tutti</option>\n"
                    + "<option value=\"partenze\">Partenze</option>\n"
                    + "<option value=\"arrivi\">Arrivi</option>\n"
                    + "</select>\n"
                    + "<input class=\"button\" type=\"submit\" value=\"Cerca\" id=\"Cerca\" name=\"Cerca\">\n"
                    + "</form>");
            out.println("<form id=\"codVolo\" action=\"PostazioneMultimediale\" method=\"POST\">"
                    + "<select id=\"codiceVolo\" name=\"codiceVolo\" required>\n"
                    + "<option value=\"\" selected disabled hidden>Seleziona cod. volo</option>");
            query = "SELECT DISTINCT codice_volo FROM volo";
            Statement statement5 = connessione.createStatement();
            ResultSet result5 = statement5.executeQuery(query);
            while (result5.next()) {
                String codvolo = result5.getString(1);
                lista_codvoli += "<option value=\"" + codvolo + "\">" + codvolo + "</option>";
            }
            lista_codvoli += "</select>";
            out.println(lista_codvoli);
            out.println("<input type=\"submit\" class=\"button\" id=\"CercaCodVolo\" name=\"CercaCodVolo\" value=\"Cerca\">"
                    + "</form></div>");
            out.println("<form id=\"codNome\" action=\"PostazioneMultimediale\" method=\"POST\">"
                    + "<input type=\"text\" id=\"sigla\" name=\"sigla\" placeholder=\"Sigla\">\n"
                    + "<input type=\"submit\" class=\"button\" id=\"Corrisponde\" name=\"Corrisponde\" value=\"Corrisponde\">"
                    + "<input type=\"text\" id=\"nomeAeroporto\" name=\"nomeAeroporto\" placeholder=\"Nome aeroporto\">"
                    + "</form></div>");

            String aeroporto = request.getParameter("aeroporto");
            String scelta = request.getParameter("scelta");
            String codiceVolo = request.getParameter("codiceVolo");
            if (aeroporto != null && scelta != null) {
                query = "SELECT compagnia, codice_volo, partenza, arrivo, sigla FROM volo WHERE aeroporto = '" + aeroporto + "' ORDER BY partenza";
                Statement statement1 = connessione.createStatement();
                ResultSet result1 = statement1.executeQuery(query);
                switch (scelta) {
                    case "tutti":
                        out.println("<table>");
                        out.println("<tr><th>Codice Volo</th><th>Compagnia</th><th>Sigla</th><th>Partenza</th><th>Arrivo</th>");
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
                                    + "<td>" + result1.getString(5) + "</td>"
                                    + "<td>" + result1.getString(3) + "</td>"
                                    + "<td>" + result1.getString(4) + "</td>"
                                    + "</tr>");
                        }
                        out.println("</table>");
                        break;
                    case "partenze":
                        out.println("<table>");
                        out.println("<tr><th>Codice Volo</th><th>Compagnia</th><th>Sigla</th><th>Partenza</th>");

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
                                    + "<td>" + result1.getString(5) + "</td>"
                                    + "<td>" + result1.getString(3) + "</td>"
                                    + "</tr>");
                        }
                        out.println("</table>");
                        break;
                    case "arrivi":
                        out.println("<table>");
                        out.println("<tr><th>Codice Volo</th><th>Compagnia</th><th>Sigla</th><th>Arrivo</th>");

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
                                    + "<td>" + result1.getString(5) + "</td>"
                                    + "<td>" + result1.getString(4) + "</td>"
                                    + "</tr>");
                        }
                        out.println("</table>");
                        break;
                    default:
                        break;
                }
            } else if (codiceVolo != null) {
                query = "SELECT compagnia, codice_volo, partenza, arrivo, sigla FROM volo WHERE codice_volo = '" + codiceVolo + "' ORDER BY partenza";
                Statement statement6 = connessione.createStatement();
                ResultSet result6 = statement6.executeQuery(query);
                
                out.println("<table>");
                out.println("<tr><th>Codice Volo</th><th>Compagnia</th><th>Sigla</th><th>Partenza</th><th>Arrivo</th>");

                while (result6.next()) {
                    String id = result6.getString(1);
                    PreparedStatement prepared = connessione.prepareStatement("SELECT nome_compagnia FROM compagnia WHERE id_compagnia = ?");
                    prepared.setString(1, id);
                    prepared.execute();
                    ResultSet rs = prepared.getResultSet();
                    while (rs.next()) {
                        nome_compagnia = rs.getString("nome_compagnia");
                    }
                    out.println("<tr>"
                            + "<td>" + result6.getString(2) + "</td>"
                            + "<td>" + nome_compagnia + "</td>"
                            + "<td>" + result6.getString(5) + "</td>"
                            + "<td>" + result6.getString(3) + "</td>"
                            + "<td>" + result6.getString(4) + "</td>"
                            + "</tr>");
                }
                out.println("</table>");
            }else{
                query = "SELECT compagnia, codice_volo, partenza, arrivo, sigla FROM volo ORDER BY partenza";
                Statement statement2 = connessione.createStatement();
                ResultSet result2 = statement2.executeQuery(query);
                out.println("<table>");
                out.println("<tr><th>Codice Volo</th><th>Compagnia</th><th>Sigla</th><th>Partenza</th><th>Arrivo</th>");

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
                            + "<td>" + result2.getString(5) + "</td>"
                            + "<td>" + result2.getString(3) + "</td>"
                            + "<td>" + result2.getString(4) + "</td>"
                            + "</tr>");
                }
                out.println("</table>");
            }
            
            String sigla = request.getParameter("sigla");
            String nomeAeroporto = request.getParameter("nomeAeroporto");
            String nomeCorr = "";
            String siglaCorr = "";
            
            if (sigla != null && !sigla.equals("Inesistente") && !sigla.equals(siglaGlob)) {
                query = "SELECT DISTINCT aeroporto FROM volo WHERE sigla = '" + sigla + "'";
                Statement statement3 = connessione.createStatement();
                ResultSet result3 = statement3.executeQuery(query);
                while (result3.next()) {
                    nomeCorr = result3.getString(1);
                }
                if (nomeCorr.equals("")) {
                    nomeCorr = "Inesistente";
                }
                out.println("<script>\n"
                        + "document.getElementById(\"nomeAeroporto\").value = \"" + nomeCorr + "\";\n"
                        + "document.getElementById(\"sigla\").value = \"" + sigla + "\";\n"
                        + "</script>");
                siglaGlob = sigla;
                nomeGlob = nomeCorr;
            } else if (nomeAeroporto != null && !nomeAeroporto.equals("Inesistente") && !nomeAeroporto.equals(nomeGlob)) {
                query = "SELECT DISTINCT sigla FROM volo WHERE aeroporto = '" + nomeAeroporto + "'";
                Statement statement4 = connessione.createStatement();
                ResultSet result4 = statement4.executeQuery(query);
                while (result4.next()) {
                    siglaCorr = result4.getString(1);
                }
                if (siglaCorr.equals("")) {
                    siglaCorr = "Inesistente";
                }
                out.println("<script>\n"
                        + "document.getElementById(\"nomeAeroporto\").value = \"" + nomeAeroporto + "\";\n"
                        + "document.getElementById(\"sigla\").value = \"" + siglaCorr + "\";\n"
                        + "</script>");
                siglaGlob = siglaCorr;
                nomeGlob = nomeAeroporto;
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
