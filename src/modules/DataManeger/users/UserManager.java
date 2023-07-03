package modules.DataManeger.users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final Set<StepperUser> users;

    public UserManager() {
        users = new HashSet<>();
    }

    public synchronized void addUser(String username) {
        StepperUser user = new StepperUser(username, "basic", new ArrayList<>());
        users.add(user);
    }

    public synchronized void removeUser(StepperUser user) {
        users.remove(user);
    }

    public synchronized Set<StepperUser> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    public boolean isUserExists(String username) {
        return users.contains(username);
    }
}
