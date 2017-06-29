/**
 * Created by David on 6/13/2017
 */
function pagination(page) {
    $("#currPage").val(page);
    $('#navigator').attr('action', 'index');
    $("#navigator").submit();
}

function filmControlMenu(state,id) {
    if(state) {
        $("#contr_" + id).css("display", "block");
    }else{
        $("#contr_" + id).css("display", "none");
    }
}

function AddToList(type,id) {

    $.ajax({url: "/data/addFilmTo"+type,type:"POST",data:{film:id,user:$("#userId").val()},success: function(result){
        $("#pop-up-result").html(result);
        $("#pop-up-result").css("display", "block");
    }});
}

function RemoveFromList(type,id) {

    $.ajax({url: "/data/removeFilmFrom"+type,type:"POST",data:{film:id,user:$("#userId").val()},success: function(result){
        $("#pop-up-result").html(result);
        $("#pop-up-result").css("display", "block");
    }});
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
$(document).ready(function() {
    $("#contact_us").on("click", function () {
        $("#touch-section").css("display", "none");
        $("#contact-section").css("display", "table-cell");
    });
    $("#touch_us").on("click", function () {
        $("#contact-section").css("display", "none");
        $("#touch-section").css("display", "table-cell");

    })

});