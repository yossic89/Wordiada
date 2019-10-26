package Engine;

import Resources.Letter;

import java.io.Serializable;


/**
 * Created by yossi_c89 on 01/04/2017.
 */
public class Tile extends Letter implements Serializable {
    private boolean isShown;
    private int quantity;

    public Tile(Letter letter, int quantity)
    {
        super.sign = letter.getSign();
        super.score = letter.getScore();
        this.isShown = false;
        this.quantity = quantity;
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    public boolean getIsShown(){return isShown;}

    public void setIsShown(boolean isShown){
        this.isShown=isShown;
    }

    public void test()
    {
        System.out.println(this.sign);
        System.out.println(this.quantity);
    }
}
