<%@page import="java.sql.*"%>
<%@include file="config.jsp"%>
<%
    if(request.getParameter("login")!=null){
        //Recupero i dati dal form
        String username=request.getParameter("user");
        String pass=request.getParameter("pass");
        
        System.out.println("Username: " + username + "\nPass: " + pass);
        //Caricamento Driver
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
           System.err.println(ex.getMessage());
        }
        //Per stabilire una connessione
        Connection connessione=null;
        //Risultato interrogazione
        ResultSet result=null;
        connessione = DriverManager.getConnection(URL,USER,PSW);
        PreparedStatement p = connessione.prepareStatement("SELECT * FROM utente WHERE username=? and psw=?");
        //bind parametri
        p.setString(1,username);
        p.setString(2,pass);
        //eseguo la query ed ottengo il recordset
        result=p.executeQuery();
        
        if(result.next()){
            session.setAttribute("username", result.getString("username"));
            response.sendRedirect("../index.jsp");
        }else{
            response.sendRedirect("loginPage.jsp");
        }
    }
%>


