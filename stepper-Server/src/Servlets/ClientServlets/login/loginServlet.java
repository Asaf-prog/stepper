package Servlets.ClientServlets.login;

import Servlets.userManager.UserManager;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modules.DataManeger.users.StepperUser;
import modules.stepper.Stepper;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static Constants.Constants.USERNAME;

@WebServlet(name = "ServletLogin",urlPatterns = "/login")
public class loginServlet extends HttpServlet {
@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws IOException{
    response.setContentType("text/plain;charset=UTF-8");
    String usernameFromSession = SessionUtils.getUsername(request);
    String username= request.getParameter(USERNAME);
    UserManager userManager = ServletUtils.getUserManager(getServletContext());//swap's lines
    //on the gui ofc
    userManager.setUpToDate(false);
    boolean isExist=isUserAlreadyExist(request, response, username, userManager);
    if (!isExist) { //user is not logged in yet

        String usernameFromParameter = username;

        if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
            //no username in session and no username in parameter - not standard situation. it's a conflict

            // stands for conflict in server state
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            //normalize the username value
            usernameFromParameter = usernameFromParameter.trim();
            synchronized (this){
                if (userManager.isUserExists(usernameFromParameter)) {//from the server we're going to engin
                    String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                    // stands for unauthorized as there is already such user with this name
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print(errorMessage);
                }
                else {
                    String sessionId = request.getSession().getId();
                    //add the new user to the users list
                    userManager.addUser(usernameFromParameter,sessionId);
                    //set the username in a session, so it will be available on each request
                    //the true parameter means that if a session object does not exist yet
                    //create a new session for the new user
                    request.getSession(true).setAttribute(USERNAME, usernameFromParameter);

                    //redirect the request  - in order to actually change the URL
                    System.out.println("On login, request URI is: " + request.getRequestURI());
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
        }
    }
    else {
        //session id already updated let him in
        StepperUser want2Login=userManager.getLoggedOutUser(username);
        userManager.getLoggedOut().remove(want2Login);
        userManager.getUsersSet().add(want2Login);
        updateSessionId(userManager, request, want2Login);
        request.getSession(true).setAttribute(USERNAME, username);

        //userManager.updateUserSessionId(usernameFromSession,request.getSession().getId());


        //user is already logged in
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

    private boolean isUserAlreadyExist(HttpServletRequest request, HttpServletResponse response, String username, UserManager userManager) {
    for (StepperUser user: userManager.getLoggedOut()){
        if (user.getUsername().equals(username)){
            //UpdateSessionId(userManager, request, user);
            return true;
        }
    }
    return false;
    }

    private void updateSessionId(UserManager userManager, HttpServletRequest request, StepperUser user) {
    String sessionId = request.getSession().getId();
    userManager.updateUserSessionId(user.getUsername(),sessionId);
    }
}
