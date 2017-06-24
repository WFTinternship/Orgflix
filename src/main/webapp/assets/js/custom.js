/**
 * Created by David on 6/13/2017
 */
function pagination(page) {
    $("#currPage").val(page);
    $("#pageForm").submit();
}

function navigator(page){
    $('#navigator').attr('action', page);
    $("#navigator").submit();
}
function submitSignUp() {
    $("#error").html(" ");
    if( $("#nick").val()=="" || $("#email").val()=="" || $("#pass").val()=="" ) {
        $("#error").html("Nick, email and pass are required fields !");
    } else if($("#pass").val() != $("#pass_check").val()){
        $("#error").html( $("#error").html() + "<br />Passwords don't match !");
        console.log("pass not match");
    } else {
        $("#signUpForm").submit();
    }
}

function submitLogin() {
    $("#error").html(" ");
    if( $("#pass").val()=="" || $("#email").val()=="" ) {
        $("#error").html("Email and pass are required fields !");
    } else {
        console.log("pass not match");
        $("#loginForm").submit();
    }
}