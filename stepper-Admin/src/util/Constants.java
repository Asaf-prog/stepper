package util;

import com.google.gson.Gson;
import okhttp3.MediaType;

import java.net.URL;

public class Constants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/chat/client/component/main/chat-app-main.fxml";
    public final static String CHAT_ROOM_FXML_RESOURCE_LOCATION = "/chat/client/component/chatroom/chat-room-main.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/stepper_server";//*********
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public static final String UPDATE_USER_ROLES =FULL_SERVER_PATH + "/updateUserRoles";
    public static final String USERS_LIST_UPDATE = FULL_SERVER_PATH + "/users-list-update";


    public static final String GET_USER_EXECUTIONS = FULL_SERVER_PATH + "/getUserExecutions";
    public static final String GET_ALL_ROLES = FULL_SERVER_PATH + "/getAllRoles";
    public static final String GET_ALL_ROLES_DTOS = FULL_SERVER_PATH + "/getAllRolesDTOs";
    public static final String GET_IS_ALREADY_LOADED = FULL_SERVER_PATH + "/getIsAlreadyLoaded";

    public static final String ADD_ROLE = FULL_SERVER_PATH + "/addRole";


    public static final String GET_ALL_FLOWS = FULL_SERVER_PATH + "/getAllFlows";

    public static final String GET_FLOWS_DEF = FULL_SERVER_PATH + "/getFlowsDefinitions";
    public final static String INIT_ADMIN = FULL_SERVER_PATH + "/initAdmin" ;//*********
    public static final String GET_USER_BY_NAME ="/getUser";
    public static final String GET_USERS_FOR_ROLE =FULL_SERVER_PATH + "/getUsersForRole" ;

    public static final String  GET_USER = FULL_SERVER_PATH + GET_USER_BY_NAME;

    public static final String UPDATE_ROLE_FLOWS = FULL_SERVER_PATH + "/updateRoleFlows";


    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/users-list";
    public static final String GET_ALL_EXE = FULL_SERVER_PATH + "/getAllExecutions";
    public static final String GET_ROLES_FOR_USER =FULL_SERVER_PATH + "/getRolesForUser";

    public final static String LOGOUT = FULL_SERVER_PATH + "/chat/logout";//*********
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";//*********
    public final static String XML_UPLOAD = FULL_SERVER_PATH + "/uploadXmlFile";//*********
    public static final String GET_FLOWS_FOR_ROLE = FULL_SERVER_PATH + "/getFlowsForRole";

    public static final String GET_TABLE_DATA_ADMIN =FULL_SERVER_PATH + "/Admin/getAllExecutions";

    public final static String INIT_ADMIN_PAGE = FULL_SERVER_PATH + "/initAdmin";//*********

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
    public final static String Hello=FULL_SERVER_PATH+"/hello";
}
