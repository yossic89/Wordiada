package Resources;

/**
 * Created by yossi_c89 on 15/07/2017.
 */
public class GameTableModel {
    String gameName;
    String userCreator;
    int size;
    int activePlayers;
    int reqPlayers;
    String dictName;
    int letters;
    boolean isRunning;

    public GameTableModel(){}

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(String userCreator) {
        this.userCreator = userCreator;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getActivePlayers() {
        return activePlayers;
    }

    public void setActivePlayers(int activePlayers) {
        this.activePlayers = activePlayers;
    }

    public int getReqPlayers() {
        return reqPlayers;
    }

    public void setReqPlayers(int reqPlayers) {
        this.reqPlayers = reqPlayers;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public int getLetters() {
        return letters;
    }

    public void setLetters(int letters) {
        this.letters = letters;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
