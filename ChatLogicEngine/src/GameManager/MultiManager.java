package GameManager;

import Engine.GameEngine;

import java.util.*;

/**
 * Created by yossi_c89 on 26/05/2017.
 */
public class MultiManager extends BasicManager {

    public MultiManager(GameEngine engine)
    {
        super(engine);
    }

    private HashMap<Integer, ArrayList<String>> getWinnerByCount()
    {
        HashMap<Integer, ArrayList<String>> res = new HashMap<Integer, ArrayList<String>>();
        for (AbstractPlayer p : players)
        {
            //check for the draw
            if (res.containsKey(p.getWordCount()))
            {
                ArrayList<String> val = res.get(p.getWordCount());
                val.add(p.getName().get(0));
                res.put(p.getWordCount(), val);
            }
            else
            {
                ArrayList<String> val = new ArrayList<String>();
                val.add(p.getName().get(0));
                res.put(p.getWordCount(),  val);
            }
        }
        return res;
    }

    private HashMap<Integer, ArrayList<String>> getWinnerByScore()
    {
        HashMap<Integer, ArrayList<String>> res = new HashMap<>();

        for (AbstractPlayer p : players)
        {
            //check for the draw
            if (res.containsKey(p.getScore()))
            {
                ArrayList<String> val = res.get(p.getScore());
                val.add(p.getName().get(0));
                res.put(p.getScore(), val);
            }
            else
            {
                ArrayList<String> val = new ArrayList<String>();
                val.add(p.getName().get(0));
                res.put(p.getScore(),  val);
            }
        }
        return res;
    }

    @Override
    public String getWinner(String method)
    {
        String res = "";
        HashMap<Integer, ArrayList<String>> sortedPlayer;
        if (method.equals("WordCount"))
        {
            sortedPlayer = this.getWinnerByCount();
        }
        else
        {
            sortedPlayer = this.getWinnerByScore();
        }

        Map<Integer, ArrayList<String>> treeMap = new TreeMap<Integer, ArrayList<String>>(sortedPlayer);
        //the last one is the winner
        for (Map.Entry<Integer, ArrayList<String>> it : treeMap.entrySet())
        {
            res = it.getValue().get(0);
        }
        return res;
    }

    @Override
    public boolean checkDraw()
    {

        boolean res =false;
        HashMap<Integer, ArrayList<String>> sortedPlayer;
        if (engine.config.config.getGameType().getWinnerAccordingTo() == "WordCount")
        {
            sortedPlayer = this.getWinnerByCount();
        }
        else
        {
            sortedPlayer = this.getWinnerByScore();
        }

        Iterator it = sortedPlayer.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            ArrayList<String> t = (ArrayList<String>)pair.getValue();
            res = t.size() > 1;
            it.remove();
        }
        return res;
    }

    @Override
    public HashMap<String, Short> getPlayersIdMap()
    {
        HashMap<String, Short> res = new HashMap<String, Short>();
        for (AbstractPlayer p : players)
        {
            res.put(p.getName().get(0), p.getId());
        }
        return res;
    }

    @Override
    public void unregisterPlayer(AbstractPlayer p)
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
        if (place >-1) {
            players.remove(place);
            this.roundCount++;
        }
    }

    @Override
    public TreeMap<Integer,  ArrayList<String>> getMapByScore()
    {
        HashMap<Integer, ArrayList<String>> sortedPlayer;
        String method = this.engine.config.config.getGameType().getWinnerAccordingTo();
        if (method.equals("WordCount"))
        {
            sortedPlayer = this.getWinnerByCount();
        }
        else
        {
            sortedPlayer = this.getWinnerByScore();
        }

        TreeMap<Integer, ArrayList<String>> treeMap = new TreeMap<Integer, ArrayList<String>>(sortedPlayer);
        return treeMap;
    }
}
