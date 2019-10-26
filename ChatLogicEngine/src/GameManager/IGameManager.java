package GameManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;
import java.util.TreeMap;

/**
 * Created by yossi_c89 on 01/04/2017.
 */
public interface IGameManager extends Serializable {
    void registerPlayer(AbstractPlayer p);
    void unregisterPlayer(AbstractPlayer p);
    void Init();
    void playNext();
    int getRoundCount();
    HashMap<String, Integer> getWordCountStat();
    HashMap<String, List<String>> getPlayersWordsStat();
    void updateWord(String word);
    String getCurrentPlayer();
    String getWinner(String method);
    boolean checkDraw();
    AbstractPlayer getPlayer();
    boolean isCurrentPlayerComputer();
    String getPlayerType();
    void initNewGame();
    HashMap<String, Short> getPlayersIdMap();
    List<AbstractPlayer> getAllPlayers();
    HashMap<String, Integer> getCurrentPlayerWordsMap();
    HashMap<String, Integer> getPlayerWordMap(AbstractPlayer p);
    void updateScore(int points);
    TreeMap<Integer,  ArrayList<String>> getMapByScore();
    int getPlayersCount();

}
