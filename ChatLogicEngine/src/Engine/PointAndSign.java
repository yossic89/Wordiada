package Engine;

/**
 * Created by Mor (Mof Shlus) on 28/07/2017.
 */
public class PointAndSign {

    private int x;
    private int y;
    private String sign;
    private int score;

    public PointAndSign(int x,int y,String sign, int score){
        this.x=x;
        this.y=y;
        this.sign=sign;
        this.score = score;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object v) {
        boolean retVal = true;

        if (v instanceof PointAndSign){
            PointAndSign ptr = (PointAndSign) v;
            retVal &= (this.x == ptr.x);
            retVal &= (this.y == ptr.y);
            retVal &= (this.sign == ptr.sign);
        }

        return retVal;
    }
}
