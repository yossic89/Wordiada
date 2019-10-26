package chatEngine.users;

import GameManager.AbstractPlayer;
import GameManager.CompPlayer;
import GameManager.HumanPlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserManager {

    private final Set<User> usersSet;

    public UserManager() {
        usersSet = new HashSet<User>();
    }

    public void addUser(User user) {
        usersSet.add(user);
    }

    public void removeUser(User user) {
        usersSet.remove(user);
    }

    public void removeUser(String name) {
        User user = null;
        for (User u : usersSet) {
            if (u.getName().equals(name)) {
                user = u;
                break;
            }
        }
        if (user != null)
            removeUser(user);
    }

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    private AbstractPlayer generatePlayerFromUser(User u)
    {
        AbstractPlayer res;
        if (u.isComputer())
            res = new CompPlayer(u.getName());
        else
            res = new HumanPlayer(u.getName());
        return res;
    }

    public AbstractPlayer getPlayerFromUserName(String name)
    {
        AbstractPlayer res = null;
        if (isUserExists(name))
        {
            for (User user:usersSet){
                if (user.getName().equals(name))
                {
                    res = this.generatePlayerFromUser(user);
                }
            }
        }
        return res;
    }

    public boolean isUserExists(User user) {
        return usersSet.contains(user);
    }

    public boolean isUserExists(String name){
        boolean res=false;
        for (User user:usersSet){
            if (user.getName().equals(name)) res=true;
        }
        return res;
    }
}
