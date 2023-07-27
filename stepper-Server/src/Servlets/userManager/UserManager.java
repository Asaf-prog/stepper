package Servlets.userManager;

import modules.DataManeger.users.StepperUser;

import java.util.*;

public class UserManager {

        private  boolean isUpToDate = true;

        private final List<StepperUser> usersSet = new ArrayList<>();
        private final List<StepperUser> loggedOutUsers = new ArrayList<>();

        public UserManager() {

        }

    public void setUpToDate(boolean upToDate) {
        isUpToDate = upToDate;
    }

    public boolean isUpToDate() {
        return isUpToDate;
    }

    public List<StepperUser> getUsersSet() {
        return usersSet;
    }


    public synchronized void addUser(String username) {
            this.usersSet.add(new StepperUser(username));

        }
    public synchronized void addUser(String username,String sessionId) {
        this.usersSet.add(new StepperUser(username));
        updateUserSessionId(username,sessionId);

    }

        public synchronized void removeUser(String username) {
            this.usersSet.remove(username);
        }

        public synchronized List<StepperUser> getUsers() {
            return Collections.unmodifiableList(this.usersSet);
        }

        public boolean isUserExists(String username) {
            for (StepperUser user : usersSet) {
                if (user.getUsername().equals(username)) {
                    return true;
                }
            }
            return false;
        }


    public List<String> getRolesForUser(String username) {
        for (StepperUser user : usersSet) {
            if (user.getUsername().equals(username)) {
                if  (user.getIsManager()) {
                    user.getRoles().clear();
                    user.getRoles().add("all-flows");
                }
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
    public boolean isUserManager(String username) {

        for (StepperUser user : usersSet) {
            if (user.getUsername().equals(username)) {
                return user.getIsManager();
            }
        }
        return false;
    }
    public synchronized void updateUserSessionId(String username, String sessionId) {
        StepperUser user = getUser(username);
        if (user != null) {
            user.setSessionId(sessionId);
        }
    }

    public StepperUser getLoggedOutUser(String username) {
        for (StepperUser user : loggedOutUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public synchronized void logoutUser(String username) {
        StepperUser user = getUser(username);
        if (user != null) {
            loggedOutUsers.add(user);
            usersSet.remove(user);
        }
    }

    public List<StepperUser> getLoggedOut() {
            return loggedOutUsers;
    }

    public void addFlowExecution(String username, UUID uniqueId) {
        StepperUser user = getUser(username);
                if (user != null) {
                    user.addFlowExecution(uniqueId);
                }

    }
}
