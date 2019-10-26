package Engine;

import Engine.Tile;
import Resources.Letter;
//import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Stock implements Serializable {
    public static ArrayList<Tile> stockLetters;
    public ArrayList<Tile> stockSave;

    public Stock(){
        stockLetters=new ArrayList<Tile>();
    }

    public void addLetterToStock(Tile t){
        stockLetters.add(t);
    }

    public int getSizeofStock(){
        return stockLetters.size();
    }

    public void beforeSave()
    {
        this.stockSave = new ArrayList<Tile>();
        this.stockSave = stockLetters;
    }

    public void afterLoad()
    {
        this.stockLetters = new ArrayList<Tile>();
        this.stockLetters = this.stockSave;
    }
    //@Nullable
    public Tile getLetterFromStock(){
        if (stockLetters.size()==0) return null;
        Tile t=stockLetters.get(stockLetters.size()-1);
        stockLetters.remove(stockLetters.size()-1);
        return t;
    }

    public static void randomTheStock(){
        Collections.shuffle(stockLetters);
    }

    public HashMap<String,Integer> getStat()
    {
        HashMap<String,Integer> res = new HashMap<String,Integer>();
        for (Tile it : stockLetters)
        {
            if (res.containsKey(it.getSign().get(0)))
            {
                res.put(it.getSign().get(0), res.get(it.getSign().get(0)) + 1);
            }
            else
            {
                res.put(it.getSign().get(0), 1);
            }
        }
        return res;
    }

}
