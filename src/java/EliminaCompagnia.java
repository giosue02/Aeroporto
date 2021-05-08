
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

@WebServlet("/EliminaCompagnia")
public class EliminaCompagnia extends HttpServlet {

    private final String URL = "jdbc:mysql://localhost:3306/aeroporto";
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String USER = "root";
    private final String PSW = "";
    private String query = "";
    private Statement statement;
    private ResultSet result;

    private static final long serialVersionUID = 1L;

    public EliminaCompagnia() {
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
        
        String idCompagnia = request.getParameter("idCompagnia");
        
        try {
            PreparedStatement st = connessione.prepareStatement("DELETE FROM compagnia WHERE id_compagnia = ?");
            st.setString(1, idCompagnia);
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
