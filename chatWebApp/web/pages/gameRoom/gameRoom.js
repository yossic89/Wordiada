/**
 * Created by user on 18/10/2016.
 */

var status;
var boardSize;
var diceResult;
var pickWord=false;
var first=true;
var firstClickBackCard=true;
var firstSelectWord=true;
var retries;
var currentRetries;
var isGoldfish;
var startGameInterval;
var statRefreshInterval;
var turnRefreshInterval;
var beginingOfTheTurn=true;
var isCompTurn = false;
var firstTimeMyTurn = true;

window.onload = function()
{

    statRefreshInterval = setInterval(refreshStat, 2000);
    startGameInterval = setInterval(checkIfGameStarted, 1000);
    setUserNameSpan();
    checkIfGameStarted();
    getRetriesNumber();
    getGoldfishMode();
    showBoard();
    $("#roolDice").hide();
    $("#flipCards").hide();
    $("#checkWord").hide();
    $("#showStockStat").hide();
    $("#showRareWords").hide();
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

function getRetriesNumber()
{
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'getRetriesNumber',
                gameName: getGameName(),

            },
            type: 'POST',
            success: function (res) {
                retries=Number(res);
                $('.turn').text("Turn: " +retries+"/"+retries);
            }
        }
    );

}

function checkIfGameStarted()
{
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'ifGameStarted',
                game: getGameName(),

            },
            type: 'POST',
            success: function (res) {
                if(res == "true")
                {
                    clearInterval(startGameInterval);
                    alert("Game started!!\nmay the force be with you!");
                    turnRefreshInterval = setInterval(refreshTurnAngFinish, 1000);
                    $("#showStockStat").show();
                    $("#showRareWords").show();
                }
            }
        }
    );
}

function getGoldfishMode()
{
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'getGoldfish',
                gameName: getGameName(),

            },
            type: 'POST',
            success: function (res) {
                isGoldfish=String(res);
            }
        }
    );
}

function checkIfCompTurn() {
    var game = getGameName();
    isCompTurn = false;
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'checkComp',
                game: game,

            },
            type: 'POST',
            success: function (res) {

                if (res == true || res == "true") {
                    isCompTurn = true;
                }
            }
        }
    );

    return isCompTurn;
}

function playComp() {
    beginingOfTheTurn= false;
    var game = getGameName();
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'playComp',
                game: game,

            },
            type: 'POST',
            success: function () {
                beginingOfTheTurn = true;
            }
        }
    );

}

function winnerDecalre()
{
    //clear all intervals
    var interval_id = window.setInterval("", 9999); // Get a reference to the last
                                                    // interval +1
    for (var i = 1; i < interval_id; i++)
        window.clearInterval(i);
    var game = getGameName();
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'getWinners',
                game: game,

            },
            type: 'POST',
            success: function (json) {

                var scoreTab = jQuery.parseJSON(json);
                var size = scoreTab.length + 1;
                $('.mainTable').empty();
                $('.gameProperties').empty();
                var winnerMain = $('.winnerCol');
                winnerMain.empty();
                $('.currentPlayer').text("Game finish!!");
                $('#progress').text("Please press 'logout' to exit or wait 5 sec");

                for (i=0; i< size; i++)
                {
                    var scoreLine = $(document.createElement('div'));
                    scoreLine.addClass('scoreLine');
                    scoreLine.appendTo(winnerMain);
                }
                var elem = document.createElement("img");
                elem.setAttribute("src", "/wordiada/images/winner.gif");

                var score_line = $('.scoreLine');
                score_line[0].appendChild(elem);
                for(i=0; i< size-1; i++)
                {
                    score_line[i+1].innerHTML = scoreTab[i];
                }
                setTimeout(function() { logoutHandle() }, 5000);
            }
        }
    );
}

function refreshTurnAngFinish()
{
    var game = getGameName();
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'checkFinish',
                game: game,

            },
            type: 'POST',
            success: function (res) {
                if (res == true || res =="true")
                {
                    winnerDecalre();
                }
                else {
                    refreshPlayerTurn();
                }
            }
        }
    );
}


