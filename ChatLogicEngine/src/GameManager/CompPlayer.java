package GameManager;

/**
 * Created by yossi_c89 on 09/04/2017.
 */

import Engine.Point;
import Resources.Player;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Random;

public class CompPlayer extends AbstractPlayer {

    public CompPlayer()
    {
        super();
    }

    public CompPlayer(String name)
    {
        super();
        super.name.add(name);
        super.type = "Computer";
    }

    public CompPlayer(Player p)
    {
        super(p);
    }

    public ArrayList<Point> chooseNumbers(ArrayList<Point> points, int n)
    {
        ArrayList<Point> res = new ArrayList<Point>();
        for (int i=0; i<n; i++)
        {
            int place = new Random().nextInt(points.size());
            Point p = new Point(points.get(place).getX()-1, points.get(place).getY()-1);

            res.add(p);
            points.remove(place);
        }
        return res;
    }

    public String buildWord(ArrayList<String> ch_list)
    {
        String res = "";
        int rand = 5;
        if (ch_list.size() < 5)
            rand = ch_list.size();
        int wordSize = new Random().nextInt((rand - 2) + 1) + 2;
        for (int i=0; i<wordSize; i++)
        {
            int place = new Random().nextInt(ch_list.size());
            res = res + ch_list.get(place).charAt(0);
            ch_list.remove(place);
        }
        return res;
    }

}