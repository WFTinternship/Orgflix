
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

function AddToList(type, id, isPublic) {
    $.ajax({url: "/data/addFilmTo"+type,type:"POST",data:{film:id,user:$("#userId").val(),isPublic:isPublic},success: function(result){
        $("#pop-up-result").html(result);
        $("#pop-up-result").css("display", "block");
        setTimeout(function() {
            timeOuter(id,false);
        }, 900);
    }});
}

function timeOuter(id,isDel){
    if(isDel){
        $("#"+id).css("display", "none");
    }
    console.log(id+" done");
    $("#pop-up-result").css("display", "none");
}

function RemoveFromList(type,id) {
    $.ajax({url: "/data/removeFilmFrom"+type,type:"POST",data:{film:id,user:$("#userId").val()},success: function(result){
        $("#pop-up-result").html(result);
        $("#pop-up-result").css("display", "block");
        setTimeout(timeOuter(id,true), 3000);
    }});
}

function getActorsList() {
    $.ajax({url: "/data/getActorsList",type:"POST",success: function(result){
        var obj = eval ("(" + result + ")");
        var str = '<div>';
        for(var i=0;i<obj.length;i++){
            str=str+'<div id="act_'+'">'+obj[i].name+'</div>';
        }
        str=str+'</div>';
        console.log(str);
        // $("#pop-up-result").html(result);
        // $("#pop-up-result").css("display", "block");
        // setTimeout(timeOuter(id,true), 3000);
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

function star(star, isClick) {
    if(isClick){
        $("#stars").val(star);
        $("#stars").siblings("span:nth-of-type(2)").html("("+star+" star)");
    } else {
        if(star==0){
            $("#stars").siblings("a").css("color","gray");
            for (i = 1; i <= $("#stars").val(); i++) {
                $("#stars").siblings("a:nth-of-type(" + i + ")").css("color", "#ffe415");
            }
        } else {
            for (i = 1; i < star; i++) {
                $("#stars").siblings("a:nth-of-type(" + i + ")").css("color", "red");
            }
        }

    }
}

function hasOscar() {
    if( $("#hasOscar").val() == 0){
        var oscar = 1;
    } else {
        var oscar = 0;
    }
    $("#hasOscar").val(oscar);
    $("#oscarCheck").toggleClass("fa-square-o fa-check-square-o");
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