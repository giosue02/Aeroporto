function Hide_Show_Password()
{
    var passw = document.getElementById("pass");
    var eye = document.getElementById("pass_eye");
    if (passw.type === "password") 
    {
        passw.type = "text";
        eye.innerHTML = 'visibility';
    } 
    else
    {
        passw.type = "password";
        eye.innerHTML = 'visibility_off';
    }
}

function ShowEye()
{
    document.getElementById("pass_eye").style.visibility = "visible";
}