function refreshPlayerTurn()
{
    var game = getGameName();
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'checkMyTurn',
                game: game,

            },
            type: 'POST',
            success: function (res) {
                var obj = jQuery.parseJSON(res);
                if (obj[0] == true && beginingOfTheTurn==true) {
                    if (firstTimeMyTurn == true)
                    {
                        $("#showStockStat").show();
                        firstTimeMyTurn = false;
                        flipCardsSameAsOtherUser();
                        flipBackCorrectCardsOfOtherUser();
                    }

                    if (obj[1] == true)
                    {
                        playComp();
                    }
                    else {
                        $(".actions").show();
                        $("#roolDice").show();
                        $("#showStockStat").show();
                        $("#flipCards").hide();
                        $("#checkWord").hide();
                        //no interval for refresh stat to current playrt
                        clearInterval(statRefreshInterval);
                        $('#progress').text("Please roll dice");
                    }
                }
                else if (obj[0] == false){
                    $(".actions").hide();
                    firstTimeMyTurn = true;
                    //flip the cards the other user fliped
                    flipCardsSameAsOtherUser();
                    flipBackCorrectCardsOfOtherUser();
                }
            }
        }
    );
}
function logoutHandle()
{
    var game = getGameName();
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'logout',
                game: game,

            },
            type: 'GET',
            success: function () {
                window.location = "/wordiada/pages/menu/MenuPage.html";
            }
        }
    );
}



function rollDice()
{
    $("#roolDice").hide();
    $("#flipCards").hide();
    $("#checkWord").hide();
    beginingOfTheTurn=false;
    currentRetries=retries;
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'rollDice',
                gameName: getGameName()
            },
            type: 'POST',
            success: function(response){
                diceResult=Number(response);
                $('#progress').text("Please select "+ diceResult + " more cards");
                $('#rollDiceDialog').text(response);
                $( '#rollDiceDialog' ).dialog({
                    autoOpen: true,
                });

                $("#roolDice").click(function() {
                    $("#rollDiceDialog").dialog('open');
                });
            }
        }
    );
    clickBackCard();
}

function showBoard() {
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'getBoardSize',
                gameName: getGameName()
            },
            type: 'POST',
            success: function(response){
                boardSize++;
                var maxRow=Number(response);
                var maxCol=Number(response);
                var myTable = $("<table class='mainTable' oncontextmenu=\"return false\"></table>").appendTo("#mainBoard");


                for (var row =0; row < maxRow ; row++) {
                    var myRow = $("<tr></tr>").appendTo(myTable);
                    for (var col = 0; col < maxCol; col++) {
                        var img = "<img src='/wordiada/images/card.png' name="+row+col+"/>";
                        //  myRow.append("<td onclick=clickBackCard() id="+row+col+">" + img+ "</td>");
                        myRow.append("<td id="+"t".concat((row).toString()).concat((col).toString())+">" + img+ "</td>");
                    }
                }

                var tbl = document.getElementsByTagName("table")[0];
                var cls = tbl.getElementsByTagName("td");

            }
        }
    )

}

function clickBackCard(){
    var clicked="clicked";
    // if (first==true){
    //     diceResult=diceResult+1;
    //     first=false;
    // }

    if (firstClickBackCard==true) {
        $('td').click(function () {
            //alert('My position in table is: '+this.cellIndex+'x'+this.parentNode.rowIndex);
            if (Number(diceResult) > 0) {
                var card = $(this);
                //card.css('backgroundColor', '#FF7F50');
                $.ajax
                (
                    {
                        url: '../../game',
                        data: {
                            action: "clickedOnBackCard",
                            y: this.cellIndex,
                            x: this.parentNode.rowIndex,
                            game: getGameName(),
                        },
                        type: 'POST',
                        success: function (res) {
                            if (res == "true")
                            {
                                card.css('backgroundColor', '#FF7F50');
                                diceResult--;
                                $('#progress').text("Please select "+ (diceResult) + " more cards");
                                if (diceResult==0){
                                    diceResult--;
                                    $('#progress').text("Please flip your cards");
                                    $("#roolDice").hide();
                                    $("#flipCards").show();
                                    $("#checkWord").hide();
                                }
                            }

                        }

                    }
                )
            }


        });
    }
    firstClickBackCard = false;
}


