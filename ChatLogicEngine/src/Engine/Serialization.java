package Engine;

import java.io.*;


/**
 * Created by yossi_c89 on 08/04/2017.
 */
public class Serialization {
    public static boolean save(String path, GameEngine engine)  {
        engine.beforeSave();
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(path))) {
            out.writeObject(engine);
            out.flush();
        }

        catch (IOException e)
        {
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    public static GameEngine load(String fullPath){ //throws IOException, ClassNotFoundException {
        // Read the array list  from the file
        GameEngine engine;
        try {
            ObjectInputStream in =
                    new ObjectInputStream(
                            new FileInputStream(fullPath));
            engine = (GameEngine) in.readObject();

        }
        catch (IOException err)

    {
        //err.printStackTrace();
        return null;
    }
    catch (ClassNotFoundException e)
    {
        //e.printStackTrace();
        return null;
        }
        engine.afterLoad();
        return engine;
    }

}
