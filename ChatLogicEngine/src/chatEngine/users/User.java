package chatEngine.users;

/**
 * Created by Mor (Mof Shlus) on 13/07/2017.
 */
public class User {

    String name=null;

    boolean computer=false;

    public User(String name,boolean isComp){
        this.name=name;
        this.computer=isComp;
    }


    public boolean isComputer() {
        return computer;
    }

    public void setComputer(boolean computer) {
        this.computer = computer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
