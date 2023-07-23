package Servlets.userManager;

import modules.DataManeger.users.StepperUser;

import java.util.*;

public class UserManager {

        private final List<StepperUser> usersSet = new ArrayList<>();

        public UserManager() {

        }

        public synchronized void addUser(String username) {
            this.usersSet.add(new StepperUser(username));
        }

        public synchronized void removeUser(String username) {
            this.usersSet.remove(username);
        }

        public synchronized List<StepperUser> getUsers() {
            return Collections.unmodifiableList(this.usersSet);
        }

        public boolean isUserExists(String username) {
            return this.usersSet.contains(username);
        }


    public List<String> getRolesForUser(String username) {
        for (StepperUser user : usersSet) {
            if (user.getUsername().equals(username)) {
                return user.getRoles();
            }
        }
        return null;
    }

    public StepperUser getUser(String username) {
        for (StepperUser user : usersSet) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
