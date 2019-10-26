package Resources; /**
 * Created by yossi_c89 on 01/04/2017.
 */

import Engine.Tile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config  implements Serializable{
    public GameDescriptor config;
    private List<String> errorsList = new ArrayList();
    private ArrayList<Tile> tileList = new ArrayList<Tile>();
    public String dictPath;


    private boolean validateById()
    {
        boolean res = true;
        HashMap<Short, Boolean> idMap = new HashMap<Short, Boolean>();
        for (Player p : config.getPlayers().getPlayer())
        {
            if (idMap.containsKey(p.getId()))
            {
                res = false;
                String err = "ID : " + p.getId() + " is not unique";
                errorsList.add(err);
            }
            else
            {
                idMap.put(p.getId(), true);
            }
        }
        return res;
    }

    private boolean validatePlayers()
    {
        boolean res = true;
        int size = config.getPlayers().getPlayer().size();
        if (size < 2)
        {
            res =false;
            errorsList.add("Minimum players is 2, you got "+ size);
        }
        if (size > 6)
        {
            res =false;
            errorsList.add("Maximum players is 6, you got "+ size);
        }
        return res;
    }

    public void setDictionaryName(String name)
    {
        this.config.getStructure().setDictionaryFileName(name);
    }

    public String getDictionaryName(){return config.structure.getDictionaryFileName();}

    private boolean validate()
    {
        boolean res = true;
        //check dict
//        String dict_name = config.structure.getDictionaryFileName();
//        if (dict_name == null)
//        {
//            errorsList.add("No DictionaryFileName element");
//            res &= false;
//        }
//        this.dictPath= base_dir + "\\dictionary\\" + dict_name;
//        File file = new File(dictPath);
//        if (!file.exists())
//        {
//            errorsList.add("Dictionary is not exists in " +dictPath);
//            res &= false;
//        }
        //check letter valid
        boolean[] letter_shown_array= new boolean[26];
        //init
        for (int i = 0 ; i< letter_shown_array.length ; i++)
        {
            letter_shown_array[i] = false;
        }
        double tilesFreq = 0;
        for (Letter l : config.getStructure().getLetters().getLetter())
        {

            int place = (int)l.getSign().get(0).toUpperCase().charAt(0) - (int)'A';
            if (letter_shown_array[place])
            {
                errorsList.add("Letter " + l.getSign().get(0).toUpperCase().charAt(0) +" is repeated");
                res &=false;
            }
            else {
                letter_shown_array[place] = true;
                tilesFreq += l.getFrequency();
            }
        }
        if (tilesFreq == 0)
        {
            res &= false;
            errorsList.add("No letters");
        }
        //check board size
        int board_size = config.getStructure().getBoardSize();
        if (board_size < 5 || board_size > 50)
        {
            res &=false;
            errorsList.add("board size is out of range : " + board_size);
        }
        //check quantiny
        if (config.getStructure().getLetters().getTargetDeckSize() < board_size * board_size)
        {
            res &= false;
            errorsList.add("target deck size is smaller than the board size");
        }
        //check player ID
        //res &= this.validateById();

        //checl players count
        // res &= this.validatePlayers();
        if (!res)
            return res;
        //build Tiles
        int total_quan = config.getStructure().getLetters().getTargetDeckSize();
        for (Letter l : config.getStructure().getLetters().getLetter())
        {
            long quan = Math.round(((l.getFrequency() / tilesFreq) * total_quan) + 0.4);
            tileList.add(new Tile(l, Math.toIntExact(quan)));
        }
        return res;
    }


    public boolean loadFromString(String xml)
    {
        //init
        errorsList.clear();
        tileList.clear();
        try {
            StringReader reader = new StringReader(xml);
            JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            config = (GameDescriptor) jaxbUnmarshaller.unmarshal(reader);
            return validate();
        } catch (JAXBException e) {
            errorsList.add("failed to load xml file(JAXBException)");
            return false;
        }
    }

    public boolean load(String filePath)
    {
        //init
        errorsList.clear();
        tileList.clear();
        //check if file name has suffix xml
        if (! filePath.endsWith(".xml"))
        {
            filePath += ".xml";
        }
        //filePath =  Config.class.getResource("").getPath() + filePath;

        String base_dir = filePath.substring(0, filePath.lastIndexOf('\\'));
        //String base_dir = Config.class.getResource("").getPath();
        try {
            File file = new File(filePath);
            if (!file.exists())
            {
                String e = filePath + " not exists";
                errorsList.add(e);
                return false;
            }
            JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            config = (GameDescriptor) jaxbUnmarshaller.unmarshal(file);
            return validate();

        } catch (JAXBException e) {
            errorsList.add("failed to load xml file(JAXBException)");
            return false;
        }

    }

    public boolean getGoldFishMode()
    {

        boolean res = false;
        try
        {
            if (config.getGameType().isGoldFishMode())
            {
                res = config.getGameType().isGoldFishMode();
            }
        }
        catch (NullPointerException e){}
        return res;
    }

    private void test()
    {
        if (load("master.xml"))
        {
            int total = 0;
            for (Tile t : tileList)
            {
                t.test();
                total += t.getQuantity();
            }
        }
        else
        {
            System.out.println("Load Fail , errors are:");
            System.out.println(errorsList);
        }
    }

    public ArrayList<Tile> getTileList(){
        return tileList;
    }

    public List<String> getErrorsList()
    {
        return errorsList;
    }

    public String getName() {
        String res = "";
        for (String s :this.config.getDynamicPlayers().getGameTitle())
        {
            res += s + " ";
        }
        return res;
    }

    public int getPlayersAmount() {return this.config.getDynamicPlayers().getTotalPlayers();}
}
