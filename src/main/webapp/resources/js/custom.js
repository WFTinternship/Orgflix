function pagination(page, type) {
    var navigatorElem = $('#navigator');
    var id = page.split("_");
    $("#currPage").val(id[1]);
    navigatorElem.attr('action', type);
    navigatorElem.submit();
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
        url: "/data/film/" + type + "/addToList",
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
        url: "/data/film/" + type + "/removeFromList",
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

function ajaxSupport(result, filmId ) {
    $("#inner-pop-up").html(result);
    $("#outer-pop-up").css("display", "block");
    setTimeout(function () {
        timeOuter(filmId, false);
    }, 900);
}
function getActorsList(elem, state, isAdd) {
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
            if (show > 10) continue;
            if (new RegExp(input, "i").test(obj[i].name)) { //
                if (obj[i].oscar == "true") {
                    hasOscar = "<span class=\"oscarSign\"><i class=\"fa fa-trophy fa-fw\"></i></span>";
                } else {
                    hasOscar = "<span class=\"oscarSign\"> </span>";
                }
                str = str + '<div class="row' + show % 2 + ' pointerA">'
                    + hasOscar
                    + '<span id="act_' + obj[i].id + '" onclick="selActor(this)">' + obj[i].name + '</span></div>';
                show++;
            }
        }
        if (show == 0 && isAdd) {
            str = "No match found, <a class=\"inline-command\" onclick=\"newActor()\">add</a> new actor";
        }
        $(elem).siblings("div:nth-of-type(1)").html(str);
    }
}

function addNewActor() {
    $.ajax({
        url: "/data/actor/add",
        type: "POST",
        data: {
            actor: $("#newActorName").val(),
            hasOscar: $("#hasOscarCast").val() == 1
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
    var numOfActorsElem = $("#numOfActors");
    var html = '<div class="container">'
        + '<div class="elem longElem">'
        + '<input type="text" name="actor" onclick="getActorsList(this,true)" onkeyup="getActorsList(this,false)" class="inputField" style="margin-bottom: 0px"/>'
        + '<input type="hidden" name="actorId" class="actorId" value="-1">'
        + '<div></div>'
        + '</div><div class="elem">'
        + '<a onclick="removeActor()"><i class="fa fa-minus-square fa-2x"></i></a>'
        + '</div></div>';
    $("#buffer").before(html);
    numOfActorsElem.val(numOfActorsElem.val() + 1);
}
function removeActor() {

}

function addGenre() {
    $('#genre').clone().appendTo('#genreContainer');

}

function submitNewFilm() {
    $("#error").html(" ");
    $("#newFilm").submit();

}
function navigator(page) {
    var navigatorElem = $('#navigator');
    navigatorElem.attr('action', page);
    navigatorElem.submit();
}
function submitSignUp() {
    var errerElem = $("#error");
    var password = $("#pass").val();
    errerElem.html(" ");
    if ($("#nick").val() == "" || $("#email").val() == "" || password == "") {
        errerElem.html("Nick, email and pass are required fields !");
    } else if (password != $("#pass_check").val()) {
        errerElem.html(errerElem.html() + "<br />Passwords don't match !");
        console.log("pass not match");
    } else {
        $("#signUpForm").submit();
    }
}

function starFilm(star, isClick, id) {
    var starElement = $("#stars_" + id);
    if (isClick) {
        starElement.val(star);
        starElement.siblings("span:nth-of-type(2)").html("(" + star + " star)");
    } else {
        if (star == 0) {
            starElement.siblings("a").css("color", "gray");
            for (i = 1; i <= starElement.val(); i++) {
                starElement.siblings("a:nth-of-type(" + i + ")").css("color", "#ffe415");
            }
        } else {
            for (i = 1; i < star; i++) {
                starElement.siblings("a:nth-of-type(" + i + ")").css("color", "red");
            }
        }
    }
}

function newActor() {
    $("#outer-pop-up").css("display", "block");
}

function closeNewCast() {
    $("#outer-pop-up").css("display", "none");
}

function setHasOscar(objectType) {
    var hasOscar = $("#hasOscar" + objectType);
    if (hasOscar.val() == 0) {
        var oscar = 1;
    } else {
        var oscar = 0;
    }
    hasOscar.val(oscar);
    $("#oscarCheck" + objectType).toggleClass("fa-square-o fa-check-square-o");
}

function isPublic(id) {
    var isPublic = $("#isPublic" + id);
    var publicVal = 0;
    if (isPublic.val() == 0) {
        publicVal = 1;
    }
    isPublic.val(publicVal);
    $("#publicCheck_" + id).toggleClass("fa-square-o fa-check-square-o");
}

function getUsersList(elem, state) {
    var usersBufferElem = $("#usersBuffer");
    if (state) {
        $.ajax({
            url: "/data/user/getList", type: "GET", success: function (result) {
                usersBufferElem.val(result);
            }
        });
    }
    if (usersBufferElem.val() != "") {
        var obj = eval("(" + usersBufferElem.val() + ")");
        var str = '';
        var input = $(elem).val();
        var show = 0;
        for (var i = 0; i < obj.length; i++) {
            if (show > 10) continue;
            if (new RegExp(input, "i").test(obj[i].nick)) { //
                str = str + '<div class="row' + show % 2 + ' pointerA">'
                    + '<span id="user_' + obj[i].id + '_' + $(elem).attr('id') +'" onclick="selUser(this)">'
                    + obj[i].nick + '</span></div>';
                show++;
            }
        }
        $(elem).siblings("div").html(str);
    }
}

function selUser(elem) {
    var navigatorElem = $('#navigator');
    var idNumber = elem.id.split("_");
    $("#otherUser").val(idNumber[1]);
    navigatorElem.attr('action', idNumber[2]+'Other');
    navigatorElem.submit();
}

function invertPrivacy(elem, id) {
    var state = elem.getAttribute("data-privacy");
    $.ajax({
        url: "/data/film/privacy",
        type: "POST",
        data: {
            filmId: id,
            isPublic: state
        },
        success: function (result) {
            if(state) {
                elem.setAttribute("data-privacy", "false");
                $(elem).html('<i class="fa fa-unlock fa-fw"></i>');
            } else {
                elem.setAttribute("data-privacy", "true");
                $(elem).html('<i class="fa fa-lock fa-fw"></i>');
            }
            $("#inner-pop-up").html(result);
            $("#outer-pop-up").css("display", "block");
            setTimeout(function () {
                $("#outer-pop-up").css("display", "none");
            }, 900);
        }
    });
}

function submitLogin() {
    var errorElem = $("#error");
    errorElem.html(" ");
    if ($("#pass").val() == "" || $("#email").val() == "") {
        errorElem.html("Email and pass are required fields !");
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

    });

    var getOtherUserNickElem = $(".get-other-user-nick");
    getOtherUserNickElem.on("click", function () {
        if (getOtherUserNickElem.val() == "Enter other user's nick") {
            getOtherUserNickElem.val("");
        }
    });
    getOtherUserNickElem.on("blur", function () {
        if (getOtherUserNickElem.val() == "") {
            getOtherUserNickElem.val("Enter other user's nick");
        }
    });
});