function flipCardsClick()
{
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'getListOfClickedCards',
                gameName: getGameName(),
            },
            type: 'POST',
            success: function(response) {
                var obj = jQuery.parseJSON(response);
                $.each(obj, function(index, el) {
                    var idString="#t".concat((el.x).toString()).concat((el.y).toString());
                    var sign=(el.sign).toString();
                    var score= (el.score).toString();
                    // $(idString).css('background-image', 'url(https://thumbs.dreamstime.com/t/%E8%8B%8D%E7%99%BD%E5%8C%85%E8%A3%85%E7%BA%B8-15068787.jpg)');
                    var v=$("#mainBoard > table > tbody > tr:nth-child("+(parseInt(el.x)+parseInt(1))+") > td:nth-child("+(parseInt(el.y)+parseInt(1))+") > img");
                    v[0].src="/wordiada/images/"+sign+".png";
                    v[0].title= "Score: " + score;

                });
            }
        }
    )
    $('td').each(function(){
        $(this).css('backgroundColor', '#f7e1b5');
    });
    $("#roolDice").hide();
    $("#flipCards").hide();
    $("#checkWord").show();
    selectWord();
}


function selectWord(){
    $('#progress').text("Please build your word");
    pickWord=true;
    $('td').each(function(){
        if($(this).css('backgroundColor')=="rgb(0, 255, 255)"){
            $(this).css('backgroundColor', '#f7e1b5');
        }
    });

    if (firstSelectWord==true) {
        $('td').click(function () {
            if (currentRetries > 0) {
                var isflipedcard = (this.firstChild.getAttribute("src")).indexOf("card") == -1;
                if (isflipedcard == true) {
                    var card = $(this);
                    $.ajax
                    (
                        {
                            url: '../../game',
                            data: {
                                action: "buildWord",
                                y: this.cellIndex,
                                x: this.parentNode.rowIndex,
                                gameName: getGameName(),
                            },
                            type: 'POST',
                            success: function (response) {
                                var turn = "turn";
                                var mode = response[1].toString();
                                if (mode.indexOf(turn) == -1) {
                                    $('#progress').text("Your word is '" + response[0] + "', press check work when you finish");

                                    var up = "up";
                                    var down = "down";
                                    if (mode.indexOf(up) !== -1) {
                                        card.css('backgroundColor', '#00FFFF');
                                    }
                                    else if (mode.indexOf(down) !== -1) {
                                        card.css('backgroundColor', '#f7e1b5');
                                    }
                                }
                            }

                        }
                    )
                }
            }
        });
    }
    firstSelectWord=false;
}

function finishRound(word)
{
    $('.turn').text("Turn: " +retries+"/"+retries);
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: "finishRound",
                game: getGameName(),
                word: word,
            },
            type: 'POST',
            success: function () {

            }
        });
}

