package GameManager;

/**
 * Created by yossi_c89 on 01/04/2017.
 */
import Engine.Tile;
import Resources.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;

abstract public class AbstractPlayer extends Player  implements Serializable{
    protected int score;
    protected List<String> myWords;

    public AbstractPlayer(Player p)
    {
        super();
        this.name = p.getName();
        this.id = p.getId();
        this.type = p.getType();
        this.score = 0;
        this.myWords = new ArrayList<String>();
    }
    public AbstractPlayer()
    {
        super();
        super.name = new ArrayList<String>();
        this.score = 0;
        this.myWords = new ArrayList<String>();
    }

    public int getWordCount() {return this.myWords.size();}

    public void clearWords(){this.myWords.clear();}

    public void updateScore(int points){ score+=points;}
    public int getScore() {return this.score;}

    public List<String> getMyWords() {return this.myWords;}

    public void addToMyWords(String word)
    {
        myWords.add(word);
    }

    public boolean isComputer() {return super.type == "Computer";}

    public HashMap<String, Integer> getWordsMap()
    {
        HashMap<String, Integer> res = new HashMap<String, Integer>();
        for (String word : this.myWords)
        {
            if (res.containsKey(word))
            {
                res.put(word, res.get(word) +1);
            }
            else
            {
                res.put(word, 1);
            }
        }
        return res;
    }

}
