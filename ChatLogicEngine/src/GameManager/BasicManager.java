package GameManager;

/**
 * Created by yossi_c89 on 01/04/2017.
 */

import Engine.GameEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class BasicManager implements IGameManager {
    protected GameEngine engine;
    protected List<AbstractPlayer> players = new ArrayList();
    protected int roundCount;
    protected int playerPosition;

    public BasicManager(GameEngine engine)
    {
        this.engine = engine;
        this.roundCount = 0;
        this.playerPosition =0;
    }
    @Override
    public void Init()
    {
        players.clear();
        this.roundCount = 0;
        this.playerPosition = 0;

    }
    @Override
    public void registerPlayer(AbstractPlayer p)
    {
        String name = p.getName().get(0);
        //search the player
        int place = -1;
        for (int i=0; i< players.size(); i++)
        {
            if (players.get(i).getName().get(0).equals(name))
            {
                place = i;
            }
        }
        if (place ==-1) {
            players.add(p);
        }

    }
    @Override
    public void playNext()
    {

        this.roundCount ++;
        this.playerPosition++;
    }

    @Override
    public void updateWord(String word)
    {
        if (players.size() == 0)
            return;
        int place = playerPosition % players.size();
        players.get(place).addToMyWords(word);
    }

    @Override
    public int getRoundCount()
    {
        return this.roundCount;
    }

    @Override
    public HashMap<String, Integer> getWordCountStat()
    {
        HashMap<String, Integer> res = new HashMap<String, Integer>();
        for (AbstractPlayer it : players)
        {
            //String name = it.getName().get(0) + " (" + it.getType()+")";
            String name = it.getName().get(0);
            res.put(name, it.getWordCount());
        }
        return res;
    }

    @Override
    public HashMap<String, List<String>> getPlayersWordsStat()
    {
        HashMap<String, List<String>> res = new HashMap<String, List<String>>();
        for (AbstractPlayer it : players)
        {
            res.put(it.getName().get(0), it.getMyWords());
        }
        return res;
    }

    @Override
    public HashMap<String, Integer> getPlayerWordMap(AbstractPlayer p)
    {
        for (AbstractPlayer it : players)
        {
            if (p.getName().get(0).equals(it.getName().get(0)))
                return it.getWordsMap();
        }
        HashMap<String, Integer> emptyRes = new HashMap<String, Integer>();
        return emptyRes;
    }

    public String getWinnerAfterExit()
    {
        if (players.size() ==0)
            return "";
        int place = (playerPosition +1) % players.size();
        return players.get(place).getName().get(0);
    }

    @Override
    public String getCurrentPlayer()
    {
        if (players.size() ==0)
            return "";
        int place = playerPosition % players.size();
        return players.get(place).getName().get(0);
    }

    @Override
    public String getWinner(String method)
    {
        String res = "";
        if (players.get(0).getWordCount() >  players.get(1).getWordCount())
            res = players.get(0).getName().get(0);
        else
            res = players.get(1).getName().get(0);
        return res;
    }

    @Override
    public boolean checkDraw()
    {
        return players.get(0).getWordCount() == players.get(1).getWordCount();
    }

    @Override
    public AbstractPlayer getPlayer()
    {
        if (players.size() ==0)
            return null;
        int place = playerPosition % players.size();
        return players.get(place);
    }

    @Override
    public boolean isCurrentPlayerComputer()
    {
        if (players.size() ==0)
            return false;
        int place = playerPosition % players.size();
        return players.get(place).isComputer();
    }

    @Override
    public String getPlayerType()
    {
        if (players.size() ==0)
            return "";
        int place = playerPosition % players.size();
        return players.get(place).getType();
    }

    @Override
    public void initNewGame()
    {
        for (AbstractPlayer p : players)
        {
            p.clearWords();
        }
    }

    @Override
    public HashMap<String, Short> getPlayersIdMap()
    {
        return new HashMap<String, Short>();
    }

    @Override
    public List<AbstractPlayer> getAllPlayers(){return players;}

    @Override
    public HashMap<String, Integer> getCurrentPlayerWordsMap()
    {
        if (players.size() ==0)
            return new HashMap<String, Integer>();
        int place = playerPosition % players.size();
        return players.get(place).getWordsMap();
    }

    @Override
    public void unregisterPlayer(AbstractPlayer p){}

    @Override
    public void updateScore(int points)
    {
        if (players.size() ==0)
            return;
        int place = playerPosition % players.size();
        players.get(place).updateScore(points);
    }

    public TreeMap<Integer,  ArrayList<String>> getMapByScore(){return null;}

    public int getPlayersCount() {return this.players.size();}
}
