package modules.DataManeger.users;

import java.util.*;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final List<StepperUser> users;

    public UserManager() {
        users = new ArrayList<>();
    }

    public synchronized void addUser(String username) {
      //  StepperUser user = new StepperUser(username, "basic", new ArrayList<>());
   //     users.add(user);


    }

    public synchronized void removeUser(StepperUser user) {
        users.remove(user);
    }

    public synchronized List<StepperUser> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public boolean isUserExists(String username) {
        for(StepperUser user : users) {
            if(user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getRolesForUser(String username) {
        for (StepperUser user : users) {
            if (user.getUsername().equals(username)) {
                return user.getRoles();
            }
        }
        return null;
    }
}
