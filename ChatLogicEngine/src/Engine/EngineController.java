package Engine;

import GameManager.AbstractPlayer;
import Resources.CurrentGameDataModel;
import Resources.GameTableModel;
import chatEngine.users.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yossi_c89 on 15/07/2017.
 */
public class EngineController {
    private HashMap<String, GameEngine> data;
    private HashMap<String, GameEngine> uploadGames;
    private static EngineController obj;

    public static EngineController getInstance()
    {
        if (obj == null)
            obj = new EngineController();
        return obj;

    }

    private EngineController()
    {
        this.data = new HashMap<>();
        this.uploadGames = new HashMap<>();
    }

    public boolean addEngine(String name)
    {
        if (data.containsKey(name))
            return false;
        if (!(uploadGames.containsKey(name)))
            return false;
        GameEngine engine = uploadGames.get(name);
        this.data.put(name, engine);
        uploadGames.remove(name);
        return true;
    }

    public boolean addLoadedGame(GameEngine engine, String owner)
    {
        String name = engine.config.getName();
        engine.setOwner(owner);
        if (data.containsKey(name))
            return false;
        if (this.uploadGames.containsKey(name))
            return false;
        this.uploadGames.put(name, engine);
        return true;
    }

    public GameEngine getGameEngine(String name)
    {
        GameEngine res = null;
        if (this.data.containsKey(name))
            res = this.data.get(name);
        else
        {
            name += " ";
            if (this.data.containsKey(name))
                res = this.data.get(name);
        }
        return res;
    }

    public GameEngine getLoadingGame(String name)
    {
        GameEngine res = null;
        if (this.uploadGames.containsKey(name))
            res = this.uploadGames.get(name);
        return res;
    }

    public void unregisterPlayerToGame(String userName, String gameName, UserManager userManager)
    {
        GameEngine engine = this.getGameEngine(gameName);
        AbstractPlayer p = userManager.getPlayerFromUserName(userName);
        if (p == null || engine == null)
        {
            return;
        }
        engine.unregisterPlayer(p);
    }

    public ArrayList<String> registerPlayerToGame(String userName, String gameName, UserManager userManager)
    {
        ArrayList<String> res = new ArrayList<>();
        GameEngine engine = this.getGameEngine(gameName);
        if (engine == null)
        {
            res.add("Server problem");
            return res;
        }

        AbstractPlayer p = userManager.getPlayerFromUserName(userName);
        if (p == null)
        {
            res.add("Server problem");
            return res;
        }
        res = engine.canPlayerRegister();
        if (res.size() == 0)
            engine.registerPlayer(p);
        return res;
    }

    public boolean isThisPlayerTurn(String Username, String game)
    {
        GameEngine eng = this.getGameEngine(game);
        String currentUser = eng.getPlayerTurn();
        return currentUser.equals(Username);
    }

    public boolean isCompTurn(String game)
    {
        GameEngine eng = this.getGameEngine(game);
        return eng.isCompTurn();
    }

    public ArrayList<GameTableModel> getGameTable()
    {
        ArrayList<GameTableModel> res = new ArrayList<>();
        for (String iter : this.data.keySet())
        {
            GameTableModel tmp = new GameTableModel();
            tmp.setGameName(iter);
            tmp.setSize(this.data.get(iter).config.config.getStructure().getBoardSize());
            tmp.setActivePlayers(this.data.get(iter).getPlayersCount());
            tmp.setReqPlayers(this.data.get(iter).config.config.getDynamicPlayers().getTotalPlayers());
            tmp.setLetters(this.data.get(iter).config.config.getStructure().getLetters().getTargetDeckSize());
            tmp.setUserCreator(this.data.get(iter).getOwner());
            tmp.setDictName(this.data.get(iter).config.getDictionaryName());
            tmp.setRunning(this.data.get(iter).isGameRunning());

            res.add(tmp);
        }
        return res;
    }

    public boolean checkIfNeedToStartGame(String game)
    {
        GameEngine eng = this.getGameEngine(game);
        if (eng == null) {
            return false;
        }
        return eng.checkStartGame();
    }

    public CurrentGameDataModel getCurrentGameModel(String game, String user, UserManager manager)
    {
        CurrentGameDataModel res = new CurrentGameDataModel();
        GameEngine eng = this.getGameEngine(game);
        //add players table
        List<AbstractPlayer> players = eng.getAllPlayers();
        for (AbstractPlayer p : players)
        {
            res.addPlayerStat(p);
        }
        //add current player
        AbstractPlayer p = manager.getPlayerFromUserName(user);
        for (Map.Entry<String, Integer> it : eng.getPlayerWordMap(p).entrySet())
        {
            String score = "1";
            if (eng.config.config.getGameType().getWinnerAccordingTo().equals("WordScore")) {

                score = eng.getCalcOfWord(it.getKey()) + " = " + eng.getWordScore(it.getKey());
            }
            res.addCurrentPlayerWord(it.getKey(), it.getValue(), score);
        }
        //add game properties
        int remain = eng.config.config.getDynamicPlayers().getTotalPlayers() - eng.getPlayersCount();
        res.setRemainPlayers(remain);
        res.setStock(eng.getStockSize());
        res.setProgressText(eng.getStatusStr());
        res.setCurrentPlayerName(eng.getPlayerTurn());
        res.setRunning(eng.isGameRunning());
        res.setRound(eng.getRound());
        res.setGoldFish(eng.config.getGoldFishMode());
        res.setWinnerMethod(eng.config.config.getGameType().getWinnerAccordingTo());
        res.setSize(eng.config.config.getStructure().getBoardSize());
        return res;
    }

    public void finishRound(String game, String word)
    {
        GameEngine eng = this.getGameEngine(game);
        eng.finishRound(word);
    }
}
