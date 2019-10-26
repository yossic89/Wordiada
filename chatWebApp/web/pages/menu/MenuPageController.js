/**
 * Created by user on 11/10/2016.
 */


window.onload = function ()
{
    setUserNameSpan();
    refreshUserList();
    refreshGamesList();
    setInterval(refreshUserList, 1000);
    setInterval(refreshGamesList, 1000);
};

function setUserNameSpan()
{
    $.ajax(
        {
            url: '../../menu',
            data: {
                action: 'username'
            },
            type: 'GET',
            success: function (json) {
                var obj = jQuery.parseJSON(json);
                $('.userNameSpan').text("You are " + obj[0]+ " ("+ obj[1]+")");
            }
        });

}

function logoutHandle(){
    $.ajax(
        {
            url: '../../menu',
            data: {
                action: 'logout'
            },
            type: 'GET',
            success: function () {
                window.location = "https://m.popkey.co/1d02f9/dg6oz.gif";
            }
        });
}


function refreshUserList() {
    $.ajax(
        {
            url: '../../menu',
            data: {
                action: 'tableUsers'
            },
            type: 'GET',
            success: function (json) {
                var usersTable = $('.usersTable tbody');
                usersTable.empty();
                json.forEach(function(user){
                    var tr = $(document.createElement('tr'));
                    var td = $(document.createElement('td')).text(user.name);
                    td.appendTo(tr);
                    tr.appendTo(usersTable);
                });
            }
        });
}

function boardPreview(size){
    var myTable = $("<table oncontextmenu=\"return false\"></table>").appendTo("#previewPopup");

    var img = "<img src='/wordiada/images/card.png' />";

    for (var row = size - 1; row >= 0; row--) {
        var myRow = $("<tr></tr>").appendTo(myTable);
        for (var col = 0; col < size; col++) {
            myRow.append("<td>" + img+ "</td>");
        }
    }
    $("#previewPopup").dialog({
        autoOpen: true,
        closeOnEscape: true,
    });
    $("#previewPopup").dialog('open');


}

function handleLoadingPopup(toStop)
{
    if (toStop == true)
    {
        $("#loading").dialog('close');
    }
    else
    {
        document.getElementById("loading").innerHTML="<img src='/wordiada/images/loading.gif' />";
        $("#loading").dialog({
            autoOpen: true,
            closeOnEscape: false,
            open: function(event, ui) {
                $(".ui-dialog-titlebar-close", ui.dialog | ui).hide();
            }
        });
        $("#loading").dialog('open');
    }
}


$(document).ready(function(){
    $('#upload').click(function(){
        var confPath = document.getElementById('config').files[0];
        if (!(confPath))
        {
            alert("Select config file !!!");
            return;
        }

        var dicPath = document.getElementById('dict').files[0];
        if (!(dicPath))
        {
            alert("Select dict file !!!");
            return;
        }
        handleLoadingPopup(false);


        var data = new FormData();
        data.append( 'config', $('#config')[0].files[0]);

        data.append( 'dict', $('#dict')[0].files[0]);

        var dictName = document.getElementById('dict').value.split("\\").pop().split(".txt")[0];
        data.append( 'dictName',dictName);


        $.ajax({
            url: '../../menu',
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            type: 'POST',
            success: function(json){
                if (json.length == 1)
                {
                    handleLoadingPopup(true);
                    document.getElementById("loadOKPopup").innerHTML="<img src='/wordiada/images/loadok.jpg' />";
                    $("#loadOKPopup").dialog({
                        autoOpen: true,
                        closeOnEscape: false,
                    });
                    $("#loadOKPopup").dialog('open');
                    clearFileInput();
                    setTimeout(function() { $("#loadOKPopup").dialog('close'); }, 2000);
                }
                else
                {
                    handleLoadingPopup(true);
                    var confName = document.getElementById('config').value.split('\\').pop();
                    json.pop();
                    alert(confName + " failed in validate\n" + json);
                    clearFileInput();
                }
            }
        });
    });
});


function refreshGamesList() {
    $.ajax
    (
        {
            url: '../../menu',
            data: {
                action: 'tableData'
            },
            type: 'GET',
            success: function(json){
                var gamesTable = $('.gamesTable tbody');
                gamesTable.empty();
                json.forEach(function (row){
                    var tr = $(document.createElement('tr'));
                    var tdGameName = $(document.createElement('td')).text(row.gameName);
                    var tdCreator = $(document.createElement('td')).text(row.userCreator);
                    var tdDict = $(document.createElement('td')).text(row.dictName);
                    var tdBoardSize = $(document.createElement('td')).text(row.size + " X " + row.size);
                    var tdPlayers = $(document.createElement('td')).text(row.activePlayers + " / " + row.reqPlayers);
                    var tdLetters = $(document.createElement('td')).text(row.letters);

                    tdGameName.appendTo(tr);
                    tdCreator.appendTo(tr);
                    tdBoardSize.appendTo(tr);
                    tdPlayers.appendTo(tr);
                    tdDict.appendTo(tr);
                    tdLetters.appendTo(tr);

                    //add specail cells
                    var img = "<img src='/wordiada/images/preview.png' />";
                    var onclick = "onclick=boardPreview(" + row.size + ")";
                    tr.append("<td " + onclick + ">" + img+ "</td>");

                    var img2 = "<img src='/wordiada/images/ingo.jpg' />";
                    var onclick2 = "onClick=\"guestInTheGame('" + row.gameName + "')\">";
                    tr.append("<td " + onclick2 + img2 + "</td>");

                    if (row.isRunning == false) {
                        var img1 = "<img src='/wordiada/images/join.png' />";
                        var onclick1 = "onClick=\"enterToGame('" + row.gameName + "')\">";

                        tr.append("<td " + onclick1 + img1 + "</td>");
                    }
                    else
                    {
                        var img = "<img src='/wordiada/images/noEnter.jpg' />";
                        tr.append("<td bgcolor='red'> " + img+ "</td>");
                    }



                    tr.appendTo(gamesTable);
                });

            }
        }
    )
}

function guestInTheGame(game)
{
    console.info("here");
    var page =  "/wordiada/pages/gameRoom/game.html";
    page += "?game=" + game;
    window.location.href = page;
}

function enterToGame(game)
{
    // var game = row.children[0];
    $.ajax
    (
        {
            url: '../../menu',
            data: {
                action: 'enterToGame',
                game: game
            },
            type: 'GET',
            success: function(json){
                if (json.length == 0){
                    var page =  "/wordiada/pages/gameRoom/game.html";
                    page += "?game=" + game;
                    window.location.href = page;
                }
                else{
                    alert("Fail to enter the game\n" + json);
                }
            }
        }
    )
}

function clearFileInput() {
    document.getElementById("config").value = "";
    document.getElementById("dict").value = "";
}