function checkWord(){
    var retriesTmp = Number(currentRetries);
    retriesTmp--;
    $('.turn').text("Turn: " +retriesTmp +"/"+retries);
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: "checkWord",
                gameName: getGameName()
            },
            type: 'POST',
            success: function (res) {
                var obj = jQuery.parseJSON(res);
                $.each(obj, function(index, el) {
                    if (el.isWord == "false" && currentRetries > 0) { //take another try
                        currentRetries--;
                        $('#correctWord').text("the word: \""+el.word+"\"\nis not in the dictionary!");
                        $( '#correctWord' ).dialog({
                            autoOpen: true,
                        });
                        selectWord();
                    } else if (el.isWord == "true") { // correct word
                        currentRetries=0;
                        beginingOfTheTurn=true;
                        $('#correctWord').text("the word: \""+el.word+"\"\nis correct!");
                        $( '#correctWord' ).dialog({
                            autoOpen: true,
                        });


                    }
                    var finalWord = "";
                    if (el.isWord == "true")
                    {
                        finalWord = el.word;
                    }
                    if (el.isWord=="true" || Number(currentRetries) == 0){
                        //return stat refresh
                        refreshStat();
                        statRefreshInterval = setInterval(refreshStat, 2000);
                        beginingOfTheTurn=true;
                        if (isGoldfish == "true") { //fliped all cards
                            $('td').each(function () {
                                if (($(this).css('backgroundColor') == "rgb(0, 255, 255)") || ($(this).css('backgroundColor') == "rgb(255, 127, 80)")) {
                                    this.firstChild.src = "/wordiada/images/card.png";
                                    $(this).css('backgroundColor', '#f7e1b5');
                                }
                            });
                        }
                        if (isGoldfish == "false" && el.isWord == "true") { //flip just the good word
                            $('td').each(function () {
                                if (($(this).css('backgroundColor') == "rgb(0, 255, 255)")) {
                                    this.firstChild.src = "/wordiada/images/card.png";
                                    $(this).css('backgroundColor', '#f7e1b5');
                                }
                            });
                        }
                        finishRound(finalWord);
                    }
                });
            }

        }
    )

}


function refreshStat()
{
    var game = getGameName();
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'stat',
                game: game
            },
            type: 'POST',
            success: function(json){
                var obj = jQuery.parseJSON(json);
                refreshPlayersTable(obj.playersTables);
                refreshGamePropeties(obj);
                refreshCurrentPlayerWords(obj.currentPlayStat);
            }
        }
    )
}

function refreshCurrentPlayerWords(playerWords)
{
    var words = $('.currentPlayerWordsCol');
    var amount = $('.currentPlayerCountCol');
    var score = $('.currentPlayerScoreCol');

    words.empty();

    for (i = 0; i < playerWords.length; i++) {
        var wordsContainerDiv = $(document.createElement('div'));
        wordsContainerDiv.addClass('wordsDiv');
        wordsContainerDiv.appendTo(words);

        var amountDiv = $(document.createElement('div'));
        amountDiv.addClass('amountDiv');
        amountDiv.appendTo(amount);

        var scoreDiv = $(document.createElement('div'));
        scoreDiv.addClass('CurrentScoreDiv');
        scoreDiv.appendTo(score);


    }

    var wordsDivs = $('.wordsDiv');
    var amountDivs = $('.amountDiv');
    var scoreDivs = $('.CurrentScoreDiv');
    for (i = 0; i < playerWords.length; i++) {
        wordsDivs[i].innerHTML = playerWords[i].word;
        amountDivs[i].innerHTML = playerWords[i].amount;
        scoreDivs[i].innerHTML = playerWords[i].score;
    }

}

function refreshGamePropeties(properties)
{
    var round = properties.round;
    var goldFish = "False";
    if (properties.isGoldFish == true)
    {
        goldFish = "True";
    }
    var winnerMethod = properties.winnerMethod;
    var size = properties.size;
    var currentPlayer = properties.currentPlayerName;
    var stock = properties.stock;

    $('.round').text("Round: " + round);
    $('.boardSize').text("Board size: " + size + "/" + size);
    $('.goldFish').text("Gold fish: " + goldFish);
    $('.winnerMethod').text("Winner method: " + winnerMethod);
    $('.stock').text("Stock size: " + stock);
    //set only if game running
    var isGameRunning = properties.isRunning;
    if (isGameRunning == true) {
        $('.currentPlayer').text("Current player: " + currentPlayer);
        $('#progress').text(properties.progressText);
    }
    else
    {
        $('.currentPlayer').text("Welcome to wordiada");
        var remain = properties.remainPlayers;
        $('#progress').text("Waiting for more " + remain +" players to start");
        $(".actions").hide();
    }


}


