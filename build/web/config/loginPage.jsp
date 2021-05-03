<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF8">  
        <title>Login</title>
        <script src="../js/login.js"></script>
        <link rel="stylesheet" type="text/css" href="../style/login.css">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    </head>
    <body>
        <div id="container">
            <div id="title" class="w3-container">
                <h2>Login</h2>
            </div>
            <form name="form" class="w3-container" action="login.jsp" method="POST">
                <p>
                    <label>Username</label>
                    <input type="text" name ="user" class="w3-input" required>
                </p>
                <p id="info"></p>
                <p>
                    <div class="input-container">
                        <label>Password</label>
                        <input type="password" name="pass" id="pass" class="w3-input" onfocus="ShowEye()" required>
                        <span id="pass_eye" class="material-icons" onclick="Hide_Show_Password()">visibility_off</span>
                    </div>
                </p>
                <p id="note"></p>
                <div id="buttons">
                    <input type="submit" id="login" name="login" value="Login" class="buttons">
                    <a href="../index.jsp" id="annulla" class="buttons">Annulla</a>
                </div>  
            </form>
        </div>
    </body>
</html>
