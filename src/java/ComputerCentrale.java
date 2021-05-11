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
                out.println("<title>Computer centrale</title>");
                out.println("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css\">");
                out.println("<link rel=\"stylesheet\" href=\"style/style.css\">");
                out.println("<script src=\"js/jquery-3.6.0.min.js\"></script>");
                out.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js\"></script>");
                out.println("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/js/bootstrap.min.js\"></script>");
                out.println("<script src=\"js/app.js\"></script>");
                out.println("</head>");
                out.println("<body style=\"line-height: 1.2;\">");
                request.getRequestDispatcher("config/menu.jsp").include(request, response);
                out.println("<div class=\"contenitore\">");
                out.println("<div class=\"modal fade\" id=\"ModalAddFlight\" role=\"dialog\">\n"
                        + "\n"
                        + "    <div class=\"modal-dialog\">\n"
                        + "      <div class=\"modal-content\">\n"
                        + "\n"
                        + "        <div class=\"modal-header\">\n"
                        + "          <h4 class=\"modal-title\">Aggiungi volo</h4>\n"
                        + "          <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n"
                        + "        </div>\n"
                        + "<div class=\"modal-body\">"
                        + "<label class=\"col-md-6\">Sigla:</label>"
                        + "<input class=\"form-control\" type=\"text\" maxlength = \"3\" id=\"siglaVolo\" name=\"siglaVolo\" placeholder=\"Inserisci sigla\"><br>\n"
                        + "<label class=\"col-md-6\">Aeroporto:</label>"
                        + "<input class=\"form-control\" type=\"text\" id=\"aeroporto\" name=\"aeroporto\" placeholder=\"Inserisci aeroporto\"><br>\n"
                        + "<label class=\"col-md-6\">Compagnia:</label>"
                        + "<select class=\"form-control\" id=\"compagnia\" name=\"compagnia\"><option value=\"\" selected disabled hidden>Selez. compagnia</option>");
                String querySelect = "SELECT id_compagnia, nome_compagnia FROM compagnia";
                Statement statement1 = connessione.createStatement();
                ResultSet result1 = statement1.executeQuery(querySelect);
                String selectCompagnia = "";
                while (result1.next()) {
                    selectCompagnia += "<option value=" + result1.getString(1) + ">" + result1.getString(2) + "</option>";
                }
                out.println(selectCompagnia + "</select><br>");
                out.println("<label class=\"col-md-6\">Codice volo:</label>");
                out.println("<input class=\"form-control\" type=\"text\" maxlength = \"7\" id=\"codicevolo\" name=\"codicevolo\" placeholder=\"Inserisci cod. volo\"><br>\n"
                        + "<label class=\"col-md-6\">Partenza:</label>"
                        + "<input class=\"form-control\" type=\"time\" id=\"partenza\" name=\"partenza\"><br>\n"
                        + "<label class=\"col-md-6\">Arrivo:</label>"
                        + "<input class=\"form-control\" type=\"time\" id=\"arrivo\" name=\"arrivo\"><br>\n"
                        + "<label class=\"col-md-6\" id=\"andata\">Andata:</label><input type=\"radio\" id=\"and\" name=\"tipo\" value=\"And\">\n"
                        + "<label class=\"col-md-6\" id=\"ritorno\">Ritorno:</label><input type=\"radio\" id=\"rit\" name=\"tipo\" value=\"Rit\">\n"
                        + "</div>\n"
                        + "\n"
                        + "        <div class=\"modal-footer\">\n"
                        + "          <button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\" id=\"annullaAddFlight\">Annulla</button>\n"
                        + "          <button type=\"button\" class=\"btn btn-primary\" id=\"confermaAddFlight\">Conferma</button>\n"
                        + "        </div>\n"
                        + "\n"
                        + "      </div>\n"
                        + "    </div>\n"
                        + "  </div>");
                out.println("<div class=\"modal fade\" id=\"ModalAddCompany\" role=\"dialog\">\n"
                        + "\n"
                        + "    <div class=\"modal-dialog\">\n"
                        + "      <div class=\"modal-content\">\n"
                        + "\n"
                        + "        <div class=\"modal-header\">\n"
                        + "          <h4 class=\"modal-title\">Aggiungi compagnia</h4>\n"
                        + "          <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n"
                        + "        </div>\n"
                        + "\n"
                        + "        <div class=\"modal-body\">\n"
                        + "            <label class=\"col-md-6\">Nome compagnia:</label>\n"
                        + "          <input type=\"text\" class=\"form-control\" id=\"nome_compagnia\" name=\"nome_compagnia\" placeholder=\"Inserisci nome\">\n"
                        + "        </div>\n"
                        + "\n"
                        + "        <div class=\"modal-footer\">\n"
                        + "          <button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\" id=\"annullaAddCompany\">Annulla</button>\n"
                        + "          <button type=\"button\" class=\"btn btn-primary\" id=\"confermaAddCompany\">Conferma</button>\n"
                        + "        </div>\n"
                        + "\n"
                        + "      </div>\n"
                        + "    </div>\n"
                        + "  </div>");
                out.println("<div class=\"modal fade\" id=\"ModalEditFlight\" role=\"dialog\">\n"
                        + "\n"
                        + "    <div class=\"modal-dialog\">\n"
                        + "      <div class=\"modal-content\">\n"
                        + "\n"
                        + "        <div class=\"modal-header\">\n"
                        + "          <h4 class=\"modal-title\">Modifica volo</h4>\n"
                        + "          <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n"
                        + "        </div>\n"
                        + "<div class=\"modal-body\">"
                        + "<div id=\"idVoloMod\" style=\"display: none;\"></div>\n"
                        + "<label class=\"col-md-6\">Sigla:</label>"
                        + "<input class=\"form-control\" type=\"text\" maxlength = \"3\" id=\"siglaVoloMod\" name=\"siglaVoloMod\"><br>\n"
                        + "<label class=\"col-md-6\">Aeroporto:</label>"
                        + "<input class=\"form-control\" type=\"text\" id=\"aeroportoMod\" name=\"aeroportoMod\"><br>\n"
                        + "<label class=\"col-md-6\">Compagnia:</label>"
                        + "<select class=\"form-control\" id=\"compagniaMod\" name=\"compagniaMod\">");
                Statement statement2 = connessione.createStatement();
                ResultSet result2 = statement2.executeQuery(querySelect);
                String selectCompagniaMod = "";
                while (result2.next()) {
                    selectCompagniaMod += "<option value=" + result2.getString(1) + ">" + result2.getString(2) + "</option>";
                }
                out.println(selectCompagniaMod + "</select><br>");
                out.println("<label class=\"col-md-6\">Codice volo:</label>");
                out.println("<input class=\"form-control\" type=\"text\" maxlength = \"7\" id=\"codicevoloMod\" name=\"codicevoloMod\"><br>\n"
                        + "<label class=\"col-md-6\">Partenza:</label>"
                        + "<input class=\"form-control\" type=\"time\" id=\"partenzaMod\" name=\"partenzaMod\"><br>\n"
                        + "<label class=\"col-md-6\">Arrivo:</label>"
                        + "<input class=\"form-control\" type=\"time\" id=\"arrivoMod\" name=\"arrivoMod\"><br>\n"
                        + "<label class=\"col-md-6\" id=\"andata\">Andata:</label><input type=\"radio\" id=\"andMod\" name=\"tipoMod\" value=\"And\">\n"
                        + "<label class=\"col-md-6\" id=\"ritorno\">Ritorno:</label><input type=\"radio\" id=\"ritMod\" name=\"tipoMod\" value=\"Rit\">\n"
                        + "</div>\n"
                        + "\n"
                        + "        <div class=\"modal-footer\">\n"
                        + "          <button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\" id=\"annullaEditFlight\">Annulla</button>\n"
                        + "          <button type=\"button\" class=\"btn btn-primary\" id=\"confermaEditFlight\">Conferma</button>\n"
                        + "        </div>\n"
                        + "      </div>\n"
                        + "    </div>\n"
                        + "  </div>");
                
                out.println("<div class=\"modal fade\" id=\"ModalEditCompany\" role=\"dialog\">\n"
                        + "\n"
                        + "    <div class=\"modal-dialog\">\n"
                        + "      <div class=\"modal-content\">\n"
                        + "\n"
                        + "        <div class=\"modal-header\">\n"
                        + "          <h4 class=\"modal-title\">Modifica compagnia</h4>\n"
                        + "          <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n"
                        + "        </div>\n"
                        + "\n"
                        + "        <div class=\"modal-body\">\n"
                        + "          <div id=\"idCompagniaMod\" style=\"display: none;\"></div>\n"
                        + "            <label class=\"col-md-6\">Nome compagnia:</label>\n"
                        + "          <input type=\"text\" class=\"form-control\" id=\"nomeCompagniaMod\" name=\"nomeCompagniaMod\">\n"
                        + "        </div>\n"
                        + "\n"
                        + "        <div class=\"modal-footer\">\n"
                        + "          <button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\" id=\"annullaEditCompany\">Annulla</button>\n"
                        + "          <button type=\"button\" class=\"btn btn-primary\" id=\"confermaEditCompany\">Conferma</button>\n"
                        + "        </div>\n"
                        + "\n"
                        + "      </div>\n"
                        + "    </div>\n"
                        + "  </div>");
                out.println("  <div class=\"modal fade\" id=\"ModalDelFlight\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n"
                        + "\n"
                        + "    <div class=\"modal-dialog\" role=\"document\">\n"
                        + "      <div class=\"modal-content\">\n"
                        + "\n"
                        + "        <div class=\"modal-header\">\n"
                        + "          <h5 class=\"modal-title\" id=\"exampleModalLabel\">Conferma eliminazione</h5>\n"
                        + "          <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n"
                        + "            <span aria-hidden=\"true\">&times;</span>\n"
                        + "          </button>\n"
                        + "        </div>\n"
                        + "\n"
                        + "        <div class=\"modal-body\" id=\"bodyCancellazione\">\n"
                        + "          Vuoi eliminare questo volo?\n"
                        + "          <div id=\"idVolo\" style=\"display: none;\"></div>\n"
                        + "        </div>\n"
                        + "\n"
                        + "        <div class=\"modal-footer\">\n"
                        + "          <button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">Annulla</button>\n"
                        + "          <button type=\"button\" class=\"btn btn-primary\" id=\"cancellaFlight\">Conferma</button>\n"
                        + "        </div>\n"
                        + "\n"
                        + "      </div>\n"
                        + "    </div>\n"
                        + "\n"
                        + "  </div>");
                out.println("  <div class=\"modal fade\" id=\"ModalDelCompany\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n"
                        + "\n"
                        + "    <div class=\"modal-dialog\" role=\"document\">\n"
                        + "      <div class=\"modal-content\">\n"
                        + "\n"
                        + "        <div class=\"modal-header\">\n"
                        + "          <h5 class=\"modal-title\" id=\"exampleModalLabel\">Conferma eliminazione</h5>\n"
                        + "          <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n"
                        + "            <span aria-hidden=\"true\">&times;</span>\n"
                        + "          </button>\n"
                        + "        </div>\n"
                        + "\n"
                        + "        <div class=\"modal-body\" id=\"bodyCancellazione\">\n"
                        + "          Vuoi eliminare questa compagnia?<br>⚠️ Effettuando l'eliminazione anche tutti i voli serviti da questa compagnia verranno eliminati! ⚠️"
                        + "          <div id=\"idCompagnia\" style=\"display: none;\"></div>\n"
                        + "        </div>\n"
                        + "\n"
                        + "        <div class=\"modal-footer\">\n"
                        + "          <button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">Annulla</button>\n"
                        + "          <button type=\"button\" class=\"btn btn-primary\" id=\"cancellaCompany\">Conferma</button>\n"
                        + "        </div>\n"
                        + "\n"
                        + "      </div>\n"
                        + "    </div>\n"
                        + "\n"
                        + "  </div>");
                out.println("<div class='tablesContainer'>");
                out.println("<div class='leftTable'>");
                out.println("<h1>Gestione voli</h1>");
                out.println("<table>");
                out.println("<tr><th>Sigla</th><th>Aeroporto</th><th>Compagnia</th><th>Codice volo</th><th>Partenza</th><th>Arrivo</th><th>Tipo</th><th class=\"centered\">Modifica</th><th class=\"centered\">Elimina</th>");
                out.println("<tr><td colspan=\"9\" class=\"centered\"><button class=\"addFlight\"></button></td></tr>");
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
                            + "<td class=\"centered\"><button class=\"editFlight\" data-id=\""+result.getString(1)+"#" + result.getString(2)+"#" + result.getString(3)+"#" + result.getString(4)+"#" + result.getString(5)+"#" + result.getString(6)+"#" + result.getString(7)+"#" + result.getString(8)+"\"></button></td>"
                            + "<td class=\"centered\"><button class=\"delFlight\" data-id=\""+result.getString(1)+"\"></button></td>"
                            + "</tr>");
                    System.out.println("ID: " + result.getString(1) + "\n");
                }
                out.println("</table>");
                out.println("</div>");

                query = "SELECT nome_compagnia, id_compagnia FROM compagnia ORDER BY nome_compagnia";

                statement = connessione.createStatement();
                result = statement.executeQuery(query);

                out.println("<div class='rightTable'>");
                out.println("<h1>Gestione compagnie</h1>");
                out.println("<table>");
                out.println("<tr><th>Nome compagnia</th><th class=\"centered\">Modifica</th><th class=\"centered\">Elimina</th>");
                out.println("<tr><td colspan=\"3\" class=\"centered\"><button class=\"addCompany\"></button></td></tr>");
                while (result.next()) {
                    out.println("<tr>"
                            + "<td>" + result.getString(1) + "</td>"
                            + "<td class=\"centered\"><button class=\"editCompany\" data-id=\""+result.getString(1)+"#" + result.getString(2)+"\"></button></td>"
                            + "<td class=\"centered\"><button class=\"delCompany\" data-id=\""+result.getString(2)+"\"></button></td>"
                            + "</tr>");
                }
                out.println("</table>");
                out.println("</div>");
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
