package Resources;

import GameManager.AbstractPlayer;

import java.util.ArrayList;

/**
 * Created by yossi_c89 on 25/07/2017.
 */
public class CurrentGameDataModel {
    int round;
    boolean isGoldFish;
    boolean isRunning;
    String winnerMethod;
    String progressText;
    int remainPlayers;
    int size;
    int stock;
    String currentPlayerName;
    ArrayList<PlayersTableModel> playersTables;
    ArrayList<CurrentPlayerTableModel> currentPlayStat;

    public CurrentGameDataModel() {
        this.playersTables = new ArrayList<>();
        this.currentPlayStat = new ArrayList<>();
    }

    public void addPlayerStat(AbstractPlayer player)
    {
        PlayersTableModel model = new PlayersTableModel(player.getName().get(0), player.getType(), player.getWordCount(), player.getScore());
        this.playersTables.add(model);
    }

    public void addCurrentPlayerWord(String word, int amount, String score)
    {
        CurrentPlayerTableModel model = new CurrentPlayerTableModel(word, amount, score);
        this.currentPlayStat.add(model);
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public boolean isGoldFish() {
        return isGoldFish;
    }

    public void setGoldFish(boolean goldFish) {
        isGoldFish = goldFish;
    }

    public String getWinnerMethod() {
        return winnerMethod;
    }

    public void setWinnerMethod(String winnerMethod) {
        this.winnerMethod = winnerMethod;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public void setCurrentPlayerName(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }

    public ArrayList<PlayersTableModel> getPlayersTables() {
        return playersTables;
    }

    public void setPlayersTables(ArrayList<PlayersTableModel> playersTables) {
        this.playersTables = playersTables;
    }

    public ArrayList<CurrentPlayerTableModel> getCurrentPlayStat() {
        return currentPlayStat;
    }

    public void setCurrentPlayStat(ArrayList<CurrentPlayerTableModel> currentPlayStat) {
        this.currentPlayStat = currentPlayStat;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public String getProgressText() {
        return progressText;
    }

    public void setProgressText(String progressText) {
        this.progressText = progressText;
    }

    public int getRemainPlayers() {
        return remainPlayers;
    }

    public void setRemainPlayers(int remainPlayers) {
        this.remainPlayers = remainPlayers;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    private class PlayersTableModel{
        String name;
        String type;
        int words;
        int score;

        public PlayersTableModel(String name, String type, int words, int score) {
            this.name = name;
            this.type = type;
            this.words = words;
            this.score = score;
        }
    }

    private class CurrentPlayerTableModel {
        String word;
        int amount;
        String score;

        public CurrentPlayerTableModel(String word, int amount, String score) {
            this.word = word;
            this.amount = amount;
            this.score = score;
        }
    }
}
