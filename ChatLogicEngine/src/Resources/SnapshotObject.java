package Resources;

import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;


import java.util.ArrayList;

/**
 * Created by yossi_c89 on 29/05/2017.
 */
public class SnapshotObject {
    private ImageView board;
    private ObservableList<String> stock;
    private ObservableList<String> playersText;
    private ArrayList<String> words;
    private String playerName;

    public SnapshotObject(ImageView im, ObservableList<String> st, ObservableList<String> txt, ArrayList<String> wrd, String player)
    {
        this.board = im;
        this.stock = st;
        this.playersText = txt;
        this.words = wrd;
        this.playerName = player;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public ImageView getBoard() {
        return board;
    }

    public ObservableList<String> getStock() {
        return stock;
    }

    public ObservableList<String> getPlayersText() {
        return playersText;
    }

    public String getPlayerName() {
        return playerName;
    }
}