function refreshPlayersTable(tableModel) {
    var playersName = $('.playersNamesCol');
    var playersType = $('.playersTypesCol');
    var playersWord = $('.playersWordsCol');
    var playersScore = $('.playersScoreCol');


    playersName.empty();
    playersType.empty();
    playersWord.empty();
    playersScore.empty();
    for (i = 0; i < tableModel.length; i++) {
        var playerContainerDiv = $(document.createElement('div'));
        playerContainerDiv.addClass('playerContainerDiv');
        playerContainerDiv.appendTo(playersName);

        var playerDiv = $(document.createElement('div'));
        playerDiv.addClass('playerDiv');
        playerDiv.appendTo(playerContainerDiv);

        var scoreDiv = $(document.createElement('div'));
        scoreDiv.addClass('scoreDiv');
        scoreDiv.appendTo(playersScore);

        var typeDiv = $(document.createElement('div'));
        typeDiv.addClass('typeDiv');
        typeDiv.appendTo(playersType);

        var wordDiv = $(document.createElement('div'));
        wordDiv.addClass('wordDiv');
        wordDiv.appendTo(playersWord);
    }

    var playerDivs = $('.playerDiv');
    var scoreDivs = $('.scoreDiv');
    var typeDivs = $('.typeDiv');
    var wordDivs = $('.wordDiv');
    for (i = 0; i < tableModel.length; i++) {
        playerDivs[i].innerHTML = tableModel[i].name;
        scoreDivs[i].innerHTML = tableModel[i].score;
        typeDivs[i].innerHTML = tableModel[i].type;
        wordDivs[i].innerHTML = tableModel[i].words;
    }

}

function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
}

function getGameName()
{
    return getURLParameter("game");
}

function flipCardsSameAsOtherUser(){
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'getBoard',
                gameName: getGameName()
            },
            type: 'POST',
            success: function(response) {
                var obj = jQuery.parseJSON(response);
                $.each(obj, function(index, el) {
                    var idString="#t".concat((el.x).toString()).concat((el.y).toString());
                    var sign=(el.sign).toString();
                    var score= (el.score).toString();
                    var v=$("#mainBoard > table > tbody > tr:nth-child("+(parseInt(el.x)+parseInt(1))+") > td:nth-child("+(parseInt(el.y)+parseInt(1))+") > img");
                    v[0].src="/wordiada/images/"+sign+".png";
                    v[0].title= "Score: " + score;

                });
            }
        }
    );
}

function flipBackCorrectCardsOfOtherUser(){
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'getRemovedCards',
                gameName: getGameName()
            },
            type: 'POST',
            success: function(response) {
                var obj = jQuery.parseJSON(response);
                $.each(obj, function(index, el) {
                    var idString="#t".concat((el.x).toString()).concat((el.y).toString());
                    var v=$("#mainBoard > table > tbody > tr:nth-child("+(parseInt(el.x)+parseInt(1))+") > td:nth-child("+(parseInt(el.y)+parseInt(1))+") > img");
                    v[0].src="/wordiada/images/card.png";
                });
            }
        }
    );
}

function getRareWords()
{
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'getRareWords',
                gameName: getGameName()
            },
            type: 'POST',
            success: function(response){

                $("#showRareWords").click(function() {
                    $("#rareWords").dialog('open');
                });
                $('#rareWords').text(response);
                $( '#rareWords' ).dialog({
                    autoOpen: true,
                });
            }
        }
    );
}

function getStockStat()
{
    $.ajax
    (
        {
            url: '../../game',
            data: {
                action: 'getStockStat',
                gameName: getGameName()
            },
            type: 'POST',
            success: function(response){
                //  alert(response);

                $("#showStockStat").click(function() {
                    $("#stockDialog").dialog('open');
                });
                $('#stockDialog').text(response);
                $( '#stockDialog' ).dialog({
                    autoOpen: true,
                });
            }
        }
    );
}
