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

@WebServlet(urlPatterns={"/Tabellone"})
public class Tabellone extends HttpServlet {
    
    private final String URL = "jdbc:mysql://localhost:3306/aeroporto";
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String USER = "root";
    private final String PSW = "";
    private String query="";
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
        
        String nome_compagnia="";
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            query = "SELECT compagnia, codice_volo, partenza FROM volo ORDER BY partenza;";
            
            statement = connessione.createStatement();
            result = statement.executeQuery(query);
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Tabellone</title>");  
            out.println("<script src=\"js/jquery-3.6.0.min.js\"></script>");
            out.println("<script src=\"js/app.js\"></script>");
            out.println("<link rel=\"stylesheet\" href=\"style/style.css\">");
            out.println("</head>");
            out.println("<body>");
            request.getRequestDispatcher("config/menu.jsp").include(request, response);
            out.println("<div class=\"contenitore\">");
            out.println("<div class='tablesContainer'>");
            out.println("<div class='singleTable'>");
            out.println("<h1>Partenze</h1>");
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
            out.println("</div>");
            
            query = "SELECT compagnia, codice_volo, arrivo FROM volo ORDER BY arrivo";
            
            statement = connessione.createStatement();
            result = statement.executeQuery(query); 
            
            out.println("<div class='singleTable'>");
            out.println("<h1>Arrivi</h1>");
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
                + "<td>" + result.getString(3) + "</td>"
                + "</tr>");
            }
            out.println("</table>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
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
            Logger.getLogger(Tabellone.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Tabellone.class.getName()).log(Level.SEVERE, null, ex);
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
