package util;

import com.google.gson.Gson;

import java.net.URL;

public class ClientConstants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/chat/client/component/main/chat-app-main.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/chat/client/component/login/login.fxml";
    public final static String CHAT_ROOM_FXML_RESOURCE_LOCATION = "/chat/client/component/chatroom/chat-room-main.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    public static final String EXECUTE_FLOW = "/Client/executeFlow";

    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/stepper_server";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static  String INIT_ADMIN = FULL_SERVER_PATH + "/initAdmin" ;

    public final static String GET_FLOWS = FULL_SERVER_PATH + "/Client/getFlows" ;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String LOAD_XML_FILE = FULL_SERVER_PATH + "/loadAndDecodeFile";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    public final static String LOGOUT = FULL_SERVER_PATH + "/chat/logout";//*********
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";//*********
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";//*********

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
