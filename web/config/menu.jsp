<%
    if (session.getAttribute("username") != null) {
%>
<div class="menu" id="menu">
    <a href="index.jsp">Home</a>
    <a href="Tabellone">Tabellone orari</a>
    <a href="PostazioneMultimediale">Postazione multimediale</a>
    <a href="ComputerCentrale">Computer centrale</a>
    <a href="config/logout.jsp">Logout</a>
</div>
<%
} else {
%>
<div class="menu" id="menu">
    <a href="index.jsp">Home</a>
    <a href="Tabellone">Tabellone orari</a>
    <a href="PostazioneMultimediale">Postazione multimediale</a>
    <a href="config/loginPage.jsp">Login</a>
</div>
<% }%>