package chat.servlets;

import Engine.*;
import Resources.CurrentGameDataModel;
import chat.constants.Constants;
import chat.utils.ServletUtils;
import chatEngine.users.UserManager;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@WebServlet(name="GameServlet", urlPatterns={"/game"})
public class GameServlet  extends HttpServlet {
    private EngineController controller = EngineController.getInstance();
    //private static String word="";
//    private  ArrayList<PointAndSign> backCardsClickedWithSign=new ArrayList<PointAndSign>();
//    private  ArrayList<PointAndSign> buildWordPointAndSign=new ArrayList<PointAndSign>();


    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action!=null) {
            switch (action) {
                case "logout":
                    this.logout(req, resp);
                    break;




            }
        }
        //req.getRequestDispatcher("/wordiada/pages/gameRoom/gameRoom.js").forward(req, resp);
        resp.sendRedirect("/wordiada/pages/gameRoom/gameRoom.js");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        GameEngine gameEngine;
        Gson gson = new Gson();
        if (action!=null) {
            switch (action) {
                case "playComp":
                    gameEngine= controller.getGameEngine(request.getParameter("game"));
                    try {
                        gameEngine.compPlay();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case "rollDice":
                    gameEngine= controller.getGameEngine(request.getParameter("gameName"));
                    gameEngine.removedCards.clear();
                    int dice= gameEngine.randNum();
                    out.print(gson.toJson(dice));
                    break;

                case "getBoard":
                    gameEngine= controller.getGameEngine(request.getParameter("gameName"));
                    out.print(gson.toJson(gameEngine.getBackCardsClickedWithSign()));
                    break;

                case "getRemovedCards":
                    gameEngine= controller.getGameEngine(request.getParameter("gameName"));
                    out.print(gson.toJson(gameEngine.removedCards));
                    break;
                case "getBoardSize":
                    gameEngine= controller.getGameEngine(request.getParameter("gameName"));

                    int boardSize=  gameEngine.buildBoardAndStock();
                    out.print(gson.toJson(boardSize));
                    break;
                case "clickedOnBackCard": {
//                    String x = request.getParameter("x");
//                    String y = request.getParameter("y");
//                    String game = request.getParameter("game");
//                    gameEngine = this.controller.getGameEngine(game);
//                    Point p = new Point(Integer.valueOf(x), Integer.valueOf(y));
//                    if (!(gameEngine.backCardsClicked.contains(p))) gameEngine.backCardsClicked.add(p);
                    this.clickBackCardForFlipAction(request, response);
                    break;
                }
                case "getListOfClickedCards":
                    gameEngine= controller.getGameEngine(request.getParameter("gameName"));
                    gameEngine.printDataBoard();
                    gameEngine.backCardsClickedWithSign.clear();
                    for(Point point:gameEngine.backCardsClicked){
                        Tile t=gameEngine.getTile(point.getX()+1,point.getY()+1);
                        t.setIsShown(true);
                        PointAndSign pointAndSign=new PointAndSign(point.getX(),point.getY(),t.getSign().get(0), t.getScore());
                        gameEngine.backCardsClickedWithSign.add(pointAndSign);
                    }
                    out.print(gson.toJson(gameEngine.backCardsClickedWithSign));
                    //if (gameEngine.isGoldFish()) {gameEngine.backCardsClicked.clear();}
                    gameEngine.finishFliping();
                    break;
                case "buildWord":
                    this.buildWordAction(request, response);
                    break;

                case "checkWord":
                    gameEngine= controller.getGameEngine(request.getParameter("gameName"));
                    boolean isWordInDic=gameEngine.isWordInDictionary(gameEngine.getBuildWord());
                    if (isWordInDic){ //remove all the points that build the correct word
                        for(PointAndSign pointAndSign:gameEngine.buildWordPointAndSign) {
                            gameEngine.takeCardFromBoard(pointAndSign.getX() + 1, pointAndSign.getY() + 1);

                            for (Point point : gameEngine.backCardsClicked) {
                                if ((point.getX() == pointAndSign.getX()) && (point.getY() == pointAndSign.getY())){
                                    gameEngine.removedCards.add(point);
                                    //gameEngine.backCardsClicked.remove(gameEngine.backCardsClicked.indexOf(point));
                                    gameEngine.backCardsClicked.remove(point);
                                    break;
                                }
                            }
                            for (PointAndSign point : gameEngine.backCardsClickedWithSign) {
                                if (point.getX() == pointAndSign.getX() && point.getY() == pointAndSign.getY()){
                                    //gameEngine.backCardsClickedWithSign.remove(gameEngine.backCardsClickedWithSign.indexOf(point));
                                    gameEngine.backCardsClickedWithSign.remove(point);
                                    break;
                                }
                            }
                        }

                    }

                    String result="[{\"isWord\":\""+String.valueOf(isWordInDic)+"\",\"word\":\""+gameEngine.getBuildWord()+"\"}]";
                    out.print(result);
                    //gameEngine.word="";
                    gameEngine.buildWordPointAndSign.clear();
                    break;

                case "stat":
                    this.getGameStat(request, response);
                    break;
                case "checkMyTurn":
                    this.isMyTurn(request, response);
                    break;
                case "checkComp":
                    this.checkCompTurn(request,response);
                    break;
                case "getRetriesNumber":
                    gameEngine= controller.getGameEngine(request.getParameter("gameName"));
                    int retries=gameEngine.getRetriesNumber();
                    out.print(gson.toJson(retries));
                    break;
                case "getGoldfish":
                    gameEngine= controller.getGameEngine(request.getParameter("gameName"));
                    boolean goldfish=gameEngine.isGoldFish();
                    out.print(gson.toJson(goldfish));
                    break;
                case "ifGameStarted":
                    this.checkIfGameStarted(request, response);
                    break;
                case "finishRound":
                    this.finishRound(request, response);
                    break;
                case "checkFinish":
                    gameEngine= controller.getGameEngine(request.getParameter("game"));
                    boolean isFinish=gameEngine.isGameFinish();
                    out.print(gson.toJson(isFinish));
                    break;
                case "getWinners":
                    gameEngine= controller.getGameEngine(request.getParameter("game"));
                    ArrayList<String> winners = gameEngine.getMapByScoreStr();
                    out.print(gson.toJson(winners));
                    break;
                case "getStockStat":
                    gameEngine= controller.getGameEngine(request.getParameter("gameName"));
                    String str="";
                    HashMap<String, Integer> stat=gameEngine.getStockStat();
                    Iterator it = stat.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        str=str+pair.getKey() + " = " + pair.getValue().toString() +"\n";
                    }
                    out.print(str);
                    break;
                case "getRareWords":
                    gameEngine= controller.getGameEngine(request.getParameter("gameName"));
                    String str1="";
                    HashMap<String, Integer> rare= gameEngine.getTenRareWords();
                    Iterator it1 = rare.entrySet().iterator();
                    while (it1.hasNext()) {
                        Map.Entry pair = (Map.Entry)it1.next();
                        str1=str1+pair.getKey() + " score: " + pair.getValue().toString() +"\n";
                    }
                    out.print(str1);
                    break;
            }
        }
        //request.getRequestDispatcher("/pages/gameRoom/gameRoom.js").forward(request, response);
    }

    private void clickBackCardForFlipAction(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String x = request.getParameter("x");
        String y = request.getParameter("y");
        String game = request.getParameter("game");
        boolean res = false;
        GameEngine gameEngine = this.controller.getGameEngine(game);
        Point p = new Point(Integer.valueOf(x), Integer.valueOf(y));
        if (!(gameEngine.backCardsClicked.contains(p))) {
            gameEngine.backCardsClicked.add(p);
            res = true;
        }
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        out.print(gson.toJson(res));
    }

    private void checkIfGameStarted(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String game = request.getParameter("game");
        boolean res = this.controller.checkIfNeedToStartGame(game);
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        out.print(gson.toJson(res));
    }

    private void isMyTurn(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String game = request.getParameter("game");
        String user = (String )request.getSession(false).getAttribute(Constants.USERNAME);
        boolean[] res = new boolean[2];
        res[0] = controller.isThisPlayerTurn(user, game);
        res[1] = controller.isCompTurn(game);
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        out.print(gson.toJson(res));
    }

    private void buildWordAction(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String x = request.getParameter("x");
        String y = request.getParameter("y");
        String actionRes="";
        GameEngine gameEngine = controller.getGameEngine(request.getParameter("gameName"));
        if (gameEngine.gameStatusStr() != "WORDING")
        {
            String[] retVal = new String[2];
            retVal[0]= "";
            retVal[1]= "turn";
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            Gson gson = new Gson();
            out.print(gson.toJson(retVal));
            return;
        }
        for (PointAndSign pointAndSign : gameEngine.backCardsClickedWithSign) {
            if (pointAndSign.getX() == Integer.valueOf(x) && pointAndSign.getY() == Integer.valueOf(y)) {
                if (!(gameEngine.buildWordPointAndSign.contains(pointAndSign))) {
                    //gameEngine.word += String.valueOf(pointAndSign.getSign());
                    PointAndSign buildWord = new PointAndSign(Integer.valueOf(x), Integer.valueOf(y), pointAndSign.getSign(), pointAndSign.getScore());
                    gameEngine.buildWordPointAndSign.add(buildWord);
                    actionRes = "up";
                }
                else
                {
                    PointAndSign removeWord = new PointAndSign(Integer.valueOf(x), Integer.valueOf(y), pointAndSign.getSign(), pointAndSign.getScore());
                    gameEngine.buildWordPointAndSign.remove(removeWord);
                    actionRes = "down";
                }
            }
        }

        String[] retVal = new String[2];
        retVal[0]= gameEngine.getBuildWord();
        retVal[1]= actionRes;
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        Gson gson = new Gson();
        out.print(gson.toJson(retVal));

    }

    private void checkCompTurn(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String game = request.getParameter("game");
        boolean res = controller.isCompTurn(game);
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        out.print(gson.toJson(res));
    }

    private void handleGoldFish(String game)
    {
        GameEngine eng = controller.getGameEngine(game);
        if (eng.isGoldFish())
        {
            //backCardsClickedWithSign
            for (PointAndSign p : eng.backCardsClickedWithSign)
            {
                eng.removedCards.add(new Point(p.getX(), p.getY()));
            }
            eng.backCardsClickedWithSign.clear();
            eng.backCardsClicked.clear();
        }
    }

    private void finishRound(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String game = request.getParameter("game");
        String word = request.getParameter("word");
        this.handleGoldFish(game);
        this.controller.finishRound(game, word);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String game = request.getParameter("game");
        String user = (String )request.getSession(false).getAttribute(Constants.USERNAME);
        UserManager manager = ServletUtils.getUserManager(getServletContext());
        controller.unregisterPlayerToGame(user, game, manager);
    }

    private void getGameStat(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String game = request.getParameter("game");
        String user = (String )request.getSession(false).getAttribute(Constants.USERNAME);
        UserManager manager = ServletUtils.getUserManager(getServletContext());
        CurrentGameDataModel res = controller.getCurrentGameModel(game, user, manager);
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        out.print(gson.toJson(res));

    }

}
