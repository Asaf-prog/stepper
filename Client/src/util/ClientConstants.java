package util;

import com.google.gson.Gson;
import okhttp3.MediaType;

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
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/stepper_server";
    public final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static  String INIT_ADMIN = FULL_SERVER_PATH + "/initAdmin" ;
    public static final String CHECK_PERMISSION = FULL_SERVER_PATH + "/Client/checkPermission";


    public static final String IS_AUTHORIZED = FULL_SERVER_PATH + "/Client/isAuthorized";
    public static final String GET_CLIENT_UPDATES = FULL_SERVER_PATH + "/Client/getClientUpdates";

    public static final String IS_ROLES_CHANGED = FULL_SERVER_PATH + "/Client/isRolesChanged";
    public static final String GET_TABLE_DATA_FOR_USER =FULL_SERVER_PATH+ "/Client/getTableDataForUser"; ;
    public static final String GET_ROLES_FOR_CLIENT = FULL_SERVER_PATH + "/getRolesForClient";
    public static final String GET_IS_MANAGER = FULL_SERVER_PATH + "/Client/isManager";
    public static final String VALIDATE_ADMIN = FULL_SERVER_PATH + "/Admin/valid";

    public final static String GET_FLOWS = FULL_SERVER_PATH + "/Client/getFlows" ;
    public static final String EXECUTE_FLOW = FULL_SERVER_PATH +"/Client/executeFlow";
    public static final String GET_LAST_FLOW = FULL_SERVER_PATH + "/Client/flowEnded";
    public static final String VALIDATE_INPUT =FULL_SERVER_PATH + "/Client/validate";
    public static final String CHECK_CONTINUATION = FULL_SERVER_PATH + "/Client/Continuation";
    public static final String FLOW_ENDED =FULL_SERVER_PATH+ "/Client/flowEnded";
    public static final String FLOW_STATUS_CHECK = FULL_SERVER_PATH+ "/Client/status-check";
    public static final String FLOW_OUTPUTS =FULL_SERVER_PATH+ "/Client/CreateContinuation";
    public static final String FILE_NAME = FULL_SERVER_PATH+"/Client/getFileTypeName";
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String LOAD_XML_FILE = FULL_SERVER_PATH + "/loadAndDecodeFile";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/users-list";
    public final static String LOGOUT = FULL_SERVER_PATH + "/Client/logout";//*********
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";//*********
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";//*********

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
