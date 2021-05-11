<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
body {
  margin: 0;
  font-family: Arial, Helvetica, sans-serif;
}

.topnav {
  overflow: hidden;
  background-color: #333;
}

.topnav a {
  float: left;
  display: block;
  color: #f2f2f2;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  font-size: 17px;
}

.topnav a:hover {
  background-color: #ddd;
  color: black;
}

.topnav a.active {
  background-color: #007bff;
  color: white;
}

.topnav .icon {
  display: none;
}

@media screen and (max-width: 600px) {
  .topnav a:not(:first-child) {display: none;}
  .topnav a.icon {
    float: right;
    display: block;
  }
}

@media screen and (max-width: 600px) {
  .topnav.responsive {position: relative;}
  .topnav.responsive .icon {
    position: absolute;
    right: 0;
    top: 0;
  }
  .topnav.responsive a {
    float: none;
    display: block;
    text-align: left;
  }
}
</style>
<%
    if (session.getAttribute("username") != null) {
%>
<div class="topnav" id="myTopnav">
    <a href="index.jsp">Home</a>
    <a href="Tabellone">Tabellone orari</a>
    <a href="PostazioneMultimediale">Postazione multimediale</a>
    <a href="ComputerCentrale">Computer centrale</a>
    <a href="config/logout.jsp" class="logout">Logout</a>
    <a href="javascript:void(0);" class="icon" onclick="Hamburger()">
    <i class="fa fa-bars"></i>
  </a>
</div>
<%
} else {
%>
<div class="topnav" id="myTopnav">
    <a href="index.jsp">Home</a>
    <a href="Tabellone">Tabellone orari</a>
    <a href="PostazioneMultimediale">Postazione multimediale</a>
    <a href="config/loginPage.jsp" class="login">Login</a>
    <a href="javascript:void(0);" class="icon" onclick="Hamburger()">
    <i class="fa fa-bars"></i>
  </a>
</div>
<% }%>
<script>
function Hamburger() {
  var x = document.getElementById("myTopnav");
  if (x.className === "topnav") {
    x.className += " responsive";
  } else {
    x.className = "topnav";
  }
}
</script>