package Servlets.login;

import Servlets.userManager.UserManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static Constants.Constants.USERNAME;

@WebServlet(name = "ServletLogin",urlPatterns = "/loginShortResponse")
public class loginServlet extends HttpServlet {
@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws IOException{
    response.setContentType("text/plain;charset=UTF-8");

    UserManager userManager = ServletUtils.getUserManager(getServletContext());//swap's lines
    String usernameFromSession = SessionUtils.getUsername(request);

    if (usernameFromSession == null) { //user is not logged in yet

        String usernameFromParameter = request.getParameter(USERNAME);

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
                    //add the new user to the users list
                    userManager.addUser(usernameFromParameter);
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
        //user is already logged in
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
}
