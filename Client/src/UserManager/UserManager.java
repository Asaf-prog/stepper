package UserManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserManager {
    private final Set<String> usersSet = new HashSet();

    public UserManager() {
    }

    public synchronized void addUser(String username) {
        this.usersSet.add(username);
    }

    public synchronized void removeUser(String username) {
        this.usersSet.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(this.usersSet);
    }

    public boolean isUserExists(String username) {
        return this.usersSet.contains(username);
    }
}
