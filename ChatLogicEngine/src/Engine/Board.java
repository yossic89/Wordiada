package Engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Board implements Serializable {
    private Tile[][] dataBoard; //in size: size+1*size+1, when user will ask the letter in place i*j this will be dataBoard[i][j]
    private int size = 0;
    private int vertical = 0;
    private int horizontal = 0;
    public Stock stock;

    public Board(int size) {
        this.size=size;
        dataBoard = new Tile[size + 1][size + 1];
    }

    public Tile[][] getDataBoard(){
        return dataBoard;
    }


    public void printDataBoard() {
        for (int i = 1; i < size+1; i++) {
            for (int j = 1; j < size+1; j++) {
                System.out.print(dataBoard[i][j].getSign());
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public void buildDataBoardAndStock(ArrayList<Tile> tiles) {
        stock=new Stock();
        ArrayList<Tile> allTiles = new ArrayList<Tile>();
        for (Tile t : tiles) {
            for (int i = 0; i < t.getQuantity(); i++) {
                allTiles.add(new Tile(t,t.getQuantity()));
            }
        }
        Collections.shuffle(allTiles);
        int index = 0;
        for (int i = 1; i < size + 1; i++) {
            for (int j = 1; j < size + 1; j++) {
                dataBoard[i][j] = allTiles.get(index);
                index++;
            }
        }
        while (index < allTiles.size()) {
            stock.addLetterToStock(allTiles.get(index));
            index++;
        }
    }

    public int getBackCardsNum() {
        int count=0;
        for (int i = 1; i < size+1; i++) {
            for (int j = 1; j < size+1; j++) {
                if(dataBoard[i][j]!=null && dataBoard[i][j].getIsShown()==false) count++;
            }
        }
        return count;
    }

    public void initDataBoard(){
        for (int i = 1; i < size+1; i++) {
            for (int j = 1; j < size+1; j++) {
                if (! isEmptyCard(i,j)) {
                    dataBoard[i][j].setIsShown(false);
                }
            }
        }
    }

    //return false if the card already was fliped
    public boolean isFlipedCard(int row, int col) {
        if (dataBoard[row][col].getIsShown()) return true;
        else return false;
    }
    public boolean isEmptyCard(int row, int col) {
        if (dataBoard[row][col] == null) return true;
        else return false;
    }

    public Tile getTile(int x,int y){
        return dataBoard[x][y];
    }

    public void setIsShown(int x,int y,boolean shown){
        if (dataBoard[x][y] !=null)
            dataBoard[x][y].setIsShown(shown);
    }

    public boolean replaceCardWithStock(int row, int col) {
        if (!(dataBoard[row][col].getIsShown()) || dataBoard[row][col] == null) return false;
        if (stock.stockLetters.size() == 0) {
            dataBoard[row][col] = null;
        } else {
            dataBoard[row][col] = stock.getLetterFromStock();
        }
        return true;
    }

    public HashMap<String, Integer> getStat()
    {
//        HashMap<String, Integer> res = new HashMap<String, Integer>();
//        for (int i = 1; i < size + 1; i++) {
//            for (int j = 1; j < size + 1; j++) {
//                if (res.containsKey(dataBoard[i][j].getSign().get(0)))
//                {
//                    res.put(dataBoard[i][j].getSign().get(0), res.get(dataBoard[i][j].getSign().get(0)) + 1);
//                }
//                else
//                {
//                    res.put(dataBoard[i][j].getSign().get(0), 1);
//                }
//            }
//        }
//        return res;
        return this.stock.getStat();
    }

    public ArrayList<Point> getTilesPositions(boolean isFlipped)
    {
        ArrayList<Point> res = new ArrayList<Point>();
        for (int i = 1; i < size+1; i++) {
            for (int j = 1; j < size + 1; j++) {
                if (!isEmptyCard(i,j))
                {
                    if (isFlipped && isFlipedCard(i,j))
                    {
                        res.add(new Point(i ,j));
                    }
                    else if (!isFlipped && !isFlipedCard(i,j))
                    {
                        res.add(new Point(i ,j));
                    }
                }
            }
        }
        return res;
    }

    public ArrayList<String> getAvailableChars()
    {
        ArrayList<String> res = new ArrayList<String>();
        for (int i = 1; i < size+1; i++) {
            for (int j = 1; j < size + 1; j++) {
                if (!isEmptyCard(i,j) && isFlipedCard(i,j))
                {
                    res.add(dataBoard[i][j].getSign().get(0));
                }
            }
        }
        return res;
    }



}


