package GameManager;

import Engine.Tile;

import  Resources.Player;

import java.util.List;

/**
 * Created by yossi_c89 on 01/04/2017.
 */
public class HumanPlayer extends AbstractPlayer {

    public HumanPlayer(String name)
    {
        super();
        super.name.add(name);
        super.type = "Human";
    }

    public HumanPlayer(Player p)
    {
        super(p);
    }

}
