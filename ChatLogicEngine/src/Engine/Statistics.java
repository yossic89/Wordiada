package Engine;


import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by yossi_c89 on 08/04/2017.
 */
public class Statistics {
    private GameEngine engine;
    public int round;
    public String time;
    public int totalWords;
    public int totalTiles;
    public Map<String,Integer> m_tile_count;
    public Map<String, Integer> m_player_score;
    public Map<String, List<String>> m_player_words;
    public HashMap<String, Integer> m_word_count;
    public HashMap<String, Short> m_player_id;

    public Statistics(GameEngine engine)
    {
        this.engine = engine;
        m_word_count = new HashMap<String, Integer>();
    }

    private void initTime()
    {
        long nowTime = System.nanoTime() - this.engine.getStartTime();
        long millis = TimeUnit.MILLISECONDS.convert(nowTime, TimeUnit.NANOSECONDS);
        this. time = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    private void initDictStat()
    {
        this.m_word_count.clear();
        this.totalWords = this.engine.getTotalWordsCount();
        for (Map.Entry<String, List<String>> it : m_player_words.entrySet())
        {
            for (String word_iter : it.getValue())
            {
                m_word_count.put(word_iter, this.engine.getWordRepeat(word_iter));
            }
        }

    }

    public void initPlayersStat()
    {
        this.m_player_score = this.engine.getPlayerWordCount();
        this.m_player_words = this.engine.getPlayersWordsStat();
        this.m_player_id = this.engine.getPlayerIdMap();
    }
    public void initStat()
    {
        this.round = this.engine.getRound();
        this.m_tile_count = this.engine.getStockStat();
        this.totalTiles = this.engine.getStockSize();
        initPlayersStat();
        initDictStat();
        initTime();
    }
}
