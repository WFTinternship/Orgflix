function pagination(page, type) {
    var id = page.split("_");
    $("#currPage").val(id[1]);
    $('#navigator').attr('action', type);
    $("#navigator").submit();
}

function filmControlMenu(state, filmId) {
    if (state) {
        $("#contr_" + filmId).css("display", "block");
    } else {
        $("#contr_" + filmId).css("display", "none");
    }
}

function timeOuter(id, isDel) {
    if (isDel) {
        $("#" + id).css("display", "none");
    }
    console.log(id + " done");
    $("#outer-pop-up").css("display", "none");
}

function AddToList(type, filmId) {
    $.ajax({
        url: "/data/film/"+type+ "/addToList",
        type: "POST",
        data: {
            film: filmId,
            isPublic: $("#isPublic_" + filmId).val()
        },
        success: function (result) {
            $("#inner-pop-up").html(result);
            $("#outer-pop-up").css("display", "block");
            setTimeout(function () {
                timeOuter(filmId, false);
            }, 900);
        }
    });
}

function RemoveFromList(type, id) {
    $.ajax({
        url: "/data/film/"+type+"/removeFromList",
        type: "POST",
        data: {
            film: id,
            user: $("#userId").val()
        },
        success: function (result) {
            $("#inner-pop-up").html(result);
            $("#outer-pop-up").css("display", "block");
            setTimeout(timeOuter(id, true), 3000);
        }
    });
}

function recordStar(star, filmId) {
    starFilm(star, true, filmId);
    $.ajax({
        url: "/data/film/star",
        type: "POST",
        data: {
            film: filmId,
            star: star
        },
        success: function (result) {
            $("#inner-pop-up").html(result);
            $("#outer-pop-up").css("display", "block");
            setTimeout(function () {
                timeOuter(filmId, false);
            }, 900);
        }
    });
}
function ajaxSupport(result) {
    $("#inner-pop-up").html(result);
    $("#outer-pop-up").css("display", "block");
    setTimeout(function () {
        timeOuter(filmId, false);
    }, 900);
}
function getActorsList(elem, state) {
    if (state) {
        $.ajax({
            url: "/data/actor/getList", type: "GET", success: function (result) {
                $("#buffer").val(result);
            }
        });
    }
    if ($("#buffer").val() != "") {
        var obj = eval("(" + $("#buffer").val() + ")");
        var str = '';
        var input = $(elem).val();
        var show = 0;
        for (var i = 0; i < obj.length; i++) {
            if (show > 10) return;
            if (new RegExp(input, "i").test(obj[i].name)) { //
                if(obj[i].oscar == "true"){
                    hasOscar = "<span class=\"oscarSign\"><i class=\"fa fa-trophy fa-fw\"></i></span>";
                }else{
                    hasOscar = "<span class=\"oscarSign\"> </span>";
                }
                str = str + '<div class="row' + show % 2 + ' pointerA">'
                    + hasOscar
                    + '<span id="act_' + obj[i].id + '" onclick="selActor(this)">' + obj[i].name + '</span></div>';
                show++;
            }
        }
        if(show == 0){
            str = "No match found, <a class=\"inline-command\" onclick=\"newActor()\">add</a> new actor";
        }
        $(elem).siblings("div:nth-of-type(1)").html(str);
    }
}

function addNewActor() {
    $.ajax({
        url: "/data/actor/add",
        type: "POST",
        data:{
            actor:$("#newActorName").val(),
            hasOscar:$("#hasOscarCast").val()==1
        },
        success: function (result) {
            $("#newActorName").val("");
            $("#oscarCheckCast").removeClass("fa-check-square-o");
            $("#oscarCheckCast").addClass("fa-square-o");
            $("#outer-pop-up").css("display", "none");
            $("#buffer").val(result);
        }
    });

}

function selActor(elem) {
    var parent = $(elem).parent().parent();
    parent.siblings(".inputField").val($("#" + elem.id).html());
    var idNumber = elem.id.split("_");
    parent.siblings(".actorId").val(idNumber[1]);
    parent.html("");
}

function addActor() {
    var html = '<div class="container">'
        + '<div class="elem longElem">'
        + '<input type="text" name="actor" onclick="getActorsList(this,true)" onkeyup="getActorsList(this,false)" class="inputField" style="margin-bottom: 0px"/>'
        + '<input type="hidden" name="actorId" class="actorId" value="-1">'
        + '<div></div>'
        + '</div><div class="elem">'
        + '<a onclick="addActor()"><i id="addActor" class="fa fa-plus-square fa-2x"></i></a>'
        + '</div></div>';
    $("#buffer").before(html);
    $("#numOfActors").val($("#numOfActors").val() + 1);
}
function removeActor() {
    $("#numOfActors").val($("#numOfActors").val() - 1);

}

function submitNewFilm() {
    $("#error").html(" ");
    $("#newFilm").submit();

}
function navigator(page) {
    $('#navigator').attr('action', page);
    $("#navigator").submit();
}
function submitSignUp() {
    $("#error").html(" ");
    if ($("#nick").val() == "" || $("#email").val() == "" || $("#pass").val() == "") {
        $("#error").html("Nick, email and pass are required fields !");
    } else if ($("#pass").val() != $("#pass_check").val()) {
        $("#error").html($("#error").html() + "<br />Passwords don't match !");
        console.log("pass not match");
    } else {
        $("#signUpForm").submit();
    }
}

function starFilm(star, isClick, id) {
    if (isClick) {
        $("#stars_" + id).val(star);
        $("#stars_" + id).siblings("span:nth-of-type(2)").html("(" + star + " star)");
    } else {
        if (star == 0) {
            $("#stars_" + id).siblings("a").css("color", "gray");
            for (i = 1; i <= $("#stars_" + id).val(); i++) {
                $("#stars_" + id).siblings("a:nth-of-type(" + i + ")").css("color", "#ffe415");
            }
        } else {
            for (i = 1; i < star; i++) {
                $("#stars_" + id).siblings("a:nth-of-type(" + i + ")").css("color", "red");
            }
        }
    }
}

function newActor() {
    $("#outer-pop-up").css("display", "block");
}

function closeNewCast() {
    $("#outer-pop-up").css("display","none");
}

function setHasOscar(objectType) {
    console.log(objectType);
    if ($("#hasOscar"+objectType).val() == 0) {
        var oscar = 1;
    } else {
        var oscar = 0;
    }
    $("#hasOscar"+objectType).val(oscar);
    $("#oscarCheck"+objectType).toggleClass("fa-square-o fa-check-square-o");
}

function isPublic(id) {
    if ($("#isPublic" + id).val() == 0) {
        var public = 1;
    } else {
        var public = 0;
    }
    $("#isPublic" + id).val(public);
    $("#publicCheck_" + id).toggleClass("fa-square-o fa-check-square-o");
}

function submitLogin() {
    $("#error").html(" ");
    if ($("#pass").val() == "" || $("#email").val() == "") {
        $("#error").html("Email and pass are required fields !");
    } else {
        console.log("pass not match");
        $("#loginForm").submit();
    }
}
$(document).ready(function () {
    $("#contact_us").on("click", function () {
        $("#touch-section").css("display", "none");
        $("#contact-section").css("display", "table-cell");
    });
    $("#touch_us").on("click", function () {
        $("#contact-section").css("display", "none");
        $("#touch-section").css("display", "table-cell");

    })

});