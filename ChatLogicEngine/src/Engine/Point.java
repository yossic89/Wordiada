package Engine;

public class Point{
    private int x=0;
    private int y=0;

    public Point(){
    }
    public Point(int x,int y){
        this.x=x;
        this.y=y;
    }

    public void setPoint(int x,int Y){
        this.x=x;
        this.y=y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public boolean isSame(int x,int y){return (this.x==x && this.y==y);}

    @Override
    public String toString(){
        return "(x: "+this.x+" y: "+y+"),";
    }

    @Override
    public boolean equals(Object p){
        boolean retVal = true;

        if (p instanceof Point)
        {
            Point ptr = (Point) p;
            retVal &= (this.x == ptr.x);
            retVal &= (this.y == ptr.y);
        }

        return retVal;
    }

    @Override
    public int hashCode(){
        return 1;
    }
}
