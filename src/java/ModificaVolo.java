
import java.io.IOException;
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

@WebServlet("/ModificaVolo")
public class ModificaVolo extends HttpServlet {

    private final String URL = "jdbc:mysql://localhost:3306/aeroporto";
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String USER = "root";
    private final String PSW = "";
    private String query = "";
    private Statement statement;
    private ResultSet result;

    private static final long serialVersionUID = 1L;

    public ModificaVolo() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connessione = null;

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
            System.err.println("Driver non trovato. " + ex.getMessage());
        }

        try {
            connessione = DriverManager.getConnection(URL, USER, PSW);
        } catch (SQLException ex) {
            Logger.getLogger(AggiungiVolo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String id = request.getParameter("id");
        String sigla = request.getParameter("sigla");
        String aeroporto = request.getParameter("aeroporto");
        String compagnia = request.getParameter("compagnia");
        String codicevolo = request.getParameter("codicevolo");
        String partenza = request.getParameter("partenza");
        String arrivo = request.getParameter("arrivo");
        String tipo = request.getParameter("tipo");
        
        try {
            PreparedStatement st = connessione.prepareStatement("UPDATE volo SET sigla = ?, aeroporto = ?, compagnia = ?, codice_volo = ?, partenza = ?, arrivo = ?, tipo = ? WHERE id = ?");
            st.setString(1, sigla);
            st.setString(2, aeroporto);
            st.setString(3, compagnia);
            st.setString(4, codicevolo);
            st.setString(5, partenza);
            st.setString(6, arrivo);
            st.setString(7, tipo);
            st.setString(8, id);
            st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AggiungiVolo.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                if (connessione != null) {
                    connessione.close();
                }
            } catch (SQLException e) {
            }
        }
        
    }
}
