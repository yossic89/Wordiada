package Engine;

/**
 * Created by yossi_c89 on 03/04/2017.
 */

import GameManager.*;
import Resources.Config;
import Resources.Player;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameEngine implements Serializable {
    private boolean game_running;
    private boolean conf_load;
    public Config config;
    private IGameManager gameManger;
    //private AbstractUI ui;
    private Dictionary dic;
    private HashMap<String,Integer> dictionary=null;
    private Board gameBoard;
    private long start_game_time;
    private String owner;
    private enum status{STARTING, ROLLING, FLIPPING, WORDING, FINISH};
    private status gameStatus;
    public ArrayList<Point> backCardsClicked;
    public ArrayList<Point> removedCards;
    public  ArrayList<PointAndSign> backCardsClickedWithSign;
    public  ArrayList<PointAndSign> buildWordPointAndSign;
    public String word;


    public GameEngine()
    {
        this.game_running = false;
        this.conf_load = false;
        this.config = new Config();
        gameStatus = status.STARTING;
        backCardsClicked=new ArrayList<Point>();
        removedCards=new ArrayList<Point>();
        backCardsClickedWithSign=new ArrayList<PointAndSign>();
        buildWordPointAndSign=new ArrayList<PointAndSign>();
        word = "";
    }

    public void initGameManager(){this.gameManger.Init();}

    public String gameStatusStr()
    {
        String res = "";
        switch (this.gameStatus)
        {
            case STARTING:
                res = "STARTING";
                break;
            case ROLLING:
                res = "ROLLING";
                break;
            case FLIPPING:
                res = "FLIPPING";
                break;
            case WORDING:
                res = "WORDING";
                break;
            case FINISH:
                res = "FINISH";
                break;
        }
        return res;
    }

    public String getStatusStr()
    {
        String user = this.gameManger.getCurrentPlayer();
        String res = "";
        switch (this.gameStatus)
        {
            case STARTING:
                res = "Waiting for more players to start";
                break;
            case ROLLING:
                res = user + " rolling dice";
                break;
            case FLIPPING:
                res = user + " flipping cards";
                break;
            case WORDING:
                res = user + " choose his words";
                break;
            case FINISH:
                res = "game finish amigo";
                break;
        }
        return res;
    }

    public List<String> loadConfFileOnline(String xml)
    {
        List<String> errors = new ArrayList();
        if (this.conf_load && this.game_running)
        {
            errors.add("You cannot change the config during playing...it's cheating");
            return errors;
        }
        boolean res = this.config.loadFromString(xml);
        if (res)
            this.gameManger = new MultiManager(this);
        else
        {
            errors.addAll(this.config.getErrorsList());
        }
        if (!errors.isEmpty())
        {
            return errors;
        }
        else
        {
            this.conf_load = true;
            return errors;
        }
    }

    public void setDictionary(String content)
    {
        this.dic=new Dictionary(content, this.config.getTileList());
    }

    public List<String> loadConfFile(String path)
    {
        List<String> errors = new ArrayList();
        if (this.conf_load && this.game_running)
        {
            errors.add("You cannot change the config during playing...it's cheating");
            return errors;
        }
        boolean res = this.config.load(path);
        if (res)
        {
            this.dic=new Dictionary(this.config.dictPath,this.config.getTileList());
            switch (this.config.config.getGameType().getValue())
            {
                case ("Basic"):
                    errors.add("Basic operation is for HW 1");
                case ("MultiPlayer"):
                    this.gameManger = new MultiManager(this);
                    break;
                default:
                    errors.add("Game type is not defined");
            }

        }
        else
        {
            errors.addAll(this.config.getErrorsList());
        }
        if (!errors.isEmpty())
        {
            //errors.add(0, "Load " + path + " failed :");
            //this.ui.printMsg(errors);
            return errors;
        }
        else
        {
            this.conf_load = true;
            return errors;
        }
    }

    public List<String> startGame()
    {
        List<String> errors = new ArrayList();
        if (!conf_load)
        {
            errors.add("No config file was loaded !!!");
        }
        if (game_running)
        {
            errors.add("You are already playing !!!");
        }
        try {
            this.dictionary = this.dic.getDictionary();
            this.dic.initHashMaps();
        } catch(Exception e){}
        if (this.dictionary == null && errors.isEmpty())
        {
            errors.add("Failed to open dictionary file : " + this.config.dictPath);
        }
        if (errors.isEmpty())
        {
            this.start_game_time = System.nanoTime();
            this.game_running = true;
            this.gameStatus = status.ROLLING;

        }
        try {
            // if (!this.gameManger.isInitiate) this.gameManger.initNewGame();
        }catch(Exception e){}
        return errors;
    }

    public int randNum()
    {
        this.gameStatus = status.FLIPPING;
        int res = new Random().nextInt((this.config.config.getStructure().getCubeFacets() - 2) + 1) + 2;
        if (res > this.getBackCardsNum())
            res = this.getBackCardsNum();
        return res;
    }

    public void finishFliping() {this.gameStatus = status.WORDING;}

    public int getRound() {return this.gameManger.getRoundCount();}

    public long getStartTime() {return this.start_game_time;}

    public  HashMap<String, Integer> getPlayerWordCount(){return this.gameManger.getWordCountStat();}

    public HashMap<String, List<String>> getPlayersWordsStat(){return this.gameManger.getPlayersWordsStat();}

    public HashMap<String, Integer> getStockStat() {
        return this.gameBoard.getStat();
    }

    public int getTotalWordsCount()
    {
        int res = 0;
        for (Map.Entry<String, Integer> it : this.dictionary.entrySet())
        {
            res += it.getValue();
        }
        return res;
    }
    public int getWordRepeat(String key)
    {
        int res = 0;
        if (this.dictionary.containsKey(key))
        {
            res = this.dictionary.get(key);
        }
        return res;
    }
    public boolean playWord(int n)
    {
        boolean res = false;
        //List<Point> selectedTiles = this.ui.selectTilesFromBoard(n);


        return res;
    }

    public Statistics getStat()
    {
        if (this.conf_load && this.game_running)
        {
            Statistics res = new Statistics(this);
            res.initStat();
            return res;
        }
        else
            return null;
    }

    public Statistics getPlayersStat()
    {
        if (this.conf_load)
        {
            Statistics res = new Statistics(this);
            res.initPlayersStat();
            return res;
        }
        else
            return null;
    }

    public List<AbstractPlayer> getAllPlayers()
    {
        if (this.gameManger == null)
        {
            return null;
        }
        else
        {
            return this.gameManger.getAllPlayers();
        }
    }

    public String basicExit()
    {
        this.game_running = false;
        BasicManager b = (BasicManager) this.gameManger;
        return b.getWinnerAfterExit();
    }

    public void finishGame(){
        this.gameStatus = status.STARTING;
        this.game_running = false;
        this.buildBoardAndStock();
        this.gameManger = new MultiManager(this);
    }

    public boolean isGameRunning()
    {
        return (conf_load && game_running);
    }

    public void finishRound(String word)
    {
        if (word != null && word != "")
        {
            this.gameManger.updateWord(word);
            this.gameManger.updateScore(getWordScore(word));
        }
        this.gameManger.playNext();
        this.gameStatus = status.ROLLING;
    }

    public void beforeSave(){this.gameBoard.stock.beforeSave();}

    public void afterLoad(){this.gameBoard.stock.afterLoad();}
    public int getStockSize(){
        return gameBoard.stock.getSizeofStock();
    }

    public String getPlayerTurn(){
        return this.gameManger.getCurrentPlayer();
    }

    public boolean isFliped(int x,int y){return gameBoard.isFlipedCard(x,y);}

    public boolean isEmpty(int x,int y){return gameBoard.isEmptyCard(x,y);}

    public Tile getTile(int x,int y){
        return gameBoard.getTile(x,y);
    }

    public void setIsShown(int x,int y,boolean shown){
        gameBoard.setIsShown(x,y,shown);
    }

    public boolean isWordInDictionary(String word){
        if (word ==null || word == "" || word.length() < 2)
            return false;
        return dictionary.containsKey(word);
    }

    public Tile takeCardFromBoard(int x,int y){
        gameBoard.replaceCardWithStock(x,y);
        return getTile(x,y);
    }

    public int getBackCardsNum(){
        return gameBoard.getBackCardsNum();
    }

    public void printDataBoard(){gameBoard.printDataBoard();}

    public String getWinner()
    {
        return this.gameManger.getWinner(this.config.config.getGameType().getWinnerAccordingTo());
    }

    public boolean checkDraw()
    {
        return this.gameManger.checkDraw();
    }

    public void initDataBoard(){
        gameBoard.initDataBoard();
    }

    public void printDic(){
        System.out.println(dictionary);
    }

    public ArrayList<Point> chooseTileRandom(int rounds)
    {
        this.gameStatus = status.WORDING;
        ArrayList<Point> unshownTiles = this.gameBoard.getTilesPositions(false);
        //known casting
        CompPlayer comp = (CompPlayer)this.gameManger.getPlayer();
        if (comp == null)
            return null;
        return comp.chooseNumbers(unshownTiles, rounds);
    }

    public ArrayList<Point> chooseRandomCards()
    {

        ArrayList<Point> shownTiles = this.gameBoard.getTilesPositions(true);
        int rand = 5;
        if (shownTiles.size() < 5)
            rand = shownTiles.size();
        int wordSize = new Random().nextInt((rand - 2) + 1) + 2;
        //known casting
        CompPlayer comp = (CompPlayer)this.gameManger.getPlayer();
        if (comp == null)
            return null;
        return comp.chooseNumbers(shownTiles, wordSize);
    }

    public ArrayList<Point> getFlippedCards() {return this.gameBoard.getTilesPositions(true);}

    public String buildRandomWord()
    {
        ArrayList<String> possiable = this.gameBoard.getAvailableChars();
        //known casting
        CompPlayer comp = (CompPlayer)this.gameManger.getPlayer();
        if (comp == null)
            return null;
        return comp.buildWord(possiable);
    }



    public ArrayList<String> getAvailableChars() {return this.gameBoard.getAvailableChars();}

    public boolean isCompTurn(){
        return this.gameManger.getPlayerType().equals("Computer");}


    public void initPlayerDueConfig()
    {
        this.initGameManager();
        for (Player p : this.config.config.getPlayers().getPlayer())
        {
            if (p.getType().equals("Human"))
            {
                this.registerPlayer(new HumanPlayer(p));
            }
            else if (p.getType().equals("Computer"))
            {
                this.registerPlayer(new CompPlayer(p));
            }

        }
    }
    public void registerPlayer(AbstractPlayer p)
    {
        this.gameManger.registerPlayer(p);
    }

    public String getPlayerType(){return this.gameManger.getPlayerType();}

    public Dictionary getDictonary(){return this.dic;}

    public int buildBoardAndStock(){
        this.gameBoard = new Board(this.config.config.getStructure().getBoardSize());
        this.gameBoard.buildDataBoardAndStock(this.config.getTileList());
        return this.config.config.getStructure().getBoardSize();
    }


    public HashMap<String, Short> getPlayerIdMap() {return this.gameManger.getPlayersIdMap();}

    public HashMap<String, Integer> getCurrentPlayerWordsMap() { return this.gameManger.getCurrentPlayerWordsMap();}

    public void unregisterPlayer(AbstractPlayer p){
        this.gameManger.unregisterPlayer(p);
        if (this.getPlayersCount() == 0)
        {
            this.finishGame();
        }
    }

    public short getCurrentPlayerID() {return this.gameManger.getPlayer().getId();}

    public int getWordScore(String word)
    {
        int res = 0;
        if (this.config.config.getGameType().getWinnerAccordingTo().equals("WordScore"))
        {
            res = this.dic.getScore(word);
        }
        else if (this.config.config.getGameType().getWinnerAccordingTo().equals("WordCount"))
        {
            res = 1;
        }
        return res;
    }

    public boolean isAllRetire(){return (this.gameManger.getAllPlayers().size() < 2);}

    public TreeMap<Integer,  ArrayList<String>> getMapByScore(){return this.gameManger.getMapByScore();}

    private String listToString(ArrayList<String> list)
    {
        String res = "";
        for (int i = 0 ; i<list.size() -1; i++)
        {
            res += list.get(i);
            res += " , ";
        }
        res += list.get(list.size()-1);
        return res;
    }

    public ArrayList<String> getMapByScoreStr()
    {
        ArrayList<String> res = new ArrayList<>();
        TreeMap<Integer, ArrayList<String>> winners = this.getMapByScore();
        ArrayList<Integer> keys = new ArrayList<>(winners.keySet());
        String header = "WINNER: "+this.listToString(winners.get(keys.get(keys.size()-1))) + " ("+ keys.get(keys.size()-1)+")";
        res.add(header);
        int place = 2;
        for(int i=keys.size()-2; i>=0;i--){
            String line = "Place " + place +": ";
            line += this.listToString(winners.get(keys.get(i)));
            line += " (" + keys.get(i) +")";
            res.add(line);
            place ++;
        }
        return res;
    }

    public String getCalcOfWord(String word){return dic.getCalcWord(word);}

    public int getPlayersCount(){return this.gameManger.getPlayersCount();}

    public void setOwner(String name){this.owner = name;}

    public String getOwner() {return this.owner;}

    public ArrayList<String> canPlayerRegister()
    {
        ArrayList<String> res = new ArrayList<>();
        if (this.isGameRunning())
            res.add("This game is already running");
        if (this.getPlayersCount() >= this.config.config.getDynamicPlayers().getTotalPlayers())
            res.add("No place for you");
        return res;
    }

    public HashMap<String,Integer> dictTest()
    {
        return this.dic.getDictionary();
    }

    public int getRetriesNumber(){return this.config.config.getStructure().getRetriesNumber() + 1;}

    public boolean isGoldFish(){return this.config.getGoldFishMode();}

    public boolean checkStartGame()
    {
        boolean res = false;
        //we have enough players
        if (this.getPlayersCount() == this.config.config.getDynamicPlayers().getTotalPlayers())
        {
            if (!(this.game_running)) {
                List<String> ll = this.startGame();
                System.out.print("The problems: ");
                for (String s : ll)
                {
                    System.out.print(s+ " ");
                }
            }
            res = this.game_running;
        }
        return res;
    }

    public void compPlay() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        int rounds = this.randNum();
        backCardsClicked.addAll(this.chooseTileRandom(rounds));

        backCardsClickedWithSign.clear();

        //flip cards
        TimeUnit.SECONDS.sleep(2);
        for(Point point:backCardsClicked){
            Tile t=getTile(point.getX()+1,point.getY()+1);
            t.setIsShown(true);
            PointAndSign pointAndSign=new PointAndSign(point.getX(),point.getY(),t.getSign().get(0), t.getScore());
            backCardsClickedWithSign.add(pointAndSign);
        }

        finishFliping();

        //build word
        TimeUnit.SECONDS.sleep(2);
        boolean success = false;
        String finalWord = "";
        for (int i=0; i<getRetriesNumber() && !success; i++) {
            ArrayList<Point> points = chooseRandomCards();
            for (Point p : points)
            {

                for (PointAndSign pointAndSign : backCardsClickedWithSign) {
                    if (pointAndSign.getX() == p.getX() && pointAndSign.getY() == p.getY()) {
                        if (!(buildWordPointAndSign.contains(pointAndSign))) {
                            PointAndSign buildWord = new PointAndSign(p.getX(), p.getY(), pointAndSign.getSign(), pointAndSign.getScore());
                            buildWordPointAndSign.add(buildWord);
                        }
                        else
                        {
                            PointAndSign removeWord = new PointAndSign(p.getX(), p.getY(), pointAndSign.getSign(), pointAndSign.getScore());
                            buildWordPointAndSign.remove(removeWord);
                        }
                    }
                }

            }

            //check word
            finalWord = this.getBuildWord();
            success=isWordInDictionary(finalWord);
            if (success){ //remove all the points that build the correct word
                for(PointAndSign pointAndSign:buildWordPointAndSign) {
                    takeCardFromBoard(pointAndSign.getX() + 1, pointAndSign.getY() + 1);

                    for (Point point : backCardsClicked) {
                        if ((point.getX() == pointAndSign.getX()) && (point.getY() == pointAndSign.getY())){
                            removedCards.add(point);
                            //gameEngine.backCardsClicked.remove(gameEngine.backCardsClicked.indexOf(point));
                            backCardsClicked.remove(point);
                            break;
                        }
                    }
                    for (PointAndSign point : backCardsClickedWithSign) {
                        if (point.getX() == pointAndSign.getX() && point.getY() == pointAndSign.getY()){
                            //gameEngine.backCardsClickedWithSign.remove(gameEngine.backCardsClickedWithSign.indexOf(point));
                            backCardsClickedWithSign.remove(point);
                            break;
                        }
                    }
                }
            }
            else
            {
                finalWord = "";
            }
            buildWordPointAndSign.clear();
        }

        //handle gild fish
        if (isGoldFish())
        {
            //backCardsClickedWithSign
            for (PointAndSign p : backCardsClickedWithSign)
            {
                removedCards.add(new Point(p.getX(), p.getY()));
            }
            backCardsClickedWithSign.clear();
            backCardsClicked.clear();
        }
        this.finishRound(finalWord);
    }

    public boolean isGameFinish()
    {
        boolean res = false;
        res |= (getBackCardsNum() == 0);
        res |= (getStockSize() == 0);
        res |= this.isAllRetire();
        return res;
    }

    public ArrayList<PointAndSign> getBackCardsClickedWithSign() {
        return backCardsClickedWithSign;
    }

    public HashMap<String, Integer> getPlayerWordMap(AbstractPlayer p){return this.gameManger.getPlayerWordMap(p);}

    public String getBuildWord()
    {
        String res ="";
        for (PointAndSign p: buildWordPointAndSign)
        {
            res += p.getSign();
        }
        return res;
    }

    public HashMap<String,Integer> getTenRareWords(){
        return this.dic.getTenRareWords();
    }
}


